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

</head>
<body>
<jsp:include page="/pages/header.jsp"/>

<div class="container">
  <div class="main-content">
    <p class="operation-title"><fmt:message key="admin.books.edit.form.title"/></p>
    <form action="controller" method="post">
      <input name="command" type="hidden" value="update-book">
      <input name="book_id" type="hidden" value="${requestScope.book.bookId}">
      <div class="elem-group">
        <label for="bookTitle"><fmt:message key="admin.books.edit.form.label.book.title"/></label>
        <input
                type="text"
                id="bookTitle"
                name="book_title"
                placeholder="<fmt:message key="admin.books.edit.form.placeholder.title"/>"
                pattern="^['a-zA-Z?!,.а-яА-ЯёЁ0-9\s-]{1,350}$"
                title="<fmt:message key="admin.books.edit.form.validation.msg.title"/>"
                value="${requestScope.book.title}"
                required
        />
        <div class="error-msg-container"></div>
      </div>

      <div class="elem-group inlined">
        <label for="firstName"><fmt:message key="admin.books.edit.form.label.author.first.name"/></label>
        <input
                type="text"
                id="firstName"
                name="first_name"
                placeholder="<fmt:message key="admin.books.edit.form.placeholder.first.name"/>"
                pattern="^['a-zA-Z?а-яА-ЯёЁ]{1,50}$"
                title="<fmt:message key="admin.books.edit.form.validation.msg.name"/>"
                value="${requestScope.book.authorFirstName}"
                required
        />
        <div class="error-msg-container"></div>
      </div>
      <div class="elem-group inlined">
        <label for="secondName"><fmt:message key="admin.books.edit.form.label.author.second.name"/></label>
        <input
                type="text"
                id="secondName"
                name="second_name"
                placeholder="<fmt:message key="admin.books.edit.form.placeholder.second.name"/>"
                pattern="^['a-zA-Z?а-яА-ЯёЁ]{1,50}$"
                title="<fmt:message key="admin.books.edit.form.validation.msg.name"/>"
                value="${requestScope.book.authorSecondName}"
                required
        />
        <div class="error-msg-container"></div>
      </div>
      <div class="elem-group inlined">
        <label for="genre-selection"><fmt:message key="admin.books.edit.form.label.select.genre"/></label>
        <select id="genre-selection" name="genre" required>
          <c:if test="${not empty requestScope.book.genre }">
            <option value="${requestScope.book.genre}">${requestScope.book.genre}</option>
          </c:if>
          <c:forEach var="genres" items="${requestScope.genres_list}" varStatus="loop">
            <option value="${genres.title}">${genres.title}</option>
          </c:forEach>

        </select>
      </div>
      <div class="elem-group inlined">
        <label for="publisher-selection"><fmt:message key="admin.books.edit.form.label.select.publisher"/></label>
        <select id="publisher-selection" name="publisher" required>
            <c:if test="${not empty requestScope.book.publisherTitle }">
                <option value="${requestScope.book.publisherTitle}">${requestScope.book.publisherTitle}</option>
            </c:if>
            <c:forEach var="publishers" items="${requestScope.publishers_list}" varStatus="loop">
                <option value="${publishers.title}">${publishers.title}</option>
            </c:forEach>
        </select>
      </div>
      <div class="elem-group">
        <label for="copies"><fmt:message key="admin.books.edit.form.label.copies"/></label>
        <input
                type="number"
                id="copies"
                name="total_copies"
                placeholder="2"
                min="0"
                value="${requestScope.book.copies}"
                required
        />
        <div class="error-msg-container"></div>
      </div>

      <hr />
      <div class="elem-group inlined">
        <label for="pages"><fmt:message key="admin.books.edit.form.label.pages"/></label>
        <input
                type="number"
                id="pages"
                name="pages"
                placeholder="100"
                min="1"
                value="${requestScope.book.pageNumber}"
                required
        />
        <div class="error-msg-container"></div>
      </div>

      <div class="elem-group inlined">
        <label for="publication-date"><fmt:message key="admin.books.edit.form.label.publication.date"/></label>
        <input
                type="date"
                id="publication-date"
                name="publication_date"
                value="${requestScope.book.publicationDate}"
                required
        />
      </div>
      <button type="submit"><fmt:message key="admin.books.edit.form.label.submit.btn"/></button>
    </form>
  </div>
</div>
  <jsp:include page="/pages/footer.jsp"/>
</body>

</html>