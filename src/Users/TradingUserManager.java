package Users;

import Exceptions.InvalidTradingUserException;
import Items.Item;
import Transactions.TransactionStatuses;
import Transactions.Transaction;

import java.util.*;

/**
 * <h1>TradingUserManager</h1>
 *
 * Manages all TradingUsers in the system.
 * <p>
 * Stores a list of all TradingUsers in the system, all flagged TradingUsers, all frozen Users, and a HashMap mapping between
 *  * TradingUsers and their UUID.
 * </p>
 */
public class TradingUserManager {
    private final List<TradingUser> allTradingUsers;
    private final List<TradingUser> flaggedAccounts;
    private final List<TradingUser> frozenAccounts;
    private Map<UUID, TradingUser> idToUser;

    /**
     * Creates a list of tradingUsers.
     */
    public TradingUserManager(List<TradingUser> tradingUsers, List<TradingUser> flaggedAccounts, List<TradingUser> frozenAccounts) {
        allTradingUsers = tradingUsers;
        this.flaggedAccounts = flaggedAccounts;
        this.frozenAccounts = frozenAccounts;
        userListToMap();
    }

    /**
     * Adds a new user with given info.
     *
     * @param username online identifier of a TradingUser
     * @param password account password
     */
    public void addTradingUser(String username, String password, String city) throws InvalidTradingUserException {
        TradingUser newTradingUser = new TradingUser(username, password);
        newTradingUser.setCity(city);
        if (allTradingUsers.size() == 0) {
            allTradingUsers.add(newTradingUser);
        }
        if (checkAvailableUsername(username)) {
            allTradingUsers.add(newTradingUser);
        } else {
            throw new InvalidTradingUserException();
        }
    }

    /**
     * Retrieves a specific user by username.
     *
     * @param username online identifier of a TradingUser
     * @return username and userId as string separated by comma
     */
    public TradingUser getTradingUser(String username) throws InvalidTradingUserException {
        for (TradingUser tradingUser : allTradingUsers) {
            if ((tradingUser.getUsername().equals(username))) {
                return tradingUser;
            }
        }
        throw new InvalidTradingUserException();// TradingUser does not exist
    }

    /**
     * Returns the user thresholds in a list given a username
     * @param username: the string username of the user
     * @return List of ints (borrowThreshold, weeklyThreshold, incompleteThreshold)
     */
    public List<Integer> getCurrThresholds(String username) {
            ArrayList<Integer> thresholdList = new ArrayList<>();
            try {
                TradingUser tradingUser = getTradingUser(username);
                thresholdList.add(tradingUser.getBorrowThreshold());
                thresholdList.add(tradingUser.getWeeklyThreshold());
                thresholdList.add(tradingUser.getIncompleteThreshold());
                return thresholdList;
            }
            catch(InvalidTradingUserException e){
                return thresholdList;
        }
    }
    /**
     * Retrieves a list of TradingUsers by city.
     * @param city = desired city
     * @return list of TradingUsers in that city
     */
    public List<TradingUser> getTradingUserByCity(String city) {
        List<TradingUser> userList = new ArrayList<>();
        for (TradingUser tradingUser : allTradingUsers)
            //check TradingUser is in the desired city and not on vacation status
            if (sameCity(tradingUser.getCity(),city)&&(!tradingUser.isOnVacation())) {
                userList.add(tradingUser);
            }
        if (userList.size() == 0) return null; // if there are no TradingUser's in this city
        else { return userList; }
    }

    private Boolean sameCity(String city1, String city2){
        String All_upper_city1 = city1.toUpperCase();
        String All_upper_city2 = city2.toUpperCase();
        return All_upper_city1.equals(All_upper_city2);
    }

    /**
     * Adds an item to tradingUser's specified list, which is either the Users.TradingUser's wishlist or inventory.
     *
     * @param tradingUserId the tradingUser's UUID
     * @param item an item in the trading system.
     * @param listType either "wishlist" or "inventory" as a String
     */
    public boolean addItem(UUID tradingUserId, Item item, String listType) {
        TradingUser tradingUser = idToUser.get(tradingUserId);
        if (listType.equals("wishlist")) {
            if (!tradingUser.getWishlist().contains(item.getId())) {
                tradingUser.getWishlist().add(item.getId());
                return true;
            }
        } else if (listType.equals("inventory")) {
            if (!tradingUser.getInventory().contains(item.getId())) {
                tradingUser.getInventory().add(item.getId());
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a item from tradingUser's specified list, which is either the Users.TradingUser's wishlist or inventory.
     *
     * @param tradingUserId a tradingUser's UUID
     * @param item an item in the trading system.
     * @param listType either "wishlist" or "inventory" as a String
     */
    public void removeItem(UUID tradingUserId, Item item, String listType) {
        TradingUser tradingUser = idToUser.get(tradingUserId);
        if (listType.equals("wishlist")) {
            tradingUser.getWishlist().remove(item.getId());
        } else if (listType.equals("inventory")) {
            tradingUser.getInventory().remove(item.getId());
        }
    }

    /**
     * Changes the tradingUser's specified threshold.
     *
     * @param tradingUserId  a tradingUser in the trading system. (the ID of them)
     * @param thresholdValue new value of threshold as an int
     * @param thresholdType  either "borrow", "weekly", or "incomplete" as a String
     */
    public void changeThreshold(UUID tradingUserId, int thresholdValue, String thresholdType) {
        TradingUser tradingUser = idToUser.get(tradingUserId);
        switch (thresholdType) {
            case "Borrow":
                tradingUser.setBorrowThreshold(thresholdValue);
                break;
            case "Weekly":
                tradingUser.setWeeklyThreshold(thresholdValue);
                break;
            case "Incomplete":
                tradingUser.setIncompleteThreshold(thresholdValue);
                break;
        }
    }

    /**
     * Changes the status of a tradingUser's account from active to frozen.
     *
     * @param tradingUser a tradingUser in the trading system.
     */
    public void freezeAccount(TradingUser tradingUser) {
        tradingUser.setStatus(UserStatuses.FROZEN);
        idToUser.get(tradingUser.getUserId()).setStatus(UserStatuses.FROZEN);
        frozenAccounts.add(tradingUser);
    }

    /**
     * Changes the status of a tradingUser's account from frozen to active.
     *
     * @param tradingUser a tradingUser in the trading system.
     */
    public void unfreezeAccount(TradingUser tradingUser) {
        tradingUser.setStatus(UserStatuses.ACTIVE);
        idToUser.get(tradingUser.getUserId()).setStatus(UserStatuses.ACTIVE);
        removeFrozenUsername(tradingUser.getUsername());
    }

    /**
     * Changes the status of a tradingUser's account to vacation.
     *
     * @param tradingUser a tradingUser in the trading system.
     */
    public void onVacation(TradingUser tradingUser){
        tradingUser.setStatus(UserStatuses.VACATION);
        idToUser.get(tradingUser.getUserId()).setStatus(UserStatuses.VACATION);
    }

    /**
     * Adds a transaction to TradingUser's transaction history.
     *
     * @param tradingUser a tradingUser in the trading system.
     * @param transaction a meetup between 2 users.
     */
    public void addToTransactionHistory(TradingUser tradingUser, Transaction transaction) {
        TransactionHistory tH = tradingUser.getTransactionHistory();
        tH.setTransactionHistory(transaction);
        updateTransactionHistoryValues(tradingUser, transaction);
    }

    /**
     * A private helper method for addToTransactionHistory that updates UserNumTradeTimes, NumItemsBorrowed, and NumItemsLended
     *
     * @param tradingUser a tradingUser in a trading system
     * @param transaction a transaction between two Users
     */
    private void updateTransactionHistoryValues(TradingUser tradingUser, Transaction transaction) {
        TransactionHistory tH = tradingUser.getTransactionHistory();
        // if the user is the person giving away the object (user1) in transaction
        if (tradingUser.getUserId().equals(transaction.getUser1())){
            // increment the numLended
            tradingUser.getTransactionHistory().setNumItemsLended();

            // get the username of user2 and see if it's in the idToUser and update it; otherwise, add the username
            String u2 = idToUser.get(transaction.getUser2()).getUsername();
            if (tH.getUsersNumTradeTimes().containsKey(u2)) {
                tH.getUsersNumTradeTimes().put(u2, tH.getUsersNumTradeTimes().get(u2) + 1);
            } else {
                tH.getUsersNumTradeTimes().put(u2, 1);
            }
            if (!transaction.isOneWay()) {
                // if the transaction is a twoway, increment borrowed
                tradingUser.getTransactionHistory().setNumItemsBorrowed();
            }
        } else { // if the user is the person receiving the object (user2) in transaction
            // increment the numBorrowed
            tradingUser.getTransactionHistory().setNumItemsBorrowed();
            // check to see if the other user is in the first user's usersNumTradeTimes list, increment or otherwise add the new user
            String u1 = idToUser.get(transaction.getUser1()).getUsername();
            if (tH.getUsersNumTradeTimes().containsKey(u1)) {
                tH.getUsersNumTradeTimes().put(u1, tH.getUsersNumTradeTimes().get(u1) + 1);
            } else {
                tH.getUsersNumTradeTimes().put(u1, 1);

                // if the transaction is twoway, increment lent
                if (!transaction.isOneWay()) {
                    tradingUser.getTransactionHistory().setNumItemsLended();
                }
            }
        }
    }

    /**
     * Returns a list of all TradingUsers in the Trading System.
     *
     * @return all tradingUsers in the system.
     */
    public List<TradingUser> getAllTradingUsers() {
        return allTradingUsers;
    }

    public List<String> getAllTradingUsersUsernames(){
        List<String> userList = new ArrayList<>();
        for(User user : allTradingUsers){
            String username = user.getUsername();
            userList.add(username);
        }
        return userList;
    }

    /**
     * Returns whether this TradingUser with the given username and password is in the system or not.
     *
     * @param username the username of the specified user
     * @param password the password of the specified user
     * @return boolean if this user account is already in the system or not.
     */
    public boolean validUser(String username, String password) {
        for (TradingUser tradingUser : allTradingUsers) {
            if (tradingUser.getUsername().equals(username) && tradingUser.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a list of TradingUsers that have had their account flagged to be frozen automatically by the system
     *
     * @return list of flagged to be frozen TradingUsers
     */
    public List<TradingUser> getFlaggedAccounts() {
        return flaggedAccounts;
    }

    /**
     * Remove a user from flaggedAccounts if a user with username is in the list of flagged accounts
     * @param username the String username of the user you want to remove
     */
    public void removeFlaggedUsername(String username){
        flaggedAccounts.removeIf(currUser -> currUser.getUsername().equals(username));
    }

    /**
     * Remove a user from frozenAccounts if a user with username is in the list of frozen accounts
     * @param username the String username of the user you want to remove
     */
    public void removeFrozenUsername(String username){
        frozenAccounts.removeIf(currUser -> currUser.getUsername().equals(username));
    }
    /**
     * Retrieves a list of TradingUsers that have had their account frozen after approval by Admin.
     *
     * @return list of frozen TradingUsers
     */
    public List<TradingUser> getFrozenAccounts() {
        return frozenAccounts;
    }

    /**
     * Returns of the number of current transactions of TradingUser exceed the incomplete transaction threshold
     *
     * @param tradingUser TradingUser of interest
     * @return true iff incompleteTransactionExceeded
     */
    public boolean incompleteTransactionExceeded(TradingUser tradingUser) {
        return tradingUser.getCurrentTransactions().size() >= tradingUser.getIncompleteThreshold();
    }

    /**
     * Takes Transaction and moves it from both involved TradingUser's currentTransactions to their TransactionHistory.
     *
     * @param transaction the Transaction being moved.
     */
    public boolean moveTransactionToTransactionHistory(Transaction transaction) {
        TransactionStatuses status = transaction.getStatus();
        TradingUser user1 = getTradingUserById(transaction.getUser1());
        TradingUser user2 = getTradingUserById(transaction.getUser2());
        if (status.equals(TransactionStatuses.INCOMPLETE) || status.equals(TransactionStatuses.COMPLETED) || status.equals(TransactionStatuses.NEVERRETURNED)) {
            UUID id = transaction.getId();
            user1.getCurrentTransactions().remove(id);
            user2.getCurrentTransactions().remove(id);
            addToTransactionHistory(user1, transaction);
            addToTransactionHistory(user2, transaction);
            return true;
        }
        return false;
    }

    /**
     * Checks whether the input username is valid.
     *
     * @param username online identifier of a Users.TradingUser
     * @return True or False as boolean
     */
    public boolean checkAvailableUsername(String username) {
        for (TradingUser user : allTradingUsers) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

    private void userListToMap() {
        idToUser = new HashMap<>();
        for (TradingUser user : allTradingUsers) {
            idToUser.put(user.getUserId(), user);
        }
    }

    /**
     * Retrieves tradingUser by userId
     *
     * @param id id of tradingUser
     * @return a tradingUser
     */
    public TradingUser getTradingUserById(UUID id) {
        return idToUser.get(id);
    }

    /**
     * Retrieves a List of TradingUsers usernames by a List of userIDs
     * @param ids List of userIDs
     * @return usernames List of TradingUser Usernames
     */

    public List<String> getUsernameListByID(List<UUID> ids){
        List<String> usernames = new ArrayList<>();
        for (UUID id : ids){
            usernames.add(getTradingUserById(id).getUsername());
        }
        return usernames;
    }

    /**
     * Handles the item changes from both users wishlists and inventory when the item(s) is involved in a permanent
     * transaction.
     * This method assumes that user2 was the user who made the transaction(consistent with
     * the assumption made in transaction class).
     * @param transaction the transaction involved.
     */
    protected void handlePermTransactionItems(Transaction transaction) { // if permanent transaction
        if (transaction.getStatus().equals(TransactionStatuses.COMPLETED)) {
            List<UUID> itemidlist = transaction.getTransactionItems();
            TradingUser user1 = this.getTradingUserById(transaction.getUser1());
            TradingUser user2 = this.getTradingUserById(transaction.getUser2());
            if (itemidlist.size() == 2) {
                user1.removeFromWishlist(transaction.getItemIdDesired(user1.getUserId()));
                user2.removeFromWishlist(transaction.getItemIdDesired(user2.getUserId()));
                user1.getInventory().remove(transaction.getItemIdOwned(user1.getUserId()));
                user2.getInventory().remove(transaction.getItemIdOwned(user2.getUserId()));
                user2.getInventory().add(transaction.getItemIdDesired(user2.getUserId()));
                user1.getInventory().add(transaction.getItemIdDesired(user1.getUserId()));
            } else if (itemidlist.size() == 1) { // user 1 giving to user 2
                user2.removeFromWishlist(transaction.getItemIdDesired(user2.getUserId()));
                user1.getInventory().remove(transaction.getItemIdOwned(user1.getUserId()));
                user2.getInventory().add(transaction.getItemIdDesired(user2.getUserId()));
            }
        }
    }

    /**
     * Handles the item changes from both users wishlists and inventory when the item(s) is involved in a temporary
     * transaction.
     * This method assumes that user2 was the user who made the transaction(consistent with
     * the assumption made in transaction class).
     * @param transaction the transaction involved.
     */
    protected void handleTempTransactionItems(Transaction transaction) { // if temporary transaction
        if (transaction.getStatus().equals(TransactionStatuses.TRADED)) { // after first meeting
            List<UUID> itemidlist = transaction.getTransactionItems();
            TradingUser user1 = this.getTradingUserById(transaction.getUser1());
            TradingUser user2 = this.getTradingUserById(transaction.getUser2());
            if (itemidlist.size() == 2) {
                user1.removeFromWishlist(transaction.getItemIdDesired(user1.getUserId()));
                user2.removeFromWishlist(transaction.getItemIdDesired(user2.getUserId()));
                user1.getInventory().remove(transaction.getItemIdOwned(user1.getUserId()));
                user2.getInventory().remove(transaction.getItemIdOwned(user2.getUserId()));
            } else if (itemidlist.size() == 1) { // user 1 giving to user 2
                user2.removeFromWishlist(transaction.getItemIdDesired(user2.getUserId()));
                user1.getInventory().remove(itemidlist.get(0));
            }
        }
        if (transaction.getStatus().equals(TransactionStatuses.COMPLETED)) { // after second meeting
            List<UUID> itemidlist = transaction.getTransactionItems();
            TradingUser user1 = this.getTradingUserById(transaction.getUser1());
            TradingUser user2 = this.getTradingUserById(transaction.getUser2());
            if (itemidlist.size() == 2) {
                user1.getInventory().add(transaction.getItemIdOwned(user1.getUserId()));
                user2.getInventory().add(transaction.getItemIdOwned(user2.getUserId()));
            } else if (itemidlist.size() == 1) { // user 1 giving to user 2
                user1.getInventory().add(transaction.getItemIdOwned(user1.getUserId()));
            }
        }
    }

    /**
     * Handles the item changes from both users wishlists and inventory when the item(s) is involved in a virtual
     * transaction.
     * This method assumes that user2 was the user who made the transaction(consistent with
     * the assumption made in transaction class).
     * @param transaction the transaction involved.
     */

    public void handleVirtTransactionItems(Transaction transaction) {
        if (transaction.getStatus().equals(TransactionStatuses.COMPLETED)){
            List<UUID> itemidlist = transaction.getTransactionItems();
            TradingUser user1 = this.getTradingUserById(transaction.getUser1());
            TradingUser user2 = this.getTradingUserById(transaction.getUser2());
            if (itemidlist.size() == 2) {
                user1.removeFromWishlist(transaction.getItemIdDesired(user1.getUserId()));
                user2.removeFromWishlist(transaction.getItemIdDesired(user2.getUserId()));
                user1.getInventory().remove(transaction.getItemIdOwned(user1.getUserId()));
                user2.getInventory().remove(transaction.getItemIdOwned(user2.getUserId()));
                user2.getInventory().add(transaction.getItemIdDesired(user2.getUserId()));
                user1.getInventory().add(transaction.getItemIdDesired(user1.getUserId()));
            } if (itemidlist.size() == 1) { // user 1 giving to user 2
                user2.removeFromWishlist(transaction.getItemIdDesired(user2.getUserId()));
                user1.getInventory().remove(transaction.getItemIdOwned(user1.getUserId()));
                user2.getInventory().add(transaction.getItemIdDesired(user2.getUserId()));
            }
        }
    }

    /**
     * Returns whether the TradingUser's borrow threshold has been exceeded.
     *
     * @param tradingUser which TradingUser to check
     * @return boolean whether the threshold has been exceeded.
     */
    public boolean borrowThresholdExceeded(TradingUser tradingUser) {
        int numBorrowed = tradingUser.getTransactionHistory().getNumItemsBorrowed();
        int numLent = tradingUser.getTransactionHistory().getNumItemsLended();
        int threshold = tradingUser.getBorrowThreshold();
        return numBorrowed - numLent >= threshold;
    }

    /**
     * Returns a list of usernames corresponding to the TradingUser's in flaggedAccounts.
     * @return a list of flagged TradingUser's usernames.
     */
    public List<String> convertFlaggedUsersToUsernames() {
        List<String> usernames = new ArrayList<>();
        for (TradingUser user : flaggedAccounts) {
            usernames.add(user.getUsername());
        }
        return usernames;
    }

    /**
     * Returns a list of usernames corresponding to the TradingUser's in frozenAccounts.
     * @return a list of flagged TradingUser's usernames.
     */
    public List<String> convertFrozenUsersToUsernames() {
        List<String> usernames = new ArrayList<>();
        for (TradingUser user : frozenAccounts) {
            usernames.add(user.getUsername());
        }
        return usernames;
    }

    /**
     * Changes the password of a TradingUser
     * @param user user of interest
     * @param password new password
     */
    public void changePassword(TradingUser user, String password) {
        user.setPassword(password);
    }
}
