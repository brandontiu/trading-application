package Admins;

import Exceptions.InvalidAdminException;
import Users.TradingUser;

import java.util.ArrayList;
import java.util.List;


/**
 * <h1>AdminManager</h1>
 * Creates and manages instances of AdminUsers, as well as all accounts that have been flagged or frozen, and a list
 * of Users who have requested their accounts to be unfrozen.
 */
public class AdminManager {
    private List<AdminUser> allAdmins;

    /**
     * A list of Users that have had their account flagged to be frozen automatically by the system.
     */
    private List<TradingUser> flaggedAccounts;

    /**
     * A list of Users that have requested their account be unfrozen after approval by an Admin.
     */
    private List<TradingUser> frozenAccounts;

    /**
     * Creates an AdminManager.
     * @param admins list of all AdminUsers in the system
     * @param flaggedAccounts list of all Users that have had their account flagged to be frozen automatically by the system.
     * @param frozenAccounts list of all Users tha have had their account frozen after approval by an Admin
     */
    public AdminManager(List<AdminUser> admins, List<TradingUser> flaggedAccounts, List<TradingUser> frozenAccounts) {
        allAdmins = admins;
        this.flaggedAccounts = flaggedAccounts;
        this.frozenAccounts = frozenAccounts;
    }

    /**
     * Creates a new AdminUser with given username and password.
     * @param username user's account name identifier
     * @param password user's account password
     */
    public AdminUser addAdmin(String username, String password) throws InvalidAdminException {
        // create the new admin
        AdminUser newAdmin = new AdminUser(username, password);
        // if no one else shares the same username, we add to the lists of all users and admins and
        // we return the admin
        if (checkAvailableUsername(username)) {
            allAdmins.add(newAdmin);
            return newAdmin;
        } else { throw new InvalidAdminException(); }
    }

    /**
     * Getter for AdminUser specified by their username.
     * @param username desired AdminUser's username
     * @return AdminUser
     * @throws InvalidAdminException the inputted username does not match any existing Admins.AdminUser account in the system.
     */
    public AdminUser getAdmin(String username) throws InvalidAdminException {
        for (AdminUser admin : allAdmins) if (admin.getUsername().equals(username)) return admin;
        throw new InvalidAdminException();
    }

    /**
     * Getter for all admins in the trading system.
     * @return list of all AdminUsers that are currently in the trading system.
     */
    public List<AdminUser> getAllAdmins() {
        return allAdmins;
    }

    /**
     * Returns whether the input username and password match an existing AdminUser account.
     * @param username inputted username
     * @param password inputted password
     * @return boolean whether the inputs match an existing AdminUser account or not.
     */
    public boolean validAdmin(String username, String password) {
        for (AdminUser admin : allAdmins)
            if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) return true;
        return false;
    }

    /**
     * Getter for this AdminMenu's list of all Users who are frozen.
     * @return a list of all users who's accounts are frozen.
     */
    public List<TradingUser> getFrozenAccounts() { return frozenAccounts; }

    /**
     * Getter for this AdminMenu's list of all flagged Users that have been flagged to be frozen by the system.
     * @return a list of all users that have been flagged to be frozen.
     */
    public List<TradingUser> getFlaggedAccounts() { return flaggedAccounts; }

    /**
     * Checks whether the input username is valid.
     *
     * @param username online identifier of a Users.TradingUser
     * @return True or False as boolean
     */
    public boolean checkAvailableUsername(String username) {
        for (AdminUser user : allAdmins) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }

    public void changePassword(AdminUser admin, String password) {
        admin.setPassword(password);
    }
}