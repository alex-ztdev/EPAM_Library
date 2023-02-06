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

<c:set var="language"
       value="${not empty param.language ? param.language : not empty sessionScope.language ? sessionScope.language : 'en'}"
       scope="session"/>

<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="locale"/>

<html lang="${language}">
<head>
    <title>404</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/index.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/404.css"/>

</head>
<body>
<div class="container">
    <jsp:include page="/pages/header.jsp"/>
    <div class="msg-container">
        <div class="messages">
            <div class="message"><fmt:message key="error.common.wrong.page.title"/> </div>
            <div class="message2"><fmt:message key="error.common.wrong.page.description"/></div>
        </div>
    </div>
    <jsp:include page="/pages/footer.jsp"/>
</div>
</body>
</html>



