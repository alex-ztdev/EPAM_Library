<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 23.01.2023
  Time: 21:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<c:set var="language" value="${not empty param.language ? param.language : not empty sessionScope.lo ? language : 'en'}"
       scope="session"/>

<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="locale"/>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/header.css"/>

<div class="header">
    <div class="left-header">
        <a class="active" href="${pageContext.request.contextPath}/index.jsp"><fmt:message key="header.common.home"/></a>
        <a href="controller?command=profile"><fmt:message key="header.common.books"/></a>
        <a href="controller?command=authors"><fmt:message key="header.common.authors"/></a>
        <a href="controller?command=user_orders"><fmt:message key="header.user.order"/></a>
        <a href="controller?command=librarians_list">Librarians</a>
        <a href="#users">Users</a>
    </div>

    <div class="right-header">
        <div class="search-container">
            <form action="#search_action">
                <input type="text" placeholder="Search.." name="search"/>
                <button type="submit"><i class="fa fa-search"></i></button>
            </form>
        </div>


        <div class="languages">
            <form method="POST" action="controller">
                <input type="hidden" name="command" value="change_language"/>
                <button class="language_button" type="submit" name="language"
                        value="ua"><fmt:message key="header.language.ua"/>
                </button>
                <button class="language_button" type="submit" name="language"
                        value="en"><fmt:message key="header.language.en"/>
                </button>
            </form>
        </div>

        <a href="#profile">Profile</a>
        <a href="#logout">Logout</a>
    </div>
</div>
