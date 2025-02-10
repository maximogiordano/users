package com.maximogiordano.users.config;

import com.maximogiordano.users.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter filter) throws Exception {
        return http
                // JWT-based authentication is stateless and does not require CSRF protection.
                .csrf().disable()
                // Allow H2 console frames.
                .headers().frameOptions().disable().and()
                // Define authorization rules for different endpoints.
                .authorizeRequests()
                // Allow unauthenticated access to the "/sign-up" endpoint.
                .antMatchers("/sign-up", "/h2-console/**").permitAll()
                // Require authentication for all other requests.
                .anyRequest().authenticated().and()
                // Ensure that Spring Security will not create or use an HTTP session for authentication.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // Ensure that requests are checked for a valid JWT token before reaching the authentication filter.
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                // Build and return the security configuration.
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
