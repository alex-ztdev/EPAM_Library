<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 22.01.2023
  Time: 12:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="language"
       value="${not empty param.language ? param.language : not empty sessionScope.language ? sessionScope.language : 'en'}"
       scope="session"/>

<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="locale"/>

<html lang="${language}">
<head>
    <meta charset="UTF-8"/>
    <title>Order book</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/profile.css"/>
</head>
<body>
<jsp:include page="/pages/header.jsp"/>

<div class="container">
    <div class="main-content">
        <div class="profile-header"><fmt:message key="header.common.profile"/></div>
        <div>

            <label class="profile-label"><fmt:message key="common.profile.login"/></label>
            <div class="user-data">${requestScope.user.login}</div>

            <label class="profile-label"><fmt:message key="common.profile.role"/></label>
            <div class="user-data">${requestScope.user.role}</div>

            <label class="profile-label"><fmt:message key="common.profile.status"/></label>
            <div class="user-data">${requestScope.user.status}</div>


            <label class="profile-label"><fmt:message key="common.profile.email"/></label>
            <div class="user-data">${requestScope.user.email}</div>

            <label class="profile-label"><fmt:message key="common.profile.phone"/></label>
            <div class="user-data">${requestScope.user.phoneNumber}</div>


            <div class="elem-group inlined"><label class="profile-label"><fmt:message key="common.profile.first.name"/></label>
                <div class="user-data">${requestScope.user.firstName}</div>
            </div>
            <div class="elem-group inlined"><label class="profile-label"><fmt:message key="common.profile.second.name"/></label>
                <div class="user-data">${requestScope.user.secondName}</div>
            </div>


            <%--            <form action="${pageContext.request.contextPath}/controller?command=order-book&book_id=${sessionScope.book.bookId}" method="post">--%>
            <%--                <select name="subscription_type">--%>
            <%--                    <option value="true">On subscription</option>--%>
            <%--                    <option value="false">To the reading room</option>--%>
            <%--                </select>--%>
            <%--                <button type="submit" style="margin-top:20px; margin-left: 43%; padding: 10px 20px"><fmt:message--%>
            <%--                        key="admin.books.edit.form.label.submit.btn"/></button>--%>
            <%--            </form>--%>

        </div>

    </div>
</div>

<jsp:include page="/pages/footer.jsp"/>
</body>
</html>
