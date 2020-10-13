package Transactions;

/**
 * Transaction statuses.
 *  * -- "Pending" -- Status given initially while transaction is still being negotiated. <br>
 *  * -- "Confirmed" -- status given when details of all meetings involved in transaction has been confirmed by users. <br>
 *  * -- "Traded" -- first meeting has been confirmed by both users as taken place.(only used for one way) <br>
 *  * -- "Completed" -- the last meeting of the transaction has happened and confirmed by both users. <br>
 *  * -- "Cancelled" -- transaction has been cancelled. The transaction can only be in this state after pending
 *  *                   (too many times edited). <br>
 *  * -- "Incomplete" -- A user did not show at meeting 1. <br>
 *  * -- "Never Returned" -- A user did not show up at meeting 2 and/or items were not returned.(only used for temporary transactions). <br>
 */
public enum TransactionStatuses {
    PENDING, CONFIRMED, TRADED, COMPLETED, CANCELLED, INCOMPLETE, NEVERRETURNED;

    //FROZEN;
    //ACTIVE;
}
