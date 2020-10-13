package Transactions;

/**
 * Actions that can be done on a transaction.
 * CONFIRM MEETING DETAILS: "Confirm Transactions Meeting(s)"
 * CANCEL: "Cancel transaction"
 * CONFIRM MEETUP: "Confirm the exchange has taken place"
 * MEETUP INCOMPLETE: "Claim that the exchange has not taken place"
 * ITEM RETURNED: "Confirm the item has been returned"
 * ITEM NOT RETURNED: "Claim that the item has not been returned past due date"
 * EDITED: "Meeting details have been edited"
 */
public enum TransactionActions {
    CONFIRMMEETINGDETAILS, CANCEL, CONFIRMMEETUP, MEETUPINCOMPLETE, ITEMRETURNED, ITEMNOTRETURNED, EDITED;
}
