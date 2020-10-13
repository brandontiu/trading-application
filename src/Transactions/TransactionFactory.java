package Transactions;

import Users.TradingUser;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

public class TransactionFactory {
    private CurrentTransactionManager tm;

    public TransactionFactory(CurrentTransactionManager tm){
        this.tm= tm;
    }

    /**
     * This method is to create a transaction given a list of meetings. The length of the list of meetings determines
     * which transaction that is made.
     *
     * meetings.size() == 2 --> Temporary Transaction
     *
     * meetings.size() == 1 --> Permanent Transaction
     *
     * meetings.size() == 0 --> Virtual Transaction
     * @param userToItems A hashmap which maps userId's to a list of
     *                    Item ids(where the list is in the form of [ItemId owned, ItemId wanted])
     * @param meetings This is a list of meetings
     * @return This returns the Transaction that was made.
     */
    public Transaction createTransaction(TreeMap<UUID,List<UUID>> userToItems, List<Meeting> meetings){
        if(meetings.size() ==0){
            Transaction transaction = new TransactionVirtual(userToItems);
            UUID id = transaction.getId();
            tm.getAllTransactions().put(id, transaction);
            return transaction;
        }
        if(meetings.size() == 1){
            Transaction transaction = new TransactionPerm(userToItems, meetings.get(0));
            UUID id = transaction.getId();
            tm.getAllTransactions().put(id, transaction);
            return transaction;
        }
        if(meetings.size() == 2){
            Transaction transaction = new TransactionTemp(userToItems, meetings.get(0), meetings.get(1));
            UUID id = transaction.getId();
            tm.getAllTransactions().put(id, transaction);
            return transaction;
        }
        return null;
    }

}
