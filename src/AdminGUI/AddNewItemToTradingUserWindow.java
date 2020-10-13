package AdminGUI;

import Admins.AdminMenuController;
import Items.Item;
import Popups.PopUpWindow;
import Presenters.AdminMenuPresenter;
import Users.TradingUser;

import javax.swing.*;
import java.awt.*;

/**
 * part of code taken from
 * https://www.javatpoint.com/java-jscrollpane
 */

public class AddNewItemToTradingUserWindow {
    private final AdminMenuController amc;
    private final AdminMenuPresenter amp = new AdminMenuPresenter();
    private final JLabel itemNameLabel = new JLabel();
    private final JLabel desLabel = new JLabel();
    private final JTextField itemNameText = new JTextField(20);
    private final JTextArea desText = new JTextArea(5,20);
    private TradingUser selectedTradingUser;
    private final JButton addToWishlist = new JButton();
    private final JButton addToInventory = new JButton();
    boolean successfullyAdded=false;


    public AddNewItemToTradingUserWindow(AdminMenuController amc) {
        this.amc = amc;
    }
    public void display(){
        //create frame
        JFrame frame = new JFrame(amp.addNewItem);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // set the frame's size and centre it
        frame.setSize(new Dimension(700, 500));
        frame.setLocationRelativeTo(null);

        //show window
        frame.setVisible(true);

        JPanel mainPanel = new JPanel();
        frame.add(mainPanel);

        // add labels, textField and textArea
        mainPanel.setLayout(null);
        itemNameLabel.setText(amp.itemName);
        itemNameLabel.setBounds(50, 80, 150, 25);
        mainPanel.add(itemNameLabel);

        itemNameText.setBounds(180, 80, 165, 25);
        mainPanel.add(itemNameText);

        desLabel.setText(amp.itemDes);
        desLabel.setBounds(50, 130, 150, 25);
        mainPanel.add(desLabel);

        desText.setBounds(180, 130, 165, 125);
        mainPanel.add(desText);


        DefaultListModel<TradingUser> tradingUsers = new DefaultListModel<>();
        for(TradingUser tradingUser:amc.getAllTradingUsers()){
            tradingUsers.addElement(tradingUser);
        }

        // create JList
        JList<TradingUser> userList = new JList<>(tradingUsers);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.addListSelectionListener(e -> {
            selectedTradingUser = userList.getSelectedValue();
        });

        // makes the Jlist scrollable
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getViewport().add(userList);
        scrollPane.setBounds(400, 80, 250, 200);
        mainPanel.add(scrollPane);

        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        //Add to Wishlist and Add to Inventory buttons
        addToInventory.setText(amp.addToInventory);
        addToInventory.setBounds(180,350,150,30);
        addToInventory.addActionListener(e -> {
            Item newItem = new Item(itemNameText.getText());
            newItem.setDescription(desText.getText());

            if(itemNameText.getText().isEmpty()) {//item name is empty give error message
                new PopUpWindow(amp.enter("item name")).display();
            } else {
                if(!successfullyAdded){//checks if item has been previously added to prevent multiply operation
                    try {//user is selected and item added to inventory successfully
                        if (amc.addItemToUser(selectedTradingUser.getUserId(), newItem, "inventory")) {
                            successfullyAdded = true;
                            new PopUpWindow(amp.successfully("Item added")).display();
                        }
                    } catch (NullPointerException nullPointerException) {//user is not selected
                        new PopUpWindow(amp.selectUser).display();
                    }
                }else{
                    new PopUpWindow(amp.itemAddedError).display();
                }
            }
        });

        addToWishlist.setText(amp.addToWishlist);
        addToWishlist.setBounds(400, 350, 150, 30);
        addToWishlist.addActionListener(e -> {
            Item newItem = new Item(itemNameText.getText());
            newItem.setDescription(desText.getText());

            if(itemNameText.getText().isEmpty()){
                new PopUpWindow(amp.enter("item name")).display();
            }else {//item name is given
                if (!successfullyAdded) {//checks if item has been previously added
                    try {//user is selected and item added to wishlist successfully
                        if (amc.addItemToUser(selectedTradingUser.getUserId(), newItem, "wishlist")) {
                            successfullyAdded = true;
                            new PopUpWindow(amp.successfully("Item added")).display();
                        }
                    } catch (NullPointerException nullPointerException) {//user is not selected
                        new PopUpWindow(amp.selectUser).display();
                    }
                } else{//prevent multiple operation
                    new PopUpWindow(amp.itemAddedError).display();
                }
            }
        });

        mainPanel.add(addToWishlist);
        mainPanel.add(addToInventory);
    }
}
