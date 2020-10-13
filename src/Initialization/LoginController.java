package Initialization;

import Admins.AdminManager;
import Exceptions.InvalidDemoUserException;
import Exceptions.InvalidTradingUserException;
import Users.DemoUserManager;
import Users.TradingUserManager;

/**
 * Contains methods with logic related to checking whether a user's login credentials are valid.
 */
public class LoginController {
    private final AdminManager am;
    private final TradingUserManager um;
    private final DemoUserManager dum;

    public LoginController(AdminManager am, TradingUserManager um, DemoUserManager dum) {
        this.am = am;
        this.um = um;
        this.dum = dum;
    }

    /**
     * Returns if something is a valid TradingUser
     * @param username username of said User
     * @param password password of said User
     * @return boolean
     */
    public boolean validUser(String username, String password) {
        return um.validUser(username, password);
    }

    /**
     * Returns if something is a valid DemoUser
     * @param username username of said User
     * @param password password of said User
     * @return boolean
     */
    public boolean validDemoUser(String username, String password) {return dum.validDemoUser(username, password);}

    /**
     * Returns if a username is available
     * @param username desired username
     * @return boolean
     */
    public boolean availableUsername(String username) {
        return am.checkAvailableUsername(username) && um.checkAvailableUsername(username) && dum.checkAvailableUsername(username);
    }

    /**
     * Adds a DemoUser in the system
     * @param username username of new DemoUser
     * @param password password of new DemoUser
     */

    public void addDemoUser(String username, String password){
        try{
            dum.addDemoUser(username, password);
        } catch (InvalidDemoUserException e){
            //
        }
    }

    /**
     * Adds a TradingUser in the system
     * @param username username of new TradingUser
     * @param password password of new TradingUser
     * @param city city of new TradingUser
     */

    public void addTradingUser(String username, String password, String city) {
        try {
            um.addTradingUser(username, password, city);
        } catch (InvalidTradingUserException e) {
            //
        }
    }
}
