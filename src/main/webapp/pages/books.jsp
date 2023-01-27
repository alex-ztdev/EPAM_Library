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
    <div class="main-content" >
        <div class="books_list_container">
            <table class="books_table">
                <tr>
                    <td>
                        <h2><fmt:message key="books.common.label.counter"/></h2>
                    </td>
                    <td>
                        <h2><fmt:message key="books.common.label.book_title"/></h2>
                    </td>
                    <td>
                        <h2><fmt:message key="books.common.label.genre"/></h2>
                    </td>
                    <td>
                        <h2><fmt:message key="books.common.label.author"/></h2>
                    </td>
                    <td>
                        <h2><fmt:message key="books.common.label.publisher"/></h2>
                    </td>
                    <td>
                        <h2><fmt:message key="books.common.label.pages"/></h2>
                    </td>
                    <td>
                        <h2><fmt:message key="books.common.label.publication_date"/></h2>
                        <%--                    <fmt:formatDate value="${booksList.publicationDate}" type="date"/>--%>
                    </td>
                    <td>
                        <h2><fmt:message key="books.common.label.available"/></h2
                    </td>
                </tr>


                <c:forEach var="booksList" items="${requestScope.booksList}" varStatus="loop">
                    <tr>
                        <td>
                            <h2> ${loop.count}</h2>
                        </td>
                        <td>
                            <h2> ${booksList.title}</h2>
                        </td>
                        <td>
                            <h2>${booksList.genre}</h2>
                        </td>
                        <td>
                            <h2>${booksList.author.firstName}${booksList.author.secondName}</h2>
                        </td>
                        <td>
                            <h2>${booksList.publisherTitle}</h2>
                        </td>
                        <td>
                            <h2>${booksList.pageNumber}</h2>
                        </td>
                        <td>
                                ${booksList.publicationDate}
                                <%--                    <fmt:formatDate value="${booksList.publicationDate}" type="date"/>--%>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${booksList.isAvailable}">
                                    <h2>
                                        <fmt:message key="books.common.available"/>
                                    </h2>
                                </c:when>
                                <c:otherwise>
                                    <h2>
                                        <fmt:message key="books.common.unavailable"/>
                                    </h2>
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
