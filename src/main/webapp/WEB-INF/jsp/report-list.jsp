<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Cash manager</title>
</head>
<body>

<c:url var="logoutUrl" value="/app/logout"/>
<c:url var="reportCategoryUrl" value="/app/report/category"/>
<c:url var="reportRecipientUrl" value="/app/report/recipient"/>
<c:url var="accountUrl" value="/app/account/list"/>
<c:url var="transactionUrl" value="/app/transaction/list/all"/>

<table style="width: 100%">

<tr>
<td style="text-align:left">
<h3><a href="${accountUrl}">Accounts</a></h3>
<h1>Reports</h1>
</td>
<td style="text-align:right; vertical-align:top">
Logged as <b>${username}</b>
<p><a href="${logoutUrl}">Logout</a></p>
</td>
</tr>

<tr>
<td COLSPAN=2>
<h3>* <a href="${reportCategoryUrl}">Cash flow by categories</a></h3>
<h3>* <a href="${reportRecipientUrl}">Cash amount by recipients</a></h3>
<h3>* <a href="${transactionUrl}">All transactions list</a></h3>
</td>
</tr>

</table>

</body>
</html>