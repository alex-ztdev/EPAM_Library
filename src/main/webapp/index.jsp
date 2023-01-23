<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="locale"/>

<html lang="${sessionScope.language}">
<html>
<head>
    <meta charset="UTF-8" />
    <title>Library Main</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/index.css" />
</head>
<body>
<div class="container">
    <div class="header">
        <div class="left-header">
            <a class="active" href="#home">Home</a>
            <a href="#books">Books</a>
            <a href="#authors">Authors</a>
            <a href="#orders">Orders</a>
            <a href="#librarians">Librarians</a>
            <a href="#users">Users</a>
        </div>

        <div class="right-header">
            <div class="search-container">
                <form action="#search_action">
                    <input type="text" placeholder="Search.." name="search" />
                    <button type="submit"><i class="fa fa-search"></i></button>
                </form>
            </div>

            <div class="languages">
                <div class="switch">
                    <input
                            id="language-toggle"
                            class="check-toggle check-toggle-round-flat"
                            type="checkbox"
                    />
                    <label for="language-toggle"></label>
                    <span class="on">UA</span>
                    <span class="off">EN</span>
                </div>
            </div>
            <a href="#profile">Profile</a>
            <a href="#logout">Logout</a>
        </div>
    </div>

    <div class="main-content"></div>

    <div class="footer">
        <div class="footer-elements">
            <a href="#about_page">About</a>
            <a href="https://github.com/alex-ztdev">GitHub</a>
        </div>
    </div>
</div>
</body>
<script
        src="https://kit.fontawesome.com/d117408745.js"
        crossorigin="anonymous"
></script>
</html>
