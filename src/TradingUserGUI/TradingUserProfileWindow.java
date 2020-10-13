package TradingUserGUI;

import Transactions.PastTransactionManager;
import Users.TradingUser;

import javax.swing.*;

public class TradingUserProfileWindow {
    private JTextArea weeklyThreshold;
    private JTextArea incompleteThreshold;
    private JTextArea numWishlist;
    private JTextArea numActiveTransactions;
    private JTextArea numLended;
    private JTextArea numBorrowed;
    private JTextArea numCompleteTransactions;
    private JTextArea numCancelledTransactions;
    private JTextArea score;
    private JTextArea numInventory;
    private JTextArea userStatus;
    private JTextArea borrowThreshold;
    private PastTransactionManager ptm;
    private TradingUser currUser;
    private JPanel mainPanel;
    private JTextArea username;
    private JLabel weeklyThresholdLabel;


    public TradingUserProfileWindow(PastTransactionManager ptm, TradingUser currUser){
        this.ptm = ptm;
        this.currUser = currUser;
    }

    public void display(){
        JFrame frame = new JFrame("UserProfile");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        weeklyThreshold.setText(Integer.toString(currUser.getWeeklyThreshold()));
        borrowThreshold.setText(Integer.toString(currUser.getBorrowThreshold()));
        incompleteThreshold.setText(Integer.toString(currUser.getIncompleteThreshold()));
        username.setText(currUser.getUsername());
        numInventory.setText(Integer.toString(currUser.getInventory().size()));
        userStatus.setText(currUser.getStatus().toString());
        numWishlist.setText(Integer.toString(currUser.getWishlist().size()));
        numActiveTransactions.setText(Integer.toString(currUser.getCurrentTransactions().size()));

        int lended = currUser.getTransactionHistory().getNumItemsLended();
        int borrowed = currUser.getTransactionHistory().getNumItemsBorrowed();
        int cancelled = ptm.getNumCancelledTransactions(currUser);
        int complete = ptm.getNumCompletedTransactions(currUser);
        numLended.setText(Integer.toString(lended));
        numBorrowed.setText(Integer.toString(borrowed));
        numCancelledTransactions.setText(Integer.toString(cancelled));
        numCompleteTransactions.setText(Integer.toString(complete));
        score.setText(Integer.toString(lended - borrowed + complete - cancelled));


    }

}
