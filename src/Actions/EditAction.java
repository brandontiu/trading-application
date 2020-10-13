package Actions;

import Transactions.Meeting;
import Transactions.Transaction;
import Users.TradingUser;

import java.io.Serializable;
import java.util.UUID;


/**
 * Represents an "edit" action, which includes editing a Meeting's details.
 */
public class EditAction extends Action implements Serializable {
    private Transaction transaction;
    private int whichMeeting;
    private Meeting previousMeeting;
    private Meeting newMeeting;

    public EditAction(UUID userId, Transaction transaction,
                      int whichMeeting, Meeting previousMeeting, Meeting newMeeting) {
        super(userId);
        this.transaction = transaction;
        this.whichMeeting = whichMeeting;
        this.newMeeting = newMeeting;
        this.previousMeeting = previousMeeting;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public int getWhichMeeting() {
        return whichMeeting;
    }

    public Meeting getNewMeeting() {
        return newMeeting;
    }

    public Meeting getPreviousMeeting() {
        return previousMeeting;
    }

    /**
     * A string representation of this action.
     * @return a string representation of what transaction was edited from and to.
     */
    public String toString() {
        return getUser() + " changed " + previousMeeting.toString() + "\n" + " to " + newMeeting.toString();
    }

    @Override
    public boolean isEditAction() {
        return true;
    }

    @Override
    public boolean isAddorDeleteAction() {
        return false;
    }
}
