package Users;

import Exceptions.InvalidDemoUserException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <h1>DemoUserManager</h1>
 *
 * Manages all the DemoUsers in the System
 *
 * <p>
 *     Stores a list of all DemoUsers and a HashMap mapping between the DemoUser and their UUID
 * </p>
 */

public class DemoUserManager {
    private final List<DemoUser> allDemoUsers;
    private Map<UUID, DemoUser> idToDemoUser;

    /**
     * Creates a list of DemoUsers
     * @param demoUsers list of DemoUsers
     */
    public DemoUserManager(List<DemoUser> demoUsers){
        allDemoUsers = demoUsers;
        userListToMap();
    }

    /**
     * Adds a new DemoUser with the provided info
     * @param username online identifier of DemoUser
     * @param password account password
     * @return the new DemoUser
     * @throws InvalidDemoUserException when a username already exists
     */
    public DemoUser addDemoUser (String username, String password) throws InvalidDemoUserException {
        DemoUser newDemoUser = new DemoUser(username, password);
        if (allDemoUsers.size() == 0) {
            allDemoUsers.add(newDemoUser);
            idToDemoUser.put(newDemoUser.getUserId(),newDemoUser);
            return newDemoUser;
        }
        if (checkAvailableUsername(username)) {
            allDemoUsers.add(newDemoUser);
            idToDemoUser.put(newDemoUser.getUserId(), newDemoUser);
            return newDemoUser;
        } else {
            throw new InvalidDemoUserException();
        } // original code written by Tingting
    }

    /**
     * Checks to see if a username for a DemoUser is taken
     * @param username username of DemoUser
     * @return false if it is taken, true if it is not taken
     */

    public boolean checkAvailableUsername(String username) {
        for (DemoUser user : allDemoUsers) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
        // original code written by Tingting
    }

    /**
     * Gets a DemoUser that has a userid of id
     * @param id target userId
     * @return target DemoUser
     */
    public DemoUser getDemoUserById(UUID id){return idToDemoUser.get(id);}

    /**
     * Gets a DemoUser by username
     * @param username username of target DemoUser
     * @return target DemoUser
     * @throws InvalidDemoUserException when no such DemoUser with the username exits
     */
    public DemoUser getDemoUser(String username) throws InvalidDemoUserException{
        for (DemoUser demoUser : allDemoUsers) {
            if ((demoUser.getUsername().equals(username))) {
                return demoUser;
            }
        }
        throw new InvalidDemoUserException();
        // original code written by Tingting
    }

    /**
     * Gets a list of all DemoUsers in Initialization.TradingSystem
     * @return a list of DemoUsers
     */

    public List<DemoUser> getAllDemoUsers(){return allDemoUsers;}

    private void userListToMap() {
        idToDemoUser = new HashMap<>();
        for (DemoUser user : allDemoUsers) {
            idToDemoUser.put(user.getUserId(), user);
        }
        // original code written by Tingting
    }

    /**
     * Removes a DemoUser from allDemoUsers, thereby removing the DemoUser from the system
     * @param id UUID of the DemoUser you'd like to remove
     */
    public void removeDemoUser(UUID id){
        DemoUser removed = getDemoUserById(id);
        idToDemoUser.remove(id);
        allDemoUsers.remove(removed);
    }

    /**
     * Returns if a DemoUser is a valid DemoUser (exists in the TradingSystem)
     * @param username username of the DemoUser in question
     * @param password password of the DemoUser in question
     * @return boolean
     */
    public boolean validDemoUser(String username, String password){
        for(DemoUser user: allDemoUsers){
            if(user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
