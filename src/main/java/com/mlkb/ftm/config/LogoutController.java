package com.mlkb.ftm.config;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class LogoutController {
    @GetMapping("/logoutUser")
    public void logout(HttpServletResponse response, HttpServletRequest request) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        //TODO: When in production must do cookie.setSecure(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
