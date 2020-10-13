package Popups;

import Admins.AdminManager;
import Admins.AdminUser;
import Presenters.MenuPresenter;
import Users.TradingUser;
import Users.TradingUserManager;
import Users.User;

import javax.swing.*;
import java.awt.*;

public class ChangePasswordWindow {
    private AdminManager am = null;
    private TradingUserManager um = null;
    private MenuPresenter mp = new MenuPresenter();
    private final User user;
    private final JLabel newLabel = new JLabel();
    private final JPasswordField newPass = new JPasswordField(20);
    private final JLabel confirmLabel = new JLabel();
    private final JPasswordField confirmPass = new JPasswordField(20);
    private final JButton confirmButton = new JButton();

    public ChangePasswordWindow(AdminManager am, AdminUser user) {
        this.am = am;
        this.user = user;
    }

    public ChangePasswordWindow(TradingUserManager um, TradingUser user) {
        this.um = um;
        this.user = user;
    }

    public void display() {
        // create the frame
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // set the frame's size and centre it
        frame.setSize(new Dimension(350, 200));
        frame.setLocationRelativeTo(null);

        // layout the fields and button on the frame
        JPanel panel = new JPanel();
        placeComponents(panel);
        frame.add(panel);

        // add event handler for confirm button
        confirmButton.addActionListener(e -> {
            changePassword();
        });

        // display the window
        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        newLabel.setText(mp.newPassword);
        newLabel.setBounds(10,20,80,25);
        panel.add(newLabel);

        newPass.setBounds(100,20,165,25);
        panel.add(newPass);

        confirmLabel.setText(mp.confirmPsw);
        confirmLabel.setBounds(10,50,80,25);
        panel.add(confirmLabel);

        confirmPass.setBounds(100,50,165,25);
        panel.add(confirmPass);

        confirmButton.setText(mp.changePsw);
        confirmButton.setBounds(10, 110, 160, 25);
        panel.add(confirmButton);
    }

    private void changePassword() {
        // get the entered passwords as strings
        String newPassText = String.valueOf(newPass.getPassword());
        String confirmPassText = String.valueOf(confirmPass.getPassword());

        // check if passwords match, if so then change password
        if (am == null) { // passed um into constructor therefore it's a TradingUser that wants to change pwd
            if (newPassText.equals(confirmPassText)) {
                um.changePassword((TradingUser) user, newPassText);
            } else { // passwords don't match
                new PopUpWindow(mp.passwordNotMatch).display();
            }
        } else if (um == null) { // passed am into constructor therefore it's an Admin that wants to change pwd
            if (newPassText.equals(confirmPassText)) {
                am.changePassword((AdminUser) user, confirmPassText);
            } else { // passwords don't match
                new PopUpWindow(mp.passwordNotMatch).display();
            }
        }
    }
}
