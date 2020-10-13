package Admins;

import Users.User;
import java.io.Serializable;

/**
 * <h1>AdminUser</h1>
 * Represents an administrative user in the trading system.
 */
public class AdminUser extends User implements Serializable {
    private boolean firstAdmin = false; // only the FIRST AdminUser will have firstAdmin = true, no one else

    /**
     * Constructs an instance of an Admin Users.TradingUser based on Strings of username and password.
     */
    public AdminUser(String username, String password) {
        super(username, password);
    }

    /**
     * Getter for if this Admins.AdminUser is the initial administrative user
     * @return boolean whether Admins.AdminUser is initial user
     */
    public boolean isFirstAdmin() { return firstAdmin; }

    /**
     * Setter for Admins.AdminUser firstAdmin
     * @param isFirstAdmin as boolean
     */
    public void setFirstAdmin(boolean isFirstAdmin) { this.firstAdmin = isFirstAdmin; }

    /**
     * Represents the current Admins.AdminUser by their username and userId
     * @return the username and adminid separated by a comma
     */
    @Override
    public String toString() { return getUsername() + ", " + getUserId(); }
}