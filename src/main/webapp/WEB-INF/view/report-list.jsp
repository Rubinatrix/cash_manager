<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href='http://fonts.googleapis.com/css?family=Bitter' rel='stylesheet' type='text/css'>
    <link href="<c:url value="/resources/css/main.css"/>" rel="stylesheet">
    <title>Cash manager</title>
</head>
<body>

<c:url var="logoutUrl" value="/logout"/>
<c:url var="reportCategoryUrl" value="/report/category"/>
<c:url var="reportRecipientUrl" value="/report/recipient"/>
<c:url var="accountUrl" value="/account/list"/>
<c:url var="transactionUrl" value="/transaction/list/all"/>

<div class="desktop">

    <div class="user-info">
        Logged as <b>${username}</b>
        <p><a href="${logoutUrl}">Logout</a></p>
    </div>

    <div class="global-menu">
        <a href="${accountUrl}"><< Accounts</a>
    </div>

    <div class="header">
        <p>Reports</p>
    </div>

    <ul class="ul-menu">
        <li><h4><a href="${reportCategoryUrl}">Cash flow by categories</a></h4></li>
        <li><h4><a href="${reportRecipientUrl}">Cash amount by recipients</a></h4></li>
        <li><h4><a href="${transactionUrl}">All transactions list</a></h4></li>
    </ul>

</div>

</body>
</html>