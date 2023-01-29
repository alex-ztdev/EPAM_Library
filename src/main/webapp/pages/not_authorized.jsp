<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 29.01.2023
  Time: 12:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="locale"/>


<html lang="${sessionScope.language}">
<head>
    <title>Not authorized!</title>
<%--    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/not_authorized.css"/>--%>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/index.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/not_authorized.css"/>

</head>
<body>
<div class="container">
    <jsp:include page="/pages/header.jsp"/>
    <div class="msg-container">
        <div class="messages">
            <div class="message"><fmt:message key="error.common.not.authorized.title"/> </div>
            <div class="message2"><fmt:message key="error.common.not.authorized.description"/></div>
        </div>
    </div>
    <jsp:include page="/pages/footer.jsp"/>
</div>
</body>
</html>
