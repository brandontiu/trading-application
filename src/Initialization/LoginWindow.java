package Initialization;

import Admins.AdminMenuController;
import AdminGUI.AdminUserMenu;
import DemoUserGUI.DemoUserMenu;
import DemoUserGUI.DemoUserRegistrationWindow;
import Popups.PopUpWindow;
import Presenters.MenuPresenter;
import TradingUserGUI.TradingUserMenu;
import Users.DemoMenuController;
import Users.UserMenuController;

import javax.swing.*;
import java.awt.*;

// ideas taken from https://beginnersbook.com/2015/07/java-swing-tutorial/

/**
 * The login window that allows a user to log into the system.
 */
public class LoginWindow {
    private final JLabel userLabel = new JLabel();
    private final JTextField userText = new JTextField(20);
    private final JLabel passwordLabel = new JLabel();
    private final JPasswordField passwordText = new JPasswordField(20);
    private final JButton loginButton= new JButton();
    private final JButton registerButton= new JButton();
    private final JButton demoButton = new JButton();
    private final LoginController lc;
    private final AdminMenuController amc;
    private final UserMenuController umc;
    private final DemoMenuController dmc;
    private final MenuPresenter mp;

    public LoginWindow(LoginController lc, AdminMenuController amc, UserMenuController umc, DemoMenuController dmc, MenuPresenter mp) {
        this.lc = lc;
        this.amc = amc;
        this.umc = umc;
        this.dmc = dmc;
        this.mp = mp;
    }

    /**
     * Displays the login window.
     */
    public void display() {
        // create the frame
        JFrame frame = new JFrame(mp.applicationTitle);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // set the frame's size and centre it
        frame.setSize(new Dimension(350, 200));
        frame.setLocationRelativeTo(null);

        // layout the fields and button on the frame
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        // add action listeners for our buttons
        // if user clicks "sign in"
        loginButton.addActionListener(e -> {
            displaySecondaryMenu(userText.getText(), String.valueOf(passwordText.getPassword()));
            frame.dispose();
        });

        // if user clicks "create an account"
        registerButton.addActionListener(e -> {
            RegistrationWindow rw = new RegistrationWindow(lc, umc, mp);
            rw.display();
            frame.dispose();
        });


        demoButton.addActionListener(e -> {
            DemoUserRegistrationWindow drw = new DemoUserRegistrationWindow(lc, umc, dmc);
            drw.display();
            frame.dispose();
        });

        // display the window
        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // add username label to panel
        userLabel.setText(mp.username);
        userLabel.setBounds(10,20,80,25);
        panel.add(userLabel);

        // add username field to panel
        userText.setBounds(100,20,165,25);
        panel.add(userText);

        // add password label to panel
        passwordLabel.setText(mp.password);
        passwordLabel.setBounds(10,50,80,25);
        panel.add(passwordLabel);

        // add password field to panel
        passwordText.setBounds(100,50,165,25);
        panel.add(passwordText);

        // add login button to panel
        loginButton.setText(mp.signIn);
        loginButton.setBounds(10, 80, 160, 25);
        panel.add(loginButton);

        // add register button to panel
        registerButton.setText(mp.createAccount);
        registerButton.setBounds(10, 110, 160, 25);
        panel.add(registerButton);

        // add demoButton
        demoButton.setText(mp.demo);
        demoButton.setBounds(180, 80, 160, 25);
        panel.add(demoButton);
    }

    // checks if credentials are valid, if so proceed to trading/admin/demo menu
    private void displaySecondaryMenu(String username, String password) {
        if (amc.validAdmin(username, password)) { // if user and pass matches an admin account
            amc.setCurrentAdmin(username);
            AdminUserMenu aum = new AdminUserMenu(amc);
            aum.display();
            clearFields();
        } else if (lc.validUser(username, password)) { // if user and pass matches a trading user account
            umc.setCurrentTradingUser(username);
            TradingUserMenu tum = new TradingUserMenu(umc);
            tum.display();
            clearFields();
        } else if (lc.validDemoUser(username, password)){
            dmc.setCurrentDemoUser(username);
            DemoUserMenu dm = new DemoUserMenu(dmc, umc, lc);
            dm.display();
        } else { // they entered something wrong or their account does not exist
            new PopUpWindow(mp.invalidMessage).display();
            clearFields();
        }

    }

    private void clearFields() {
        userText.setText("");
        passwordText.setText("");
    }
}
