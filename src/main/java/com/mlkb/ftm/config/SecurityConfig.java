package com.mlkb.ftm.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mlkb.ftm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final RestAuthenticationSuccessHandler authenticationSuccessHandler;
    private final RestAuthenticationFailureHandler authenticationFailureHandler;
    private final String secret;
    private final ObjectMapper objectMapper;
    private final boolean isDebugMode;
    private final UserRepository userRepository;
    private final AuthenticationConfiguration authenticationConfiguration;

    public SecurityConfig(RestAuthenticationSuccessHandler authenticationSuccessHandler,
                          RestAuthenticationFailureHandler authenticationFailureHandler,
                          @Value("${jwt.secret}") String secret,
                          @Value("${spring.profiles.active:Unknown}") String profile,
                          ObjectMapper objectMapper,
                          UserRepository userRepository,
                          AuthenticationConfiguration authenticationConfiguration) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.secret = secret;
        this.isDebugMode = profile.equals("dev");
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.authenticationConfiguration = authenticationConfiguration;
    }


    @Bean
    public UserDetailsService userDetailsService() {
        return new PostgresUserDetailsService(this.userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
        return encoder;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.debug(isDebugMode);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        return http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth -> auth
                .requestMatchers(mvcMatcherBuilder.pattern("/")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/login")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/register")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/logoutUser")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/swagger-ui.html")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/v2/api-docs")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/webjars/**")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/swagger-resources/**")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/swagger-ui/**")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/v3/api-docs")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "/swagger-ui/**")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "/v3/api-docs/**")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.OPTIONS, "/api/**")).permitAll()
                .requestMatchers(mvcMatcherBuilder.pattern("/isLogin")).hasRole("USER")
                .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "/api/**")).hasRole("USER")
                .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.PUT, "/api/**")).hasRole("USER")
                .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.DELETE, "/api/**")).hasRole("USER")
                .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.POST, "/api/**")).hasRole("USER")
                .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.DELETE,"/**")).denyAll()
                .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET,"/**")).denyAll()
                .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.PUT,"/**")).denyAll()
                .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.POST,"/**")).denyAll()
                .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.OPTIONS,"/**")).permitAll()
                .anyRequest().authenticated())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilter(authenticationFilter())
                .addFilter(new JwtAuthorizationFilter(authenticationManager(this.authenticationConfiguration), userDetailsService(), secret, new JwtController()))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .build();
    }

    @Bean
    public JsonObjectAuthenticationFilter authenticationFilter() throws Exception {
        JsonObjectAuthenticationFilter filter = new JsonObjectAuthenticationFilter(objectMapper);
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        filter.setAuthenticationManager(authenticationManager(this.authenticationConfiguration));
        return filter;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
