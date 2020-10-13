package DemoUserGUI;

import Initialization.LoginController;
import Items.Item;
import Popups.PopUpWindow;
import Presenters.UserMenuPresenter;
import TradingUserGUI.TradingUserMenu;
import Users.DemoMenuController;
import Users.DemoUser;
import Users.DemoUserManager;
import Users.UserMenuController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DemoUserRegistrationWindow {
    private final JLabel userLabel = new JLabel();
    private final JTextField userText = new JTextField(20);
    private final JLabel passwordLabel = new JLabel();
    private final JPasswordField passwordText = new JPasswordField(20);
    private final JTextField confirmText = new JPasswordField(20);
    private final JLabel confirmLabel = new JLabel();
    private final JButton createButton = new JButton();
    private final JTextArea desc = new JTextArea();
    private final LoginController lc;
    private final UserMenuController umc;
    private final DemoMenuController dmc;
    private final UserMenuPresenter ump = new UserMenuPresenter();

    public DemoUserRegistrationWindow(LoginController lc, UserMenuController umc, DemoMenuController dmc) {
        this.lc = lc;
        this.umc = umc;
        this.dmc = dmc;
    }

    /**
     * Displays the DemoUserRegistrationWindow
     */
    public void display() {
        // create the frame
        JFrame frame = new JFrame(ump.demoCreationTitle);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // set the frame's size and centre it
        frame.setSize(new Dimension(400, 400));
        frame.setLocationRelativeTo(null);

        // layout the fields and button on the frame
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        // add action listener for the "sign up" button
        createButton.addActionListener(e -> {
            createAccount(userText.getText(), passwordText.getText(), confirmText.getText());
            frame.dispose();
        });

        // display the window
        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // add description
        desc.setText(ump.tryDemoMessage);
        desc.setBounds(25, 50, 350, 50);
        panel.add(desc);

        // add username label to panel
        userLabel.setText(ump.username);
        userLabel.setBounds(50,140,80,25);
        panel.add(userLabel);

        // create field for user to enter their username
        userText.setBounds(150,140,165,25);
        panel.add(userText);

        // add password label to panel
        passwordLabel.setText(ump.password);
        passwordLabel.setBounds(50,190,80,25);
        panel.add(passwordLabel);

        // add password field to panel
        passwordText.setBounds(150,190,165,25);
        panel.add(passwordText);

        // add confirm label to panel
        confirmLabel.setText(ump.confirmPsw);
        confirmLabel.setBounds(50,240,80,25);
        panel.add(confirmLabel);

        // add confirm field to panel
        confirmText.setBounds(150,240,165,25);
        panel.add(confirmText);

        // add "sign up" button to panel
        createButton.setText(ump.signUp);
        createButton.setBounds(50, 290, 160, 25);
        panel.add(createButton);
    }
    private void createAccount(String username, String password, String password2) {
        if (lc.availableUsername(username) && password.equals(password2)) {
            // checks to see if the username is available and if the two password textfields are correct

            lc.addDemoUser(username, password);
            dmc.setCurrentDemoUser(username);

            // add Items to the Inventory and WishList as demonstration
            Item item1 = new Item("Book");
            Item item2 = new Item("Laptop");
            dmc.addItem(item1, "wishlist");
            dmc.addItem(item2, "inventory");
            new DemoUserMenu(dmc,umc,lc).display();
        } else {
            new PopUpWindow(ump.usernameTaken).display();
        }
    }
}
