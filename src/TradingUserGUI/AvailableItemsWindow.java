package TradingUserGUI;

import Exceptions.InvalidItemException;
import Popups.PopUpWindow;
import Items.Item;
import Presenters.UserMenuPresenter;
import Users.TradingUser;
import Users.UserMenuController;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class AvailableItemsWindow {
    private final UserMenuController umc;
    private final UserMenuPresenter ump = new UserMenuPresenter();
    private Map<Item, TradingUser> availableItemsMap;
    private final JPanel panel = new JPanel();
    private final JButton itemButton = new JButton();
    private Item selectedItem;

    public AvailableItemsWindow(UserMenuController umc) {
        this.umc = umc;
    }

    public void display() {
        JFrame frame = new JFrame(ump.displayAvailableItems);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.setSize(400, 600);
        frame.setLocationRelativeTo(null);

        availableItemsMap = umc.getAvailableItems();
        frame.setVisible(true);

        if (availableItemsMap.isEmpty()) {
            new PopUpWindow(ump.noItem).display();
        } else {
            List<Item> itemList = new ArrayList<Item>(availableItemsMap.keySet());

            DefaultListModel<String> itemNames = new DefaultListModel<>();
            for (Item item : availableItemsMap.keySet()) {
                itemNames.addElement(item.toString());
            }
            JList<String> itemsList = new JList<>(itemNames);
            itemsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            itemsList.addListSelectionListener(e -> {
                JList<String> itemList1 = (JList<String>) e.getSource();
                selectedItem = itemList.get(itemList1.getSelectedIndex());
            });

            itemButton.setText(ump.selectToView);
            itemButton.setBounds(100, 200, 100, 50);
            itemButton.addActionListener(e -> {
                if(selectedItem == null){
                    new PopUpWindow(ump.pleaseSelect("an item.")).display();
                }
                else{
                    try {
                        ItemDetailsWindow idw = new ItemDetailsWindow(umc, selectedItem.getId(), availableItemsMap);
                        idw.display();
                    } catch (InvalidItemException invalidItemException) {
                        // invalidItemException.printStackTrace();
                    }
                }
            });

            panel.add(itemsList);
            panel.add(itemButton);
            frame.setContentPane(panel);
            // frame.pack();
            // panel.setLayout(null);
        }
    }

}
