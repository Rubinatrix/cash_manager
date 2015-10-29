<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true"%>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Home</title>
</head>
<body>

<c:url var="loginUrl" value="/app/login"/>

<div>
    <h1>
	You registered successfully. Now you can simply log in using your name and password!
	</h1>
<a href="${loginUrl}">Login here</a>
</div>
</body>
</html>