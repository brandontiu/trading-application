package AdminGUI;

import Admins.AdminMenuController;
import Exceptions.InvalidItemException;
import Items.Item;
import Popups.PopUpWindow;
import Presenters.AdminMenuPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class CheckPendingItemsWindow {
    private final AdminMenuController amc;
    private final AdminMenuPresenter amp = new AdminMenuPresenter();
    private Item selectedItem;
    private String itemName; // name of item
    private String itemDesc; // item description
    private String itemUser; // who the item belongs to
    private JTextArea desc = new JTextArea();

    public CheckPendingItemsWindow(AdminMenuController amc) { this.amc = amc; }

    /**
     * Part of code taken from:
     * https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/SplitPaneDemoProject/src/components/SplitPaneDemo.java
     */
    public void display() {
        // create the frame
        JFrame frame = new JFrame(amp.checkPendingItem);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // closes the current frame but doesn't terminate the app

        // set the frame's size and centre it
        frame.setSize(new Dimension(710, 350));
        frame.setLocationRelativeTo(null);

        // LEFT SIDE OF SPLITPANE
        // scrollable list of all pending items
        DefaultListModel<String> pendingItems = new DefaultListModel<>();
        DefaultListModel<String> itemDescs = new DefaultListModel<>();
        ArrayList<Item> listItems = new ArrayList<>();
        if (amc.getAllPendingItems().isEmpty()) {
            PopUpWindow pw = new PopUpWindow(amp.noPendingItem);
            pw.display();
        }
        else {
            for (Item curr : amc.getAllPendingItems().keySet()) {
                listItems.add(curr);
                pendingItems.addElement(curr.toString());
                itemDescs.addElement(curr.getDescription());
            }

            // create JList
            JList<String> items = new JList<>(pendingItems);
            items.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            items.addListSelectionListener(e -> {
                // JList<String> items1 = (JList<String>)e.getSource(); // TO-DO: i don't think below is being updated properly
                try {
                    Item currItem = listItems.get(items.getSelectedIndex());
                    itemUser = amc.getItemOwner(currItem).toString();
                    itemName = pendingItems.get(items.getSelectedIndex());
                    itemDesc = itemDescs.get(items.getSelectedIndex());
                    String text = amp.itemName + itemName + "\n" + amp.itemDes + itemDesc + "\n" + amp.itemOwner + itemUser;
                    desc.setText(text);
                } catch (InvalidItemException invalidItemException) {
                    invalidItemException.printStackTrace();
                }

            });
            // create JScrollPane (makes list scrollable)
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.getViewport().add(items);

            Panel leftPanel = new Panel();
            leftPanel.add(scrollPane);

            //RIGHT SIDE OF SPLITPANE
            Panel rightPanel = new Panel();
            rightPanel.add(desc);

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
            splitPane.setBounds(50, 50, 600, 200);

            frame.getContentPane().add(splitPane);

            //APPROVE AND REJECT BUTTONS
            Panel sidePanel = new Panel();
            // if Admin clicked approve item
            JButton approve = new JButton(amp.approve);
            approve.setBounds(600,100,100,50);
            approve.addActionListener(e -> {
                amc.approvePendingItem(listItems.get(items.getSelectedIndex()));
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                new PopUpWindow(amp.itemApproved).display();
            });

            // if Admin clicked reject item
            JButton reject = new JButton(amp.reject);
            reject.setBounds(600, 300, 100, 50);
            reject.addActionListener(e -> {
                amc.rejectPendingItem(listItems.get(items.getSelectedIndex()));
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                new PopUpWindow(amp.itemRejected).display();
            });

            sidePanel.add(approve);
            sidePanel.add(reject);
            frame.add(sidePanel);

            frame.setVisible(true);
        }
    }
}