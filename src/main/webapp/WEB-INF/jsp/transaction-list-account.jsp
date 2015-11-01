<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Cash manager</title>
</head>
<body>

<c:url var="editImgUrl" value="/resources/img/edit.png" />
<c:url var="deleteImgUrl" value="/resources/img/delete.png" />
<c:url var="addRegularUrl" value="/app/transaction/add/regular" />
<c:url var="addTransferUrl" value="/app/transaction/add/transfer" />
<c:url var="accountUrl" value="/app/account/list"/>
<c:url var="logoutUrl" value="/app/logout"/>

<table style="width: 100%">

<tr>
<td style="text-align:left">
<p><a href="${accountUrl}"><< Accounts</a></p>
</td>
<td style="text-align:right; vertical-align:top">
Logged as <b>${username}</b>
<p><a href="${logoutUrl}">Logout</a></p>
</td>
</tr>

<tr>
<td style="text-align:left">
<h1>${account.name}</h1>
</td>
<td style="text-align:right">
<h2>Cash: ${amount} ${account.currency.name}</h2>
</td>
</tr>

<tr>
<td COLSPAN=2>

<table style="border: 1px solid; width: 100%; text-align:center">
	<thead style="background:#d3dce3">
		<tr>
			<th>Date</th>
			<th>Account</th>
			<th>Category|Account</th>
			<th>Recipient</th>
            <th>Comment</th>
            <th>Amount</th>
			<th colspan="2"></th>
		</tr>
	</thead>
	<tbody style="background:#ccc">
	<c:forEach items="${transactions}" var="transaction">
		<c:url var="editUrl" value="/app/transaction/edit?id=${transaction.id}" />
		<c:url var="deleteUrl" value="/app/transaction/delete?id=${transaction.id}" />
		<fmt:formatDate value="${transaction.date}" var="datetime" pattern="yyyy-MM-dd HH:mm"/>
		<tr>
			<td style = "width: 130px"><c:out value="${datetime}" /></td>
			<td><c:out value="${transaction.account.name}" /></td>
			<c:choose>
                <c:when test="${transaction.type=='TRANSFER'}">
                    <td><c:out value="${transaction.accountTo.name}" /></td>
                </c:when>
                <c:otherwise>
                    <td><c:out value="${transaction.category.name}" /></td>
                </c:otherwise>
            </c:choose>
            <td><c:out value="${transaction.recipient}" /></td>
            <td><c:out value="${transaction.comment}" /></td>
			<c:choose>
            	<c:when test="${transaction.type=='WITHDRAW'}">
                	<td style="text-align: right; color: red"><c:out value="-${transaction.amount}" /></td>
                </c:when>
                <c:when test="${transaction.type=='DEPOSIT'}">
                    <td style="text-align: right; color: green"><c:out value="+${transaction.amount}" /></td>
                </c:when>
                <c:otherwise>
                    <c:choose>
                    	<c:when test="${transaction.account.id==account.id}">
                    		<td style="text-align: right; color: red"><c:out value="-${transaction.amount}" /></td>
                    	</c:when>
                    	<c:otherwise>
                    		<td style="text-align: right; color: green"><c:out value="+${transaction.amount}" /></td>
                    	</c:otherwise>
                    </c:choose>
                </c:otherwise>
            </c:choose>
			<td style = "width: 40px"><a href="${editUrl}"><img src="${editImgUrl}"></img></a></td>
			<td style = "width: 40px"><a href="${deleteUrl}"><img src="${deleteImgUrl}"></img></a></td>
		</tr>
	</c:forEach>
	</tbody>
</table>

<c:if test="${empty transactions}">
	No transactions found.
</c:if>

<p><a href="${addRegularUrl}">New transaction</a></p>
<p><a href="${addTransferUrl}">New transfer</a></p>

</td>
</tr>

</table>

</body>
</html>