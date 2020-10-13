package Transactions;

import Exceptions.InvalidTransactionException;

import java.util.*;

/**
 * <h1>TransactionManager</h1>
 * Manages all transactions in the system.
 */
public class TransactionManager {
    private Map<UUID, Transaction> allTransactions;
    public TransactionManager(Map<UUID, Transaction> transactions) {
        allTransactions = transactions;
    }


    /**
     * The getter that returns all of the Transactions across the whole system
     * @return allTransactions, a HashMap<UUID, Transaction>
     */
    public Map<UUID, Transaction> getAllTransactions() {
        return allTransactions;
    }

    public void removeTransactionFromAllTransactions(UUID id) throws InvalidTransactionException {
        if (allTransactions.containsKey(id)){
            allTransactions.remove(id);}
        else {
            throw new InvalidTransactionException();}
    }

    /**
     * get the transaction from the list of all of the transactions by calling the id
     * @param id the UUID of the transaction
     * @return the transaction
     */
    public Transaction getTransactionFromId(UUID id) throws InvalidTransactionException {
        if (allTransactions.containsKey(id)){
            return allTransactions.get(id);}
        else{
            throw new InvalidTransactionException();
        }
    }

    /**
     * Put a list of ids and get an ArrayList of Transactions back
     * @param transactionList : the list of transactions ids to be converted into Transactions
     * @return An Array list of Transaction Objects
     * @throws InvalidTransactionException the id's in the transactionList must match with the id's in AllTransactions
     */
    public ArrayList<Transaction> getTransactionsFromIdList(List<UUID> transactionList) {
        ArrayList<Transaction> transactions= new ArrayList<>();
        for (UUID id : transactionList){
            try{
            Transaction transaction = getTransactionFromId(id);
            transactions.add(transaction);}
            catch(InvalidTransactionException e){
                System.out.println("Transaction id does not map to a transaction");
            }
        }
        return transactions;
    }
}