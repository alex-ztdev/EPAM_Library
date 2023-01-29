<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 22.01.2023
  Time: 16:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="locale"/>


<html>
<head>
    <title>404</title>
</head>
<body>
        <fmt:message key="error.common.wrong.page"/>
</body>
</html>
