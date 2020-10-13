package Admins;

import Actions.ActionManager;
import Actions.AddOrDeleteAction;
import Actions.EditAction;
import Exceptions.InvalidAdminException;
import Exceptions.InvalidItemException;
import Exceptions.InvalidTradingUserException;
import Exceptions.InvalidTransactionException;
import Items.Item;
import Items.ItemManager;
import Transactions.Meeting;
import Transactions.PastTransactionManager;
import Transactions.Transaction;
import Users.TradingUser;
import Users.TradingUserManager;

import java.util.*;

/**
 * <h1>AdminMenuController</h1>
 * Decides which use case/manager methods to call depending on the user input taken from the presenter.
 * <p>It stores instances of all use cases/managers (AdminManager, TradingUserManager, ItemManager), the AdminMenuPresenter,
 *  * and a list of allPendingItems (which is the list of all items that have been requested by a TradingUser to be added
 *  * to their inventory). <p/>
 */
public class AdminMenuController {
    private AdminUser currentAdmin = null; // admin that's logged in
    private final AdminManager am;
    private final TradingUserManager um;
    protected final Map<Item, TradingUser> allPendingItems;
    private final ItemManager im;
    private final ActionManager acm;
    private final PastTransactionManager ptm;

    /**
     * Constructs an instance an AdminMenuController.
     * @param adminManager manager of all AdminUsers
     * @param tradingUserManager manager of all TradingUsers
     * @param pendingItems list of all pending items that have been requested by users to be approved
     * @param items manager of all Items
     */
    public AdminMenuController(AdminManager adminManager, TradingUserManager tradingUserManager,
                               Map<Item, TradingUser> pendingItems, ItemManager items, ActionManager actionManager,
                               PastTransactionManager pastTransactionManager) {
        allPendingItems = pendingItems;
        um = tradingUserManager;
        am = adminManager;
        im = items;
        acm = actionManager;
        ptm = pastTransactionManager;
    }

    /**
     * Adds the item to the TradingUser's inventory if the admin has approved the pending item.
     * @param item the Item that has been approved.
     */
    public void approvePendingItem(Item item) {
        try {
            String username = allPendingItems.get(item).getUsername();
            TradingUser user = um.getTradingUser(username);
            im.addItem(item); // add to allItems master list of all existing items
            um.addItem(user.getUserId(), item, "inventory"); // add item to the TradingUser's inventory
            allPendingItems.remove(item);
            AddOrDeleteAction action = new AddOrDeleteAction(user.getUserId());
            action.setIsInventory();
            action.setAdded(item);
            acm.addAction(user.getUserId(), action);
        } catch (InvalidTradingUserException e) {
            //
        }
    }

    /**
     * creates a new admin which can only be done by the first admin
     * @param username user's account name identifier
     * @param password user's account password
     * @return true iff admin user is successfully created
     */
    public boolean createAdmin(String username, String password){
        boolean isSuccessful = false;
        if (currentAdmin.isFirstAdmin()) {
            try{
                am.addAdmin(username, password);// adds the new admin to the list of all AdminUsers
                isSuccessful = true;
            } catch (InvalidAdminException e) {//username taken
                }
        }
        return isSuccessful;
    }

    /**
     * Manually adds an item to any TradingUser's inventory or wishlist
     * @param tradingUserId trading user in the system
     * @param item an item in the system
     * @param listType either "wishlist" or "inventory" as a stirng
     * @return true iff item has been added successfully
     * @throws NullPointerException
     */
    public boolean addItemToUser(UUID tradingUserId, Item item, String listType) throws NullPointerException{
        boolean isSuccessful = false;
        if(listType.equals("wishlist")) {
            AddOrDeleteAction action = new AddOrDeleteAction(tradingUserId);
            action.setIsWishlist();
            action.setAdded(item);
            acm.addAction(tradingUserId, action);
            um.addItem(tradingUserId, item, "wishlist");
            im.addItem(item);
            isSuccessful = true;
        }else if (listType.equals("inventory")){
            AddOrDeleteAction action = new AddOrDeleteAction(tradingUserId);
            action.setIsInventory();
            action.setAdded(item);
            acm.addAction(tradingUserId, action);
            um.addItem(tradingUserId, item, "inventory");
            im.addItem(item);
            isSuccessful = true;
        }
        return isSuccessful;
    }

    /* helper method for changeUserThreshold */
    public void updateThreshold(String username, int newThreshold, String whichThreshold) throws InvalidTradingUserException {
        um.changeThreshold(um.getTradingUser(username).getUserId(), newThreshold, whichThreshold);
    }

    /**
     * Returns whether an admin with the inputted username and password exists.
     * @param username input username
     * @param password input password
     * @return true if admin with the input username and password exists in the system.
     */
    public boolean validAdmin(String username, String password) {
        return am.validAdmin(username, password);
    }

    public void setCurrentAdmin(String username) {
        try {
            currentAdmin = am.getAdmin(username);
        } catch (InvalidAdminException e) {
            //
        }
    }

    /**
     * Getter for PastTransactionManager
     * @return PastTransactionManager
     */
    public PastTransactionManager getPTM(){return this.ptm;}
    /**
     * Getter for a TradingUserManager.
     * @return TradingUserManager
     */
    public TradingUserManager getTUM(){
        return this.um;
    }

    /**
     * Getter for the list of all trading users in the trading system.
     * @return list of all TradingUsers in the system.
     */
    public List<TradingUser> getAllTradingUsers() {
        return um.getAllTradingUsers();
    }

    /**
     * Getter for the currently logged-in admin user.
     * @return AdminUser currently logged in.
     */
    public AdminUser getCurrentAdmin() {
        return currentAdmin;
    }

    /**
     * Returns a map of all pending items that TradingUser's have requested to be added to their inventory.
     * @return Map of each pending item and the TradingUser that requested it.
     */
    public Map<Item, TradingUser> getAllPendingItems() {
        return allPendingItems;
    }

    /**
     * This method takes the toString of an item and then returns the owner of that item if the item is in allPendingItems
     * @param searchItem the toString of the item
     * @return a TradingUser who requested the item to be added to his inventory
     * @throws InvalidItemException
     */
    public TradingUser getItemOwner(Item searchItem) throws InvalidItemException {
        for (Item item : allPendingItems.keySet()){
            if (item.getId().equals(searchItem.getId())){
                return allPendingItems.get(item);
            }
        }
       throw new InvalidItemException("item not found in allPendingItems");
    }

    /**
     * Getter for an AdminManager
     * @return AdminManager
     */
    public AdminManager getAm() {
        return am;
    }

    /**
     * Getter for an ItemManager.
     * @return ItemManager
     */
    public ItemManager getIm() {
        return im;
    }

    /**
     * Getter for an ActionManager.
     * @return ActionManager
     */
    public ActionManager getAcm() {
        return acm;
    }

    /**
     * Removes an item from the list of all pending items if the admin has decided to reject it.
     * @param item item that was rejected
     */
    public void rejectPendingItem(Item item) {
        allPendingItems.remove(item);
    }

    /**
     * Returns the inventory/wishlist to the previous state by adding or removing an item from the corresponding list,
     * depending on the action that is being undone.
     * @param action an AddOrDeleteAction
     */
    public void undoAddOrDeleteAction(AddOrDeleteAction action) {
        if (action.wasAdded()) {
            if (action.isInventory()) {
                um.removeItem(action.getUser(), action.getItem(), "inventory");
                acm.getAllActions().get(action.getUser()).remove(action);
                removeUserFromAllActions(action.getUser());
                acm.removeAction(action);
            } else if (action.isWishlist()) {
                um.removeItem(action.getUser(), action.getItem(), "wishlist");
                acm.getAllActions().get(action.getUser()).remove(action);
                removeUserFromAllActions(action.getUser());
                acm.removeAction(action);
            }
        } else if (action.wasRemoved()) {
            if (action.isInventory()) {
                um.addItem(action.getUser(), action.getItem(), "inventory");
                acm.getAllActions().get(action.getUser()).remove(action);
                removeUserFromAllActions(action.getUser());
                acm.removeAction(action);
            } else if (action.isWishlist()) {
                um.addItem(action.getUser(), action.getItem(), "wishlist");
                acm.getAllActions().get(action.getUser()).remove(action);
                removeUserFromAllActions(action.getUser());
                acm.removeAction(action);
            }
        }
    }

    /**
     * Returns the Meeting of a Transaction to its previous state before the most recent edit was made.
     * @param action an EditAction
     */
    public void undoEditAction(EditAction action) {
        try {
            Transaction t = ptm.getTransactionFromId(action.getTransaction().getId());
            List<Meeting> meetings = t.getTransactionMeetings();
            meetings.set(action.getWhichMeeting(), action.getPreviousMeeting());
            t.setMeetings(meetings);
            acm.removeAction(action);
        } catch (InvalidTransactionException e) {
            System.out.println("An exception was caught :(");
        }
    }

    /**
     * removes user from list of all actions if that user has no undoable actions
     */
    private void removeUserFromAllActions(UUID userId) {
        if (acm.getAllActions().get(userId).isEmpty()) {
            acm.getAllActions().remove(userId);
        }
    }

}