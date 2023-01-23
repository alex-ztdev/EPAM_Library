<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 22.01.2023
  Time: 12:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="locale"/>

<html>
<head>
    <title>Title</title>
</head>
<body>
<a href="${pageContext.request.contextPath}/login.jsp">Login</a>
</body>
</html>
