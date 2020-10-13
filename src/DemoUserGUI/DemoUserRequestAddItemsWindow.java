package DemoUserGUI;

import Popups.PopUpWindow;
import Items.Item;
import Presenters.UserMenuPresenter;

import javax.swing.*;
import java.awt.*;

public class DemoUserRequestAddItemsWindow {

    private JPanel mainPanel = new JPanel();
    private final JButton requestButton = new JButton();
    private final JLabel itemNameLabel = new JLabel();
    private final JTextField userItemNameText = new JTextField(20);
    private final JLabel itemDescriptionLabel = new JLabel();
    private final JTextField userItemDescription = new JTextField(100);
    private final UserMenuPresenter ump = new UserMenuPresenter();

    public DemoUserRequestAddItemsWindow() {
    }

    /**
     * Displays DemoUserRequestAddItemsWindows
     */
    public void display() {
        JFrame frame = new JFrame(ump.requestAddItem);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(new Dimension(700, 500));
        frame.setLocationRelativeTo(null);

        frame.add(mainPanel);
        placeComponents(mainPanel);
        frame.setVisible(true);
        requestButton.addActionListener(e -> {
                    Item requestedItem = new Item(userItemNameText.getText());
                    requestedItem.setDescription(userItemDescription.getText());
                    new PopUpWindow(ump.upgradeMessage).display();
                }
        );

    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // add username label to panel
        itemNameLabel.setText(ump.itemName);
        itemNameLabel.setBounds(10, 20, 80, 25);
        panel.add(itemNameLabel);

        // add username field to panel
        userItemNameText.setBounds(100, 20, 165, 25);
        panel.add(userItemNameText);

        // add password label to panel
        itemDescriptionLabel.setText(ump.itemDes);
        itemDescriptionLabel.setBounds(10, 50, 80, 25);
        panel.add(itemDescriptionLabel);

        // add password field to panel
        userItemDescription.setBounds(100, 50, 165, 100);
        panel.add(userItemDescription);

        // add login button to panel
        requestButton.setText(ump.requestItem);
        requestButton.setBounds(10, 180, 160, 25);
        panel.add(requestButton);

    }

}

