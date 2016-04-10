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

<c:url var="accountUrl" value="/account/list"/>
<c:url var="currencyUrl" value="/currency/list"/>
<c:url var="categoryUrl" value="/category/list"/>
<c:url var="logoutUrl" value="/logout"/>

<div class="desktop">

    <div class="user-info">
        Logged as <b>${username}</b>
        <p><a href="${logoutUrl}">Logout</a></p>
    </div>

    <div class="global-menu">
        <a href="${accountUrl}"><< Accounts</a>
    </div>

    <div class="header">
        <p>Settings</p>
    </div>

    <ul class="ul-menu">
        <li><h4><a href="${currencyUrl}">Currencies</a></h4></li>
        <li><h4><a href="${categoryUrl}">Categories</a></h4></li>
    </ul>

</div>

</body>
</html>