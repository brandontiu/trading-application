package TradingUserGUI;

import Exceptions.InvalidItemException;
import Items.Item;
import Popups.PopUpWindow;
import Presenters.UserMenuPresenter;
import Transactions.TransactionBuilder;
import Users.TradingUser;
import Users.UserMenuController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

public class TransactionWindow {
    private UserMenuController umc;
    private final UserMenuPresenter ump = new UserMenuPresenter();
    private Item offeredItem;
    private final TradingUser selectedItemOwner;
    private final JPanel panel1 = new JPanel();
    private JPanel panel2 = new JPanel();
    private final TransactionBuilder tb;
    private final JFrame frame = new JFrame(ump.createTrans);
    private final JButton submit = new JButton("Submit");
    private Item item;
    private Calendar dateCalendar;
    private Calendar timeCalendar;
    private final JTextField location = new JTextField("Location");
    private final SpinnerDateModel dateModel = new SpinnerDateModel();
    private final JSpinner meetingDate = new JSpinner(dateModel);
    private final SpinnerDateModel timeModel = new SpinnerDateModel();
    private final JSpinner meetingTime = new JSpinner(timeModel);

    public TransactionWindow(UserMenuController umc, UUID itemId, UUID ownerId) throws InvalidItemException {
        this.umc = umc;
        this.tb = umc.getTransBuilder();
        selectedItemOwner = umc.getUm().getTradingUserById(ownerId);
        tb.declareIntent(ownerId, itemId); // this is the selected item that the user chose from the available items window, which would be the desired item and item's owner
        item = umc.getIm().getItem(itemId);
    }

    public void display() {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(new Dimension(1000, 500));
        frame.setLocationRelativeTo(null);

        // set the panels on the frame
        frame.add(panel1, BorderLayout.WEST);

        setupLeftPanel();
        // display the window
        frame.setVisible(true);
    }

    private void setupLeftPanel() {
        panel1.add(new JLabel(ump.tranStatus));

        // create the radio buttons
        JRadioButton virtualButton = new JRadioButton(ump.virtual);
        JRadioButton permButton = new JRadioButton(ump.perm);
        JRadioButton tempButton = new JRadioButton(ump.temp);

        // group the buttons
        ButtonGroup group = new ButtonGroup();
        group.add(virtualButton);
        group.add(permButton);
        group.add(tempButton);

        // add event handlers for the buttons
        virtualButton.addActionListener(e -> {
            try {
                panel2.removeAll();
                setupRightPanel(ump.virtual);
            } catch (InvalidItemException invalidItemException) {
                //
            }
        });

        permButton.addActionListener(e -> {
            try {
                panel2.removeAll();
                setupRightPanel(ump.perm);
            } catch (InvalidItemException invalidItemException) {
                //
            }
        });

        tempButton.addActionListener(e -> {
            try {
                panel2.removeAll();
                setupRightPanel(ump.temp);
            } catch (InvalidItemException invalidItemException) {
                //
            }
        });

        // add the buttons to the panel
        panel1.add(virtualButton);
        panel1.add(permButton);
        panel1.add(tempButton);
    }

    private void setupRightPanel(String type) throws InvalidItemException {
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.PAGE_AXIS));
        panel2.add(new JLabel(ump.offerItem));

        // create JList of suggested items
        List<Item> itemSuggestions = umc.getIm().itemSuggestions(umc.getCurrentTradingUser(), selectedItemOwner);
        DefaultListModel<String> itemStrings = new DefaultListModel<>();
        for (Item item : itemSuggestions) {
            itemStrings.addElement(item.toString());
        }
        JList<String> suggestions = new JList<>(itemStrings);
        panel2.add(suggestions);

        // create JComboBox of user's inventory
        List<Item> inventory = umc.getIm().convertIdsToItems(umc.getCurrentTradingUser().getInventory());
        JComboBox<Item> comboBox = new JComboBox<>();
        for (Item item : inventory) {
            comboBox.addItem(item);
        }

        // add event handler for comboBox
        comboBox.addActionListener(e -> {
            offeredItem = comboBox.getItemAt(comboBox.getSelectedIndex());
        });

        JButton offerItem = new JButton("Offer selected item, don't click if u don't wanna offer smth");

            offerItem.addActionListener(e -> {
                try {
                tb.AddItemOffered(offeredItem.getId()); // this is the item that the current user is offering from their own inventory
                new PopUpWindow(offeredItem + " selected").display();
                } catch (NullPointerException j)  {
                    new PopUpWindow(ump.pleaseSelect("an item.")).display(); }

            });


        panel2.add(comboBox);
        panel2.add(offerItem);

        JButton cont = new JButton("Continue making tran");
        panel2.add(cont);
        frame.add(panel2, BorderLayout.EAST);
        frame.setVisible(true);

        cont.addActionListener(e -> {
            if (type.equals(ump.virtual)) {
                try {
                    areYouSureWindow();
                } catch (InvalidItemException invalidItemException) {
                    //
                }
            }
            // panel2.add(submit);

            // add meetings fields
            if (type.equals(ump.perm)) {
                JFrame frame2 = new JFrame("make first meeting");
                frame2.setSize(new Dimension(600, 500));
                frame2.setLocationRelativeTo(null);

                JPanel panel3 = setMeetingPanel("first");
                submit.addActionListener(g -> {
                    try {
                        frame2.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                        tb.buildFirstMeeting(location.getText(), timeModel.getDate(), dateModel.getDate());
                        areYouSureWindow();
                    } catch (InvalidItemException invalidItemException) {
                        //
                    }
                });
                panel3.add(submit);
                frame2.add(panel3, BorderLayout.WEST);
                frame2.setVisible(true);
            }
            else if (type.equals(ump.temp)) {
                JFrame frame2 = new JFrame("make first meeting");
                frame2.setSize(new Dimension(600, 500));
                frame2.setLocationRelativeTo(null);

                JPanel panel3 = setMeetingPanel("first");
                submit.addActionListener(h -> {
                    frame2.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    tb.buildFirstMeeting(location.getText(), timeModel.getDate(), dateModel.getDate());
                    secondMeetingWindow();
                });
                panel3.add(submit);
                frame2.add(panel3, BorderLayout.WEST);
                frame2.setVisible(true);
            }
        });
    }

    private void setDateCalendar() { // helper method for making a date JSpinner
        dateCalendar = Calendar.getInstance();
        // dateCalendar.set(Calendar.DATE, 1);
        // dateCalendar.set(Calendar.MONTH, 1);
        // dateCalendar.set(Calendar.YEAR, 2020);
    }

    private void setTimeCalendar() { // helper method for making a time JSpinner
        timeCalendar = Calendar.getInstance();
        // timeCalendar.set(Calendar.HOUR_OF_DAY, 12);
        // timeCalendar.set(Calendar.MINUTE, 0);
    }

    private JPanel setMeetingPanel(String meetingNum) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(meetingNum + "Meeting"));

        location.setBounds(100, 100, 50, 20);

        setDateCalendar();
        dateModel.setValue(dateCalendar.getTime());

        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(meetingDate, "dd/mm/yyyy");
        meetingDate.setEditor(dateEditor);

        setTimeCalendar();
        timeModel.setValue(timeCalendar.getTime());

        JSpinner.DateEditor editor = new JSpinner.DateEditor(meetingTime, "hh:mm");
        meetingTime.setEditor(editor);

        panel.add(location);
        panel.add(meetingDate);
        panel.add(meetingTime);
        return panel;
    }

    private void areYouSureWindow() throws InvalidItemException {
        int a = JOptionPane.showConfirmDialog(frame, "Are you sure you want to create this transaction?");
        if (a == JOptionPane.YES_OPTION) {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            umc.transactionUpdate(tb.getTransaction());
            new PopUpWindow("The Transaction has been made.").display();
        }
        if (a == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }

    private void secondMeetingWindow() {
        JFrame tempFrame = new JFrame("Second Meeting");
        tempFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tempFrame.setSize(new Dimension(500, 500));
        tempFrame.setLocationRelativeTo(null);

        JPanel panel3 = setMeetingPanel("second");
        JButton submit2 = new JButton("Submit Second Meeting");
        submit2.addActionListener(e -> {
            tempFrame.dispatchEvent(new WindowEvent(tempFrame, WindowEvent.WINDOW_CLOSING));
            try {
                tb.buildSecondMeeting(location.getText(), timeModel.getDate(), dateModel.getDate());
                areYouSureWindow();
            } catch (InvalidItemException invalidItemException) {
                //
            }
        });
        panel3.add(submit2);
        tempFrame.add(panel3);
        tempFrame.setVisible(true);
    }
}