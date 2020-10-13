package AdminGUI;

import Admins.AdminMenuController;

import Initialization.Filepaths;
import Initialization.Serializer;
import Popups.PopUpWindow;
import Popups.ChangePasswordWindow;
import Presenters.AdminMenuPresenter;
import Users.TradingUserManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Main Admin Menu
 */
public class AdminUserMenu {
    private final AdminMenuController amc;
    private final TradingUserManager tum;
    private final Filepaths fp = new Filepaths();
    private final JButton button1 = new JButton();
    private final JButton button2 = new JButton();
    private final JButton button3 = new JButton();
    private final JButton button4 = new JButton();
    private final JButton button5 = new JButton();
    private final JButton button6 = new JButton();
    private final JButton button7 = new JButton();
    private JFrame frame = new JFrame();
    private final AdminMenuPresenter amp = new AdminMenuPresenter();

    public AdminUserMenu(AdminMenuController amc) {
        this.amc = amc;
        this.tum = amc.getTUM();
    }

    public void display() {
        // create the frame
        frame = new JFrame(amp.adminMenuTitle);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // set the frame's size and centre it
        frame.setSize(new Dimension(1020, 500));
        frame.setLocationRelativeTo(null);

        // create the menu bar
        JMenuBar menuBar = new JMenuBar();

        // build a menu
        JMenu menu = new JMenu(amp.menu);
        menu.setMnemonic('M'); // press the M key to access the menu

        formatMenuOptions(menu);

        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        // layout the fields and button on the frame
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        // if button1 (check pending items) is clicked
        button1.addActionListener(e -> {
            CheckPendingItemsWindow iw = new CheckPendingItemsWindow(amc);
            iw.display();
        });

        // if button2 (Check Flagged Users) is clicked
        button2.addActionListener(e -> {
            FlaggedUsersWindow fuw = new FlaggedUsersWindow(amc);
            fuw.display();
        });

        // if button3 (create new admin user) is clicked
        button3.addActionListener(e -> {
            if (amc.getCurrentAdmin().isFirstAdmin()){//creates a new admin which can only be done by the first admin
                AddAdminUserWindow auw = new AddAdminUserWindow(amc);
                auw.display();
            }else {
                //JOptionPane.showMessageDialog(null, amp.permissionDenied, "Error Message", JOptionPane.WARNING_MESSAGE);
                new PopUpWindow(amp.permissionDenied).display();
            }});

        //if button4 is clicked
        button4.addActionListener(e -> {
            AddNewItemToTradingUserWindow aiw = new AddNewItemToTradingUserWindow(amc);
            aiw.display();
        });

        // if button5 (Change TradingUser Threshold) is clicked
        button5.addActionListener(e ->{
            ChangeThresholdWindow tw = new ChangeThresholdWindow(amc);
            tw.display();
        });

        // if button6 (Check Unfreeze TradingUser Account Requests) is click
        button6.addActionListener(e -> {
            FrozenUsersWindow frw = new FrozenUsersWindow(amc);
            frw.display();
        });

        // view undoable actions
        button7.addActionListener(e -> {
            UndoActionWindow uaw = new UndoActionWindow(amc);
            uaw.display();
        });

        // display the window
        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // add button's to panel
        button1.setBounds(50, 50, 300, 75);
        //button1.setBackground(Color.PINK); // DO NOT CHANGE THIS >: ((
        //button1.setForeground(Color.PINK);
        button1.setText(amp.checkPendingItem);
        panel.add(button1);

        button2.setBounds(350, 50, 300, 75);
        button2.setText(amp.checkFlaggedUser);
        panel.add(button2);

        button3.setBounds(650, 50, 300, 75);
        button3.setText(amp.createNewAdmin);
        panel.add(button3);

        button4.setBounds(50, 150, 300, 75);
        button4.setText(amp.addNewItem);
        panel.add(button4);

        button5.setBounds(350, 150, 300, 75);
        button5.setText(amp.changeThreshold);
        panel.add(button5);

        button6.setBounds(650, 150, 300, 75);
        button6.setText(amp.unfreezeRequest);
        panel.add(button6);

        button7.setBounds(50, 250, 300, 75);
        button7.setText(amp.undoActions);
        panel.add(button7);

    }

    private void formatMenuOptions(JMenu menu) {
        // change password menu option
        JMenuItem changePassword = new JMenuItem(amp.changePsw, KeyEvent.VK_C); // press the C key to access this menu item
        changePassword.addActionListener(e -> {
            new ChangePasswordWindow(amc.getAm(),amc.getCurrentAdmin()).display();
        });

        // log out menu option
        JMenuItem logOut = new JMenuItem(amp.logOut, KeyEvent.VK_L); // press the L key to access log out option
        logOut.addActionListener(e -> {
            writeData();
            frame.dispose();
        });
        menu.add(changePassword);
        menu.add(logOut);
    }

    private void writeData() {
        Serializer serializer = new Serializer();
        serializer.writeUsersToFile(fp.USERS, amc.getTUM().getAllTradingUsers());
        serializer.writeAdminsToFile(fp.ADMINS, amc.getAm().getAllAdmins());
        serializer.writeItemsToFile(fp.REQUESTEDITEMS, amc.getAllPendingItems());
        serializer.writeAccountsToFile(fp.FLAGGEDACCOUNTS, amc.getTUM().getFlaggedAccounts());
        serializer.writeAccountsToFile(fp.FROZENACCOUNTS, amc.getTUM().getFrozenAccounts());
        serializer.writeItemsMapToFile(fp.ITEMS, amc.getIm().getAllItems());
        serializer.writeActionsToFile(fp.ACTIONS, amc.getAcm().getAllActions());
        serializer.writeTransactionsToFile(fp.TRANSACTIONS, amc.getPTM().getAllTransactions());
    }

}
