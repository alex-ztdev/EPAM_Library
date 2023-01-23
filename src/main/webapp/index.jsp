<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="locale"/>



<html lang="${sessionScope.language}">
<html>
<head>
    <meta charset="UTF-8" />
    <title>Library Main</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/index.css" />
</head>
<body>
<div class="container">
    <jsp:include page="/pages/header.jsp"/>

    <div class="main-content"></div>

    <div class="footer">
        <div class="footer-elements">
            <a href="#about_page">About</a>
            <a href="https://github.com/alex-ztdev">GitHub</a>
        </div>
    </div>
</div>
</body>
<script
        src="https://kit.fontawesome.com/d117408745.js"
        crossorigin="anonymous"
></script>
</html>
