<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 29.01.2023
  Time: 16:43
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
    <title>Library Main</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/book_edit.css"/>
    <link rel="icon" href="${pageContext.request.contextPath}/static/img/icon/icon.ico">
</head>
<body>
<jsp:include page="/pages/header.jsp"/>

<div class="container">
    <div class="main-content">
        <c:if test="${not empty sessionScope.operation_type}">
            <p class="operation-title"><fmt:message key="admin.books.edit.form.title.update"/></p>
        </c:if>
        <c:if test="${not empty requestScope.operation_type }">
            <p class="operation-title"><fmt:message key="admin.books.edit.form.title.add"/></p>
        </c:if>

        <c:if test="${not empty sessionScope.successfully_updated}">
            <p class="success-msg"><fmt:message key="admin.books.edit.success.msg.updated"/></p><br>
        </c:if>
        <c:if test="${not empty sessionScope.successfully_added}">
            <p class="success-msg"><fmt:message key="admin.books.edit.success.msg.added"/></p><br>
        </c:if>
        <c:if test="${not empty sessionScope.invalid_book_data}">
            <p class="error-msg"><fmt:message key="admin.books.edit.error.msg.invalid.data"/></p><br>
        </c:if>
        <c:if test="${not empty sessionScope.book_exists}">
            <p class="error-msg"><fmt:message key="admin.books.edit.error.msg.already.exists"/></p>
        </c:if>


        <form action="controller" method="post">
            <c:choose>
                <c:when test="${not empty sessionScope.operation_type}">
                    <input name="command" type="hidden" value="update-book">
                </c:when>
                <c:otherwise>
                    <input name="command" type="hidden" value="add-book">
<%--                    <input name="command" type="hidden" value="add-book">--%>
                </c:otherwise>
            </c:choose>

            <input name="book_id" type="hidden" value="${sessionScope.book.bookId}">
            <div class="elem-group">
                <label class="book-edit-label" for="bookTitle"><fmt:message key="admin.books.edit.form.label.book.title"/></label>
                <input
                        type="text"
                        id="bookTitle"
                        name="book_title"
                        class="edit-book-input"
                        placeholder="<fmt:message key="admin.books.edit.form.placeholder.title"/>"
                        pattern="^['a-zA-Z?!,.??-????-??????0-9\s\-:]{1,350}$"
                        title="<fmt:message key="admin.books.edit.form.validation.msg.title"/>"
                        value="${sessionScope.book.title}"
                        required
                />
                <div class="error-msg-container"></div>
            </div>

            <div class="elem-group inlined">
                <label  class="book-edit-label" for="firstName"><fmt:message key="admin.books.edit.form.label.author.first.name"/></label>
                <input
                        type="text"
                        id="firstName"
                        class="edit-book-input"
                        name="first_name"
                        placeholder="<fmt:message key="admin.books.edit.form.placeholder.first.name"/>"
                        pattern="^['a-zA-Z???-????-??????]{1,50}$"
                        title="<fmt:message key="admin.books.edit.form.validation.msg.name"/>"
                        value="${sessionScope.book.authorFirstName}"
                        required
                />
                <div class="error-msg-container"></div>
            </div>
            <div class="elem-group inlined">
                <label class="book-edit-label" for="secondName"><fmt:message key="admin.books.edit.form.label.author.second.name"/></label>
                <input
                        type="text"
                        id="secondName"
                        class="edit-book-input"
                        name="second_name"
                        placeholder="<fmt:message key="admin.books.edit.form.placeholder.second.name"/>"
                        pattern="^['a-zA-Z???-????-??????]{1,50}$"
                        title="<fmt:message key="admin.books.edit.form.validation.msg.name"/>"
                        value="${sessionScope.book.authorSecondName}"
                        required
                />
                <div class="error-msg-container"></div>
            </div>
            <div class="elem-group inlined">
                <label  class="book-edit-label" for="genre-selection"><fmt:message key="admin.books.edit.form.label.select.genre"/></label>
                <select class="book-edit-select" id="genre-selection" name="genre" required>
                    <c:if test="${not empty sessionScope.book.genre }">
                        <option value="${sessionScope.book.genre}">${sessionScope.book.genre}</option>
                    </c:if>
                    <c:forEach var="genres" items="${sessionScope.genres_list}" varStatus="loop">
                        <option value="${genres.title}">${genres.title}</option>
                    </c:forEach>

                </select>
            </div>
            <div class="elem-group inlined">
                <label class="book-edit-label" for="publisher-selection"><fmt:message
                        key="admin.books.edit.form.label.select.publisher"/></label>
                <select class="book-edit-select" id="publisher-selection" name="publisher" required>
                    <c:if test="${not empty sessionScope.book.publisherTitle }">
                        <option value="${sessionScope.book.publisherTitle}">${sessionScope.book.publisherTitle}</option>
                    </c:if>
                    <c:forEach var="publishers" items="${sessionScope.publishers_list}" varStatus="loop">
                        <option value="${publishers.title}">${publishers.title}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="elem-group">
                <label class="book-edit-label" for="copies"><fmt:message key="admin.books.edit.form.label.copies"/></label>
                <input
                        type="number"
                        class="edit-book-input"
                        id="copies"
                        name="total_copies"
                        placeholder="2"
                        min="0"
                        value="${sessionScope.book.copies}"
                        required
                />
                <div class="error-msg-container"></div>
            </div>

            <hr/>
            <div class="elem-group inlined">
                <label class="book-edit-label" for="pages"><fmt:message key="admin.books.edit.form.label.pages"/></label>
                <input
                        class="edit-book-input"
                        type="number"
                        id="pages"
                        name="pages"
                        placeholder="100"
                        min="1"
                        value="${sessionScope.book.pageNumber}"
                        required
                />
                <div class="error-msg-container"></div>
            </div>

            <div class="elem-group inlined">
                <label class="book-edit-label" for="publication-date"><fmt:message key="admin.books.edit.form.label.publication.date"/></label>
                <input
                        class="edit-book-input"
                        type="date"
                        id="publication-date"
                        name="publication_date"
                        value="${sessionScope.book.publicationDate}"
                        required
                />
            </div>
            <button class="edit-book-button" type="submit" style="margin-left: 43%; padding: 10px 20px"><fmt:message
                    key="admin.books.edit.form.label.submit.btn"/></button>
        </form>
    </div>
</div>
<jsp:include page="/pages/footer.jsp"/>
</body>

</html>
