<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Wallets: ${username}</title>
</head>
<body>

<c:url var="editImgUrl" value="/resources/img/edit.png" />
<c:url var="deleteImgUrl" value="/resources/img/delete.png" />
<c:url var="addUrl" value="add" />
<c:url var="homeUrl" value="/app/homepage" />
<c:url var="logoutUrl" value="/app/logout"/>

<table style="width: 100%">

<tr>
<td style="text-align:left">
<p><a href="${homeUrl}">home</a></p>
<h1>Wallets</h1>
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
			<th>Name</th>
			<th>Amount</th>
            <th>Currency</th>
			<th colspan="2"></th>
		</tr>
	</thead>
	<tbody style="background:#ccc">
	<c:forEach items="${walletsWithAmount}" var="walletWithAmount">
		<c:url var="editUrl" value="/app/wallet/edit?id=${walletWithAmount[0].id}" />
		<c:url var="deleteUrl" value="/app/wallet/delete?id=${walletWithAmount[0].id}" />
		<c:url var="transactionUrl" value="/app/transaction/list?id=${walletWithAmount[0].id}" />
		<tr>
			<td><a href="${transactionUrl}"><c:out value="${walletWithAmount[0].name}" /></a></td>
			<td><c:out value="${walletWithAmount[1]}" /></td>
			<td><c:out value="${walletWithAmount[0].currency.name}" /></td>
			<td><a href="${editUrl}"><img src="${editImgUrl}"></img></a></td>
			<td><a href="${deleteUrl}"><img src="${deleteImgUrl}"></img></a></td>
		</tr>
	</c:forEach>
	</tbody>
</table>

<c:if test="${empty walletsWithAmount}">
	No records found. 
</c:if>

<p><a href="${addUrl}">Create new wallet</a></p>

</td>
</tr>

</table>

</body>
</html>