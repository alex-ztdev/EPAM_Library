<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<%--<c:set var="language"--%>
<%--       value="${not empty param.language ? param.language : not empty language ? language : 'en'}"--%>
<%--       scope="session"/>--%>

<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="locale"/>


<html lang="${sessionScope.language}">
<head>
    <meta charset="UTF-8"/>
    <title>Library Main</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/index.css"/>

</head>
<body>
<div class="container">
    <jsp:include page="/pages/header.jsp"/>

    <div class="main-content">

    </div>

    <jsp:include page="/pages/footer.jsp"/>
</div>
</body>

</html>
