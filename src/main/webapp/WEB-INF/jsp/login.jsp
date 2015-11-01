<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Cash manager</title>
</head>
<body>

<c:url var="registrationUrl" value="/app/registration"/>

<h1>Login</h1>

<form action="login" method="POST">

<input type="hidden" name="_spring_security_remember_me" value="false">

<table>
    <tr>
        <td><label for="username">Username:</label></td>
        <td><input type="text" id="username" name="username"></td>
    </tr>
    <tr>
        <td><label for="password">Password:</label></td>
        <td><input type="password" id="password" name="password"></td>
    </tr>
</table>

<p>
<input type="submit" value="Login">
</p>

</form>

<a href="${registrationUrl}">Register new user</a>

</body>
</html>