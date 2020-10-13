package Transactions;

import Items.Item;
import Users.TradingUser;

import java.sql.Time;
import java.util.*;

public class TransactionBuilder {

    private List<Meeting> meetingList;
    private UUID currentUserId;
    private UUID otherUserId;
    private TreeMap<UUID, List<UUID>> userToItems;
    private TransactionFactory transFact;

    /**
     * This is the builder for a transaction. This will be called by GUI/UserMenuController.
     * @param currentUserId The UUID of the current TradingUser using the program.
     * @param transFact An instance of Transaction Factory.
     */
    public TransactionBuilder(UUID currentUserId, TransactionFactory transFact){
        this.currentUserId = currentUserId;
        this.transFact = transFact;
        this.userToItems = new TreeMap<UUID, List<UUID>>();
        this.meetingList = new ArrayList<>();
    }

    /**
     * @param otherUserId The other user that you want to make the transaction with.
     * @param desiredItem The item that the currentUser wants from the transaction.(whether they are borrowing or
     *            keeping it.)
     */
    public void declareIntent(UUID otherUserId, UUID desiredItem){
        this.otherUserId = otherUserId;

        List<UUID> otherUserItemIds = new ArrayList<>();

        otherUserItemIds.add(desiredItem);
        otherUserItemIds.add(null);
        userToItems.put(otherUserId,otherUserItemIds);


        List<UUID> currentUserItemIds = new ArrayList<>();

        currentUserItemIds.add(null);
        currentUserItemIds.add(desiredItem);
        userToItems.put(currentUserId,currentUserItemIds);

    }

    /**
     * Builds the first meeting of a transaction
     * @param location meeting location
     * @param meetingTime time of Meeting
     * @param meetingDate date of Meeting
     */
    public void buildFirstMeeting(String location, Date meetingTime, Date meetingDate){
        meetingList.add(new Meeting(location,meetingTime,meetingDate));
    }

    /**
     * Builds the second meeting of a transaction
     * @param location meeting location
     * @param meetingTime time of Meeting
     * @param meetingDate date of Meeting
     */

    public void buildSecondMeeting(String location, Date meetingTime, Date meetingDate){
        meetingList.add(new Meeting(location,meetingTime,meetingDate));
    }

    /**
     * Allows user to offer an item for transaction
     * @param otherUserItem id of the Item being offered for Transaction
     */

    public void AddItemOffered(UUID otherUserItem){
        this.userToItems.get(currentUserId).set(0,otherUserItem);
        this.userToItems.get(otherUserId).set(1,otherUserItem);
    }

    /**
     * Returns the Transaction created with the inputted information
     * @return the Transaction created with the information provided
     */

    public Transaction getTransaction(){
        return transFact.createTransaction(userToItems,meetingList);
    }

}
