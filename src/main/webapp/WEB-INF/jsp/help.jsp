<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Cash manager</title>
</head>
<body>

<c:url var="accountUrl" value="/app/account/list"/>

<a href="${accountUrl}"><< Accounts</a>
<h2>Tips</h2>

<p>
1. The main function of this web application is to be some kind of notepad for your cash operations in real life. And then allow you to make simple analysis using reports.
</p>

<p>
2. Before working with transactions you have to enter your currencies and cash flow categories (use "settings")
</p>

<p>
3. You can store your cash operations by using transactions of one of three types:
<div>- regular "deposit", when you earn money
<div>- regular "withdraw"", when you spend money
<div>- "transfer"", when you move you money from one place to another
</p>

<p>
4. When you add regular transaction, you can use one of two fields: category or recipient.
Use "category" when you spend or earn money, and use "recipient" when you borrow money from somebody or when somebody borrow it from you.
</p>

<p>
5. Application won't allow you to add transfer transaction between accounts with different currencies.
</p>

<p>
6. Application won't allow you to delete currency, if there are one or more accounts with this currency.
</p>

<p>
7. Application won't allow you to delete category, if there are one or more transactions with this category.
</p>

<p>
8. You can delete you account, but if you do this, you'll lost all regular transactions with this account.
Transfer transactions will be automatically converted:
<div>- if deleted account was in From field - this transaction will be converted to regular deposit to remaining account
<div>- if deleted account was in To field - this transaction will be converted to regular withdraw from remaining account
</p>

</body>
</html>