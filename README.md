# Trading Application

A trading application that allows users to trade [small, tangible] items and digital media [files, such as a PDF] through a GUI made with Java's [Swing](https://docs.oracle.com/javase/7/docs/api/javax/swing/package-summary.html) package.

This application was the result of the final project for [CSC 207H1 Software Design](https://fas.calendar.utoronto.ca/course/csc207h1) course at the University of Toronto. The main purpose of this application was to implement Clean Architecture and SOLID design principles in addition to design patterns such as factory, builder, and dependency injection.

## Overview

The system has two types of users, TradingUsers which are users that are able to trade items, AdminUsers which are administrative users that control TradingUser accounts, and DemoUsers which allows the user to explore a limited version of the Trading System. The TradingUser will be allowed to browse for available items to trade, request a Transaction with the TradingUser owner of the desired item, and set a Meeting for the Transaction to take place. TradingUsers may request for temporary or permanent transactions, either of which may be a one-way transaction (one item is traded) or a two-way (two items are traded). In addition, TradingUsers may request for Virtual Transactions which allows for two way permanent transactions of digital media to occur. TradingUsers are restricted by a number of limitations (see “How a TradingUser gets frozen”). The violation of these limitations results in the TradingUser being flagged. The TradingUser may subsequently be frozen by an AdminUser. TradingUsers may request to add an Item for trade. AdminUsers must approve or reject said Item to allow other TradingUsers to view the Item. DemoUsers may observe some functionalities of the program and decide if they would like to upgrade to a TradingUser.

## How to run the system

1. Run the main method located within TradingApplication.
2. If running the program for the first time, all .ser files are automatically created if they do not already exist. Otherwise all data is saved and .ser files are updated upon log out.

## How to [generally] use the system (as a AdminUser)

* View items requested for approval by TradingUsers
  * The item will be removed from the pending items list after an AdminUser approves/declines the item once the window is closed
* View flagged TradingUser by the system, freeze or pardon these accounts
* View requests by frozen TradingUser accounts to have their account unfrozen and approve or disapprove
* Change a TradingUser’s thresholds within the system
* Create new administrators if you are using the master admin account
* Admins may undo certain Actions performed by the TradingUser
  * The Action class represents actions conducted by TradingUser that may be undone
  * Actions include:
    * Adding/removing from Inventory (an AddorRemoveAction)
    * Adding/removing from Wishlist (an AddorRemoveAction)
    * Editing a meeting location/time (an EditAction)

## How to [generally] use the system (as a TradingUser)

* If you are running the application for the first time, select the create a new account option in the main menu
* Otherwise log into your existing account
* Add items to your inventory by submitting item requests to admins.
  * System must be fully exited before the Admin can approve pending items
* Browse available items for trade to add to your wishlist or create a trade for any items of interest
* View your active transactions to edit, confirm, or cancel them
* If your account ever gets frozen, submit a request to the admins to unfreeze it
* Set your account to “Vacation” if you are away and unable to trade for a period of time

## How to [generally] use the system (as a DemoUser)

* If you are running the application for the first time and would like to try out the system, select the “Demo?” button on the main menu and create an account
* Otherwise log into your existing demo account
* Upon creation of the DemoUser account, one item is automatically added to the user’s Inventory and Wishlist. This item can be used to try out the functionality of inventory and wishlist
* Browse available items in the Trading System
* Explore the functionality of the Request Add Item Feature
* If desired, a TradingUser account can be created by clicking the Upgrade Account button
  * Once a new TradingUser account is created, the User’s old account will be deleted
  * The User must log out of BOTH the old DemoUser account and the new TradingUser account

## How a TradingUser gets frozen

If any of the below thresholds are reached, the system flags the TradingUser to be frozen, and the AdminUser will need to check flagged accounts to decide whether the TradingUser should actually be frozen or for their status to remain active.
*Note: these thresholds can be changed by the AdminUser

* borrowThreshold = 1
  * the minimum number of items the TradingUser needs to lend before they can borrow
* weeklyThreshold = 3
  * the maximum number of transactions a TradingUser can be involved in per calendar week (virtual transactions not included)
* incompleteThreshold = 3
  * the maximum number of incomplete transactions that can occur per TradingUse

## Types of Transactions

* Permanent (One meeting involved)
  * One-Way: TradingUser1  permanently gives an Item to TradingUser2
  * Two-Way: Two TradingUsers permanently give two Items to each other
* Temporary (Two meetings involved)
  * One-Way: TradingUser1 lends an Item to TradingUser2 at the first meeting, then TradingUser2 returns the Item at the second meeting
  * Two-Way: Two TradingUsers lend an Item to each other at the first meeting, then both TradingUsers return those Items back to each other at the second meeting
* Virtual (No Meeting)
  * A permanent two way transaction where virtual objects are created

## Transaction Thresholds

* editThreshold = 3
  * the maximum number of edits (per TradingUser) per Transaction can be done before the Transaction is automatically cancelled

## Exceptions

* InvalidAdminException checks if a AdminUsers corresponds to a valid AdminUser during login  
* InvalidItemException checks if an Item is valid
* InvalidTradingUserException checks if the TradingUser corresponds to a valid TradingUser
* InvalidTransactionException checks if Transaction id maps to a Transaction 
* InvalidDemoUserException checks if the DemoUser corresponds to a valid TradingUser

## What’s Being Saved

* admins.ser contains a list of all AdminUsers
* users.ser contains a list of all TradingUsers
* items.ser contains a map of a UUID to the Item corresponding to it
* transactions.ser contains a map of UUID to the Transaction corresponding to it
* flaggedAccounts.ser contains a list of all flagged TradingUsers
* frozenAccounts.ser contains a list of all frozen TradingUsers
* requestedItems.ser contains a map of requested Item to the TradingUser who requested it
* demoUsers.ser contains a list of all DemoUsers
* actions.ser contains a map of all Actions

## Packages

* Actions package contains the classes that specifically pertain to Actions. This includes: Action, ActionManager, AddOrDeleteAction, EditAction
* AdminGUI package contains the classes that specifically pertain to the GUI of Admin. This includes: AddAdminUserWindow, AddNewItemToTradingUserWindow, AdminUserMenu, ChangePasswordWindow, CheckPendingItemsWindow, UndoActionWindow, ChangeThresholdWindow, FlaggedUsersWindow, FrozenUsersWindow
* Admins package contains the classes that specifically pertain to Admins. This includes: AdminUser, AdminManager, AdminMenuController, AdminMenuPresenter.
* DemoUserGUI package contains the classes that specifically pertain to the GUI of DemoUser. This includes: DemoUserMenu, DemoUserRegistrationWindow. DemoUserRequestAddItemsWindow, DemoUserViewInventoryWindow, DemoUserViewWishlistWindow, UpgradeAccountWindow
* Exceptions package contains the Exceptions that are unique to the TradingSystem software. This includes: InvalidAdminException, InvalidItemException, InvalidTradingUserException, InvalidTransactionException, InvalidDemoUserException
* Initialization package contains the classes that pertain to the initialization of the program: FilePaths, LoginController, LoginWindow, RegistrationWindow, Serializer, TradingApplication, TradingSystem  
* Items package contains the classes that specifically pertain to Items. This includes: Item, ItemManager.
* PopUps package contains the classes that pertain to certain popup windows of the GUI. This includes: ChangePasswordWindow, PopUpWindow 
* The Presenters package contains the String objects used throughout the GUI. The classes containing these String objects are: AdminMenuPresenter, MenuPresenter, UserMenuPresenter
* TradingUserGUI package includes all the classes that pertain to the GUI of TradingUser and its actions. This includes: ItemDetailsWindow, RequestAddItemsWindow, TradingUserMenu, TransactionWindow, ViewActionTransactionsWindows, ViewInventoryWindow, ViewTransactionHistoryWindow, ViewWishlistWindow, AvailableItemsWindow, TradingUserProfileWindow
* Transactions package contain the classes that specifically pertains to the handling of transactions: This includes: CurrentTransactionManager, PastTransactionManager, Meeting, Transaction, TransactionActions, TransactionBuilder, TransactionFactory, TransactionManager, TransactionPerm, TransactionStatuses, TransactionStatusStrategy, TransactionTemp, TransactionVirtual 
* Users package contains the classes that pertain to Users. This includes: DemoMenuController, DemoUser, DemoUserManager, User, TradingUser, TradingUserManager, TransactionHistory, UserMenuController, and UserStatuses

## Assumptions

* Different users of the TradingApplication program must either use the program on the same computer or use a repository that stores the system to operate on the same program
* TradingApplication must log out PROPERLY after each use. Otherwise, information will not be serialized and will be lost 
* There is one master admin user which can create all other admins in the system. The username is: admin, and the password: password to access this account
* TradingUser accounts are flagged only when they perform an action past a specific limitation. For example, if a user reaches their weekly transaction limit, they will be flagged when the number of their weekly transactions EXCEEDS the weekly transaction limit
* For virtual transactions, traders trade digital media outside of the Trading System (for example, by email)
* When a demoUser removes an item from their wishlist/inventory, they can’t request for the item back 
* A given username of a User object is unique from any other User subclass, regardless of its subclass 
* The actions that are reasonably (defined by our group) undoable are adding/removing an item from a TradingUser’s inventory/wishlist, and editing meeting details before the transaction has taken place.
* The GUI was made assuming that it would run on a mac computer (everyone in the group has macs)
* When a certain action (like completing a transaction) is performed, the effects of that action (like transaction history updating) will appear AFTER the user logs out and logs back in

## UML

* The UML diagram is colour coded based on clean architecture
  * Pink: Entity Classes
  * Yellow: Use Case Classes
  * Blue: Presenter, Controller, Gateway Classes
* Due to a paywall on the website that was used to make the UML, the UML was split into three parts: the main system, GUI, Phase 2 features
  * DESIGN1.pdf: UML of the main program
  * DESIGN2.pdf: Added Features in Phase 2
  * DESIGN3.pdf: GUI UML

## Acknowledgements

This project was completed collaboratively by Casey Hon, Annie Liu, Christian Mitrache, Anna Shirkalina, Brandon Tiu, Tingting Marina Zhang.
