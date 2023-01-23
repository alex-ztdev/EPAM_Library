<%--
  Created by IntelliJ IDEA.
  User: Alex
  Date: 20.01.2023
  Time: 19:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : 'en'}"
       scope="session"/>

<fmt:setLocale value="${language}"/>
<fmt:setBundle basename="locale"/>

<html lang="${language}">
<head>
    <meta charset="UTF-8"/>
    <title>Library login</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/login.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/login.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/config.js"></script>
</head>
<body>
<div class="container">
    <form class="form" id="login" method="POST" action="controller">
        <input name="command" type="hidden" value="login">
        <h1 class="form__title"><fmt:message key="loginForm.login"/></h1>
        <div class="form__message form__message--error">
            <c:if test="${not empty requestScope.invalidLoginPassword}">
                <fmt:message key="loginForm.msg.loginError"/>
            </c:if>
            <c:if test="${not empty requestScope.isBlocked}">
                <fmt:message key="loginForm.msg.userIsBlocked"/>
            </c:if>
        </div>
        <div class="form__input-group">
            <input id="loginUsername" name="login" type="text" class="form__input" autofocus placeholder="<fmt:message key="commonForm.username"/>">
            <div class="form__input-error-message"></div>
        </div>
        <div class="form__input-group">
            <input id="loginPassword" name="password" type="password" class="form__input" autofocus
                   placeholder="<fmt:message key="commonForm.password"/>">
            <div class="form__input-error-message"></div>
        </div>
        <button class="form__button" type="submit"><fmt:message key="loginForm.btn.continue"/></button>
        <%--        <p class="form__text">--%>
        <%--            <a href="#" class="form__link">Forgot your password?</a>--%>
        <%--        </p>--%>
        <p class="form__text">
            <a class="form__link" href="./" id="linkCreateAccount"><fmt:message key="loginForm.btn.createAccount"/></a>
        </p>
    </form>
    <form class="form form--hidden" id="createAccount"  action="controller" method="post">
        <input name="command" type="hidden" value="registration">
        <h1 class="form__title">
            <fmt:message key="registrationForm.registration"/>
        </h1>
        <div class="form__message form__message--error"></div>
        <div class="form__input-group">
            <input type="text" id="signupUsername" name="login" class="form__input" autofocus placeholder="<fmt:message key="commonForm.username"/>">
            <div class="form__input-error-message"></div>
        </div>
        <div class="form__input-group">
            <input type="text" class="form__input" name="email" autofocus placeholder="<fmt:message key="registrationForm.email"/>">
            <div class="form__input-error-message"></div>
        </div>
        <div class="form__input-group">
            <input type="password" class="form__input" name="password" autofocus placeholder="<fmt:message key="commonForm.password"/>">
            <div class="form__input-error-message"></div>
        </div>
        <div class="form__input-group">
            <input type="password" class="form__input" autofocus placeholder="<fmt:message key="registrationForm.confirmPassword"/>">
            <div class="form__input-error-message"></div>
        </div>
        <div class="form__input-group">
            <input type="text" class="form__input" autofocus placeholder="<fmt:message key="registrationForm.phone"/>">
            <div class="form__input-error-message"></div>
        </div>
        <button class="form__button" type="submit"><fmt:message key="registrationForm.btn.create"/></button>
        <p class="form__text">
            <a class="form__link" href="./" id="linkLogin"><fmt:message key="registrationForm.btn.signIn"/></a>
        </p>
    </form>
    <div id="registration_flag" hidden>${requestScope.regForm}</div>
</div>
</body>
</html>