package Presenters;

/**
 * <h1>MenuPresenter</h1>
 * Stores all common strings between the menu accessed by an AdminUser, the menu accessed by a TradingUser and DemoUser.
 * Class variables represent commonly used strings between the AdminMenuPresenter and UserMenuPresenter.
 */

public class MenuPresenter {
    //labels
    public String username = "Username:";
    public String password = "Password:";
    public String newPassword = "New Password:";
    public String confirmPsw = "Confirm Password:";

    public String itemName = "Item Name:";
    public String itemDes = "Description:";
    public String itemOwner = "Item Owner:";
    public String addToWishlist = "Add to Wishlist";


    //main menu strings
    public String signUp = "Sign up";
    public String signIn = "Sign in";
    public String demo = "Demo?";
    public String createAccount = "Create Account";
    public String changePsw = "Change Password";
    public String logOut = "Log out";
    public String menu = "Menu";

    //error message strings
    public String invalidMessage = "Invalid credentials or account does not exist.";
    public String usernameTaken = "Username already taken!";
    public String passwordNotMatch = "Passwords do not match!";


    //frame name strings
    public String accountCreationTitle = "Account Creation";
    public String applicationTitle = "Trading Application";

    public String successfully(String what){
        return what + " successfully!";
    }
    public String enter(String what){
        return "Error: Please enter the " + what;
    }
    public String pleaseSelect(String what){return "Error: Please select " + what;}


}
