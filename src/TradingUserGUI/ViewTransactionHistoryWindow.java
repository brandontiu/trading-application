package TradingUserGUI;
import Items.Item;
import Items.ItemManager;
import Presenters.UserMenuPresenter;
import Transactions.*;
import Users.TradingUser;
import Users.TradingUserManager;
import Users.UserMenuController;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ViewTransactionHistoryWindow {
    private final UserMenuController umc;
    private final UserMenuPresenter ump = new UserMenuPresenter();
    private JPanel panel = new JPanel();


    public ViewTransactionHistoryWindow(UserMenuController umc) {
        this.umc = umc;

    }
    public void display(){

        // create the frame
        JFrame frame = new JFrame(ump.viewTranHistory);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // sets to close the window, but not the program once this is done


        //centre frame
        frame.setSize(new Dimension(550, 550));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.add(panel);

       JButton twoWayButton = new JButton(ump.mostRecentTwoWayTrans);
       twoWayButton.setBounds(50, 50, 150, 75);
       twoWayButton.addActionListener(
               e -> mostRecentTwoWaysWindow()
       );

       JButton oneWayButton = new JButton(ump.mostRecentOneWayTrans);
       oneWayButton.setBounds(200, 50, 150, 75);
       oneWayButton.addActionListener(
               e -> mostRecentOneWaysWindow()
       );

       JButton tradedUsersButton = new JButton(ump.mostTradedUser);
       tradedUsersButton.setBounds(250, 100, 150, 75);
       tradedUsersButton.addActionListener(
               e -> mostTradedWithUsersWindow()
       );

        panel.add(twoWayButton);
        panel.add(oneWayButton);
        panel.add(tradedUsersButton);

    }
    private void mostRecentOneWaysWindow()  { // condense this a bit
        JFrame frame = new JFrame(ump.mostRecentOneWayTrans);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.setSize(new Dimension(350, 200));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        List<UUID> mostRecentOneWays = umc.getCurrentTradingUser().getTransactionHistory().mostRecentOneWayTransactions(); // need to handle this when it's null
        StringBuilder text = new StringBuilder(" ");
        ArrayList<Transaction> allRecentOneWays = umc.getTm().getTransactionsFromIdList(mostRecentOneWays);
        for (Transaction t: allRecentOneWays){
            String id = t.getId().toString();
            // String date = t.getTransactionMeetings().get(t.getTransactionMeetings().size()-1).getDate().toString();
            List<UUID> itemIDs = t.getTransactionItems();
            List<Item> items = umc.getIm().convertIdsToItems(itemIDs);
            String transItems = items.toString();
            String users = umc.getUm().getUsernameListByID(t.getUsers()).toString();
            text.append("\n").append(ump.transaction).append(id).append("\n").append(transItems).append("\n").append(users);
        }
        String fullText = text.toString();

        JTextArea descr = new JTextArea(fullText);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getViewport().add(descr);

        frame.add(descr);
    }

    private void mostRecentTwoWaysWindow(){
        JFrame frame = new JFrame(ump.mostRecentTwoWayTrans);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.setSize(new Dimension(350, 200));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        List<UUID> mostRecentTwoWays = umc.getCurrentTradingUser().getTransactionHistory().mostRecentTwoWayTransactions(); // need to handle this when it's null

        StringBuilder text = new StringBuilder(" ");
        ArrayList<Transaction> allRecentOneWays = umc.getTm().getTransactionsFromIdList(mostRecentTwoWays);
        for (Transaction t: allRecentOneWays){
            String id = t.getId().toString();
            // String date = t.getTransactionMeetings().get(t.getTransactionMeetings().size()-1).getDate().toString();
            List<UUID> itemIDs = t.getTransactionItems();
            List<Item> items = umc.getIm().convertIdsToItems(itemIDs);
            String transItems = items.toString();
            String users = umc.getUm().getUsernameListByID(t.getUsers()).toString();
            text.append("\n").append(ump.transaction).append(id).append("\n").append(transItems).append("\n").append(users);
        }

        String fullText = text.toString();

        JTextArea descr = new JTextArea(fullText);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getViewport().add(descr);

        frame.add(descr);
        frame.setVisible(true);

    }

    private void mostTradedWithUsersWindow(){
        JFrame frame = new JFrame(ump.mostTradedUser);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.setSize(new Dimension(350, 200));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        StringBuilder text = new StringBuilder(" ");
        List<String> usernames = umc.getCurrentTradingUser().getTransactionHistory().mostTradedWithUsers();
        for (String u: usernames){
            text.append(u).append("\n");
        }

        String fullText = text.toString();

        JTextArea descr = new JTextArea(fullText);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getViewport().add(descr);

        frame.add(descr);
        frame.setVisible(true);

    }
}
