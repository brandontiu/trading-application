package Users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <h1>TradingUser</h1>
 * Represents a TradingUser in the trading system
 */
public class TradingUser extends User implements Serializable {

    private final TransactionHistory transactionHistory;
    private final List<UUID> currentTransactions;
    private final List<UUID> inventory;
    private int borrowThreshold = 1;
    private int weeklyThreshold = 3;
    private int incompleteThreshold = 2;
    private final List<UUID> wishlist;
    private UserStatuses status = UserStatuses.ACTIVE;
    private String city;

    /**
     * Constructs an instance of TradingUser based on Strings of username, password, and email.
     * @param username online identifier of a TradingUser
     * @param password account password
     */
     public TradingUser(String username, String password) {
         super(username,password);
         currentTransactions = new ArrayList<>();
         inventory = new ArrayList<>();
         wishlist = new ArrayList<>();
         transactionHistory = new TransactionHistory();
     }

    /**
     * Getter for this TradingUser's transactionHistory as list of Transactions they have previously been involved with.
     * @return list of Transactions.Transaction objects
     */
    public TransactionHistory getTransactionHistory() { return transactionHistory; }

    /**
     * Getter for this Users.TradingUser's TransactionDetails.
     * @return list of Transactions.Transaction objects
     */
    public List<UUID> getCurrentTransactions() { return currentTransactions; }

    /**
     * Getter for this Users.TradingUser's inventory as a list of (approved) Items.
     * @return list of Items
     */
    public List<UUID> getInventory() {
        return this.inventory;
    }

    /**
     * Getter for this TradingUser's wishlist as a list of Items.
     * @return list of Items
     */
    public List<UUID> getWishlist() {
        return wishlist;
    }

    /**
     * Getter for this TradingUser's city.
     * @return city as String
     */
    public String getCity() { return city; }

    /**
     * Setter for this TradingUser's city.
     * @param city TradingUser's city
     */
    public void setCity(String city) { this.city = city; }

    /**
     * This method checks for an item id in the wishlist and removes it if found.
     * @param itemId the item id of the item you wish to remove.
     * @return returns true iff it found the item and it removed.
     */
    public boolean removeFromWishlist(UUID itemId){
        if(wishlist.contains(itemId)){
            wishlist.remove(itemId);
            return(true);
        }
        else{
            return(false);
        }
    }


    /**
     * This method checks for an item id in the wishlist and adds it if not found.
     * @param itemId the item id of the item you wish to remove.
     * @return returns true iff it found the item and it removed.
     */
    public boolean addToWishlist(UUID itemId){
        if(!wishlist.contains(itemId)){
            wishlist.add(itemId);
            return(true);
        }
        else{
            return(false);
        }
    }

    /**
     * Getter for the minimum number of Items that this Users.TradingUser has to have lent before they can borrow an Items.Item.
     * @return borrowThreshold as an integer
     */
    public int getBorrowThreshold() { return borrowThreshold; }

    /**
     * Setter for the minimum number of Items that this Users.TradingUser has to have lent before they can borrow an Items.Item.
     * @param borrowThreshold as an integer
     */
    public void setBorrowThreshold(int borrowThreshold) { this.borrowThreshold = borrowThreshold; }

    /**
     * Getter for maximum number of Transactions that this Users.TradingUser can make in a given week.
     * @return weeklyThreshold as an integer
     */
    public int getWeeklyThreshold() { return weeklyThreshold; }

    /**
     * Setter for maximum number of Transactions that this Users.TradingUser can make in a given week.
     * @param weeklyThreshold as an integer
     */
    public void setWeeklyThreshold(int weeklyThreshold) { this.weeklyThreshold = weeklyThreshold; }

    /**
     * Getter for maximum number of incomplete Transactions that this Users.TradingUser can make before their account is frozen.
     * @return incompleteThreshold as an integer
     */
    public int getIncompleteThreshold() { return incompleteThreshold; }

    /**
     * Setter for maximum number of incomplete Transactions that this Users.TradingUser can make before their account is frozen.
     * @param incompleteThreshold as an integer
     */
    public void setIncompleteThreshold(int incompleteThreshold) { this.incompleteThreshold = incompleteThreshold; }

    /**
     * Getter for the current Users.TradingUser's account status, which can be active or frozen.
     * @return status as a String
     */
    public UserStatuses getStatus() { return status; }

    /**
     * Setter for the current Users.TradingUser's account status, which can be active or frozen.
     * @param status as a String
     */
    public void setStatus(UserStatuses status) { this.status = status; }

    /**
     * Represents the current Users.TradingUser by their username and userId
     * @return the username and userid separated by a comma
     */
    @Override
    public String toString() { return getUsername(); }

    /**
     * Represents the status of this TradingUser as a boolean.
     * @return true if this TradingUser's status is frozen, false otherwise.
     */
    public boolean isFrozen(){
        return status.equals(UserStatuses.FROZEN);
    }

    /**
     * Represents the status of this TradingUser as a boolean.
     * @return true if this TradingUser's status is vacation, false otherwise.
     */
    public boolean isOnVacation(){
        return status.equals(UserStatuses.VACATION);
    }
}