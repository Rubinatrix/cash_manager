<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
<c:url var="addRegularUrl" value="/transaction/add/regular"/>
<c:url var="addTransferUrl" value="/transaction/add/transfer"/>
<c:url var="logoutUrl" value="/logout"/>
<c:url var="accountUrl" value="/account/list"/>
<c:url var="reportsUrl" value="/report/list"/>

<div class="desktop wide">

    <div class="user-info">
        Logged as <b>${username}</b>
        <p><a href="${logoutUrl}">Logout</a></p>
    </div>

    <div class="global-menu">
        <a href="${accountUrl}"><< Accounts</a>
        <p><a href="${reportsUrl}"><< Reports</a></p>
    </div>

    <div class="header">
        <p>All transactions</p>
    </div>

    <br/>

    <div class="menu">
        <p><a href="${addRegularUrl}">New transaction</a></p>
        <p><a href="${addTransferUrl}">New transfer</a></p>
    </div>

    <table class="entity-list">
        <thead>
        <tr>
            <th>Date</th>
            <th>Account</th>
            <th>Category|Account</th>
            <th>Recipient</th>
            <th>Comment</th>
            <th>Type</th>
            <th>Amount</th>
            <th colspan="2"></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${transactions}" var="transaction">
            <c:url var="editUrl" value="/transaction/edit?id=${transaction.id}"/>
            <c:url var="deleteUrl" value="/transaction/delete?id=${transaction.id}"/>
            <fmt:formatDate value="${transaction.date}" var="datetime" pattern="yyyy-MM-dd HH:mm"/>
            <tr>
                <td class="date"><c:out value="${datetime}"/></td>
                <td><c:out value="${transaction.account.name}"/></td>
                <c:choose>
                    <c:when test="${transaction.type=='TRANSFER'}">
                        <td><c:out value="${transaction.accountTo.name}"/></td>
                    </c:when>
                    <c:otherwise>
                        <td><c:out value="${transaction.category.name}"/></td>
                    </c:otherwise>
                </c:choose>
                <td><c:out value="${transaction.recipient}"/></td>
                <td><c:out value="${transaction.comment}"/></td>
                <td><c:out value="${transaction.type}"/></td>
                <td><c:out value="${transaction.amount}"/></td>
                <td class="button"><a href="${editUrl}"><img src="${editImgUrl}"></img></a></td>
                <td class="button"><a href="${deleteUrl}"><img src="${deleteImgUrl}"></img></a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <c:if test="${empty transactions}">
        No transactions found.
    </c:if>

</div>
</body>
</html>