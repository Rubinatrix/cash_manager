<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Registration Form</title>
</head>
<body>

<c:url var="loginUrl" value="/app/login"/>

<h1>Registration Form</h1>

<form:form modelAttribute="user" method="POST" enctype="utf8">
<table>
    <tr>
        <td><label>Username:</label></td>
        <td><form:input path="username" value="" /></td>
    </tr>
    <tr>
        <td><label>Password:</label></td>
        <td><form:input path="password" value="" type="password" /></td>
    </tr>
    <tr>
        <td><label>Confirm password:</label></td>
        <td><form:input path="matchingPassword" value="" type="password" /></td>
    </tr>
</table>

<p>
<form:errors element="div" cssStyle="color: #ff0000;" />
</p>

<p>
<input type="submit" value="Submit">
</p>

</form:form>

<a href="${loginUrl}">Back to login</a>

</body>
</html>