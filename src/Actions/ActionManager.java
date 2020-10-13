package Actions;

import Transactions.Transaction;
import Users.TradingUser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

/**
 * Manages and stores all undoable actions in the system.
 */
public class ActionManager {
    private final LinkedHashMap<UUID, List<Action>> allActions;

    public ActionManager(LinkedHashMap<UUID, List<Action>> allActions) {
        this.allActions = allActions;
    }

    /**
     * Getter for the LinkedHashMap of all actions (keys) mapped to the Action's UUID (values).
     * @return LinkedHashMap of all actions and their UUIDs.
     */
    public LinkedHashMap<UUID, List<Action>> getAllActions() {
        return allActions;
    }

    /**
     * Appends an Action to the list of the specified User's actions.
     * @param newAction new Action object
     * @param userId the specified TradingUser
     */
    public void addAction(UUID userId, Action newAction) {
        if (!allActions.containsKey(userId)) {
            List<Action> listActions = new ArrayList<>();
            listActions.add(newAction);
            allActions.put(userId, listActions);
        }
        else {
            allActions.get(userId).add(newAction);
        }
    }

    /**
     * Removes selectedAction from allActions.
     * @param selectedAction
     */
    public void removeAction(Action selectedAction) {
        for (UUID id : allActions.keySet()) {
            allActions.get(id).removeIf(action -> action.getId().equals(selectedAction.getId()));
        }

    }

    /**
     * Filter list of all actions by a specific user.
     */
    public List<Action> getActionsByUser(TradingUser user) {
        if (allActions.containsKey(user.getUserId())) {
            return allActions.get(user.getUserId());
        }
        return new ArrayList<>(); // if this TradingUser hasn't made any actions yet
    }

    /**
     * Gets a list of EditActions made by user.
     * @param user
     * @return
     */
    public List<EditAction> getEditActionsByUser(TradingUser user) {
        List<EditAction> lst = new ArrayList<>();
        for (Action action : getActionsByUser(user)) {
            if (action.isEditAction()) {
                lst.add((EditAction) action);
            }
        }
        return lst;
    }

    /**
     * Clears previous EditActions made by user that involves transaction.
     * @param user
     * @param transaction
     */
    public void clearPreviousEditActions(TradingUser user, Transaction transaction) {
        for (EditAction action : getEditActionsByUser(user)) {
            if (action.getTransaction().getId().equals(transaction.getId())) {
                allActions.get(user.getUserId()).remove(action);
            }
        }
    }

}
