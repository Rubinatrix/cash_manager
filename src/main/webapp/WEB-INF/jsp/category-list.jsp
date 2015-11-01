<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
<c:url var="addUrl" value="add" />
<c:url var="settingsUrl" value="/app/settings" />
<c:url var="accountUrl" value="/app/account/list"/>
<c:url var="logoutUrl" value="/app/logout"/>

<table style="width: 100%">

<tr>
<td style="text-align:left">
<p><a href="${accountUrl}"><< Accounts</a></p>
<p><a href="${settingsUrl}"><< Settings</a></p>
<h1>Categories</h1>
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
			<th colspan="2"></th>
		</tr>
	</thead>
	<tbody style="background:#ccc">
	<c:forEach items="${categories}" var="category">
		<c:url var="editUrl" value="/app/category/edit?id=${category.id}" />
		<c:url var="deleteUrl" value="/app/category/delete?id=${category.id}" />
		<tr>
			<td><c:out value="${category.name}" /></td>
			<td style = "width: 40px"><a href="${editUrl}"><img src="${editImgUrl}"></img></a></td>
			<td style = "width: 40px"><a href="${deleteUrl}"><img src="${deleteImgUrl}"></img></a></td>
		</tr>
	</c:forEach>
	</tbody>
</table>

<c:if test="${empty categories}">
	No records found. 
</c:if>

<div style="color: red">
<c:if test="${not empty errorDescription}">
	${errorDescription}
</c:if>
</div>

<p><a href="${addUrl}">Create new category</a></p>

</td>
</tr>

</table>

</body>
</html>