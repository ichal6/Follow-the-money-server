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
import org.springframework.http.ResponseCookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;

@Component
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final int expirationTime;
    private final String secret;
    private final Logger log;
    private final String activeProfile;


    public RestAuthenticationSuccessHandler(@Value("${jwt.expirationTime}") int expirationTime,
                                            @Value("${jwt.secret}") String secret,
                                            @Value("${spring.profiles.active:Unknown}") String activeProfile) {
        this.expirationTime = expirationTime;
        this.secret = secret;
        this.log = LoggerFactory.getLogger(CORSFilter.class);
        this.activeProfile = activeProfile;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
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

    private ResponseCookie.ResponseCookieBuilder getResponseCookieBuilder(String cookieName, String cookieValue) {
        return ResponseCookie
                .from(cookieName, cookieValue)
                .maxAge(expirationTime/1000);
    }

    private void addCookieWithEmail(String email, HttpServletResponse response){
        ResponseCookie.ResponseCookieBuilder responseCookieBuilder = getResponseCookieBuilder("e-mail", email);
            if(activeProfile.equals("prod")) {
                responseCookieBuilder
                        .sameSite("None")
                        .secure(true);
            } else {
                responseCookieBuilder
                        .secure(false);
            }
            ResponseCookie responseCookie = responseCookieBuilder.build();

        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    private void addCookieWithToken(HttpServletResponse response, String token){
        ResponseCookie.ResponseCookieBuilder responseCookieBuilder = getResponseCookieBuilder("token", token);
            if(activeProfile.equals("prod")) {
                responseCookieBuilder
                        .sameSite("None")
                        .secure(true);
            }
            ResponseCookie responseCookie = responseCookieBuilder
                    .httpOnly(true)
                    .secure(false)
                    .build();

        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    private void setHeader(HttpServletRequest request, HttpServletResponse response, String token){
        response.addHeader("Authorization", "Bearer " + token);

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");

    }
}
