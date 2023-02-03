<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 31.01.2023
  Time: 19:35
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
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/order_book.css"/>
    <link rel="icon" href="${pageContext.request.contextPath}/static/img/icon/icon.ico">
</head>
<body>
<jsp:include page="/pages/header.jsp"/>

<div class="container">
    <div class="main-content">
        <input name="book_id" type="hidden" value="${sessionScope.book.bookId}">
        <div class="order-header"><fmt:message key="user.books.label.title"/></div>
        <div>
            <label><fmt:message key="admin.books.edit.form.label.book.title"/></label>
            <div class="book-data">${sessionScope.book.title}</div>

            <label><fmt:message key="user.books.label.author"/></label>
            <div class="book-data">${sessionScope.book.authorFirstName} ${sessionScope.book.authorSecondName}</div>

            <label><fmt:message key="user.books.label.genre"/></label>
            <div class="book-data">${sessionScope.book.genre}</div>


            <label><fmt:message key="user.books.label.publisher"/></label>
            <div class="book-data">${sessionScope.book.publisherTitle}</div>

            <label><fmt:message key="user.books.label.page.number"/></label>
            <div class="book-data">${sessionScope.book.pageNumber}</div>


            <label><fmt:message key="user.books.label.publication.date"/></label>
            <div class="book-data">${sessionScope.book.publicationDate}</div>

            <form action="${pageContext.request.contextPath}/controller?command=order-book&book_id=${sessionScope.book.bookId}"
                  method="post">
                <select name="subscription_type">
                    <option value="true">On subscription</option>
                    <option value="false">To the reading room</option>
                </select>
                <button type="submit" style="margin-top:20px; margin-left: 43%; padding: 10px 20px"><fmt:message
                        key="admin.books.edit.form.label.submit.btn"/></button>
            </form>

        </div>

    </div>
</div>

<jsp:include page="/pages/footer.jsp"/>
</body>
</html>
