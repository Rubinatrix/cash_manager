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
<c:url var="addUrl" value="/account/add"/>
<c:url var="settingsUrl" value="/settings"/>
<c:url var="reportUrl" value="/report/list"/>
<c:url var="logoutUrl" value="/logout"/>
<c:url var="helpUrl" value="/help"/>

<div class="desktop">
    <div class="user-info">
        Logged as <b>${username}</b>
        <p><a href="${logoutUrl}">Logout</a></p>
        <p>...</p>
        <p>(<a href="${helpUrl}">help</a>)</p>
    </div>

    <div class="global-menu">
        <a href="${reportUrl}">Reports</a>
        <p><a href="${settingsUrl}">Settings</a></p>
    </div>

    <div class="header">
        <p>Accounts</p>
    </div>

    <br/>

    <div class="menu">
        <p><a href="${addUrl}">Create new account</a></p>
    </div>

    <div>

        <table class="entity-list">
            <thead>
            <tr>
                <th>Name</th>
                <th>Amount</th>
                <th>Currency</th>
                <th colspan="2"></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${accountsWithAmount}" var="accountWithAmount">
                <c:url var="editUrl" value="/account/edit?id=${accountWithAmount[0].id}"/>
                <c:url var="deleteUrl" value="/account/delete?id=${accountWithAmount[0].id}"/>
                <c:url var="transactionUrl" value="/transaction/list?id=${accountWithAmount[0].id}"/>
                <tr>
                    <td><a href="${transactionUrl}"><c:out value="${accountWithAmount[0].name}"/></a></td>
                    <td><c:out value="${accountWithAmount[1]}"/></td>
                    <td><c:out value="${accountWithAmount[0].currency.name}"/></td>
                    <td class="button"><a href="${editUrl}"><img src="${editImgUrl}"></img></a></td>
                    <td class="button"><a href="${deleteUrl}"><img src="${deleteImgUrl}"></img></a></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <c:if test="${empty accountsWithAmount}">
            No records found.
        </c:if>

    </div>

</div>

</body>
</html>