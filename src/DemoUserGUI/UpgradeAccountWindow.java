package DemoUserGUI;

import Initialization.LoginController;
import Popups.PopUpWindow;
import Presenters.UserMenuPresenter;
import TradingUserGUI.TradingUserMenu;
import Users.DemoMenuController;
import Users.UserMenuController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

public class UpgradeAccountWindow {
    private final JLabel userLabel = new JLabel();
    private final JTextField userText = new JTextField(20);
    private final JLabel passwordLabel = new JLabel();
    private final JPasswordField passwordText = new JPasswordField(20);
    private final JLabel cityLabel = new JLabel();
    private final JTextField cityText = new JTextField(20);
    private final JTextField confirmText = new JPasswordField(20);
    private final JLabel confirmLabel = new JLabel();
    private final JButton createButton = new JButton();
    private final LoginController lc;
    private final DemoMenuController dmc;
    private final UserMenuPresenter ump = new UserMenuPresenter();
    private final UserMenuController umc;


    public UpgradeAccountWindow(DemoMenuController dmc, LoginController lc, UserMenuController umc){
        this.dmc = dmc;
        this.lc = lc;
        this.umc = umc;
    }

    /**
     * Displays Upgrade Window
     */
    public void display(){
        // create the frame
        JFrame frame = new JFrame(ump.accountCreationTitle);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // set the frame's size and centre it
        frame.setSize(new Dimension(400, 350));
        frame.setLocationRelativeTo(null);

        // layout the fields and button on the frame
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        // add action listener for the "sign up" button
        createButton.addActionListener(e -> createAccount(userText.getText(), passwordText.getText(), confirmText.getText(), cityText.getText()));

        // display the window
        frame.setVisible(true);

    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // add username label to panel
        userLabel.setText(ump.username);
        userLabel.setBounds(50,30,80,25);
        panel.add(userLabel);

        // create field for user to enter their username
        userText.setBounds(150,30,165,25);
        panel.add(userText);

        // add password label to panel
        passwordLabel.setText(ump.password);
        passwordLabel.setBounds(50,80,80,25);
        panel.add(passwordLabel);

        // add password field to panel
        passwordText.setBounds(150,80,165,25);
        panel.add(passwordText);

        cityLabel.setText("Enter city");
        cityLabel.setBounds(50, 130, 80, 25);
        panel.add(cityLabel);

        cityText.setBounds(150, 130, 165, 25);
        panel.add(cityText);

        // add confirm label to panel
        confirmLabel.setText(ump.confirmPsw);
        confirmLabel.setBounds(50,180,80,25);
        panel.add(confirmLabel);

        // add confirm field to panel
        confirmText.setBounds(150,180,165,25);
        panel.add(confirmText);

        // add "sign up" button to panel
        createButton.setText(ump.signUp);
        createButton.setBounds(50, 230, 160, 25);
        panel.add(createButton);
    }

    private void createAccount(String username, String password, String password2, String city) {
        if (lc.availableUsername(username) && password.equals(password2)) {
            lc.addTradingUser(username, password, city);
            UUID oldID = dmc.getCurrentDemoUser().getUserId();
            dmc.getDum().removeDemoUser(oldID);
            new TradingUserMenu(umc).display();
        } else {
            new PopUpWindow(ump.usernameTaken).display();
        }
    }
}
