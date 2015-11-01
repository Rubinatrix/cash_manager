<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Cash manager</title>
</head>
<body>

<c:url var="accountUrl" value="/app/account/list"/>
<c:url var="currencyUrl" value="/app/currency/list"/>
<c:url var="categoryUrl" value="/app/category/list"/>
<c:url var="logoutUrl" value="/app/logout"/>

<table style="width: 100%">

<tr>
<td style="text-align:left">
<p><a href="${accountUrl}"><< Accounts</a></p>
<h1>Settings</h1>
</td>
<td style="text-align:right; vertical-align:top">
Logged as <b>${username}</b>
<p><a href="${logoutUrl}">Logout</a></p>
</td>
</tr>

<tr>
<td COLSPAN=2>
<h3>* <a href="${currencyUrl}">Currencies</a></h3>
<h3>* <a href="${categoryUrl}">Categories</a></h3>
</td>
</tr>

</table>

</body>
</html>