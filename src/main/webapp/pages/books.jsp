<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 27.01.2023
  Time: 10:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="locale"/>

<html lang="${sessionScope.language}">
<head>
    <meta charset="UTF-8"/>
    <title>Books</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/books.css"/>
</head>
<body>
    <jsp:include page="header.jsp"/>

    <div>Books</div>

    <jsp:include page="footer.jsp"/>
</body>
</html>
