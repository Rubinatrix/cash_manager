<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Transaction</title>
</head>
<body>

<c:choose>
    <c:when test="${type=='edit'}">
		<c:url var="saveUrl" value="/app/transaction/edit?id=${transactionAttribute.id}" />
		<h2>edit Transaction</h2>
    </c:when>
    <c:when test="${type=='add'}">
    	<c:url var="saveUrl" value="/app/transaction/add" />
    	<h2>new Transaction</h2>
    </c:when>
</c:choose>

<form:form modelAttribute="transactionAttribute" method="POST" action="${saveUrl}">

	<table>

	    <tr>
        	<td><form:label path="date">Date:</form:label></td>
        	<td><form:input type="datetime-local" path="date"/></td>
        </tr>

		<tr>
        	<td><form:label path="wallet">From:</form:label></td>
        	<td><form:select required="required" path="wallet">
        	<c:forEach items="${walletAttribute}" var="wallet">
               	<option value="${wallet.id}" <c:if test="${wallet.id==transactionAttribute.wallet.id}">selected</c:if>>${wallet.name}</option>
            </c:forEach>
            </form:select></td>
        </tr>

        <tr>
            <td><form:label path="category">Category:</form:label></td>
            <td><form:select path="category">
            <option value="0"></option>
            <c:forEach items="${categoryAttribute}" var="category">
                <option value="${category.id}" <c:if test="${category.id==transactionAttribute.category.id}">selected</c:if>>${category.name}</option>
            </c:forEach>
            </form:select></td>
        </tr>

        <tr>
            <td><form:label path="recipient">Recipient:</form:label></td>
        	<td><form:input path="recipient"/></td>
        </tr>

		<tr>
            <td><form:label path="type">Type:</form:label></td>
            <td><form:select required="required" path="type">
            <c:forEach items="${transactionTypeAttribute}" var="transactionType">
               	<option value="${transactionType}" <c:if test="${transactionType==transactionAttribute.type}">selected</c:if>>${transactionType.name}</option>
            </c:forEach>
            </form:select></td>
        </tr>

		<tr>
			<td><form:label path="amount">Amount:</form:label></td>
			<td><form:input type="number" min="0" path="amount"/></td>
		</tr>

		<tr>
            <td><form:label path="comment">Comment:</form:label></td>
            <td><form:input path="comment"/></td>
        </tr>

		<tr>
            <td><form:input type="hidden" value="0" path="walletTo"/></td>
        </tr>

	</table>
	
	<input type="submit" value="Save" />

</form:form>

</body>
</html>