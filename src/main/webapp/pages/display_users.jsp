<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 02.02.2023
  Time: 12:31
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

<c:set var="command" scope="request"
       value="${sessionScope.user.role eq 'ADMIN' ? 'display-users' : 'display-readers'}"/>


<html lang="${language}">
<head>
    <meta charset="UTF-8"/>
    <title>Users</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/users.css"/>
    <script src="https://kit.fontawesome.com/e83e8567ce.js" crossorigin="anonymous"></script>
    <link rel="icon" href="${pageContext.request.contextPath}/static/img/icon/icon.ico">
</head>
<body>
<jsp:include page="header.jsp"/>

<div class="container">
    <div class="main-content">
        <div class="top-row">
            <c:choose>
                <c:when test="${sessionScope.user.role eq 'ADMIN'}">
                    <p class="users-list-title"><fmt:message key="admin.user.users.title"/></p>
                </c:when>
                <c:otherwise>
                    <p class="users-list-title" style="text-align: center"><fmt:message key="admin.user.reader.title"/></p>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="users_list_container">
            <table class="users_table">

                <tr>
                    <th><fmt:message key="common.label.counter"/></th>

                    <th><fmt:message key="admin.users.user.id"/></th>
                    <c:if test="${sessionScope.user.role eq 'ADMIN'}">
                        <th><fmt:message key="admin.users.user.login"/></th>

                    </c:if>
                    <th><fmt:message key="admin.users.user.name"/></th>
                    <th><fmt:message key="admin.users.user.email"/></th>
                    <th><fmt:message key="admin.users.user.phone"/></th>


                    <c:if test="${sessionScope.user.role eq 'ADMIN'}">
                        <th><fmt:message key="admin.users.user.role"/></th>
                        <th  style="min-width: 150px;"><fmt:message key="admin.users.user.status"/></th>
                        <th style="min-width: 230px;"><fmt:message key="admin.users.user.change.role"/></th>
                    </c:if>

                    <c:if test="${sessionScope.user.role eq 'LIBRARIAN'}">
                        <th><fmt:message key="admin.users.user.status"/></th>
                    </c:if>
                </tr>
                <c:forEach var="users" items="${requestScope.users_list}" varStatus="loop">
                    <tr <c:if test="${users.status eq 'BLOCKED'}">style="background: #d72d2d;"</c:if>>

                        <td> ${loop.count + (requestScope.page - 1) * requestScope.usersPerPage} </td>
                        <td> ${users.userId} </td>

                        <c:if test="${sessionScope.user.role eq 'ADMIN'}">
                            <td> ${users.login}</td>
                        </c:if>

                        <td> ${users.firstName} ${users.secondName}</td>
                        <td> ${users.email} </td>
                        <td> ${users.phoneNumber} </td>

                        <c:if test="${sessionScope.user.role eq 'LIBRARIAN'}">
                            <c:choose>
                                <c:when test="${users.status eq 'BLOCKED'}">
                                    <td><fmt:message key="admin.user.msg.banned"/></td>
                                </c:when>
                                <c:otherwise>
                                    <td><fmt:message key="admin.user.msg.not.banned"/></td>
                                </c:otherwise>
                            </c:choose>

                        </c:if>

                        <c:if test="${sessionScope.user.role eq 'ADMIN'}">
                            <td> ${users.role} </td>
                            
                            <c:choose>
                                <c:when test="${users.role ne 'ADMIN'}">
                                    <c:choose>
                                        <c:when test="${users.status eq 'BLOCKED'}">
                                            <td class="ban-user">
                                                <style>
                                                    .ban-user:hover a p.label:after {
                                                        content: '<fmt:message key="admin.users.action.unban"/>';
                                                    }

                                                    .ban-user:hover a p.label span {
                                                        display: none;
                                                    }

                                                </style>
                                                <div class="removed-order-div">
                                                    <a class="remove-link"
                                                       href="controller?command=unblock-user&user_id=${users.userId}">
                                                        <p class="label"><span class="align"><fmt:message
                                                                key="admin.user.msg.banned"/></span></p>
                                                    </a>
                                                </div>
                                            </td>
                                        </c:when>
                                        <c:otherwise>
                                            <td class="unban-user">
                                                <style>
                                                    .unban-user:hover a p.label:after {
                                                        content: '<fmt:message key="admin.users.action.ban"/>';
                                                    }

                                                    .unban-user:hover a p.label span {
                                                        display: none;
                                                    }
                                                </style>
                                                <div class="removed-book-div">
                                                    <a class="remove-link"
                                                       href="controller?command=block-user&user_id=${users.userId}">
                                                        <p class="label"><span class="align">
                                                            <fmt:message key="admin.user.msg.not.banned"/></span></p>
                                                    </a>
                                                </div>
                                            </td>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${users.status eq 'BLOCKED'}">
                                            <td><fmt:message key="admin.user.msg.banned"/></td>
                                        </c:when>
                                        <c:otherwise>
                                            <td><fmt:message key="admin.user.msg.not.banned"/></td>
                                        </c:otherwise>
                                    </c:choose>
                                </c:otherwise>
                            </c:choose>


                            <c:choose>
                                <c:when test="${users.role ne 'ADMIN' and users.status ne 'BLOCKED'}">
                                    <c:choose>
                                        <c:when test="${users.role eq 'USER'}">
                                            <td class="change-role-to-user">
                                                <style>
                                                    .change-role-to-user:hover a p.label:after {
                                                        content: '<fmt:message key="admin.users.action.lib"/>';
                                                    }
                                                    .change-role-to-user:hover a p.label span {
                                                        display: none;
                                                    }
                                                </style>
                                                <div class="removed-book-div">
                                                    <a class="remove-link"
                                                       href="controller?command=change-user-role&role=librarian&user_id=${users.userId}">
                                                        <p class="label"><span class="align"><fmt:message
                                                                key="admin.users.msg.user"/></span></p>
                                                    </a>
                                                </div>
                                            </td>
                                        </c:when>
                                        <c:otherwise>
                                            <td class="change-role-to-lib">
                                                <style>
                                                    .change-role-to-lib:hover a p.label:after {
                                                        content: '<fmt:message key="admin.users.action.user"/>';
                                                    }

                                                    .change-role-to-lib:hover a p.label span {
                                                        display: none;
                                                    }
                                                </style>
                                                <div class="removed-book-div">
                                                    <a class="remove-link"
                                                       href="controller?command=change-user-role&role=user&user_id=${users.userId}">
                                                        <p class="label"><span class="align" style="text-align: center;">
                                                            <fmt:message key="admin.users.msg.librarian"/></span></p>
                                                    </a>
                                                </div>
                                            </td>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <c:choose>
                                        <c:when test="${users.role ne 'ADMIN'}">
                                            <td><fmt:message key="admin.user.msg.unban.first"/></td>
                                        </c:when>
                                        <c:otherwise>
                                           <td><fmt:message key="admin.user.msg.unsupported"/></td>
                                        </c:otherwise>
                                    </c:choose>
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
