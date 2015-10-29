<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Transactions: ${username}</title>
</head>
<body>

<c:url var="editImgUrl" value="/resources/img/edit.png" />
<c:url var="deleteImgUrl" value="/resources/img/delete.png" />
<c:url var="addRegularUrl" value="/app/transaction/add/regular" />
<c:url var="addTransferUrl" value="/app/transaction/add/transfer" />
<c:url var="homeUrl" value="/app/homepage" />
<c:url var="logoutUrl" value="/app/logout"/>

<table style="width: 100%">

<tr>
<td style="text-align:left">
<p><a href="${homeUrl}">home</a></p>
<h1>Transactions</h1>
</td>
<td style="text-align:right; vertical-align:top">
Logged as <b>${username}</b>
<p><a href="${logoutUrl}">Logout</a></p>
</td>
</tr>

<tr>
<td COLSPAN=2>

<table style="border: 1px solid; width: 100%; text-align:center">
	<thead style="background:#d3dce3">
		<tr>
			<th>Date</th>
			<th>Wallet</th>
			<th>Category|Wallet</th>
			<th>Recipient</th>
			<th>Comment</th>
            <th>Type</th>
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
			<td><c:out value="${datetime}" /></td>
			<td><c:out value="${transaction.wallet.name}" /></td>
			<c:choose>
                <c:when test="${transaction.type=='TRANSFER'}">
                    <td><c:out value="${transaction.walletTo.name}" /></td>
                </c:when>
                <c:otherwise>
                    <td><c:out value="${transaction.category.name}" /></td>
                </c:otherwise>
            </c:choose>
            <td><c:out value="${transaction.recipient}" /></td>
            <td><c:out value="${transaction.comment}" /></td>
			<td><c:out value="${transaction.type}" /></td>
			<td><c:out value="${transaction.amount}" /></td>
			<td><a href="${editUrl}"><img src="${editImgUrl}"></img></a></td>
			<td><a href="${deleteUrl}"><img src="${deleteImgUrl}"></img></a></td>
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