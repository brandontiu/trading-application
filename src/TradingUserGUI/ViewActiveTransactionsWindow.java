package TradingUserGUI;

import Actions.EditAction;
import Exceptions.InvalidTransactionException;
import Popups.PopUpWindow;
import Presenters.UserMenuPresenter;
import Transactions.TransactionActions;
import Transactions.Meeting;
import Transactions.TransactionStatuses;
import Transactions.Transaction;
import Users.UserMenuController;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ViewActiveTransactionsWindow {
    private final UserMenuController umc;
    private final UserMenuPresenter ump = new UserMenuPresenter();
    private Transaction selectedTransaction;
    private Meeting selectedMeeting;
    private int whichMeetingSelected;
    private String transactionDetails;
    private String inputLocation;
    private Calendar dateCalendar;
    private Calendar timeCalendar;
    private Date inputDate;
    private Date inputTime;
    private String oldLocation;
    private Date oldDate;
    private Date oldTime;
    private Meeting oldMeeting;

    public ViewActiveTransactionsWindow(UserMenuController umc) {
        this.umc = umc; }

    public void display() {
        if (umc.currentTransactionList().isEmpty()) {
            PopUpWindow e = new PopUpWindow(ump.noCurrTrans);
            e.display();
        } else if(umc.getCurrentTradingUser().isFrozen()){
            PopUpWindow e = new PopUpWindow(ump.userFrozen);
            e.display();
        }
        else{
            List<Transaction> allTransactions = umc.currentTransactionList();

            JFrame frame = new JFrame(ump.viewActiveTrans);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            frame.setSize(new Dimension(700, 500));
            frame.setLocationRelativeTo(null);

            DefaultListModel<String> transactionList = new DefaultListModel<>();
            for (Transaction transaction : allTransactions) {
                transactionList.addElement(umc.getTransactionString(transaction,umc.getCurrentTradingUser())); //
                // THERE IS NO toString Implemented (use the method in menupresenter)

            }

            JList<String> trans = new JList<>(transactionList);
            trans.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            // trans.setSelectedIndex(0);
            trans.addListSelectionListener(e -> {
                selectedTransaction = allTransactions.get(trans.getSelectedIndex());
                transactionDetails = umc.getTransactionString(selectedTransaction, umc.getCurrentTradingUser());
            });

            JScrollPane scrollPane = new JScrollPane(); // makes the list scrollable
            scrollPane.getViewport().add(trans);

            Panel leftPanel = new Panel();
            // leftPanel.setLayout(null);
            leftPanel.add(scrollPane);

            JTextArea desc = new JTextArea(transactionDetails);

            Panel rightPanel = new Panel();
            // rightPanel.setLayout(null);
            rightPanel.add(desc);

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
            splitPane.setBounds(150, 100, 400, 200);

            frame.getContentPane().add(splitPane);

            JButton options = new JButton(ump.options);
            options.setBounds(600,100,100,50);
            options.addActionListener(e -> {
                TransactionStatuses transactionStatus = allTransactions.get(trans.getSelectedIndex()).getStatus();
                switch (transactionStatus) { // opens a different window depending on the status of the selected transaction
                    case PENDING:
                        if (selectedTransaction.isVirtual()) {
                            confirmedTransactionWindow();
                        }
                        else {
                            pendingTransactionWindow();
                        }
                        break;
                    case TRADED:
                        tradedTransactionWindow();
                        break;
                    case CONFIRMED:
                        confirmedTransactionWindow();
                        break;
                }
            });

            Panel bottomPanel = new Panel();
            // bottomPanel.setLayout(null);
            bottomPanel.add(options);
            frame.add(bottomPanel);
            frame.setVisible(true);
        }
    }

    public void pendingTransactionWindow() {
        JFrame frame = new JFrame(ump.editMeeting);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(new Dimension(700, 500));
        frame.setLocationRelativeTo(null);

        Panel leftPanel = new Panel();
        // leftPanel.setLayout(null);

        // edit meeting stuff
        // which meeting
        JComboBox<String> whichMeeting = new JComboBox<>();
        try {
            List<Meeting> transactionMeetings = selectedTransaction.getTransactionMeetings();
            for (Meeting meeting : transactionMeetings) {
                whichMeeting.addItem(meeting.toString());
            }
            whichMeeting.addActionListener(e -> {
                selectedMeeting = transactionMeetings.get(whichMeeting.getSelectedIndex());
                inputDate = selectedMeeting.getDate();
                inputLocation = selectedMeeting.getLocation();
                inputTime = selectedMeeting.getTime();
                whichMeetingSelected = whichMeeting.getSelectedIndex();
            });
        } catch (NullPointerException e) {
            new PopUpWindow(ump.pleaseSelect("a transaction.")).display();
        }

        // location
        JTextField location = new JTextField(ump.location);
        location.setBounds(100, 100, 50, 20);

        // date
        setDateCalendar();
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateModel.setValue(dateCalendar.getTime());

        JSpinner meetingDate = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(meetingDate, "dd/mm/yyyy");
        meetingDate.setEditor(dateEditor);

        // time
        setTimeCalendar();
        SpinnerDateModel timeModel = new SpinnerDateModel();
        timeModel.setValue(timeCalendar.getTime());

        JSpinner meetingTime = new JSpinner(timeModel);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(meetingTime, "hh:mm");
        meetingTime.setEditor(editor);

        leftPanel.add(whichMeeting);
        leftPanel.add(location);
        leftPanel.add(meetingDate);
        leftPanel.add(meetingTime);
        frame.getContentPane().add(leftPanel, BorderLayout.PAGE_START);

        Panel rightPanel = new Panel();
        // rightPanel.setLayout(null);
        // button to submit edit meeting changes
        JButton updateMeeting = new JButton(ump.updateMeeting);
        updateMeeting.setBounds(100, 150, 200, 50);
        updateMeeting.addActionListener(e -> {
            try {
                inputLocation = location.getText();
                inputDate = dateModel.getDate();
                inputTime = timeModel.getDate();
                oldLocation = selectedTransaction.getTransactionMeetings().get(whichMeetingSelected).getLocation();
                oldDate = selectedTransaction.getTransactionMeetings().get(whichMeetingSelected).getDate();
                oldTime = selectedTransaction.getTransactionMeetings().get(whichMeetingSelected).getTime();
                oldMeeting = new Meeting(oldLocation, oldDate, oldTime);
                editMeeting();
            } catch (InvalidTransactionException invalidTransactionException) {
                System.out.println("Meeting was not edited properly.");
            }
        });

        // button to confirm finalized meeting details
        JButton finalizeMeeting = new JButton(ump.finalizeDetail);
        finalizeMeeting.setBounds(100, 200, 200, 50);
        finalizeMeeting.addActionListener(e -> {
            try {
                umc.updateUsers(selectedTransaction.getId(), TransactionActions.CONFIRMMEETINGDETAILS);
            } catch (InvalidTransactionException invalidTransactionException) {
                // invalidTransactionException.printStackTrace();
            }
            confirmedMeeting();
        });

        // button to cancel transaction
        JButton cancelMeeting = new JButton(ump.cancelTrans);
        cancelMeeting.setBounds(100, 300, 200, 50);
        cancelMeeting.addActionListener(e -> {
            try {
                areYouSureWindow();
            } catch (InvalidTransactionException invalidTransactionException) {
                // invalidTransactionException.printStackTrace();
            }
        });

        rightPanel.add(updateMeeting);
        rightPanel.add(finalizeMeeting);
        rightPanel.add(cancelMeeting);

        frame.getContentPane().add(rightPanel, BorderLayout.PAGE_END);
        frame.setVisible(true);
    }

    private void setDateCalendar() { // helper method for making a date JSpinner
        dateCalendar = Calendar.getInstance();
        dateCalendar.set(Calendar.DAY_OF_MONTH, 1);
        dateCalendar.set(Calendar.MONTH, 1);
        dateCalendar.set(Calendar.YEAR, 2020);
    }

    private void setTimeCalendar() { // helper method for making a time JSpinner
        timeCalendar = Calendar.getInstance();
        timeCalendar.set(Calendar.HOUR_OF_DAY, 12);
        timeCalendar.set(Calendar.MINUTE, 0);
    }

    private void editMeeting() throws InvalidTransactionException { // helper method to check if edit threshold has been reached or not if user clicked "edit meeting"
        if (umc.editMeetingFlow(umc.getCurrentTradingUser().getUserId(), selectedTransaction.getId(), whichMeetingSelected,
                inputLocation) | umc.editMeetingFlow(umc.getCurrentTradingUser().getUserId(), selectedTransaction.getId(), whichMeetingSelected, inputTime, inputDate)) {

            // clear old edit actions involving this transaction
            umc.getAcm().clearPreviousEditActions(umc.getCurrentTradingUser(), selectedTransaction);

            // create a new action object
            UUID userId = umc.getCurrentTradingUser().getUserId();
            Meeting newMeeting = new Meeting(inputLocation, inputTime, inputDate);
            EditAction action = new EditAction(userId, selectedTransaction, whichMeetingSelected, oldMeeting, newMeeting);

            // log this action in the manager
            umc.getAcm().addAction(userId, action);

            // display msg telling user meeting was edited
            PopUpWindow edited = new PopUpWindow(ump.successfully("Edited meeting"));
            edited.display();
        }
        else {
            PopUpWindow thresholdReached = new PopUpWindow(ump.reachEditThreshold);
            thresholdReached.display();
        }
    }

    private void confirmedMeeting() { // helper method that pops up window depending on if other user has confirmed meeting details yet
        if (umc.getTm().userStatuses(selectedTransaction)) {
            PopUpWindow waiting = new PopUpWindow(ump.waitForConfirm);
            waiting.display();
        }
        else {
            PopUpWindow confirmed = new PopUpWindow(ump.meetingConfirmed);
            confirmed.display();
        }
    }

    /* https://www.javatpoint.com/java-joptionpane#:~:text=%E2%86%92%20%E2%86%90%20prev-,Java%20JOptionPane,JOptionPane%20class%20inherits%20JComponent%20class. */
    private void areYouSureWindow() throws InvalidTransactionException {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        int a = JOptionPane.showConfirmDialog(frame, ump.optionPrompt("cancel the transaction?"));
        if (a == JOptionPane.YES_OPTION) {
            umc.updateUsers(selectedTransaction.getId(), TransactionActions.CANCEL);
            cancelledWindow();
        }
    }

    private void cancelledWindow() {
        PopUpWindow cancelled = new PopUpWindow(ump.transCancelled);
        cancelled.display();
    }

    public void tradedTransactionWindow() {
        JFrame frame = new JFrame();
        frame.setSize(new Dimension(500, 500));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JButton confirm = new JButton(ump.itemReturned);
        confirm.setBounds(100, 100, 200, 50);
        confirm.addActionListener(e -> {
            try {
                umc.updateUsers(selectedTransaction.getId(), TransactionActions.ITEMRETURNED);
            } catch (InvalidTransactionException invalidTransactionException) {
                // invalidTransactionException.printStackTrace();
            }
            PopUpWindow confirmed = new PopUpWindow(ump.itemReturnConfirmed);
            confirmed.display();
        });

        JButton claim = new JButton(ump.itemNotReturned);
        claim.setBounds(100, 300, 200, 50);
        claim.addActionListener(e -> {
            try {
                umc.updateUsers(selectedTransaction.getId(), TransactionActions.ITEMNOTRETURNED);
            } catch (InvalidTransactionException invalidTransactionException) {
                // invalidTransactionException.printStackTrace();
            }
            cancelledWindow();
        });

        panel.add(confirm);
        panel.add(claim);
        frame.add(panel);
        frame.setVisible(true);
    }

    public void confirmedTransactionWindow() {
        JFrame frame = new JFrame();
        frame.setSize(new Dimension(500, 500));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JButton confirm = new JButton(ump.confirmExchange);
        confirm.setBounds(100, 100, 300, 50);
        confirm.addActionListener(e -> {
            try {
                umc.updateUsers(selectedTransaction.getId(), TransactionActions.CONFIRMMEETUP);
            } catch (InvalidTransactionException invalidTransactionException) {
                invalidTransactionException.printStackTrace();
            }
            PopUpWindow confirmed = new PopUpWindow(ump.meetupOccurrenceConfirmed);
            confirmed.display();
        });

        JButton claim = new JButton(ump.exchangeNotTakenPlace);
        claim.setBounds(100, 300, 300, 50);
        claim.addActionListener(e -> {
            try {
                umc.updateUsers(selectedTransaction.getId(), TransactionActions.MEETUPINCOMPLETE);
            } catch (InvalidTransactionException invalidTransactionException) {
                invalidTransactionException.printStackTrace();
            }
            cancelledWindow();
        });

        panel.add(confirm);
        panel.add(claim);
        frame.add(panel);
        frame.setVisible(true);
    }
}
