package Users;

import Actions.ActionManager;
import Actions.AddOrDeleteAction;
import Admins.AdminManager;
import Exceptions.InvalidItemException;
import Exceptions.InvalidTradingUserException;
import Exceptions.InvalidTransactionException;
import Items.Item;
import Items.ItemManager;
import Transactions.*;

import java.sql.Time;
import java.util.*;

/**
 * <h1>UserMenuController</h1>
 * Decides which use case/manager methods to call depending on the user input taken from the presenter.
 * <p>It stores instances of all use cases/managers (AdminManager, TradingUserManager, ItemManager,
 * CurrentTransactionManager, PastTransactionManager), the UserMenuPresenter,
 * and a list of allPendingItems (which is the list of all items that have been requested by a
 * TradingUser to be added to their inventory). <p/>
 */
public class UserMenuController {
    private TradingUser currentTradingUser = null; // user that's logged in
    private final AdminManager am;
    private final TradingUserManager um;
    private final CurrentTransactionManager tm;
    private final PastTransactionManager ptm;
    private final ItemManager im;
    private final ActionManager acm;
    private final Map<Item, TradingUser> allPendingItems;

    public UserMenuController(TradingUserManager tradingUserManager, AdminManager adminManager,
                              CurrentTransactionManager currentTransactionManager,
                              PastTransactionManager pastTransactionManager, ItemManager itemManager, ActionManager actionManager,
                              Map<Item, TradingUser> pendingItems) {
        allPendingItems = pendingItems;
        this.am = adminManager;
        this.um = tradingUserManager;
        this.tm = currentTransactionManager;
        this.ptm = pastTransactionManager;
        this.im = itemManager;
        this.acm = actionManager;
    }

    public TransactionBuilder getTransBuilder(){
        TransactionFactory newFactory = new TransactionFactory(tm);
        TransactionBuilder transactionBuilder = new TransactionBuilder(this.currentTradingUser.getUserId(), newFactory);
        return transactionBuilder;
    }

    /**
     * Method that adds pending items to the current trading user's pending items
     */
    public void addToPendingItems(Item requestedItem) {
        allPendingItems.put(requestedItem, currentTradingUser);
    }

    /**
     * Adds item to a wishlist and returns true if the item was not already in the wishlist. If already in wishlist, returns false.
     */
    public boolean addToWishlist(Item item) {
        AddOrDeleteAction action = new AddOrDeleteAction(currentTradingUser.getUserId());
        action.setIsWishlist();
        action.setAdded(item);
        acm.addAction(currentTradingUser.getUserId(), action);
        return um.addItem(currentTradingUser.getUserId(), item, "wishlist");
    }

    /**
     * Creates a HashMap of all the available items in other user's inventory.
     * @return HashMap of items that are available in other user's inventory.
     */
    public Map<Item, TradingUser> getAvailableItems(){
        try {
            List<TradingUser> allTradingUsersInCity = um.getTradingUserByCity(currentTradingUser.getCity());
            HashMap<Item, TradingUser> availableItems = new HashMap<>();
            for (TradingUser tradingUser : allTradingUsersInCity) {
                if(!tradingUser.equals(currentTradingUser)) {
                    for (Item item : im.convertIdsToItems(tradingUser.getInventory())) {
                        availableItems.put(item, tradingUser);
                    }
                }
            }
            return availableItems;
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * This method updates the rest of the program after a transaction has been made.
     * @param newTransaction This is the transaction that has been recently made.
     */
    public void transactionUpdate(Transaction newTransaction) throws InvalidItemException {
        TradingUser otherUser = um.getTradingUserById(newTransaction.getOtherUser(currentTradingUser.getUserId()));
        tm.updateUsersCurrentTransactions(otherUser,currentTradingUser,newTransaction);
        List<UUID> items  = newTransaction.getTransactionItems();
        flagAccountIfAboveThreshold(otherUser);
        flagAccountIfAboveThreshold(currentTradingUser);
        for (UUID id : items) {
            Item item = im.getItem(id);
            getAvailableItems().remove(item);
        }
    }

    /**
     * This method generates the list of the User's ongoing Transactions
     * @return a list of Transactions
     */
    public List<Transaction> currentTransactionList() {
        try {
            List<UUID> currentTransactionsIds = currentTradingUser.getCurrentTransactions();
            return tm.getTransactionsFromIdList(currentTransactionsIds);
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
    }

    public boolean editMeetingFlow(UUID user, UUID transactionId, int meetingNum, Date newTime, Date newDate) throws InvalidTransactionException {
        updateUsers(transactionId, TransactionActions.EDITED);
        if (tm.editMeeting(meetingNum, transactionId, user, newTime) | tm.editMeeting(meetingNum, transactionId, user, newDate, true)) {
            tm.updateEdits(meetingNum, transactionId, user);
            return true;
        }
        return false;
    }

    public boolean editMeetingFlow(UUID user, UUID transactionId, int meetingNum, String newLocation) throws InvalidTransactionException {
        updateUsers(transactionId, TransactionActions.EDITED);
        // newlocation/time/date needs to not be null
        //if (newLocation == null | newTime == null | newDate == null) {
        //    return false;
        //}
        // else {
            if (tm.editMeeting(meetingNum, transactionId, user, newLocation)) {
                tm.updateEdits(meetingNum, transactionId, user);
                return true;
            }
        return false;
    }

    public void updateUsers(UUID transactionId, TransactionActions optionChosen) throws InvalidTransactionException {
        Transaction transaction = tm.getTransactionFromId(transactionId);
        tm.updateStatusUser(currentTradingUser, transaction, optionChosen);
        List<UUID> currentTransactionsIds = currentTradingUser.getCurrentTransactions();
        tm.updateStatus(transaction); //update status of transaction
        if (transaction.isPerm()) { // if transaction is permanent (only one meeting)
            um.handlePermTransactionItems(transaction); // remove items from both users inventories and wishlists
        }
        /* if transaction is temporary (two meetings) */
        if (transaction.isTemp()){ um.handleTempTransactionItems(transaction); } // handles users inventories and wishlists

        if(transaction.isVirtual()){um.handleVirtTransactionItems(transaction);}
        /* if transaction is cancelled, remove from current transactions */
        if (transaction.getStatus().equals(TransactionStatuses.CANCELLED)) {
            try {
                tm.removeTransactionFromAllTransactions(transaction.getId()); // if cancelled, the transaction is deleted forever
                currentTransactionsIds.remove(transaction.getId()); // remove from current/active transactions
            }
            catch (InvalidTransactionException e) {
                //
            }
        }
        /* if transaction is over (incomplete, complete, never returned) then move to transaction history
        * and remove from current transactions */
        if (um.moveTransactionToTransactionHistory(transaction)) {
            currentTransactionsIds.remove(transaction.getId()); // remove from the list of active transaction's the logged in user sees
        }
    }

    /* set a TradingUser to be flagged for admin approval if either the borrow, weekly, or incomplete thresholds have been reached */
    private void flagAccountIfAboveThreshold(TradingUser user) {
        if (userFlaggable(user)) {
            List<String> flaggedUsernames = um.convertFlaggedUsersToUsernames();
            if (!flaggedUsernames.contains(user.getUsername())) {
                um.getFlaggedAccounts().add(user);
            }
        }
    }

    /**
     * Return True if a user is exceeding any of the threshold's
     * @param user Trading User
     * @return booleen
     */
    public boolean userFlaggable(TradingUser user){
        boolean weeklyThreshold = ptm.weeklyThresholdExceeded(user);
        boolean TransactionsExceeded = um.incompleteTransactionExceeded(user);
        boolean borrowThreshold = um.borrowThresholdExceeded(user);
        return  (weeklyThreshold || TransactionsExceeded || borrowThreshold);
    }

    /**
     * This method takes in a transaction and the current user using the system. It then formats a string
     * representation of the transaction by making calls to the different managers in the program.
     * This method should output different strings depending on if the transaction is Permanent/Temporary,
     * One Way/Two Way.
     * @param transaction The transaction whose string representation you want.
     * @param currentUser The user who is currently using the program.
     * @return returns a string representation for the transaction in the perspective of the user who is using the
     * program.
     */
    public String getTransactionString(Transaction transaction, User currentUser){
        String returnString;
        UUID otherUserid = transaction.getOtherUser(currentUser.getUserId());
        String otherUser = um.getTradingUserById(otherUserid).toString();
        if(transaction.isPerm()){
            returnString = "Permanent Transaction with " +otherUser;
        }
        else if (transaction.isVirtual()) {
            returnString = "Virtual Transaction with " + otherUser;
        }
        else {
            returnString = "Temporary Transaction with " + otherUser;
        }
        if(transaction.getItemIdDesired(currentUser.getUserId()) == null) {
            String YourItemString = null;
            try {
                Item YourItem = im.getItem(transaction.getItemIdOwned(currentUser.getUserId()));
                YourItemString = YourItem.toString();
            } catch (InvalidItemException e) {
                System.out.println("Item Manager is not being updated properly");
            }
            return returnString + " to give " + YourItemString;
        }
        if(transaction.getItemIdOwned(currentUser.getUserId()) == null){
            String TheirItemString = null;
            try {
                Item TheirItem = im.getItem(transaction.getItemIdOwned(otherUserid));
                TheirItemString = TheirItem.toString();
            } catch (InvalidItemException e) {
                System.out.println("Item Manager is not being updated  properly");
            }
            return returnString + " to get " + TheirItemString;
        }
        else{
            String TheirItemString = null;
            String YourItemString = null;
            try {
                Item TheirItem = im.getItem(transaction.getItemIdOwned(otherUserid));
                TheirItemString = TheirItem.toString();
                Item YourItem = im.getItem(transaction.getItemIdOwned(currentUser.getUserId()));
                YourItemString = YourItem.toString();
            } catch (InvalidItemException e) {
                System.out.println("Item Manager is not being updated  properly");
            }
            return returnString + " to get " + TheirItemString+ " for " + YourItemString;
        }
    }

    /**
     *  Allows for a frozen TradingUser to request their account to be unfrozen
     *  */
    public void requestUnfreezeAccount() {
        if (currentTradingUser.isFrozen()) {
            am.getFrozenAccounts().add(currentTradingUser);
        }
    }

    /**
     * Returns the ItemManagers
     * @return ItemManager
     */
    public ItemManager getIm(){
        return im;
    }

    /**
     * Returns the CurrentTransactionManager
     * @return CurrentTransactionManager
     */
    public CurrentTransactionManager getTm() { return tm; }

    /**
     * Returns the TradingUserManager
     * @return TradingUserManager
     */
    public TradingUserManager getUm() { return um; }

    /**
     * Returns the PastTransactionManager
     * @return PastTransactionManager
     */
    public PastTransactionManager getPtm(){return ptm;}

    /**
     * Return all the pending items
     * @return allPendingItems
     */
    public Map<Item, TradingUser> getAllPendingItems() {
        return allPendingItems;
    }

    /**
     * Returns the current trading users
     * @return the Current Trading User
     */
    public TradingUser getCurrentTradingUser() {
        return currentTradingUser;
    }

    /**
     * Sets the CurrentTradingUser
     */
    public void setCurrentTradingUser(String username) {
        try {
            currentTradingUser = um.getTradingUser(username);
        } catch(InvalidTradingUserException e) {
            //
        }
    }

    /**
     * Returns the ActionManager
     * @return ActionManager
     */
    public ActionManager getAcm() {
        return acm;
    }

    /**
     * Removes from the Wishlist
     * @param id id of the item to be removed
     */
    public void removeFromWishlist(UUID id){
        currentTradingUser.getWishlist().remove(id);
    }

    /**
     * Removes from Inventory
     * @param id id of the item to be removed
     */
    public void removeFromInventory(UUID id){
        currentTradingUser.getInventory().remove(id);
    }
}