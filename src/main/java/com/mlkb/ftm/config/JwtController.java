package com.mlkb.ftm.config;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.profiles.active:Unknown}")
    private String activeProfile;

    @PostMapping("/login")
    public void login(@RequestBody LoginCredentials credentials) {
    }

    @GetMapping("/logoutUser")
    public void logout(HttpServletResponse response) {
        removeJwtCookie(response);
        removeEmailCookie(response);
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    private ResponseCookie.ResponseCookieBuilder getResponseCookieBuilder(String cookieName) {
        return ResponseCookie
                .from(cookieName, null)
                .maxAge(0);
    }

    private void removeEmailCookie(HttpServletResponse response){
        ResponseCookie.ResponseCookieBuilder responseCookieBuilder = getResponseCookieBuilder("e-mail");
                if(activeProfile.equals("prod")) {
                    responseCookieBuilder
                            .sameSite("None")
                            .secure(true);
                } else{
                    responseCookieBuilder
                            .secure(false);
                }
        ResponseCookie responseCookie = responseCookieBuilder.build();

        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    private void removeJwtCookie(HttpServletResponse response){
        ResponseCookie responseCookie = ResponseCookie.from("token", null)
                //TODO Active on PROD .sameSite("None")
                .httpOnly(true)
                .secure(false)
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    @GetMapping("/isLogin")
    public void isLogin(){

    }
}
