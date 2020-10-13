package Users;

import Exceptions.InvalidDemoUserException;
import Items.Item;
import Items.ItemManager;

import java.util.UUID;

public class DemoMenuController {

    private DemoUser currentDemoUser = null;
    private final DemoUserManager dum;
    private TradingUserManager tum;
    private ItemManager im;

    public DemoMenuController(DemoUserManager dum, TradingUserManager tum, ItemManager im) {
        this.dum = dum;
        this.tum = tum;
        this.im = im;
    }

    public DemoUserManager getDum(){
        return dum;
    }

    /**
     * Returns true if the Username is available
     */
    public boolean availableUsername(String username) {
        return dum.checkAvailableUsername(username);
    }

    public void addDemoUser(String username, String password) throws InvalidDemoUserException {
        dum.addDemoUser(username, password);
    }

    public boolean validDemoUser(String username, String password){
        return dum.validDemoUser(username, password);
    }

    /**
     * This method is only used in the creation of a DemoUser so there is an item present in the DemoUser
     * Inventory and Wishlist to try out
     * @param item item to add
     * @param list list to add to
     */
    public void addItem(Item item, String list){
        im.addItem(item);
        if (list.equals("wishlist")){
            currentDemoUser.getWishlist().add(item.getId());
        } else if (list.equals("inventory")) {
            currentDemoUser.getInventory().add(item.getId());
        }
    }

    /**
     * Sets the currentDemoUser
     * @param username username of DemoUser
     */

    public void setCurrentDemoUser(String username){
        try{
            currentDemoUser = dum.getDemoUser(username);
        } catch (InvalidDemoUserException e) {
           //
        }
    }

    /**
     * Removes item from Inventory
     * @param itemID id of the item that needs to be removed
     */
    public void removeFromInventory(UUID itemID){
        currentDemoUser.getInventory().remove(itemID);
    }

    /**
     * Removes item from Wishlist
     * @param itemID id of the item that needs to be removed
     */

    public void removeFromWishlist(UUID itemID){
        currentDemoUser.getWishlist().remove(itemID);
    }

    /**
     * Returns the currentDemoUser
     * @return the currentDemoUser
     */
    public DemoUser getCurrentDemoUser(){
        return currentDemoUser;
    }
}
