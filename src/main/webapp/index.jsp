<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<%--<c:set var="language"--%>
<%--       value="${not empty param.language ? param.language : not empty language ? language : 'en'}"--%>
<%--       scope="session"/>--%>
<c:set var="language"
       value="${not empty param.language ? param.language : not empty sessionScope.language ? sessionScope.language : 'en'}"
       scope="session"/>

<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="locale"/>

<html lang="${language}">
<head>
    <meta charset="UTF-8"/>
    <title>Library Main</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/index.css"/>
    <link rel="icon" href="${pageContext.request.contextPath}/static/img/icon/icon.ico">
</head>
<body>
<jsp:include page="/pages/header.jsp"/>

<div class="container">

    <div class="main-content">
        <div class="main-title"><fmt:message key="common.label.library.title"/></div>
        <div class="sub-title"><fmt:message key="common.label.creator"/></div>
    </div>


</div>
<jsp:include page="/pages/footer.jsp"/>
</body>

</html>
