<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Reports: ${username}</title>
</head>
<body>

<c:url var="homeUrl" value="/app/homepage" />
<c:url var="logoutUrl" value="/app/logout"/>
<c:url var="reportCategoryUrl" value="/app/report/category"/>
<c:url var="reportRecipientUrl" value="/app/report/recipient"/>

<table style="width: 100%">

<tr>
<td style="text-align:left">
<p><a href="${homeUrl}">home</a></p>
<h1>Reports</h1>
</td>
<td style="text-align:right; vertical-align:top">
Logged as <b>${username}</b>
<p><a href="${logoutUrl}">Logout</a></p>
</td>
</tr>

<tr>
<td COLSPAN=2>
<h2><a href="${reportCategoryUrl}">Cash flow by categories</a></h2>
<h2><a href="${reportRecipientUrl}">Borrows/takings by recipients</a></h2>

</td>
</tr>

</table>

</body>
</html>