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

<c:set var="order_by" scope="request" value="${requestScope.order_by}"/>
<c:set var="order_dir" scope="request" value="${requestScope.order_dir}"/>

<html lang="${sessionScope.language}">
<head>
    <meta charset="UTF-8"/>
    <title>Books</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/books.css"/>
    <%--    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/index.css"/>--%>
    <script
            src="https://kit.fontawesome.com/d117408745.js"
            crossorigin="anonymous"
    ></script>

</head>
<body>
<jsp:include page="header.jsp"/>


<div class="container">
    <div class="main-content">
        <div class="books_list_container">
            <table class="books_table">
                <tr>
                    <th><fmt:message key="books.common.label.counter"/></th>

                    <c:choose>
                        <c:when test="${order_by eq 'BY_TITLE'}">
                            <c:choose>
                                <c:when test="${order_dir eq 'ASC'}">
                                    <th class="active">
                                        <a href="controller?command=books-list&order_by=by_title&order_dir=desc">
                                            <fmt:message key="books.common.label.book_title"/>
                                            <i class="fa fa-long-arrow-up" aria-hidden="true"></i>
                                        </a>
                                    </th>
                                </c:when>
                                <c:otherwise>
                                    <th class="active">
                                        <a href="controller?command=books-list&order_by=by_title&order_dir=asc">
                                            <fmt:message key="books.common.label.book_title"/>
                                            <i class="fa fa-long-arrow-down" aria-hidden="true"></i>
                                        </a>
                                    </th>
                                </c:otherwise>
                            </c:choose>
                        </c:when>
                        <c:otherwise>
                            <th>
                                <a href="controller?command=books-list&order_by=by_title&order_dir=asc">
                                    <fmt:message key="books.common.label.book_title"/>
                                    <i class="fa fa-sort" aria-hidden="true"></i>
                                </a>
                            </th>
                        </c:otherwise>
                    </c:choose>


                    <th><fmt:message key="books.common.label.genre"/> <i class="fa fa-sort" aria-hidden="true"></i></th>
                    <th><fmt:message key="books.common.label.author"/> <i class="fa fa-sort" aria-hidden="true"></i>
                    </th>
                    <th><fmt:message key="books.common.label.publisher"/> <i class="fa fa-sort" aria-hidden="true"></i>
                    </th>
                    <th><fmt:message key="books.common.label.pages"/> <i class="fa fa-sort" aria-hidden="true"></i></th>
                    <th><fmt:message key="books.common.label.publication_date"/> <i class="fa fa-sort"
                                                                                    aria-hidden="true"></i></th>
                    <th><fmt:message key="books.common.label.available"/> <i class="fa fa-sort" aria-hidden="true"></i>
                    </th>
                </tr>
                <c:forEach var="booksList" items="${requestScope.booksList}" varStatus="loop">
                    <tr>
                        <td> ${loop.count + (requestScope.page - 1) * requestScope.booksPerPage} </td>
                        <td> ${booksList.title} </td>
                        <td> ${booksList.genre} </td>
                            <%--TODO: transfer book to BookDTO with authors fullName --%>
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
        <div class="pages_container">
            <%--For displaying Previous link except for the 1st page --%>
            <c:if test="${requestScope.page != 1}">
                <a href="controller?command=books-list&page=${requestScope.page - 1}">&laquo;</a>
            </c:if>
            <%--For displaying Page numbers.
                The when condition does not display a link for the current page--%>
            <c:forEach begin="1" end="${requestScope.totalPages}" var="i">
                <c:choose>
                    <c:when test="${requestScope.page eq i}">
                        <a href="controller?command=books-list&page=${i}" class="active"> ${i}</a>
                    </c:when>
                    <c:otherwise>
                        <a href="controller?command=books-list&page=${i}">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <%--For displaying Next link --%>
            <c:if test="${requestScope.page lt requestScope.totalPages}">
                <a href="controller?command=books-list&page=${requestScope.page + 1}">&raquo;</a>
            </c:if>

        </div>


    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>