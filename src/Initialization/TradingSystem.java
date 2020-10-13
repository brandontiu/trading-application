package Initialization;

import Actions.Action;
import Actions.ActionManager;
import Admins.AdminManager;
import Admins.AdminMenuController;
import Admins.AdminUser;
import Exceptions.InvalidAdminException;
import Items.Item;
import Items.ItemManager;
import Presenters.MenuPresenter;
import Transactions.PastTransactionManager;
import Transactions.Transaction;
import Transactions.CurrentTransactionManager;
import Users.*;

import java.io.File;
import java.util.*;

/**
 * <h1>Initialization.TradingSystem</h1>
 *
 * <p>The Initialization.TradingSystem class handles logging on and the creation of new Users.TradingUser accounts.</p>
 * <p>If logging on to an existing account, directs the user to either the administrator menu
 * or the user menu.</p>
 */

public class TradingSystem {
    private Filepaths fp = new Filepaths();
    private AdminManager am;
    private TradingUserManager tum;
    private DemoUserManager dum;
    private CurrentTransactionManager tm;
    private PastTransactionManager ptm;
    private ItemManager im;
    private Map<Item, TradingUser> pendingItems;
    private LoginController lc;
    private AdminMenuController amc;
    private UserMenuController umc;
    private DemoMenuController dmc;
    private ActionManager acm;
    private MenuPresenter mp;

    /**
     * Calls to different helper methods to read data from saved files, redirects user to
     * appropriate class depending on user input, and saves changes by writing files. Manages the
     * initialization required for the application to run.
     */
    public void run() {
        readData();
        checkFirstAdmin();
        LoginWindow lw = new LoginWindow(lc, amc, umc, dmc, mp);
        lw.display();
    }

    /**
     * Helper method to retrieve data from files.
     */
    private void readData() {
        // make sure files exists so they can be read
        checkFileExists(fp.ADMINS);
        checkFileExists(fp.USERS);
        checkFileExists(fp.REQUESTEDITEMS);
        checkFileExists(fp.FLAGGEDACCOUNTS);
        checkFileExists(fp.FROZENACCOUNTS);
        checkFileExists(fp.TRANSACTIONS);
        checkFileExists(fp.ITEMS);
        checkFileExists(fp.DEMOUSERS);
        checkFileExists(fp.ACTIONS);

        // files exists so we can deserialize them
        Serializer serializer = new Serializer();
        List<AdminUser> admins = serializer.readAdminsFromFile(fp.ADMINS);
        List<TradingUser> tradingUsers = serializer.readUsersFromFile(fp.USERS);
        pendingItems = serializer.readItemsFromFile(fp.REQUESTEDITEMS);
        List<TradingUser> flaggedAccounts = serializer.readAccountsFromFile(fp.FLAGGEDACCOUNTS);
        List<TradingUser> frozenAccounts = serializer.readAccountsFromFile(fp.FROZENACCOUNTS);
        Map<UUID, Transaction> transactions = serializer.readTransactionMapFromFile(fp.TRANSACTIONS);
        Map<UUID, Item> items = serializer.readItemMapFromFile(fp.ITEMS);
        List<DemoUser> demoUsers = serializer.readDemoUsersFromFile(fp.DEMOUSERS);
        LinkedHashMap<UUID, List<Action>> actions = serializer.readActionsFromFile(fp.ACTIONS);

        // create new Managers
        am = new AdminManager(admins, flaggedAccounts, frozenAccounts);
        tum = new TradingUserManager(tradingUsers, flaggedAccounts, frozenAccounts);
        tm = new CurrentTransactionManager(transactions, acm);
        ptm = new PastTransactionManager(transactions);
        im = new ItemManager(items);
        dum = new DemoUserManager(demoUsers);
        acm = new ActionManager(actions);

        // create new controllers
        lc = new LoginController(am, tum, dum);
        amc = new AdminMenuController(am, tum, pendingItems, im, acm, ptm);
        umc = new UserMenuController(tum, am, tm, ptm, im, acm, pendingItems);
        dmc = new DemoMenuController(dum, tum, im);

        mp = new MenuPresenter();
    }

    /**
     * A helper method to check if the file specified by filePath exists. If this file does
     * not exist, this method generates it.
     */
    private void checkFileExists(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            // do nothing
        } else {
            // create a new file
            Serializer serializer = new Serializer();
            if (filePath.equals(fp.ADMINS)) {
                List<AdminUser> list = new ArrayList<>();
                serializer.writeAdminsToFile(filePath, list);
            } else if (filePath.equals(fp.USERS)) {
                List<TradingUser> list = new ArrayList<>();
                serializer.writeUsersToFile(filePath, list);
            } else if (filePath.equals(fp.REQUESTEDITEMS)) {
                HashMap<Item, TradingUser> map = new HashMap<>();
                serializer.writeItemsToFile(filePath, map);
            } else if (filePath.equals(fp.FLAGGEDACCOUNTS) | filePath.equals(fp.FROZENACCOUNTS)) {
                List<TradingUser> tradingUsers = new ArrayList<>();
                serializer.writeAccountsToFile(filePath, tradingUsers);
            } else if (filePath.equals(fp.TRANSACTIONS)) {
                Map<UUID, Item> itemMap = new HashMap<>();
                serializer.writeItemsMapToFile(filePath, itemMap);
            } else if (filePath.equals(fp.DEMOUSERS)) {
                List<DemoUser> demoUsers = new ArrayList<>();
                serializer.writeDemoUsersToFile(filePath, demoUsers);
            } else if (filePath.equals(fp.ACTIONS)) {
                LinkedHashMap<UUID, List<Action>> actions = new LinkedHashMap<>();
                serializer.writeActionsToFile(filePath, actions);
            } else if (filePath.equals(fp.ITEMS)) {
                Map<UUID, Item> items = new HashMap<>();
                serializer.writeItemsMapToFile(filePath, items);
            }
        }
    }

    /**
     * A helper method to check if the first admin already exists. If the first admin does not exist
     * this method creates it.
     */
    private void checkFirstAdmin() {
        if (am.validAdmin("admin", "password")) {
            // the first admin exists, do nothing
        } else {
            // the first admin does not exist yet, create it
            try {
                am.addAdmin("admin", "password").setFirstAdmin(true);
            } catch(InvalidAdminException e) {
                // If this admin is the first to be added in the list then it will never
                // conflict with another admin so an exception will never be thrown by addAdmin()
            }
        }
    }
}


