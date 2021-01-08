package com.mlkb.ftm.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.Collection;

@Component
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final int expirationTime;
    private final String secret;
    private final Logger log;


    public RestAuthenticationSuccessHandler(@Value("${jwt.expirationTime}") int expirationTime,
                                            @Value("${jwt.secret}") String secret) {
        this.expirationTime = expirationTime;
        this.secret = secret;
        this.log = LoggerFactory.getLogger(CORSFilter.class);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        UserDetails principal = (UserDetails) authentication.getPrincipal(); // 1
        long actualTime = System.currentTimeMillis();
        String token = JWT.create() // 2
                .withSubject(principal.getUsername()) //
                .withIssuedAt(new Date(actualTime))
                .withExpiresAt(new Date(actualTime + expirationTime)) // 4
                .sign(Algorithm.HMAC256(secret)); // 5

        setHeader(request, response, token);
        addCookieWithToken(response, token);
        addCookieWithEmail(principal.getUsername(), response);
        log.info("Token has created successfully");
    }

    private void addCookieWithEmail(String email, HttpServletResponse response){
        Cookie cookieWithEmail = new Cookie("e-mail", email);
//        cookieWithEmail.setHttpOnly(true);
        //TODO: When in production must do cookieWithEmail.setSecure(true);
        cookieWithEmail.setMaxAge(expirationTime/1000);
        response.addCookie(cookieWithEmail);
    }

    private void addCookieWithToken(HttpServletResponse response, String token){
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        //TODO: When in production must do cookie.setSecure(true);
        cookie.setMaxAge(expirationTime/1000);
        response.addCookie(cookie);
    }

    private void setHeader(HttpServletRequest request, HttpServletResponse response, String token){
        response.addHeader("Authorization", "Bearer " + token);
//        response.addHeader("Access-Control-Allow-Origin", "*");

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");

    }
}
