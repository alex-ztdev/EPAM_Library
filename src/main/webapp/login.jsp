<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 20.01.2023
  Time: 19:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"  %>

<head>
  <meta charset="UTF-8" />
  <title>Library login</title>
  <link
          rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.1.3/css/bootstrap.min.css"
  />

  <link
          rel="stylesheet"
          href="https://fonts.googleapis.com/css?family=Muli"
  />
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/login.css">
  <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/login.js"></script>

</head>
<body>
<div class="container">
    <form class="form" id="login" method="POST" action="controller">
        <input name="command" type="hidden" value="login">
        <h1 class="form__title">Login</h1>
        <div class="form__message form__message--error">${requestScope.invalidLoginPassword}</div>
        <div class="form__input-group">
            <input id="loginUsername" name="login" type="text" class="form__input" autofocus placeholder="Username">
            <div class="form__input-error-message"></div>
        </div>
        <div class="form__input-group">
            <input id="loginPassword" name="password" type="password" class="form__input" autofocus placeholder="Password">
            <div class="form__input-error-message"></div>
        </div>
        <button class="form__button" type="submit" >Continue</button>
        <p class="form__text">
            <a href="#" class="form__link">Forgot your password?</a>
        </p>
        <p class="form__text">
            <a class="form__link" href="./" id="linkCreateAccount">Don't have an account? Create account</a>
        </p>
    </form>
    <form class="form form--hidden" id="createAccount">
        <h1 class="form__title">Create Account</h1>
        <div class="form__message form__message--error"></div>
        <div class="form__input-group">
            <input type="text" id="signupUsername" name="login" class="form__input" autofocus placeholder="Username">
            <div class="form__input-error-message"></div>
        </div>
        <div class="form__input-group">
            <input type="text" class="form__input" name="email" autofocus placeholder="Email Address">
            <div class="form__input-error-message"></div>
        </div>
        <div class="form__input-group">
            <input type="password" class="form__input" name="password" autofocus placeholder="Password">
            <div class="form__input-error-message"></div>
        </div>
        <div class="form__input-group">
            <input type="password" class="form__input" autofocus placeholder="Confirm password">
            <div class="form__input-error-message"></div>
        </div>
        <button class="form__button" type="submit" >Continue</button>
        <p class="form__text">
            <a class="form__link" href="./" id="linkLogin">Already have an account? Sign in</a>
        </p>
    </form>
</div>
</body>
</html>