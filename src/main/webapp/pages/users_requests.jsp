<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 05.02.2023
  Time: 13:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="custom" uri="/WEB-INF/custom.tld" %>


<c:set var="language"
       value="${not empty param.language ? param.language : not empty sessionScope.language ? sessionScope.language : 'en'}"
       scope="session"/>

<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="locale"/>

<c:set var="command" scope="request"
       value="${sessionScope.user.role eq 'USER' ? 'display-my-requested-orders' : 'display-users-requested-orders'}"/>


<html lang="${language}">
<head>
    <meta charset="UTF-8"/>
    <title>Requests</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/requests.css"/>
    <script src="https://kit.fontawesome.com/e83e8567ce.js" crossorigin="anonymous"></script>
    <link rel="icon" href="${pageContext.request.contextPath}/static/img/icon/icon.ico">
</head>
<body>
<jsp:include page="header.jsp"/>

<div class="container">
    <div class="main-content">
        <div class="top-row">
            <c:choose>
                <c:when test="${sessionScope.user.role eq 'ADMIN' or sessionScope.user.role eq 'LIBRARIAN'}">
                    <p class="users-orders-title"><fmt:message key="admin.orders.requests.title"/></p>
                </c:when>
                <c:otherwise>
                    <p class="users-orders-title"><fmt:message key="my.orders.requests.title"/></p>
                    <c:if test="${not empty requestScope.msg}">
                        <p class="success-msg"><fmt:message key="user.orders.success.msg"/></p>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="orders_list_container">
            <table class="orders_table">

                <tr>
                    <th><fmt:message key="common.label.counter"/></th>

                    <th><fmt:message key="orders.common.order.id"/></th>
                    <c:if test="${sessionScope.user.role eq 'ADMIN' or sessionScope.user.role eq 'LIBRARIAN'}">
                        <th><fmt:message key="orders.common.user.id"/></th>
                        <th><fmt:message key="orders.common.user.name"/></th>
                    </c:if>

                    <th><fmt:message key="orders.common.book.title"/></th>
                    <th><fmt:message key="orders.common.order.start.date"/></th>
                    <th><fmt:message key="orders.common.order.end.date"/></th>
                    <th><fmt:message key="orders.common.order.place"/></th>
                    <th><fmt:message key="orders.common.status"/></th>


                </tr>
                <c:forEach var="orders" items="${requestScope.ordersList}" varStatus="loop">

                    <c:choose>
                        <c:when test="${orders.orderStatus eq 'CANCELED'}">
                            <tr class="overdue-tr" style="background: #d72d2d;">
                        </c:when>
                        <c:when test="${orders.orderStatus eq 'ACCEPTED'}">
                            <tr class="returned-tr" style="background: #18a223;">
                        </c:when>
                        <c:otherwise>
                            <tr>
                        </c:otherwise>
                    </c:choose>

                    <tr>
                        <td> ${loop.count + (requestScope.page - 1) * requestScope.ordersPerPage} </td>
                        <td> ${orders.orderId} </td>
                        <c:if test="${sessionScope.user.role eq 'ADMIN' or sessionScope.user.role eq 'LIBRARIAN'}">
                            <td>${orders.userId}</td>
                            <td>${orders.userName}</td>
                        </c:if>
                        <td> ${orders.bookTitle} </td>

                        <td> ${custom:formatLocalDateTime(orders.orderStartDate,"dd MMM yyyy HH:mm", language)} </td>
                        <td> ${custom:formatLocalDateTime(orders.orderEndDate,"dd MMM yyyy HH:mm", language)} </td>


                        <c:choose>
                            <c:when test="${orders.onSubscription}">
                                <td><fmt:message key="orders.common.order.subscription"/></td>
                            </c:when>
                            <c:otherwise>
                                <td><fmt:message key="orders.common.order.in.reading.hall"/></td>
                            </c:otherwise>
                        </c:choose>


                        <c:choose>
                            <c:when test="${orders.orderStatus eq 'CANCELED'}">
                                <td><fmt:message key="orders.common.status.canceled"/></td>
                            </c:when>
                            <c:when test="${orders.orderStatus eq 'ACCEPTED'}">
                                <td><fmt:message key="orders.common.status.accepted"/></td>
                            </c:when>
                            <c:when test="${orders.orderStatus eq 'PROCESSING'}">
                                <td><fmt:message key="orders.common.status.processing"/></td>
                            </c:when>
                        </c:choose>

                        <c:if test="${sessionScope.user.role eq 'ADMIN' or sessionScope.user.role eq 'LIBRARIAN'}">

                            <c:choose>
                                <c:when test="${orders.orderStatus eq 'PROCESSING'}">
                                    <td class="accept-order">
                                        <div class="accept-order2">
                                            <a href="${pageContext.request.contextPath}/controller?command=lib-accept-order&order_id=${orders.orderId}">
                                                <fmt:message key="orders.common.action.accept"/>
                                            </a>
                                        </div>
                                    </td>
                                    <td class="decline-order">
                                        <div class="decline-order2">
                                            <a href="${pageContext.request.contextPath}/controller?command=lib-decline-order&order_id=${orders.orderId}" >
                                                <fmt:message key="orders.common.action.decline"/>
                                            </a>
                                        </div>
                                    </td>
                                </c:when>
                            </c:choose>


                        </c:if>
                    </tr>
                </c:forEach>
            </table>


        </div>
        <div class="pages_container">
            <%--For displaying Previous link except for the 1st page --%>
            <c:if test="${requestScope.page != 1}">
                <a href="controller?command=${command}&page=${requestScope.page - 1}">&laquo;</a>
            </c:if>
            <%--For displaying Page numbers.
                The when condition does not display a link for the current page--%>
            <c:forEach begin="1" end="${requestScope.totalPages}" var="i">
                <c:choose>
                    <c:when test="${requestScope.page eq i}">
                        <a href="controller?command=${command}&page=${i}"
                           class="active"> ${i}</a>
                    </c:when>
                    <c:otherwise>
                        <a href="controller?command=${command}&page=${i}">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <%--For displaying Next link --%>
            <c:if test="${requestScope.page lt requestScope.totalPages}">
                <a href="controller?command=${command}&page=${requestScope.page + 1}">&raquo;</a>
            </c:if>

        </div>

    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>
