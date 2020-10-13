package Users;

import Transactions.Transaction;

import java.io.Serializable;
import java.util.*;

/**
 * <h1>TransactionHistory</h1>
 * Represents the Transactions.Transaction History of Users.TradingUser by storing information about completed transactions
 */
public class TransactionHistory implements Serializable {
    private final List<UUID> oneWayTransactions;
    private final List<UUID> twoWayTransactions;
    private final HashMap<String, Integer> usersNumTradeTimes;
    private int numItemsLended = 0;
    private int numItemsBorrowed = 0;

    /**
     * Constructs an instance of Users.TransactionHistory with an empty ArrayList transactions and an empty Hashtable usersNumTradeTimes
     * The most recent Transactions.Transaction is added to the end of the list transactions
     */
    public TransactionHistory() {
        oneWayTransactions = new ArrayList<>();
        twoWayTransactions = new ArrayList<>();
        usersNumTradeTimes = new HashMap<>();
    }

    /**
     * @return the three most recent OneWay Transactions
     */
    public List<UUID> mostRecentOneWayTransactions(){ //TH manager

        // if oneWayTransactions are less than 3, just return oneWayTransactions
        if (oneWayTransactions.size() <= 3) {
           return oneWayTransactions;
        } // otherwise, follow this algorithm
        return oneWayTransactions.subList(oneWayTransactions.size() - 3, oneWayTransactions.size());
    }

    /**
     * @return the three most recent TwoWay Transactions.Transaction
     */
    public List<UUID> mostRecentTwoWayTransactions(){
        // if twoWayTransactions are less than 3, just return twoWayTransactions
        if(twoWayTransactions.size() <= 3){
            return twoWayTransactions;
        }
        return twoWayTransactions.subList(twoWayTransactions.size() - 3, twoWayTransactions.size());
    }

    /**
     * @return the usernames of the Users.TradingUser's top three trading partners
     */
    public List<String> mostTradedWithUsers()  {
        // list for the mostTradedWith usernames
        List<String> mostTradedWithUsernames = new ArrayList<>();

        // if usersNumTradTimes size is less than or less than 3
        if(usersNumTradeTimes.size() <= 3){
            Set<String> users = usersNumTradeTimes.keySet();
            mostTradedWithUsernames.addAll(users);
            return mostTradedWithUsernames; }
        int j = 0;
        // if userNumTradeTimes size is greater than 3
        while(j < 3){
            Map.Entry<String, Integer> maxUser = null;
            for (Map.Entry<String, Integer> entry : usersNumTradeTimes.entrySet()) {
                if (maxUser == null || entry.getValue() >= maxUser.getValue()) {
                    maxUser = entry;
                    if(!mostTradedWithUsernames.contains(maxUser.getKey())){
                        mostTradedWithUsernames.add(maxUser.getKey());
                    }
                }
                j++;
            }
        }return mostTradedWithUsernames;
    }

    /**
     * @return an List of all Transactions
     */
    public List<UUID> getAllPastTransactions(){
        // add all the transactions in one list and return it
        List<UUID> allTransactions = new ArrayList<>();
        allTransactions.addAll(oneWayTransactions);
        allTransactions.addAll(twoWayTransactions);
        return allTransactions;
    }

    /**
     * @return all of users and the times they have been traded with
     */
    public HashMap<String, Integer> getUsersNumTradeTimes(){
        return usersNumTradeTimes;
    }

    /**
     * Setter for numItemsLended, increases by 1 every time it is called
     */
    public void setNumItemsLended() { this.numItemsLended++; }

    /**
     * Setter for numItemsBorrowed, increases by 1 every time it is called
     */
    public void setNumItemsBorrowed() { this.numItemsBorrowed++; }

    /**
     * adds a Transactions.Transaction to the Transactions.Transaction History
     * @param transaction the transaction being added to the history
     */

    public void setTransactionHistory(Transaction transaction) {
        if (transaction.isOneWay()) {
            oneWayTransactions.add(transaction.getId());
        } else {
            twoWayTransactions.add(transaction.getId());
        }
    }
    /**
     * Getter for numItemsLended as an int
     * @return numItemsLended as an integer
     */
    public int getNumItemsLended() { return numItemsLended; }

    /**
     * Getter for numItemsBorrowed
     * @return numItemsBorrowed as an integer
     */
    public int getNumItemsBorrowed() { return numItemsBorrowed; }
}
