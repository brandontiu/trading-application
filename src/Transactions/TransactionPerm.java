package Transactions;

import java.util.*;

/**
 * <h1>TransactionTwoWayPerm</h1>
 * This class represents a meetup between 2 Users where there is a two way permanent transaction happening.
 * Note: user 1 originally had item 1 and user 2 originally had item 2.
 */

public class TransactionPerm extends Transaction {


    private Meeting firstMeeting;
    /**
     *This is the constructor which takes in the following parameters and makes an instance of a permanent
     * transaction.
     * @param userToItems This is a map where each user id  is a key. The values stored in this map are lists of
     *                    item ids of the following form: [Item Owned,Item Desired].
     * @param FirstMeeting This is a meeting object representing the first meeting details of the transaction.
     */
    //Constructor with no return time given (default is a month (31 days))
    public TransactionPerm(TreeMap<UUID,List<UUID>> userToItems, Meeting FirstMeeting){
        super(userToItems);
        this.addMeeting(FirstMeeting);
    }

    /**
     * Returns if a Transaction is permanent
     * @return boolean
     */

    @Override
    public boolean isPerm() {
        return true;
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