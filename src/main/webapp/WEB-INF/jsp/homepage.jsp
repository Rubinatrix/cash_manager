<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Home page: ${username}</title>
</head>
<body>

<c:url var="walletUrl" value="/app/wallet/list"/>
<c:url var="currencyUrl" value="/app/currency/list"/>
<c:url var="categoryUrl" value="/app/category/list"/>
<c:url var="transactionUrl" value="/app/transaction/list/all"/>
<c:url var="logoutUrl" value="/app/logout"/>
<c:url var="reportUrl" value="/app/report"/>

<table style="width: 100%">

<tr>
<td style="text-align:left"><h1>Home</h1></td>
<td style="text-align:right; vertical-align:top">
Logged as <b>${username}</b>
<p><a href="${logoutUrl}">Logout</a></p>
</td>
</tr>

<tr>
<td COLSPAN=2>
<h2><a href="${walletUrl}">Wallets</a></h2>
<h2><a href="${currencyUrl}">Currencies</a></h2>
<h2><a href="${categoryUrl}">Categories</a></h2>
<h2><a href="${transactionUrl}">Transactions</a></h2>
<h2><a href="${reportUrl}">Reports</a></h2>
</td>
</tr>

</table>

</body>
</html>