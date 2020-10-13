package TradingUserGUI;

import Popups.PopUpWindow;
import Items.Item;
import Presenters.UserMenuPresenter;
import Users.UserMenuController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class RequestAddItemsWindow {

    private final UserMenuController umc;
    private final UserMenuPresenter ump = new UserMenuPresenter();
    private final JPanel mainPanel = new JPanel();
    private final JButton requestButton = new JButton(ump.requestItem);
    private final JLabel itemNameLabel = new JLabel(ump.itemName);
    private final JTextField userItemNameText = new JTextField(20);
    private final JLabel itemDescriptionLabel = new JLabel(ump.itemDes);
    private final JTextArea userItemDescription = new JTextArea();

    public RequestAddItemsWindow(UserMenuController umc) {
        this.umc = umc;
    }

    public void display(){
        JFrame frame = new JFrame(ump.requestAddItem);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(new Dimension(700, 300));
        frame.setLocationRelativeTo(null);

        frame.add(mainPanel);
        placeComponents(mainPanel);
        frame.setVisible(true);
        requestButton.addActionListener(e -> {
            Item requestedItem = new Item(userItemNameText.getText());
            requestedItem.setDescription(userItemDescription.getText());
            if(userItemNameText.getText().equals("")) {
                new PopUpWindow(ump.enter("item name.")).display();
            }
            else{
                umc.addToPendingItems(requestedItem);
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                new PopUpWindow(userItemNameText.getText() + " " + ump.submitForApproval).display();
            }
        });
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // add username label to panel
        itemNameLabel.setBounds(10,20,80,25);
        panel.add(itemNameLabel);

        // add username field to panel
        userItemNameText.setBounds(100,20,165,25);
        panel.add(userItemNameText);

        // add password label to panel
        itemDescriptionLabel.setBounds(10,50,80,25);
        panel.add(itemDescriptionLabel);

        // add password field to panel
        userItemDescription.setBounds(100,50,165,100);
        panel.add(userItemDescription);

        // add login button to panel
        requestButton.setBounds(10, 180, 160, 25);
        panel.add(requestButton);

    }

}
