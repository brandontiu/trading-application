package DemoUserGUI;

import Initialization.Filepaths;
import Initialization.LoginController;
import Popups.PopUpWindow;
import Initialization.Serializer;
import Presenters.UserMenuPresenter;
import TradingUserGUI.AvailableItemsWindow;
import Users.DemoMenuController;
import Users.UserMenuController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Main DemoUser Menu
 */
public class DemoUserMenu {
    private final DemoMenuController dmc;
    private final UserMenuController umc;
    private final UserMenuPresenter ump = new UserMenuPresenter();
    private final LoginController lc;
    private final Filepaths fp = new Filepaths();
    private final JButton button1 = new JButton();
    private final JButton button2 = new JButton();
    private final JButton button3 = new JButton();
    private final JButton button4 = new JButton();
    private final JButton button5 = new JButton();
    private final JButton button6 = new JButton();
    private final JButton button7 = new JButton();

    public DemoUserMenu(DemoMenuController dmc, UserMenuController umc, LoginController lc){
        this.dmc = dmc;
        this.umc = umc;
        this.lc = lc;
    }

    /**
     * Displays DemoUserMenu
     */
    public void display(){
        // create the frame
        JFrame frame = new JFrame(ump.demoUserMenu);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // set the frame's size and centre it
        frame.setSize(new Dimension(660, 500));
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

        // if clicked "Request Add Item" --> go to the window for adding items but you can't actually add items
        button1.addActionListener(e -> {
            DemoUserRequestAddItemsWindow rtw = new DemoUserRequestAddItemsWindow();
            rtw.display();
        });
        // if clicked "display available items" button
        button2.addActionListener(e -> {
            new AvailableItemsWindow(umc).display();
        });

        // if clicked "view active transactions" button
        button3.addActionListener(e -> {
            new PopUpWindow(ump.upgradeMessage).display();
        });

        // if clicked "View Transaction History" button
        button4.addActionListener(e -> {
            new PopUpWindow(ump.upgradeMessage).display();
        });

        // if clicked "View Wishlist"
        button5.addActionListener(e -> {
            new DemoUserViewWishlistWindow(dmc, umc).display();
        });

        // if clicked "View Inventory"
        button6.addActionListener(e -> {
            new DemoUserViewInventoryWindow(dmc, umc).display();
        });

        // if clicked "Upgrade to Full Account"
        button7.addActionListener(e -> {
            new UpgradeAccountWindow(dmc, lc, umc).display();
        });

        // display the window
        frame.setVisible(true);
    }
    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // add button's to panel
        button1.setBounds(20, 50, 200, 75);
        button1.setBackground(Color.PINK); // DO NOT CHANGE THIS >: ((
        button1.setForeground(Color.PINK);
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

        button7.setBounds(20, 250, 200, 75);
        button7.setText(ump.upgradeToFull);
        panel.add(button7);
    }

    private void formatMenuOptions(JMenu menu) {
        // log out menu option
        JMenuItem logOut = new JMenuItem(ump.logOut, KeyEvent.VK_L); // press the L key to access log out option
        logOut.addActionListener(e -> {
            writeData();
            System.exit(0);
        });
        menu.add(logOut);
    }

    private void writeData() {
        Serializer serializer = new Serializer();
        serializer.writeDemoUsersToFile(fp.DEMOUSERS, dmc.getDum().getAllDemoUsers());
    }
}
