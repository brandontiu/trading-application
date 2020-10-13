package Presenters;

/**
 * <h1>AdminMenuPresenter</h1>
 * Stores all common strings by an AdminUser.
 */

public class AdminMenuPresenter extends MenuPresenter {
    //admin main menu strings
    public String adminMenuTitle = "Admin Menu";
    public String permissionDenied = "Permission Denied";
    public String checkPendingItem = "Check Pending Items for Approval";
    public String checkFlaggedUser = "Check Flagged Users";
    public String createNewAdmin = "Create New Admin User";
    public String addNewItem = "Add New Item to a TradingUser's Wishlist/Inventory";
    public String unfreezeRequest = "Check Unfreeze TradingUser Account Requests";
    public String changeThreshold = "Change User's Threshold";
    public String username = "New Admin Username:";
    public String password = "New Admin Password:";
    public String addToInventory = "Add to Inventory";
    public String approve = "Approve";
    public String reject = "Reject";
    public String undoActions = "Undo Actions";
    public String undoOptions = "Undo Options";
    public String undoAction = "Undo Action";


    //warning message string
    public String selectUser = "Error: Please select an user.";
    public String itemAddedError = "Error: Item has been added already.";
    public String updateThresholdError = "Error: UpdateThreshold method in adminController failed, so the threshold wasn't changed.";
    public String actionUndone = "Action has been undone.";
    public String noUndoableAction = "No users have made any undoable actions yet!";
    public String noPendingItem = "No pending items to be checked.";
    public String itemApproved = "This item has been approved";
    public String itemRejected = "This item has been rejected.";



}


