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
<c:set var="userRole" scope="request" value="${sessionScope.user.role}"/>

<html lang="${sessionScope.language}">
<head>
    <meta charset="UTF-8"/>
    <title>Books</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/books.css"/>
    <%--    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/index.css"/>--%>
    <script src="https://kit.fontawesome.com/e83e8567ce.js" crossorigin="anonymous"></script>

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
                            <i class="fa fa-long-arrow-down" aria-hidden="true"></i>
                        </a>
                    </th>
                    </c:when>
                    <c:otherwise>
                    <th class="active">
                        <a href="controller?command=books-list&order_by=by_title&order_dir=asc">
                            <fmt:message key="books.common.label.book_title"/>
                            <i class="fa fa-long-arrow-up" aria-hidden="true"></i>
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

                    <c:choose>
                    <c:when test="${order_by eq 'BY_GENRE'}">
                    <c:choose>
                    <c:when test="${order_dir eq 'ASC'}">
                    <th class="active">
                        <a href="controller?command=books-list&order_by=by_genre&order_dir=desc">
                            <fmt:message key="books.common.label.genre"/>
                            <i class="fa fa-long-arrow-down" aria-hidden="true"></i>
                        </a>
                    </th>
                    </c:when>
                    <c:otherwise>
                    <th class="active">
                        <a href="controller?command=books-list&order_by=by_genre&order_dir=asc">
                            <fmt:message key="books.common.label.genre"/>
                            <i class="fa fa-long-arrow-up" aria-hidden="true"></i>
                        </a>
                    </th>
                    </c:otherwise>
                    </c:choose>
                    </c:when>
                    <c:otherwise>
                    <th>
                        <a href="controller?command=books-list&order_by=by_genre&order_dir=asc">
                            <fmt:message key="books.common.label.genre"/>
                            <i class="fa fa-sort" aria-hidden="true"></i>
                        </a>
                    </th>
                    </c:otherwise>
                    </c:choose>

                    <c:choose>
                    <c:when test="${order_by eq 'BY_AUTHOR'}">
                    <c:choose>
                    <c:when test="${order_dir eq 'ASC'}">
                    <th class="active">
                        <a href="controller?command=books-list&order_by=by_author&order_dir=desc">
                            <fmt:message key="books.common.label.author"/>
                            <i class="fa fa-long-arrow-down" aria-hidden="true"></i>
                        </a>
                    </th>
                    </c:when>
                    <c:otherwise>
                    <th class="active">
                        <a href="controller?command=books-list&order_by=by_author&order_dir=asc">
                            <fmt:message key="books.common.label.author"/>
                            <i class="fa fa-long-arrow-up" aria-hidden="true"></i>
                        </a>
                    </th>
                    </c:otherwise>
                    </c:choose>
                    </c:when>
                    <c:otherwise>
                    <th>
                        <a href="controller?command=books-list&order_by=by_author&order_dir=asc">
                            <fmt:message key="books.common.label.author"/>
                            <i class="fa fa-sort" aria-hidden="true"></i>
                        </a>
                    </th>
                    </c:otherwise>
                    </c:choose>

                    <c:choose>
                    <c:when test="${order_by eq 'BY_PUBLISHER'}">
                    <c:choose>
                    <c:when test="${order_dir eq 'ASC'}">
                    <th class="active">
                        <a href="controller?command=books-list&order_by=by_publisher&order_dir=desc">
                            <fmt:message key="books.common.label.publisher"/>
                            <i class="fa fa-long-arrow-down" aria-hidden="true"></i>
                        </a>
                    </th>
                    </c:when>
                    <c:otherwise>
                    <th class="active">
                        <a href="controller?command=books-list&order_by=by_publisher&order_dir=asc">
                            <fmt:message key="books.common.label.publisher"/>
                            <i class="fa fa-long-arrow-up" aria-hidden="true"></i>
                        </a>
                    </th>
                    </c:otherwise>
                    </c:choose>
                    </c:when>
                    <c:otherwise>
                    <th>
                        <a href="controller?command=books-list&order_by=by_publisher&order_dir=asc">
                            <fmt:message key="books.common.label.publisher"/>
                            <i class="fa fa-sort" aria-hidden="true"></i>
                        </a>
                    </th>
                    </c:otherwise>
                    </c:choose>

                    <c:choose>
                    <c:when test="${order_by eq 'BY_PAGES'}">
                    <c:choose>
                    <c:when test="${order_dir eq 'ASC'}">
                    <th class="active">
                        <a href="controller?command=books-list&order_by=by_pages&order_dir=desc">
                            <fmt:message key="books.common.label.pages"/>
                            <i class="fa fa-long-arrow-down" aria-hidden="true"></i>
                        </a>
                    </th>
                    </c:when>
                    <c:otherwise>
                    <th class="active">
                        <a href="controller?command=books-list&order_by=by_pages&order_dir=asc">
                            <fmt:message key="books.common.label.pages"/>
                            <i class="fa fa-long-arrow-up" aria-hidden="true"></i>
                        </a>
                    </th>
                    </c:otherwise>
                    </c:choose>
                    </c:when>
                    <c:otherwise>
                    <th>
                        <a href="controller?command=books-list&order_by=by_pages&order_dir=asc">
                            <fmt:message key="books.common.label.pages"/>
                            <i class="fa fa-sort" aria-hidden="true"></i>
                        </a>
                    </th>
                    </c:otherwise>
                    </c:choose>

                    <c:choose>
                    <c:when test="${order_by eq 'BY_PUBLICATION_DATE'}">
                    <c:choose>
                    <c:when test="${order_dir eq 'ASC'}">
                    <th class="active">
                        <a href="controller?command=books-list&order_by=by_publication_date&order_dir=desc">
                            <fmt:message key="books.common.label.publication_date"/>
                            <i class="fa fa-long-arrow-down" aria-hidden="true"></i>
                        </a>
                    </th>
                    </c:when>
                    <c:otherwise>
                    <th class="active">
                        <a href="controller?command=books-list&order_by=by_publication_date&order_dir=asc">
                            <fmt:message key="books.common.label.publication_date"/>
                            <i class="fa fa-long-arrow-up" aria-hidden="true"></i>
                        </a>
                    </th>
                    </c:otherwise>
                    </c:choose>
                    </c:when>
                    <c:otherwise>
                    <th>
                        <a href="controller?command=books-list&order_by=by_publication_date&order_dir=asc">
                            <fmt:message key="books.common.label.publication_date"/>
                            <i class="fa fa-sort" aria-hidden="true"></i>
                        </a>
                    </th>
                    </c:otherwise>
                    </c:choose>

                    <c:choose>
                    <c:when test="${order_by eq 'BY_COPIES'}">
                    <c:choose>
                    <c:when test="${order_dir eq 'ASC'}">
                    <th class="active">
                        <a href="controller?command=books-list&order_by=by_copies&order_dir=desc">
                            <fmt:message key="books.common.label.available"/>
                            <i class="fa fa-long-arrow-down" aria-hidden="true"></i>
                        </a>
                    </th>
                    </c:when>
                    <c:otherwise>
                    <th class="active">
                        <a href="controller?command=books-list&order_by=by_copies&order_dir=asc">
                            <fmt:message key="books.common.label.available"/>
                            <i class="fa fa-long-arrow-up" aria-hidden="true"></i>
                        </a>
                    </th>
                    </c:otherwise>
                    </c:choose>
                    </c:when>
                    <c:otherwise>
                    <th>
                        <a href="controller?command=books-list&order_by=by_copies&order_dir=asc">
                            <fmt:message key="books.common.label.available"/>
                            <i class="fa fa-sort" aria-hidden="true"></i>
                        </a>
                    </th>
                    </c:otherwise>
                    </c:choose>

                    <c:if test="${sessionScope.user.role eq 'ADMIN'}">
                    <c:choose>
                    <c:when test="${order_by eq 'BY_REMOVED'}">
                    <c:choose>
                    <c:when test="${order_dir eq 'ASC'}">
                    <th class="active">
                        <a href="controller?command=books-list&order_by=by_removed&order_dir=desc">
                            <fmt:message key="books.admin.label.removed"/>
                            <i class="fa fa-long-arrow-down" aria-hidden="true"></i>
                        </a>
                    </th>
                    </c:when>
                    <c:otherwise>
                    <th class="active">
                        <a href="controller?command=books-list&order_by=by_removed&order_dir=asc">
                            <fmt:message key="books.admin.label.removed"/>
                            <i class="fa fa-long-arrow-up" aria-hidden="true"></i>
                        </a>
                    </th>
                    </c:otherwise>
                    </c:choose>
                    </c:when>
                    <c:otherwise>
                    <th>
                        <a href="controller?command=books-list&order_by=by_removed&order_dir=asc">
                            <fmt:message key="books.admin.label.removed"/>
                            <i class="fa fa-sort" aria-hidden="true"></i>
                        </a>
                    </th>
                    </c:otherwise>
                    </c:choose>
                    </c:if>


                    <%--                    <th><fmt:message key="books.common.label.available"/> <i class="fa fa-sort" aria-hidden="true"></i></th>--%>
                    <%--                </tr>--%>
                    <c:forEach var="booksList" items="${requestScope.booksList}" varStatus="loop">
                <tr>
                    <td> ${loop.count + (requestScope.page - 1) * requestScope.booksPerPage} </td>
                    <td> ${booksList.title} </td>
                    <td> ${booksList.genre} </td>
                    <td> ${booksList.authorFullName} </td>
                    <td> ${booksList.publisherTitle} </td>
                    <td> ${booksList.pageNumber} </td>
                    <td> ${booksList.publicationDate} </td>
                    <td>
                        <c:choose>
                            <c:when test="${booksList.copies gt 0}">
                                ${booksList.copies}
                            </c:when>
                            <c:otherwise>
                                <fmt:message key="books.common.unavailable"/>
                            </c:otherwise>
                        </c:choose>
                    </td>

                    <c:if test="${userRole eq 'ADMIN'}">
                        <c:choose>
                            <c:when test="${booksList.isRemoved}">
                                <td class="removed-book">
                                    <fmt:message key="books.admin.msg.removed"/>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td class="present-book">
                                    <a href="controller?command=unblock-book&book_id=${booksList.bookId}}" >
                                        <fmt:message key="books.admin.msg.present"/>
                                    </a>

                                </td>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </tr>
                </c:forEach>
            </table>


        </div>
        <div class="pages_container">
            <%--For displaying Previous link except for the 1st page --%>
            <c:if test="${requestScope.page != 1}">
                <a href="controller?command=books-list&page=${requestScope.page - 1}&order_by=${order_by}&order_dir=${order_dir}">&laquo;</a>
            </c:if>
            <%--For displaying Page numbers.
                The when condition does not display a link for the current page--%>
            <c:forEach begin="1" end="${requestScope.totalPages}" var="i">
                <c:choose>
                    <c:when test="${requestScope.page eq i}">
                        <a href="controller?command=books-list&page=${i}&order_by=${order_by}&order_dir=${order_dir}"
                           class="active"> ${i}</a>
                    </c:when>
                    <c:otherwise>
                        <a href="controller?command=books-list&page=${i}&order_by=${order_by}&order_dir=${order_dir}">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <%--For displaying Next link --%>
            <c:if test="${requestScope.page lt requestScope.totalPages}">
                <a href="controller?command=books-list&page=${requestScope.page + 1}&order_by=${order_by}&order_dir=${order_dir}">&raquo;</a>
            </c:if>

        </div>


    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>