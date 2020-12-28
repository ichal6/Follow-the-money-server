package com.mlkb.ftm.config;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class JwtController {

    @PostMapping("/login")
    public void login(@RequestBody LoginCredentials credentials) {
    }

    @GetMapping("/logoutUser")
    public void logout(HttpServletResponse response) {
        removeJwtCookie(response);
        removeEmailCookie(response);

        SecurityContextHolder.getContext().setAuthentication(null);
    }

    private void removeEmailCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("e-mail", null);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    private void removeJwtCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @GetMapping("/isLogin")
    public void isLogin(){

    }
}
