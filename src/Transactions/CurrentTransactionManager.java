package Transactions;
import Actions.ActionManager;
import Actions.AddOrDeleteAction;
import Actions.EditAction;
import Exceptions.InvalidTransactionException;
import Users.TradingUser;

import java.sql.Time;
import java.util.*;

/**
 * <h1>CurrentTransactionManager</h1>
 * Manages a transaction between two users. A transaction begins once a user expresses interest in an item.
 */
public class CurrentTransactionManager extends TransactionManager {
    private ActionManager acm;
    private final TransactionStatusStrategy ss;
    /**
     * Constructs an instance of the CurrentTransactionManager.
     * @param transactions a map of all transactions to their UUID
     */
    public CurrentTransactionManager(Map<UUID, Transaction> transactions, ActionManager actionManager) {
        super(transactions);
        this.acm = actionManager;
        this.ss = new TransactionStatusStrategy();
    }

    /**
     * Determines if the user who is editing a transaction or meeting is user1 or user2
     * @param transaction the transaction that the user wants to work with
     * @param userId the UUID of the user
     * @return an integer either 1 or 2
     */
    public int findUserNum(Transaction transaction, UUID userId){
        return transaction.getUsers().indexOf(userId) + 1;
    }

    /**
     * Edits a meeting using overloading to selectively edit the location
     * @param meetingNum the meeting number that the user wants to edit
     * @param transactionId the transaction to which the meeting belongs to
     * @param userId the UUID of the Users.TradingUser who want to edit the transaction
     * @param newLocation the new location that the user want to the meeting to take place
     * @return True if the meeting was successfully edited
     */
    public boolean editMeeting(int meetingNum, UUID transactionId, UUID userId, String newLocation) throws InvalidTransactionException {
        Transaction transaction = getTransactionFromId(transactionId);
        int userNum = findUserNum(transaction, userId);
        List<Meeting> list = transaction.getTransactionMeetings();
        Meeting meeting = list.get(meetingNum);
        if (canEdit(meeting, userNum) && transaction.getStatus().equals(TransactionStatuses.PENDING)) {
            meeting.setLocation(newLocation);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Edits a meeting using overloading to selectively edit the time
     * @param meetingNum the meeting number that the user wants to edit
     * @param transactionId the transaction to which the meeting belongs to
     * @param userId the UUID of the TradingUser who want to edit the transaction
     * @param date the new time/date the user want to have the meeting take place, must be in Date format
     * @return boolean whether the meeting was successfully edited or not
     */
    public boolean editMeeting(int meetingNum, UUID transactionId, UUID userId, Date date, boolean ifDate) throws InvalidTransactionException {
        Transaction transaction = getTransactionFromId(transactionId);
        int userNum = findUserNum(transaction, userId);
        Meeting meeting = transaction.getTransactionMeetings().get(meetingNum);
        if (canEdit(meeting, userNum) && transaction.getStatus().equals(TransactionStatuses.PENDING)) {
            meeting.setDate(date);
            return true;
        } else {
            return false;
        }
    }

    public boolean editMeeting(int meetingNum, UUID transactionId, UUID userId, Date time) throws InvalidTransactionException {
        Transaction transaction = getTransactionFromId(transactionId);
        int userNum = findUserNum(transaction, userId);
        Meeting meeting = transaction.getTransactionMeetings().get(meetingNum);
        if (canEdit(meeting, userNum) && transaction.getStatus().equals(TransactionStatuses.PENDING)) {
            meeting.setTime(time);
            return true;
        } else {
            return false;
        }
    }

    public void updateEdits(int meetingNum, UUID transactionId, UUID userId) throws InvalidTransactionException {
        Transaction transaction = getTransactionFromId(transactionId);
        int userNum = findUserNum(transaction, userId);
        Meeting meeting = transaction.getTransactionMeetings().get(meetingNum);
        meeting.userEdits(userNum);
    }

    protected boolean canEdit(Meeting meeting, int userNum) {
        return meeting.getNumEditsUser(userNum) < meeting.getMaxNumEdits();
    }

    /**
     * This method is ONLY allowed to be used in the createTransactionMenu
     *
     * DO NOT USE THIS METHOD. THERE ARE NO EXCEPTIONS WRITTEN AND THIS METHOD IS OUT OF PLACE
     * THIS WILL HAVE TO BE MOVED SOMEWHERE ELSE
     * @param user1 one of the users in transaction.
     * @param user2 one of the users in transaction.
     * @param newTransaction the actual transaction object. (for which method will get ids for)
     */
    public void updateUsersCurrentTransactions(TradingUser user1,TradingUser user2,Transaction newTransaction){
        user1.getCurrentTransactions().add(newTransaction.getId());
        user2.getCurrentTransactions().add(newTransaction.getId());
    }

    /**
     * @param transaction the transaction who's status is being updated
     * @return true if the status of the transaction has been updated, the transaction status will we updated based on
     * user input by changing their status user
     */
    public boolean updateStatus(Transaction transaction){
        return (ss.noMeetingComplete(transaction) | ss.pendingToCancelled(transaction) | ss.pendingToConfirmed(transaction) | ss.confirmedToTraded(transaction) |
                ss.confirmedToIncomplete(transaction) | ss.confirmedToComplete(transaction) | ss.tradedToComplete(transaction) |
                ss.tradedToNeverReturned(transaction));
    }

    /**
     * returns if one of the users statuses are pending
     * @param transaction which transaction
     * @return boolean
     */
    public boolean userStatuses(Transaction transaction) {
        return !(transaction.getStatusUser1().equals(TransactionStatuses.PENDING) | transaction.getStatusUser2().equals(TransactionStatuses.PENDING));
    }

    /**
     * Updates the inputted transaction's status depending on what the TradingUser selected.
     * @param tradingUser currently logged in TradingUser
     * @param transaction inputted transaction
     * @param optionChosen which option has been chosen by the currently logged in TradingUser
     */
    public void updateStatusUser(TradingUser tradingUser, Transaction transaction, TransactionActions optionChosen){
        UUID userId = tradingUser.getUserId();
        if (optionChosen.equals(TransactionActions.CONFIRMMEETINGDETAILS)){
            transaction.setStatusUserID(TransactionStatuses.CONFIRMED, userId);
        }
        if (optionChosen.equals(TransactionActions.CANCEL)){
            transaction.setStatusUserID(TransactionStatuses.CANCELLED, userId);
        }
        if (optionChosen.equals(TransactionActions.CONFIRMMEETUP)) {
            transaction.setStatusUserID(TransactionStatuses.TRADED, userId);
        }
        if (optionChosen.equals(TransactionActions.MEETUPINCOMPLETE)) {
            transaction.setStatusUserID(TransactionStatuses.INCOMPLETE, userId);
        }
        if (optionChosen.equals(TransactionActions.ITEMRETURNED)){
            transaction.setStatusUserID(TransactionStatuses.COMPLETED, userId);
        }
        if (optionChosen.equals(TransactionActions.ITEMNOTRETURNED)){
            transaction.setStatusUserID(TransactionStatuses.NEVERRETURNED, userId);
        }
        if (optionChosen.equals(TransactionActions.EDITED)){
            transaction.setStatusUserID(TransactionStatuses.PENDING, userId);
        }
    }

}