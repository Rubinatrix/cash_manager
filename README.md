#  Cash Manager

Web application for simple managing cash, classifying cash flow by categories (Spring mvc, security, jsp, Hibernate, hsqldb)

<b>Hints & Tips</b>

1. The main purpose of this web application is to be some kind of notepad for your cash operations in real life. And then to allow you to make simple analysis using reports.

2. Before working with transactions you have to make lists of your currencies and cash flow categories (use "settings")

3. You can store your cash operations by using transactions of three types: 
<br>- regular "deposit", when you earn (or borrow) money 
<br>- regular "withdraw"", when you spend (or lend) money 
<br>- "transfer"", when you move you money from one place to another

4. You can see all transactions with specific account by simply clicking on account name in "accounts" list (the main page). When you create new transaction from this account transaction list - current account will be set to it by default.

5. When you add regular transaction, you can use one of two fields: category or recipient. Use "category" when you spend or earn money, and use "recipient" when you borrow money from somebody or when somebody borrow it from you.

6. Application won't allow you to add transfer transaction between accounts with different currencies.

7. Application won't allow you to delete currency, if there are one or more accounts with this currency.

8. Application won't allow you to delete category, if there are one or more transactions with this category.

9. You can delete you cash account, but if you do this, you'll lost all regular transactions with this account. Transfer transactions will be automatically converted: 
<br>- if deleted account was in From field - this transaction will be converted to regular deposit to remaining account 
<br>- if deleted account was in To field - this transaction will be converted to regular withdraw from remaining account

<b>Demo</b>

You can explore some demo example and try application before using.
<br>Use 
<br>login: test
<br>password: test
<br>for this.
