package Transactions;

import java.util.UUID;

public class TransactionStatusStrategy {

    protected boolean noMeetingComplete(Transaction transaction){
        if(!transaction.isVirtual()){
            return false;
        }
        else{
            for(UUID userId: transaction.getUsers()){
                if (transaction.getUserStatus(userId).equals(TransactionStatuses.PENDING)){
                    return false;
                }
            }
            transaction.setStatus(TransactionStatuses.COMPLETED);
            return true;
        }
    }


    protected boolean pendingToConfirmed(Transaction transaction){
        if (!transaction.getStatus().equals(TransactionStatuses.PENDING)){
            return false;
        }
        else{
            for (UUID currUser : transaction.getUsers()) {
                if (!transaction.getUserStatus(currUser).equals(TransactionStatuses.CONFIRMED)) {
                    return false;
                }
            }
            transaction.setStatus(TransactionStatuses.CONFIRMED);
            return true;
        }
    }

    protected boolean pendingToCancelled(Transaction transaction){
        if (transaction.getStatus().equals(TransactionStatuses.PENDING) & (transaction.getStatusUser1().equals(TransactionStatuses.CANCELLED) || transaction.getStatusUser2().equals(TransactionStatuses.CANCELLED))){
            transaction.setStatus(TransactionStatuses.CANCELLED);
            return true;
        }
        if (transaction.getStatus().equals(TransactionStatuses.CONFIRMED) & (transaction.getStatusUser1().equals(TransactionStatuses.CANCELLED) || transaction.getStatusUser2().equals(TransactionStatuses.CANCELLED))){
            if (transaction.getStatusUser1().equals(TransactionStatuses.TRADED) || transaction.getStatusUser1().equals(TransactionStatuses.COMPLETED)){
                return false;
            }
            if (transaction.getStatusUser2().equals(TransactionStatuses.TRADED) || transaction.getStatusUser2().equals(TransactionStatuses.COMPLETED)){
                return false;
            }
            else{
                transaction.setStatus(TransactionStatuses.CANCELLED);
                return true;
            }
        }
        else{
            return false;
        }
    }

    protected boolean confirmedToTraded(Transaction transaction){
        if (!transaction.getStatus().equals(TransactionStatuses.CONFIRMED)){
            return false;
        }
        else if (transaction.isPerm()){
            return false;
        }
        else{
            if (transaction.getStatusUser1().equals(TransactionStatuses.TRADED) & transaction.getStatusUser2().equals(TransactionStatuses.TRADED)){
                transaction.setStatus(TransactionStatuses.TRADED);
                return true;
            }
            else{
                return false;
            }
        }
    }

    protected boolean confirmedToIncomplete(Transaction transaction){
        if (!transaction.getStatus().equals(TransactionStatuses.CONFIRMED)){
            return false;
        }
        // else if (transaction.isPerm()){
        // return false;
        // }
        else{
            if (transaction.getStatusUser1().equals(TransactionStatuses.INCOMPLETE) || transaction.getStatusUser2().equals(TransactionStatuses.INCOMPLETE)){
                transaction.setStatus(TransactionStatuses.INCOMPLETE);
                return true;
            }
            else{
                return false;
            }

        }
    }

    protected boolean confirmedToComplete(Transaction transaction){
        if (!transaction.getStatus().equals(TransactionStatuses.CONFIRMED)){
            return false;
        }
        else if (!transaction.isPerm()){
            return false;
        }
        else{
            if (transaction.getStatusUser1().equals(TransactionStatuses.TRADED) & transaction.getStatusUser2().equals(TransactionStatuses.TRADED)){
                transaction.setStatus(TransactionStatuses.COMPLETED);
                return true;
            }
            else{
                return false;
            }
        }
    }

    protected boolean tradedToComplete(Transaction transaction){
        if (!transaction.getStatus().equals(TransactionStatuses.TRADED)){
            return false;
        }
        else if (transaction.isPerm()){
            return false;
        }
        else{
            if (transaction.getStatusUser1().equals(TransactionStatuses.COMPLETED) & transaction.getStatusUser2().equals(TransactionStatuses.COMPLETED)){
                transaction.setStatus(TransactionStatuses.COMPLETED);
                return true;
            }
            else{
                return false;
            }
        }
    }

    protected boolean tradedToNeverReturned(Transaction transaction){
        if (!transaction.getStatus().equals(TransactionStatuses.TRADED)){
            return false;
        }
        else if (transaction.isPerm()){
            return false;
        }
        else{
            if (transaction.getStatusUser1().equals(TransactionStatuses.NEVERRETURNED) || transaction.getStatusUser2().equals(TransactionStatuses.NEVERRETURNED)){
                transaction.setStatus(TransactionStatuses.NEVERRETURNED);
                return true;
            }
            else{
                return false;
            }
        }
    }
}
