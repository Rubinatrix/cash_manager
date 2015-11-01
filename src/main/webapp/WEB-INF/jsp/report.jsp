<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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

<c:url var="accountUrl" value="/app/account/list"/>
<c:url var="logoutUrl" value="/app/logout"/>
<c:url var="reportsUrl" value="/app/report"/>

<table style="width: 100%">

<tr>
<td style="text-align:left">
<p><a href="${accountUrl}"><< Accounts</a></p>
<p><a href="${reportsUrl}"><< Reports</a></p>
<c:choose>
    <c:when test="${reportType=='category'}">
		<h1>Cash flow by categories</h1>
    </c:when>
    <c:when test="${reportType=='recipient'}">
    	<h1>Cash amount by recipients</h1>
    </c:when>
    <c:otherwise>
    	<h1>Unknown report</h1>
    </c:otherwise>
</c:choose>
</td>
<td style="text-align:right; vertical-align:top">
Logged as <b>${username}</b>
<p><a href="${logoutUrl}">Logout</a></p>
</td>
</tr>

<tr>
<td COLSPAN=2>
<form:form modelAttribute="reportSettings" method="POST">

	<table>

	    <tr>
        	<td><form:label path="startDate">Period:</form:label></td>
        	<td><form:input type="date" path="startDate"/></td>
            <td><form:label path="endDate"> - </form:label></td>
            <td><form:input type="date" path="endDate"/></td>
        </tr>

        <tr>
            <td><form:label path="currency">Currency:</form:label></td>
            <td><form:select required="required" path="currency">
            <c:forEach items="${currencies}" var="currency">
               	<option value="${currency.id}" <c:if test="${currency.id==reportSettings.currency.id}">selected</c:if>>${currency.name}</option>
            </c:forEach>
            </form:select></td>
        </tr>

        <c:if test="${reportType=='category'}">

		<tr>
            <td><form:label path="transactionType">Type:</form:label></td>
            <td><form:select required="required" path="transactionType">
            <c:forEach items="${transactionTypes}" var="transactionType">
               	<option value="${transactionType}" <c:if test="${transactionType==reportSettings.transactionType}">selected</c:if>>${transactionType.name}</option>
            </c:forEach>
            </form:select></td>
        </tr>

        </c:if>

	</table>

	<p><input type="submit" value="Get Report" /></p>

</form:form>
</td>
</tr>

<tr>
<td COLSPAN=2>

<table style="border: 1px solid; width: 100%; text-align:center">
	<thead style="background:#d3dce3">
		<tr>
			<th>Entity</th>
			<th>Amount</th>
			<c:if test="${reportType=='category'}">
				<th>% total</th>
			</c:if>
		</tr>
	</thead>
	<tbody style="background:#ccc">
	<c:forEach items="${categoryAmount}" var="position">
		<tr>
			<td><c:out value="${position[0]}" /></td>
			<td><fmt:formatNumber type="number" minFractionDigits="2" value="${position[1]}" /></td>
			<c:if test="${reportType=='category'}">
				<td><fmt:formatNumber type="percent" minFractionDigits="2" value="${position[1] / totalAmount}" /></td>
			</c:if>
		</tr>
	</c:forEach>
	<tr>
    	<td><b><c:out value="TOTAL" /></b></td>
    	<td><b><fmt:formatNumber type="number" minFractionDigits="2" value="${totalAmount}" /></b></td>
    	<c:if test="${reportType=='category'}">
    		<td></td>
    	</c:if>
    </tr>
	</tbody>
</table>

<c:if test="${empty categoryAmount} or ${empty totalAmount}">
	No records found. 
</c:if>

</td>
</tr>

</table>

</body>
</html>