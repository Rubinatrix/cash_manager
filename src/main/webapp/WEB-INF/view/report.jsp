<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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

<c:url var="accountUrl" value="/account/list"/>
<c:url var="logoutUrl" value="/logout"/>
<c:url var="reportsUrl" value="/report/list"/>

<div class="desktop">

    <div class="user-info">
        Logged as <b>${username}</b>
        <p><a href="${logoutUrl}">Logout</a></p>
    </div>

    <div class="global-menu">
        <a href="${accountUrl}"><< Accounts</a>
        <p><a href="${reportsUrl}"><< Reports</a></p>
    </div>

    <div class="header">
        <p>
            <c:choose>
                <c:when test="${reportType=='category'}">
                    Cash flow by categories
                </c:when>
                <c:when test="${reportType=='recipient'}">
                    Cash amount by recipients
                </c:when>
                <c:otherwise>
                    Unknown report
                </c:otherwise>
            </c:choose>
        </p>
    </div>

    <form:form class="desktop-inner" modelAttribute="reportSettings" method="POST">

        <div class="form-elements">
            <form:label path="startDate">Period</form:label>
            <form:input type="date" path="startDate"/>
            <form:input type="date" path="endDate"/>
            <br/>
            <form:label path="currency">Currency</form:label>
            <form:select path="currency" required="required">
                <c:forEach items="${currencies}" var="currency">
                    <option value="${currency.id}"
                            <c:if test="${currency.id==reportSettings.currency.id}">selected</c:if>>${currency.name}</option>
                </c:forEach>
            </form:select>
            <br/>
            <c:if test="${reportType=='category'}">
                <form:label path="transactionType">Type</form:label>
                <form:select path="transactionType" required="required">
                    <c:forEach items="${transactionTypes}" var="transactionType">
                        <option value="${transactionType}"
                                <c:if test="${transactionType==reportSettings.transactionType}">selected</c:if>>${transactionType.name}</option>
                    </c:forEach>
                </form:select>
            </c:if>
        </div>

        <p><input type="submit" value="Get Report"/></p>

        <br/>

    </form:form>

    <table class="entity-list">
        <thead>
        <tr>
            <c:choose>
                <c:when test="${reportType=='category'}">
                    <th>Category</th>
                </c:when>
                <c:when test="${reportType=='recipient'}">
                    <th>Recipient</th>
                </c:when>
                <c:otherwise>
                    <h1>Unknown entity</h1>
                </c:otherwise>
            </c:choose>
            <th>Amount</th>
            <c:if test="${reportType=='category'}">
                <th>% total</th>
            </c:if>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${categoryAmount}" var="position">
            <tr>
                <td><c:out value="${position[0]}"/></td>
                <td><fmt:formatNumber type="number" minFractionDigits="2" value="${position[1]}"/></td>
                <c:if test="${reportType=='category'}">
                    <td><fmt:formatNumber type="percent" minFractionDigits="2"
                                          value="${position[1] / totalAmount}"/></td>
                </c:if>
            </tr>
        </c:forEach>
        <tr>
            <td><b><c:out value="TOTAL"/></b></td>
            <td><b><fmt:formatNumber type="number" minFractionDigits="2" value="${totalAmount}"/></b></td>
            <c:if test="${reportType=='category'}">
                <td></td>
            </c:if>
        </tr>
        </tbody>
    </table>

    <c:if test="${empty categoryAmount} or ${empty totalAmount}">
        No records found.
    </c:if>
</div>

</body>
</html>