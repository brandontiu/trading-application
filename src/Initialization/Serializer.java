package Initialization;

import Actions.Action;
import Admins.AdminUser;
import Items.Item;
import Transactions.Transaction;
import Users.DemoUser;
import Users.TradingUser;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <h1>Initialization.Serializer</h1>
 * This class is responsible for the serialization and deserialization of collections of objects.
 *
 * <p>General ideas were taken from week 6 StudentManager.java example in ReadWriteEx</p>
 */
public class Serializer {

    /**
     * Writes a List of TradingUser into a file specified by a filepath.
     * @param path The filepath corresponding to the file it is written to.
     * @param tradingUsers A List of TradingUser that is being written.
     */
    public void writeUsersToFile(String path, List<TradingUser> tradingUsers) {
        try {
            // create a connection to the file specified by path
            OutputStream file = new FileOutputStream(path);
            ObjectOutput output = new ObjectOutputStream(file);

            // write user into the file
            output.writeObject(tradingUsers);

            // close the file
            output.close();
        } catch (IOException e) {
            System.out.println("IOException was caught.");
        }
    }

    /**
     * Reads a List of TradingUser from a file specified by a filepath.
     * @param path The filepath corresponding to the file it is being read from.
     * @return A List of TradingUser.
     */
    public List<TradingUser> readUsersFromFile(String path) {
        try {
            // create a connection to the file specified by path
            InputStream file = new FileInputStream(path);
            ObjectInput input = new ObjectInputStream(file);

            // deserialize the list
            List<TradingUser> tradingUsers = (List<TradingUser>) input.readObject();

            // close the file
            input.close();

            // return the list
            return tradingUsers;
        } catch(IOException e) {
            System.out.println("IO Exception was caught.");
            return null;
        } catch(ClassNotFoundException e) {
            System.out.println("ClassNotFoundException was caught.");
            return null;
        }
    }

    /**
     * Writes a List of AdminUser into a file specified by a filepath.
     * @param path The filepath corresponding to the file it is written to.
     * @param admins A List of AdminUser that is being written.
     */
    public void writeAdminsToFile(String path, List<AdminUser> admins) {
        try {
            OutputStream file = new FileOutputStream(path);
            ObjectOutput output = new ObjectOutputStream(file);
            output.writeObject(admins);
            output.close();
        } catch (IOException e) {
            System.out.println("IO Exception was caught.");
        }
    }

    /**
     * Reads a List of AdminUser from a file specified by a filepath.
     * @param path The filepath corresponding to the file it is being read from.
     * @return A List of AdminUser.
     */
    public List<AdminUser> readAdminsFromFile(String path) {
        try {
            InputStream file = new FileInputStream(path);
            ObjectInput input = new ObjectInputStream(file);
            List<AdminUser> admins = (List<AdminUser>) input.readObject();
            input.close();
            return admins;
        } catch (IOException e) {
            System.out.println("IO Exception was caught.");
            return null;
        } catch(ClassNotFoundException e) {
            System.out.println("ClassNotFoundException was caught.");
            return null;
        }
    }

    /**
     * Writes a Map of UUID to Transaction into a file specified by a filepath.
     * @param path The filepath corresponding to the file it is written to.
     * @param transactionMap A Map of UUID to Transaction.
     */
    public void writeTransactionsToFile(String path, Map<UUID, Transaction> transactionMap) {
        try {
            OutputStream file = new FileOutputStream(path);
            ObjectOutput output = new ObjectOutputStream(file);
            output.writeObject(transactionMap);
            output.close();
        } catch (IOException e) {
            System.out.println("IO Exception was caught.");
        }

    /**
     * Reads a Map of UUID to Transaction from a file specified by a filepath.
     * @param path The filepath corresponding to the file it is being read from.
     * @return A Map of UUID to Transaction.
     */
}

    public Map<UUID, Transaction> readTransactionMapFromFile(String path) {
        try {
            InputStream file = new FileInputStream(path);
            ObjectInput input = new ObjectInputStream(file);
            Map<UUID, Transaction> transactionMap = (Map<UUID, Transaction>) input.readObject();
            input.close();
            return transactionMap;
        } catch (IOException e) {
            System.out.println("IO Exception was caught.");
            return null;
        } catch(ClassNotFoundException e) {
            System.out.println("ClassNotFoundException was caught.");
            return null;
        }
    }

    /**
     * Writes a Map of Item to TradingUser into a file specified by a filepath.
     * @param path The filepath corresponding to the file it is written to.
     * @param pendingItems A Map of Item to TradingUser.
     */
    public void writeItemsToFile(String path, Map<Item, TradingUser> pendingItems) {
        try {
            OutputStream file = new FileOutputStream(path);
            ObjectOutput output = new ObjectOutputStream(file);
            output.writeObject(pendingItems);
            output.close();
        } catch (IOException e) {
            System.out.println("IO Exception was caught.");
        }
    }

    /**
     * Reads a Map of Item to TradingUser from a file specified by a filepath.
     * @param path The filepath corresponding to the file it is being read from.
     * @return A Map of Item to TradingUser.
     */
    public Map<Item, TradingUser> readItemsFromFile(String path) {
        try {
            InputStream file = new FileInputStream(path);
            ObjectInput input = new ObjectInputStream(file);
            Map<Item, TradingUser> pendingItems = (Map<Item, TradingUser>) input.readObject();
            input.close();
            return pendingItems;
        } catch (IOException e) {
            System.out.println("IO Exception was caught.");
            return null;
        } catch(ClassNotFoundException e) {
            System.out.println("ClassNotFoundException was caught.");
            return null;
        }
    }

    /**
     * Writes a List of TradingUser into a file specified by a filepath.
     * @param path The filepath corresponding to the file it is written to.
     * @param Accounts A List of TradingUser.
     */
    public void writeAccountsToFile(String path, List<TradingUser> Accounts) {
        try {
            OutputStream file = new FileOutputStream(path);
            ObjectOutput output = new ObjectOutputStream(file);
            output.writeObject(Accounts);
            output.close();
        } catch (IOException e) {
            System.out.println("IO Exception was caught.");
        }
    }

    /**
     * Reads a List of TradingUser from a file specified by a filepath.
     * @param path The filepath corresponding to the file it is being read from.
     * @return A List of TradingUser
     */
    public List<TradingUser> readAccountsFromFile(String path) {
        try {
            InputStream file = new FileInputStream(path);
            ObjectInput input = new ObjectInputStream(file);
            List<TradingUser> Accounts = (List<TradingUser>) input.readObject();
            input.close();
            return Accounts;
        } catch (IOException e) {
            System.out.println("IO Exception was caught.");
            return null;
        } catch(ClassNotFoundException e) {
            System.out.println("ClassNotFoundException was caught.");
            return null;
        }
    }

    /**
     * Writes a Map of UUID to Item into a file specified by a filepath.
     * @param path The filepath corresponding to the file it is written to.
     * @param itemMap A Map of UUID to Item.
     */
    public void writeItemsMapToFile(String path, Map<UUID, Item> itemMap) {
        try {
            OutputStream file = new FileOutputStream(path);
            ObjectOutput output = new ObjectOutputStream(file);
            output.writeObject(itemMap);
            output.close();
        } catch (IOException e) {
            System.out.println("IO Exception was caught.");
        }
    }

    /**
     * Reads a Map of UUID to Item into a file specified by a filepath.
     * @param path The filepath corresponding to the file it is being read from.
     * @return A Map of UUID to Item.
     */
    public Map<UUID, Item> readItemMapFromFile(String path) {
        try {
            InputStream file = new FileInputStream(path);
            ObjectInput input = new ObjectInputStream(file);
            Map<UUID, Item> itemMap = (Map<UUID, Item>) input.readObject();
            input.close();
            return itemMap;
        } catch (IOException e) {
            System.out.println("IO Exception was caught.");
            return null;
        } catch(ClassNotFoundException e) {
            System.out.println("ClassNotFoundException was caught.");
            return null;
        }
    }

    /**
     * Writes a List of DemoUser into a file specified by a filepath.
     * @param path The filepath corresponding to the file it is written to.
     * @param demoUsers A List of DemoUser that is being written.
     */
    public void writeDemoUsersToFile(String path, List<DemoUser> demoUsers) {
        try {
            // create a connection to the file specified by path
            OutputStream file = new FileOutputStream(path);
            ObjectOutput output = new ObjectOutputStream(file);

            // write user into the file
            output.writeObject(demoUsers);

            // close the file
            output.close();
        } catch (IOException e) {
            System.out.println("IOException was caught.");
        }
    }

    /**
     * Reads a List of DemoUser from a file specified by a filepath.
     * @param path The filepath corresponding to the file it is being read from.
     * @return A List of DemoUser.
     */
    public List<DemoUser> readDemoUsersFromFile(String path) {
        try {
            // create a connection to the file specified by path
            InputStream file = new FileInputStream(path);
            ObjectInput input = new ObjectInputStream(file);

            // deserialize the list
            List<DemoUser> demoUsers = (List<DemoUser>) input.readObject();

            // close the file
            input.close();

            // return the list
            return demoUsers;
        } catch(IOException e) {
            System.out.println("IO Exception was caught.");
            return null;
        } catch(ClassNotFoundException e) {
            System.out.println("ClassNotFoundException was caught.");
            return null;
        }
    }

    /**
     * Writes a Map of UUID to a List of Action into a file specified by a filepath.
     * @param path The filepath corresponding to the file it is written to.
     * @param actionMap A Map of UUID to a List of Action.
     */
    public void writeActionsToFile(String path, LinkedHashMap<UUID, List<Action>> actionMap) {
        try {
            OutputStream file = new FileOutputStream(path);
            ObjectOutput output = new ObjectOutputStream(file);
            output.writeObject(actionMap);
            output.close();
        } catch (IOException e) {
            System.out.println("IO Exception was caught.");
        }
    }

    /**
     * Reads a Map of UUID to a List of Action into a file specified by a filepath.
     * @param path The filepath corresponding to the file it is being read from.
     * @return A Map of UUID to a List of Action.
     */
    public LinkedHashMap<UUID, List<Action>> readActionsFromFile(String path) {
        try {
            InputStream file = new FileInputStream(path);
            ObjectInput input = new ObjectInputStream(file);
            LinkedHashMap<UUID, List<Action>> actionMap = (LinkedHashMap<UUID, List<Action>>) input.readObject();
            input.close();
            return actionMap;
        } catch (IOException e) {
            System.out.println("IO Exception was caught.");
            return null;
        } catch(ClassNotFoundException e) {
            System.out.println("ClassNotFoundException was caught.");
            return null;
        }
    }
}