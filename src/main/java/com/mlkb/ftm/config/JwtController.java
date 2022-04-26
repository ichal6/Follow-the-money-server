package com.mlkb.ftm.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        ResponseCookie responseCookie = ResponseCookie.from("e-mail", null)
                //TODO Active ONLY on PROD
                .sameSite("None")
                .secure(true) //TODO set to true on PROD
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    private void removeJwtCookie(HttpServletResponse response){
        ResponseCookie responseCookie = ResponseCookie.from("token", null)
                //TODO Active only on PROD
                .sameSite("None")
                .httpOnly(true)
                .secure(true) //TODO set to true on PROD
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    @GetMapping("/isLogin")
    public void isLogin(){

    }
}
