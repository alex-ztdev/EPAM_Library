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

<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="locale"/>


<html lang="${sessionScope.language}">
<head>
  <meta charset="UTF-8"/>
  <title>Library Main</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/book_edit.css"/>

</head>
<body>
<jsp:include page="/pages/header.jsp"/>

<div class="container">
  <div class="main-content">
    <p class="operation-title">Update Book</p>
    <form action="controller" method="post">
      <input name="command" type="hidden" value="update-book">
      <div class="elem-group">
        <label for="bookTitle">Book title</label>
        <input
                type="text"
                id="bookTitle"
                name="book_title"
                placeholder="book_title_fmt"
                pattern="^['a-zA-Z?!,.а-яА-ЯёЁ0-9\s]{1,350}$"
                title="Title should contain no more than 350 letters, digits or special symbols like '!,."
                value=""
                required
        />
        <div class="error-msg-container"></div>
      </div>

      <div class="elem-group inlined">
        <label for="firstName">Author's first name</label>
        <input
                type="text"
                id="firstName"
                name="first_name"
                placeholder="John"
                pattern="^['a-zA-Z?а-яА-ЯёЁ]{1,50}$"
                title="Name should contain no more than 50 letters or special symbols like '"
                value=""
                required
        />
        <div class="error-msg-container"></div>
      </div>
      <div class="elem-group inlined">
        <label for="secondName">Author's second name</label>
        <input
                type="text"
                id="secondName"
                name="second_name"
                placeholder="Smith"
                pattern="^['a-zA-Z?а-яА-ЯёЁ]{1,50}$"
                title="Name should contain no more than 50 letters or special symbols like '"
                value=""
                required
        />
        <div class="error-msg-container"></div>
      </div>
      <div class="elem-group inlined">
        <label for="genre-selection">Select genre</label>
        <select id="genre-selection" name="genre_preference" required>
          <option value="">Choose a Genre from the List</option>
          <option value="connecting">Connecting</option>
        </select>
      </div>
      <div class="elem-group inlined">
        <label for="publisher-selection">Select Publisher</label>
        <select id="publisher-selection" name="publisher" required>
          <option value="">Choose a Publisher from the List</option>
          <option value="connecting">Connecting</option>
        </select>
      </div>
      <div class="elem-group">
        <label for="copies">Copies</label>
        <input
                type="number"
                id="copies"
                name="total_copies"
                placeholder="2"
                min="1"
                value=""
                required
        />
        <div class="error-msg-container"></div>
      </div>

      <hr />
      <div class="elem-group inlined">
        <label for="pages">Page Number</label>
        <input
                type="number"
                id="pages"
                name="pages"
                placeholder="100"
                min="1"
                required
        />
        <div class="error-msg-container"></div>
      </div>

      <div class="elem-group inlined">
        <label for="publication-date">Publication Date</label>
        <input
                type="date"
                id="publication-date"
                name="publication"
                required
        />
      </div>
      <button type="submit">Book The Rooms</button>
    </form>
  </div>
</div>
  <jsp:include page="/pages/footer.jsp"/>
</body>

</html>
