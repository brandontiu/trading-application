package AdminGUI;

import Admins.AdminMenuController;
import Exceptions.InvalidTradingUserException;
import Presenters.AdminMenuPresenter;
import TradingUserGUI.TradingUserProfileWindow;
import Users.TradingUser;
import Users.TradingUserManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.List;

public class FlaggedUsersWindow {
    private final AdminMenuController amc;
    private final TradingUserManager tum;
    private final AdminMenuPresenter amp = new AdminMenuPresenter();
    private JComboBox<String> users;
    private JButton freezeButton;
    private JButton unflagButton;
    private JPanel mainPanel;
    private JLabel usernameLabel;
    private JButton userProfile;
    private String currUser;

    public FlaggedUsersWindow(AdminMenuController amc) {
        this.amc = amc;
        this.tum = amc.getTUM();
    }

    public void display() {
        JFrame frame = new JFrame(amp.checkFlaggedUser);
        frame.setContentPane(mainPanel);
        frame.setSize(new Dimension(500, 300));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // frame.pack();
        frame.setVisible(true);


        List<TradingUser> listUsers = tum.getFlaggedAccounts();

        DefaultComboBoxModel<String> userNames = new DefaultComboBoxModel<>();
        for (TradingUser curr : listUsers) {
            String username = curr.getUsername();
            userNames.addElement(username);
        }

        users.setModel(userNames);


        freezeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currUser == null) {
                    return;
                }
                try {
                    tum.freezeAccount(tum.getTradingUser(currUser));
                } catch (InvalidTradingUserException invalidTradingUserException) {
                    invalidTradingUserException.printStackTrace();
                }
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }

        });
        unflagButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!(currUser == null)) {
                    List<String> flaggedUsernames = tum.convertFlaggedUsersToUsernames();
                    if (flaggedUsernames.contains(currUser)) {
                        tum.removeFlaggedUsername(currUser);
                    }
                }
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }});

        users.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currUser = (String) users.getSelectedItem();

            }
        });

        userProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TradingUser user;
                try {
                    user = tum.getTradingUser(currUser);
                    TradingUserProfileWindow tupw = new TradingUserProfileWindow(amc.getPTM(), user);
                    tupw.display();
                } catch (InvalidTradingUserException invalidTradingUserException) {
                    invalidTradingUserException.printStackTrace();
                }

            }
        });
    }

}
