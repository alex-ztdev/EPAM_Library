<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 29.01.2023
  Time: 16:43
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
