<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 29.01.2023
  Time: 12:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
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
            <div class="message">You are not authorized.</div>
            <div class="message2">You tried to access a page you did not have prior authorization for.</div>
        </div>
    </div>
    http://localhost:8080/Library/controller?command=remove-book
    <jsp:include page="/pages/footer.jsp"/>
</div>
</body>
</html>
