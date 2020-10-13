package TradingUserGUI;

import Initialization.Filepaths;
import Initialization.Serializer;
import Presenters.UserMenuPresenter;
import Popups.ChangePasswordWindow;
import Users.TradingUser;
import Users.UserMenuController;
import Users.UserStatuses;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Main Trading User Menu
 */
public class TradingUserMenu {
    private final UserMenuController umc;
    private final UserMenuPresenter ump = new UserMenuPresenter();
    private final Filepaths fp = new Filepaths();
    private final JFrame frame = new JFrame(ump.tradingUserMenuTitle);
    private final JButton button1 = new JButton();
    private final JButton button2 = new JButton();
    private final JButton button3 = new JButton();
    private final JButton button4 = new JButton();
    private final JButton button5 = new JButton();
    private final JButton button6 = new JButton();

    public TradingUserMenu(UserMenuController umc) {
        this.umc = umc;
    }

    public void display() {
        // configure the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // set the frame's size and centre it
        frame.setSize(new Dimension(660, 300));
        frame.setLocationRelativeTo(null);

        // create the menu bar
        JMenuBar menuBar = new JMenuBar();

        // build a menu
        JMenu menu = new JMenu(ump.menu);
        menu.setMnemonic('M'); // press the M key to access the menu

        formatMenuOptions(menu);

        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        // layout the fields and button on the frame
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        // if clicked request add items button
        button1.addActionListener(e -> {
            RequestAddItemsWindow riw = new RequestAddItemsWindow(umc);
            riw.display();
        });

        // if clicked "display available items" button
        button2.addActionListener(e -> {
            AvailableItemsWindow aiw = new AvailableItemsWindow(umc);
            aiw.display();
        });

        // if clicked "view active transactions" button
        button3.addActionListener(e -> {
            ViewActiveTransactionsWindow atw = new ViewActiveTransactionsWindow(umc);
            atw.display();
        });

        // if clicked "View Transaction History" button
        button4.addActionListener(e -> {
            ViewTransactionHistoryWindow thw = new ViewTransactionHistoryWindow(umc);
            thw.display();
        });

        // if clicked "View Wishlist" button
        button5.addActionListener(e -> {
            ViewWishlistWindow vww = new ViewWishlistWindow(umc, umc.getAcm());
            vww.display();

        });

        // if clicked "View Inventory" button
        button6.addActionListener(e -> {
            ViewInventoryWindow viw = new ViewInventoryWindow(umc, umc.getAcm());
            viw.display();
        });
        // display the window
        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // add button's to panel
        button1.setBounds(20, 50, 200, 75);
        //button1.setBackground(Color.PINK); // DO NOT CHANGE THIS >: ((
        //button1.setForeground(Color.PINK); // LOL why pink of all colours?
        button1.setText(ump.requestAddItem);
        panel.add(button1);

        button2.setBounds(220, 50, 200, 75);
        button2.setText(ump.displayAvailableItems);
        panel.add(button2);

        button3.setBounds(420, 50, 200, 75);
        button3.setText(ump.viewActiveTrans);
        panel.add(button3);

        button4.setBounds(20, 150, 200, 75);
        button4.setText(ump.viewTranHistory);
        panel.add(button4);

        button5.setBounds(220, 150, 200, 75);
        button5.setText(ump.viewWishlist);
        panel.add(button5);

        button6.setBounds(420, 150, 200, 75);
        button6.setText(ump.viewInventory);
        panel.add(button6);
    }

    private void formatMenuOptions(JMenu menu) {
        // change password option
        JMenuItem changePassword = new JMenuItem(ump.changePsw);
        changePassword.addActionListener(e -> {
            new ChangePasswordWindow(umc.getUm(), umc.getCurrentTradingUser()).display();
        });
        menu.add(changePassword);

        // change home city option
        JMenuItem changeCity = new JMenuItem(ump.changeCity);
        changeCity.addActionListener(e -> {
            String newCity = JOptionPane.showInputDialog(null,
                    "Enter new city", "Change City");
            if (newCity != null) {
                umc.getCurrentTradingUser().setCity(newCity);
            }
        });
        menu.add(changeCity);

        // request to have your account unfrozen option
//        JMenuItem requestUnfrozen = new JMenuItem(ump.requestUnfreeze);
//        requestUnfrozen.addActionListener(e -> {
//            int input = JOptionPane.showConfirmDialog(null,
//                    "Request admin to unfreeze your account?", "Unfreeze Account",
//                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//            if (input == JOptionPane.YES_OPTION) {
//                umc.requestUnfreezeAccount();
//            }
//        });

        // change your account to vacation status
        JMenuItem setVacationStatus = new JMenuItem(ump.setVacation);
        setVacationStatus.addActionListener(e -> {
            int input = JOptionPane.showConfirmDialog(null,
                    "Set vacation status?", ump.vacationStatus, JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (input == JOptionPane.YES_OPTION) {
                changeVacationStatus(true, umc.getCurrentTradingUser());
            }
        });
        menu.add(setVacationStatus);

        // change your account back to active status
        JMenuItem unsetVacationStatus = new JMenuItem(ump.unsetVacation);
        unsetVacationStatus.addActionListener(e -> {
            int input = JOptionPane.showConfirmDialog(null,
                    "Unset the vacation status?", ump.vacationStatus, JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (input == JOptionPane.YES_OPTION) {
                changeVacationStatus(false, umc.getCurrentTradingUser());
            }
        });
        menu.add(unsetVacationStatus);

        // view account profile
        JMenuItem userProfile = new JMenuItem(ump.accountProfile);
        userProfile.addActionListener(e -> {
            TradingUserProfileWindow tradingUserProfileWindow = new TradingUserProfileWindow(this.umc.getPtm(), umc.getCurrentTradingUser());
            tradingUserProfileWindow.display();
        });
        menu.add(userProfile);

        // log out menu option
        JMenuItem logOut = new JMenuItem(ump.logOut, KeyEvent.VK_L); // press the L key to access log out option
        logOut.addActionListener(e -> {
            writeData();
            frame.dispose();
        });
        menu.add(logOut);

    }

    private void writeData() {
        Serializer serializer = new Serializer();
        serializer.writeUsersToFile(fp.USERS, umc.getUm().getAllTradingUsers());
        serializer.writeItemsToFile(fp.REQUESTEDITEMS, umc.getAllPendingItems());
        serializer.writeAccountsToFile(fp.FLAGGEDACCOUNTS, umc.getUm().getFlaggedAccounts());
        serializer.writeAccountsToFile(fp.FROZENACCOUNTS, umc.getUm().getFrozenAccounts());
        serializer.writeTransactionsToFile(fp.TRANSACTIONS, umc.getTm().getAllTransactions());
        serializer.writeItemsMapToFile(fp.ITEMS, umc.getIm().getAllItems());
        serializer.writeActionsToFile(fp.ACTIONS, umc.getAcm().getAllActions());
    }

    private void changeVacationStatus(boolean b, TradingUser user) {
        if (b && !user.isFrozen()) {
            user.setStatus(UserStatuses.VACATION);
        } else if (!b && !user.isFrozen()) {
            user.setStatus(UserStatuses.ACTIVE);
        }
    }
}
