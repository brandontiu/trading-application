package Transactions;

import java.util.*;

/**
 * <h1>TransactionTwoWayTemp</h1>
 * This class represents a meetup between 2 Users where there is a two way borrowing transaction happening.
 * Note: user 1 originally had item 1 and user 2 originally had item 2.
 */
public class TransactionTemp extends Transaction {

    /**
     * Constructor for Transactions.TransactionTwoWayPerm class. This constructor initializes a 2 way permanent transaction with
     * @param userToItems A hashmap which maps userId's to a list of
     *      Item ids(where the list is in the form of [ItemId owned, ItemId wanted]).
     * @param firstMeeting This is just a meeting object representing where the users will meet for the first time.
     * @param secondMeeting The second meeting in the transaction where the users return the items.
     */
    //Constructor with no return time given (default is a month (31 days))
    public TransactionTemp(TreeMap<UUID, List<UUID>> userToItems, Meeting firstMeeting, Meeting secondMeeting){
        super(userToItems);
        this.addMeeting(firstMeeting);
        this.addMeeting(secondMeeting);
    }

    /**
     * Getter for the second meeting.
     * @return returns the second meeting of the transaction.
     */

    public Meeting getSecondMeeting(){
        return this.getTransactionMeetings().get(1);
    }

    /**
     * getter for the first (and only) meeting of this transaction.
     * @return returns the first meeting of this transaction.
     */
    public Meeting getFirstMeeting() {
        return this.getTransactionMeetings().get(0);
    }

    /**
     * Returns if a Transaction is permanent
     * @return boolean
     */

    @Override
    public boolean isPerm() {
        return false;
    }

    /**
     * Returns if a Transaction is virtual
     * @return boolean
     */
    @Override
    public boolean isVirtual() {
        return false;
    }

    /**
     * Returns a list of Meetings associated with a Transaction
     * @return a list of Meetings
     */
}