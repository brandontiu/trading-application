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
import java.util.List;

public class FrozenUsersWindow {
    private final AdminMenuController amc;
    private final TradingUserManager tum;
    private final AdminMenuPresenter amp = new AdminMenuPresenter();
    private JComboBox<String> users;
    private JButton unfreeze;
    private JPanel mainPanel;
    private JLabel usernameLabel;
    private JButton userProfile;
    private String currUsername;


    public FrozenUsersWindow(AdminMenuController amc) {
        this.amc = amc;
        this.tum = amc.getTUM();
    }

    public void display() {
        JFrame frame = new JFrame(amp.unfreezeRequest);
        frame.setContentPane(mainPanel);
        frame.setSize(new Dimension(500, 300));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // frame.pack();
        frame.setVisible(true);


        List<TradingUser> listUsers = tum.getFrozenAccounts();

        DefaultComboBoxModel<String> userNames = new DefaultComboBoxModel<String>();
        for (TradingUser curr : listUsers) {
            String username = curr.getUsername();
            userNames.addElement(username);
        }
        users.setModel(userNames);


        users.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currUsername = (String) users.getSelectedItem();


            }
        });
        unfreeze.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!(currUsername == null)) {
                    List<String> frozenUsernames = tum.convertFrozenUsersToUsernames();
                    if (frozenUsernames.contains(currUsername)) {
                        tum.removeFrozenUsername(currUsername);
                    }

            }
        }});

        userProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TradingUser user;
                try {
                    user = tum.getTradingUser(currUsername);
                    TradingUserProfileWindow tupw = new TradingUserProfileWindow(amc.getPTM(), user);
                    tupw.display();
                } catch (InvalidTradingUserException invalidTradingUserException) {
                    invalidTradingUserException.printStackTrace();
                }
            }
        });
    }

}