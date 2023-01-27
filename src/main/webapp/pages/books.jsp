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
    <%--    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/index.css"/>--%>


</head>
<body>
<jsp:include page="header.jsp"/>

<div class="container">
    <div class="main-content">
        <div class="books_list_container">
            <table class="books_table">
                <tr>
                    <th><fmt:message key="books.common.label.counter"/></th>
                    <th><fmt:message key="books.common.label.book_title"/></th>
                    <th><fmt:message key="books.common.label.genre"/></th>
                    <th><fmt:message key="books.common.label.author"/></th>
                    <th><fmt:message key="books.common.label.publisher"/></th>
                    <th><fmt:message key="books.common.label.pages"/></th>
                    <th><fmt:message key="books.common.label.publication_date"/></th>
                    <th><fmt:message key="books.common.label.available"/></th>
                </tr>
                <c:forEach var="booksList" items="${requestScope.booksList}" varStatus="loop">
                    <tr>
                        <td> ${loop.count} </td>
                        <td> ${booksList.title} </td>
                        <td> ${booksList.genre} </td>
                        <td> ${booksList.author.firstName}${booksList.author.secondName} </td>
                        <td> ${booksList.publisherTitle} </td>
                        <td> ${booksList.pageNumber} </td>
                        <td> ${booksList.publicationDate} </td>
                        <td>
                            <c:choose>
                                <c:when test="${booksList.isAvailable}">
                                    <fmt:message key="books.common.available"/>
                                </c:when>
                                <c:otherwise>
                                    <fmt:message key="books.common.unavailable"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>