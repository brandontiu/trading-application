package AdminGUI;

import Admins.AdminMenuController;
import Exceptions.InvalidTradingUserException;
import Popups.PopUpWindow;
import Presenters.AdminMenuPresenter;
import TradingUserGUI.TradingUserProfileWindow;
import Users.TradingUser;
import Users.TradingUserManager;
import Users.User;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ChangeThresholdWindow {
    private final AdminMenuController amc;
    private final TradingUserManager tum;
    private final AdminMenuPresenter amp = new AdminMenuPresenter();
    private JPanel JPanel1;
    private JButton refreshThresholdsButton;
    private JTextField newBorrowThreshold;
    private JTextField newWeeklyThreshold;
    private JTextField newIncompleteThreshold;
    private JButton saveChangesButton;
    private JComboBox<String> users;
    private JTextArea currBorrowThreshold;
    private JTextArea currWeeklyThreshold;
    private JTextArea currIncompleteThreshold;
    private JButton viewUserProfile;
    private String currUsername;

    public ChangeThresholdWindow(AdminMenuController amc) {
        this.amc = amc;
        this.tum = amc.getTUM();

    }

    public void display() {
        JFrame frame = new JFrame(amp.changeThreshold);
        frame.setContentPane(JPanel1);
        frame.setSize(new Dimension(800, 500));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // frame.pack();
        frame.setVisible(true);

        List<TradingUser> listUsers = amc.getAllTradingUsers();

        DefaultComboBoxModel<String> userNames = new DefaultComboBoxModel<String>();
        for (TradingUser curr : listUsers) {
            String username = curr.getUsername();
            userNames.addElement(username);
        }

        users.setModel(userNames);

        users.addActionListener(e -> currUsername = (String) users.getSelectedItem());

        saveChangesButton.addActionListener(e -> {
            int borrowThreshold = Integer.parseInt(newBorrowThreshold.getText());
            int weeklyThreshold = Integer.parseInt(newWeeklyThreshold.getText());
            int incompleteThreshold = Integer.parseInt(newIncompleteThreshold.getText());
            try {
                amc.updateThreshold(currUsername, borrowThreshold,"Borrow");
                amc.updateThreshold(currUsername, weeklyThreshold, "Weekly");
                amc.updateThreshold(currUsername, incompleteThreshold, "Incomplete");
            } catch (InvalidTradingUserException invalidTradingUserException) {
                //JOptionPane.showMessageDialog(null,
                //"Error: UpdateThreshold method in adminController failed, so the threshold wasn't changed", "Error Message",
                //JOptionPane.ERROR_MESSAGE);
                new PopUpWindow(amp.updateThresholdError).display();
            }


        });

        refreshThresholdsButton.addActionListener(e -> {
            List<Integer> thresholdList = tum.getCurrThresholds(currUsername);
            currBorrowThreshold.setText(thresholdList.get(0).toString());
            currWeeklyThreshold.setText(thresholdList.get(1).toString());
            currIncompleteThreshold.setText(thresholdList.get(2).toString());

        });

        newBorrowThreshold.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();
            }
            public void removeUpdate(DocumentEvent e) {
                warn();
            }
            public void insertUpdate(DocumentEvent e) {
                warn();
            }});


        newWeeklyThreshold.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();
            }
            public void removeUpdate(DocumentEvent e) {
                warn();
            }
            public void insertUpdate(DocumentEvent e) {
                warn();
            }});


        newIncompleteThreshold.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                warn();
            }
            public void removeUpdate(DocumentEvent e) {
                warn();
            }
            public void insertUpdate(DocumentEvent e) {
                warn();
            }});

        viewUserProfile.addActionListener(new ActionListener() {
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
    private void warn() {
        try {
            int newThreshold = Integer.parseInt(newBorrowThreshold.getText());
            if (newThreshold <= 0) {
                new PopUpWindow(amp.enter("number bigger than 0")).display();
            }
        } catch (NumberFormatException e) {
            new PopUpWindow(amp.enter("an integer")).display();
        }
    }
}
