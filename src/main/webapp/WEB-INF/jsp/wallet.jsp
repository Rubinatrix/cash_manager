<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Wallet</title>
</head>
<body>

<c:choose>
    <c:when test="${type=='edit'}">
		<c:url var="saveUrl" value="/app/wallet/edit?id=${walletAttribute.id}" />
		<h2>edit Wallet</h2>
    </c:when>
    <c:when test="${type=='add'}">
    	<c:url var="saveUrl" value="/app/wallet/add" />
    	<h2>new Wallet</h2>
    </c:when>
</c:choose>

<form:form modelAttribute="walletAttribute" method="POST" action="${saveUrl}">
	<table>
		<tr>
			<td><form:label path="name">Name:</form:label></td>
			<td><form:input path="name"/></td>
		</tr>

		<tr>
			<td><form:label path="currency">Currency:</form:label></td>
			<td><form:select required="required" path="currency">
			<c:forEach items="${currencyAttribute}" var="currency">
            	<option value="${currency.id}" <c:if test="${currency.id==walletAttribute.currency.id}">selected</c:if>>${currency.name}</option>
            </c:forEach>
           	</form:select></td>
        </tr>

	</table>
	
	<input type="submit" value="Save" />
</form:form>

</body>
</html>