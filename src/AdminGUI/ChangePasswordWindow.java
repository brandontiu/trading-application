package AdminGUI;

import Admins.AdminMenuController;
import Presenters.AdminMenuPresenter;

import javax.swing.*;
import java.awt.*;

public class ChangePasswordWindow {
    private final AdminMenuController amc; // TO-DO: also do for UserMenu when packages reorganized
    private final AdminMenuPresenter amp = new AdminMenuPresenter();
    // TO-DO: also ask them for their old password before they can change to new one?
    private final JLabel passwordLabel = new JLabel();
    private final JPasswordField passwordText = new JPasswordField(20);
    private final JPasswordField confirmText = new JPasswordField(20);
    private final JLabel confirmLabel = new JLabel();
    private final JButton changePassword = new JButton();

    public ChangePasswordWindow(AdminMenuController amc) {
        this.amc = amc;
    }

    public void display() {
        JFrame frame = new JFrame(amp.changePsw);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(new Dimension(400, 250));
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        changePassword.addActionListener(e -> {
            String passText = new String(passwordText.getPassword());
            String confText = new String(confirmText.getPassword());
            changePassword(passText, confText);
        });

        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        passwordLabel.setText(amp.newPassword);
        passwordLabel.setBounds(50,80,80,25);
        panel.add(passwordLabel);

        passwordText.setBounds(150,80,165,25);
        panel.add(passwordText);

        confirmLabel.setText(amp.confirmPsw);
        confirmLabel.setBounds(50,130,80,25);
        panel.add(confirmLabel);

        confirmText.setBounds(150,130,165,25);
        panel.add(confirmText);

        changePassword.setText(amp.changePsw);
        changePassword.setBounds(50, 200, 160, 25);
        panel.add(changePassword);
    }

    private void changePassword(String password, String password2) {
        if (password.equals(password2)) {
            amc.getCurrentAdmin().setPassword(password);
            // Popups.PopUpWindow puw = new Popups.PopUpWindow("Password successfully changed");
            System.exit(0);
        }
        // else { Popups.PopUpWindow puw = new Popups.PopUpWindow("Passwords do not match."); }
    }
}