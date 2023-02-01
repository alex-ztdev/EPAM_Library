<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 01.02.2023
  Time: 10:39
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
    <title>Orders</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/books.css"/>
    <script src="https://kit.fontawesome.com/e83e8567ce.js" crossorigin="anonymous"></script>
</head>
<body>
<jsp:include page="header.jsp"/>

<div class="container">
    <div class="main-content">
        <div class="top-row">
            <c:choose>
                <c:when test="${sessionScope.user.role eq 'ADMIN' or 'LIBRARIAN'}">
                    <p class="users-orders-title"><fmt:message key="admin.orders.orders.title"/></p>
                </c:when>
                <c:otherwise>
                    <p class="users-orders-title"><fmt:message key="user.orders.orders.title"/></p>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="books_list_container">
            <table class="books_table">
                <tr>
                    <th><fmt:message key="common.label.counter"/></th>

                    <th><fmt:message key="orders.common.order.id"/></th>
                    <c:if test="${sessionScope.user.role eq 'ADMIN' or 'LIBRARIAN'}">
                        <th><fmt:message key="orders.common.user.id"/></th>
                        <th><fmt:message key="orders.common.user.name"/></th>
                    </c:if>

                    <th><fmt:message key="orders.common.book.title"/></th>
                    <th><fmt:message key="orders.common.order.start.date"/></th>
                    <th><fmt:message key="orders.common.order.end.date"/></th>
                    <th><fmt:message key="orders.common.order.place"/> </th>
                    <th><fmt:message key="orders.common.returned"/></th>
                    <th><fmt:message key="orders.common.fine"/></th>

                    <c:forEach var="orders" items="${requestScope.ordersList}" varStatus="loop">
                <tr>
                    <td> ${loop.count + (requestScope.page - 1) * requestScope.booksPerPage} </td>
                    <td> ${orders.orderId} </td>
                    <td> ${orders.book.book.title} </td>
                    <td> ${booksList.authorFirstName} ${booksList.authorSecondName}

                    </td>
                    <td> ${booksList.publisherTitle} </td>
                    <td> ${booksList.pageNumber} </td>
                    <td> ${booksList.publicationDate} </td>
                    <td>
                            ${booksList.copies}
                            <%--                        <c:choose>--%>
                            <%--                            <c:when test="${booksList.copies gt 0}">--%>
                            <%--                                ${booksList.copies}--%>
                            <%--                            </c:when>--%>
                            <%--                            <c:otherwise>--%>
                            <%--                                <fmt:message key="books.common.unavailable"/>--%>
                            <%--                            </c:otherwise>--%>
                            <%--                        </c:choose>--%>
                    </td>

                    <c:if test="${userRole eq 'ADMIN'}">
                        <c:choose>
                            <c:when test="${booksList.isRemoved}">
                                <td class="removed-book">
                                    <style>
                                        .removed-book:hover a p.label:after {
                                            content: '<fmt:message key="books.admin.action.restore"/>';
                                        }

                                        .removed-book:hover a p.label span {
                                            display: none;
                                        }
                                    </style>
                                    <div class="removed-book-div">
                                        <a class="remove-link"
                                           href="controller?command=restore-book&book_id=${booksList.bookId}">
                                            <p class="label"><span class="align"><fmt:message
                                                    key="books.admin.msg.removed"/></span></p>
                                        </a>
                                    </div>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td class="present-book">
                                    <style>
                                        .present-book:hover a p.label:after {
                                            content: '<fmt:message key="books.admin.action.remove"/>';
                                        }

                                        .present-book:hover a p.label span {
                                            display: none;
                                        }
                                    </style>
                                    <div class="removed-book-div">
                                        <a class="remove-link"
                                           href="controller?command=remove-book&book_id=${booksList.bookId}">
                                            <p class="label"><span class="align"><fmt:message
                                                    key="books.admin.msg.present"/></span></p>
                                        </a>
                                    </div>
                                </td>
                            </c:otherwise>
                        </c:choose>

                        <td>
                            <div class="update-book-div">
                                <a class="remove-link"
                                   href="controller?command=update-book-redirect&book_id=${booksList.bookId}">
                                    <fmt:message key="books.admin.action.update"/>
                                </a>
                            </div>

                        </td>
                    </c:if>
                    <c:if test="${userRole eq 'USER'}">
                        <c:choose>
                            <c:when test="${booksList.copies gt 0}">
                                <td class="order-book" >
                                    <a class="order-book-link" style="height: 100%; width: 100%; text-align: center"
                                       href="controller?command=order_book_redirect&book_id=${booksList.bookId}">
                                        <fmt:message key="user.books.action.order.book"/>
                                    </a>
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td class="order-book-impossible" >
                                    <div class="order-book-div">
                                        <fmt:message key="books.common.unavailable"/>
                                    </div>
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
