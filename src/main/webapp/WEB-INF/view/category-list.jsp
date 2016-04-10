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

<c:url var="editImgUrl" value="/resources/img/edit.png"/>
<c:url var="deleteImgUrl" value="/resources/img/delete.png"/>
<c:url var="addUrl" value="/category/add"/>
<c:url var="settingsUrl" value="/settings"/>
<c:url var="accountUrl" value="/account/list"/>
<c:url var="logoutUrl" value="/logout"/>

<div class="desktop">

    <div class="user-info">
        Logged as <b>${username}</b>
        <p><a href="${logoutUrl}">Logout</a></p>
    </div>

    <div class="global-menu">
        <a href="${accountUrl}"><< Accounts</a>
        <p><a href="${settingsUrl}"><< Settings</a></p>
    </div>

    <div class="header">
        <p>Accounts</p>
    </div>

    <br/>

    <div class="menu">
        <p><a href="${addUrl}">Create new category</a></p>
    </div>


    <table class="entity-list">
        <thead>
        <tr>
            <th>Name</th>
            <th colspan="2"></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${categories}" var="category">
            <c:url var="editUrl" value="/category/edit?id=${category.id}"/>
            <c:url var="deleteUrl" value="/category/delete?id=${category.id}"/>
            <tr>
                <td><c:out value="${category.name}"/></td>
                <td class="button"><a href="${editUrl}"><img src="${editImgUrl}"></img></a></td>
                <td class="button"><a href="${deleteUrl}"><img src="${deleteImgUrl}"></img></a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <c:if test="${empty categories}">
        No records found.
    </c:if>

    <div class="error">
        <c:if test="${not empty errorDescription}">
            ${errorDescription}
        </c:if>
    </div>

</div>

</body>
</html>