package DemoUserGUI;

import Items.Item;
import Popups.PopUpWindow;
import Presenters.UserMenuPresenter;
import Users.DemoMenuController;
import Users.UserMenuController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DemoUserViewInventoryWindow {

    private DemoMenuController dmc;
    private UserMenuController umc;
    private final UserMenuPresenter ump = new UserMenuPresenter();
    private String itemName;
    private String itemDesc;
    private final JTextArea desc = new JTextArea();


    public DemoUserViewInventoryWindow(DemoMenuController dmc, UserMenuController umc) {
        this.dmc = dmc;
        this.umc = umc;
    }

    /**
     * Displays Inventory Window
     */
    public void display() {
        if (dmc.getCurrentDemoUser().getInventory().isEmpty()) {
            new PopUpWindow(ump.emptyInventory).display();
        } else {
            // create the frame
            JFrame frame = new JFrame(ump.viewInventory);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // closes the current frame but doesn't terminate the app

            // set the frame's size and centre it
            frame.setSize(new Dimension(700, 500));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            // LEFT SIDE OF SPLIT
            List<Item> items = umc.getIm().convertIdsToItems(dmc.getCurrentDemoUser().getInventory());

            DefaultListModel<String> itemNames = new DefaultListModel<>();
            DefaultListModel<String> itemDescs = new DefaultListModel<>();
            List<UUID> ids = new ArrayList<>();
                for (Item item : items) {
                    itemNames.addElement(item.toString());
                    itemDescs.addElement(item.getDescription());
                    ids.add(item.getId());
                }
                JList<String> itemsList = new JList<>(itemNames);
                itemsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                itemsList.addListSelectionListener(e -> {
                    JList<String> itemList1 = (JList<String>) e.getSource();
                    itemName = itemNames.get(itemList1.getSelectedIndex());
                    itemDesc = itemDescs.get(itemList1.getSelectedIndex());
                    String text = ump.itemName + itemName + "\n" + ump.itemDes + itemDesc;
                    desc.setText(text);
                });

                // create a JScrollPane
                JScrollPane scrollPane = new JScrollPane();
                scrollPane.getViewport().add(itemsList);

                Panel leftPanel = new Panel();
                leftPanel.add(scrollPane);

                // RIGHT SIDE OF SPLIT


                Panel rightPanel = new Panel();
                rightPanel.add(desc);

                JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
                splitPane.setBounds(150, 100, 400, 200);

                frame.getContentPane().add(splitPane);


                // Remove Button
                JButton removeB = new JButton(ump.remove);
                removeB.setBounds(600, 300, 100, 50);
                // make a JOptionPane when JButton is pressed
                removeB.addActionListener(e -> {
                    int input = JOptionPane.showOptionDialog(null, ump.optionPrompt("remove this item from your Inventory?"), "Remove Item?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                    if (input == JOptionPane.YES_OPTION) {
                        UUID id = ids.get(itemsList.getSelectedIndex());
                        dmc.removeFromInventory(id);
                        itemNames.remove(itemsList.getSelectedIndex()); // if YES, remove the item
                    }
                });

                // subpanel to hold the Remove button
                Panel subPanel = new Panel();
                subPanel.add(removeB);

                // Add the Remove Button on to the Frame
                frame.add(subPanel);




        }
    }
}
