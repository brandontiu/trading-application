# Trading Application

<p>A trading application that allows users to trade [small, tangible] items and digital media [files, such as a PDF] through a GUI (Graphical User Interface).</p>

<p>The system has two types of users, TradingUsers which are users that are able to trade items, AdminUsers which are administrative users that control TradingUser accounts, and DemoUsers which allows the user to explore a limited version of the Trading System. The TradingUser will be allowed to browse for available items to trade, request a Transaction with the TradingUser owner of the desired item, and set a Meeting for the Transaction to take place. TradingUsers may request for temporary or permanent transactions, either of which may be a one-way transaction (one item is traded) or a two-way (two items are traded). In addition, TradingUsers may request for Virtual Transactions which allows for two way permanent transactions of digital media to occur. TradingUsers are restricted by a number of limitations (see “How a TradingUser gets frozen”). The violation of these limitations results in the TradingUser being flagged. The TradingUser may subsequently be frozen by an AdminUser. TradingUsers may request to add an Item for trade. AdminUsers must approve or reject said Item to allow other TradingUsers to view the Item. DemoUsers may observe some functionalities of the program and decide if they would like to upgrade to a TradingUser.</p>

## How to run the system

<ol>
<li>Run the main method located within TradingApplication.</li>
<li>If running the program for the first time, all .ser files are automatically created if they do not already exist. Otherwise all data is saved and .ser files are updated upon log out.</li>
</ol>