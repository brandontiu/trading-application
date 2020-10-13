package AdminGUI;

import Actions.Action;
import Actions.ActionManager;
import Actions.AddOrDeleteAction;
import Actions.EditAction;
import Admins.AdminMenuController;
import Exceptions.InvalidTradingUserException;
import Items.Item;
import Popups.PopUpWindow;
import Presenters.AdminMenuPresenter;
import Users.TradingUser;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UndoActionWindow {
    private AdminMenuController amc;
    private ActionManager am;
    private UUID selectedUser;
    private Action selectedAction;
    private AdminMenuPresenter amp = new AdminMenuPresenter();

    public UndoActionWindow(AdminMenuController amc) {
        this.amc = amc;
        am = amc.getAcm();
    }

    public void display() {
        JFrame frame = new JFrame(amp.undoAction);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // closes the current frame but doesn't terminate the app

        frame.setSize(new Dimension(700, 500));
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();

        DefaultListModel<String> users = new DefaultListModel<>(); // list of all users who have made undoable actions
        ArrayList<UUID> listUsers = new ArrayList<>();
        for (UUID userId : am.getAllActions().keySet()) {
            // TradingUser user = amc.getTUM().getTradingUserById(userId);
            while (!listUsers.contains(userId)) {
                String username = amc.getTUM().getTradingUserById(userId).getUsername();
                users.addElement(username); // usernames should just
                listUsers.add(userId);
            }
        }

        JList<String> allUsers = new JList<>(users);
        allUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // allUsers.setSelectedIndex(0);
        allUsers.addListSelectionListener(e -> {
            selectedUser = listUsers.get(allUsers.getSelectedIndex());
            helper(selectedUser);
            // frame.setVisible(false);
        });

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getViewport().add(allUsers);

        panel.add(scrollPane);

        frame.getContentPane().add(panel);
        frame.setVisible(true);

    }

    private void helper(UUID selectedUserId) {
        JFrame frame2 = new JFrame(amp.undoOptions);
        frame2.setSize(new Dimension(700, 500));
        frame2.setLocationRelativeTo(null);
        frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultListModel<String> actions = new DefaultListModel<>(); // list of all actions by the selected user
        ArrayList<Action> listActions = new ArrayList<>();
        TradingUser selectedUser = amc.getTUM().getTradingUserById(selectedUserId);
        if (am.getActionsByUser(selectedUser) == null) {
            PopUpWindow pw = new PopUpWindow(amp.noUndoableAction);
            pw.display();
        }
        else {
            for (Action action : am.getActionsByUser(selectedUser)) {
                actions.addElement(action.toString());
                listActions.add(action);
            }
            JList<String> usersActions = new JList<>(actions);
            usersActions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            // usersActions.setSelectedIndex(0);
            usersActions.addListSelectionListener(e -> {
                selectedAction = listActions.get(usersActions.getSelectedIndex());
            });

            JButton undo = new JButton(amp.undoAction);
            undo.setBounds(600, 300, 100, 50);
            undo.addActionListener(e -> {
                if (selectedAction.isAddorDeleteAction()) {
                    amc.undoAddOrDeleteAction((AddOrDeleteAction) selectedAction); // idk how to not have to cast this
                    PopUpWindow undone = new PopUpWindow(amp.actionUndone);
                    undone.display();
                } else if (selectedAction.isEditAction()) {
                    amc.undoEditAction((EditAction) selectedAction);
                    PopUpWindow undone = new PopUpWindow(amp.actionUndone);
                    undone.display();
                }
            });

            JPanel panel2 = new JPanel();
            panel2.add(usersActions);
            panel2.add(undo);
            frame2.getContentPane().add(panel2);
        }
        frame2.setVisible(true);
    }
}