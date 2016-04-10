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

<form:form class="desktop" modelAttribute="accountAttribute" method="POST" action="${saveUrl}">

    <div class="section">
        <c:choose>
            <c:when test="${type=='edit'}">
                <c:url var="saveUrl" value="/account/edit?id=${accountAttribute.id}"/>
                edit Account
            </c:when>
            <c:when test="${type=='add'}">
                <c:url var="saveUrl" value="/account/add"/>
                new Account
            </c:when>
        </c:choose>
    </div>

    <div class="form-elements">
        <form:label path="name">
            Name
            <form:input type="text" path="name"/>
        </form:label>
        <form:label path="currency">
            Currency
            <form:select path="currency" required="required">
                <c:forEach items="${currencyAttribute}" var="currency">
                    <option value="${currency.id}"
                            <c:if test="${currency.id==accountAttribute.currency.id}">selected</c:if>>${currency.name}</option>
                </c:forEach>
            </form:select>
        </form:label>
    </div>

    <input type="submit" value="Save"/>

    <br/>
    <br/>

</form:form>

</body>
</html>