<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href='http://fonts.googleapis.com/css?family=Bitter' rel='stylesheet' type='text/css'>
    <link href="<c:url value="/resources/css/main.css"/>" rel="stylesheet">
    <title>Cash manager</title>
</head>
<body>

<form:form class="desktop" modelAttribute="transactionAttribute" method="POST" action="${saveUrl}">

    <div class="section">
        <c:choose>
            <c:when test="${type=='edit'}">
                <c:url var="saveUrl" value="/transaction/edit?id=${transactionAttribute.id}"/>
                edit Transfer
            </c:when>
            <c:when test="${type=='add'}">
                <c:url var="saveUrl" value="/transaction/add"/>
                new Transfer
            </c:when>
        </c:choose>
    </div>

    <div class="form-elements">
        <form:label path="Date">
            Date
            <form:input type="datetime-local" path="date"/>
        </form:label>
        <form:label path="account">
            From
            <form:select path="account" required="required">
                <c:forEach items="${accountAttribute}" var="account">
                    <option value="${account.id}"
                            <c:if test="${account.id==transactionAttribute.account.id}">selected</c:if>>${account.name}</option>
                </c:forEach>
            </form:select>
        </form:label>
        <form:label path="accountTo">
            To
            <form:select path="accountTo" required="required">
                <c:forEach items="${accountAttribute}" var="account">
                    <option value="${account.id}"
                            <c:if test="${account.id==transactionAttribute.accountTo.id}">selected</c:if>>${account.name}</option>
                </c:forEach>
            </form:select>
        </form:label>
        <form:label path="amount">
            Amount
            <form:input type="number" min="0" step="0.01" path="amount"/>
        </form:label>
        <form:label path="comment">
            Comment
            <form:input type="text" path="comment"/>
        </form:label>
        <form:input type="hidden" value="TRANSFER" path="type"/>
        <form:input type="hidden" value="0" path="category"/>
    </div>

    <div>
        <form:errors element="div" class="error"/>
        <input type="submit" value="Save"/>
    </div>

    <br/>
    <br/>

</form:form>

</body>
</html>