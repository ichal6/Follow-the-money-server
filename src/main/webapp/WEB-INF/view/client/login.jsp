<%--
  Created by IntelliJ IDEA.
  User: michael
  Date: 8/13/20
  Time: 2:36 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Follow the money login</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/client/menu.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/client/login.css">

    </head>
    <body>
        <header>
            <div class="menu-wrapper">
                <div class="menu-header">
                    <a href="#home" class="logo"><img src="${pageContext.request.contextPath}/assets/icons/logo.svg"></a>
                    <input class="menu-btn" type="checkbox" id="menu-btn" />
                    <label class="menu-icon" for="menu-btn"><span class="navicon"></span></label>
                    <ul class="menu">
                        <li><a href="#about">About</a></li>
                        <li><a href="#contact">Blog</a></li>
                        <li><a href="#contact">FAQ</a></li>
                    </ul>
                </div>
            </div>
            <img id="background-img" src="${pageContext.request.contextPath}/assets/img/login-background.svg">
            <h1 id="login-label">Log in:</h1>
            <div id="login-label-tablet">
                <h1>We’re glad to see you back!</h1>
                <p>Making saving a habit is a key to succeed in controlling your money.</p>
                <p>You don’t have an account?</p>
                <button class="button" id="button-sign-up-tablet" onclick="location.href='#'">Sign up. It's free</button>
            </div>

        </header>

        <form id="login-form">
            <h1 id="login-header-tablet">Log in:</h1>
            <div class="login-input">
                <img src="${pageContext.request.contextPath}/assets/icons/login-email.svg">
                <input placeholder="Your email">
            </div>
            <div class="login-input">
                <img src="${pageContext.request.contextPath}/assets/icons/login-password.svg">
                <input placeholder="Your password">
            </div>

            <a id="forgot-password" href="#forgot-password">forgot your password?</a>
            <button class="button" id="button-login" type="submit">Ready to go!</button>
            <button class="button" id="button-sign-up" onclick="location.href='#'">No account? Sign up!</button>
        </form>
    </body>
</html>
