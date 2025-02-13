package com.maximogiordano.users.filter;

import com.maximogiordano.users.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {
    @InjectMocks
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    JwtUtils jwtUtils;

    @Mock
    HttpServletRequest request; // internal use

    @Mock
    HttpServletResponse response; // internal use

    @Mock
    FilterChain filterChain; // internal use

    @Mock
    SecurityContext securityContext; // internal use

    @Test
    void testNullHeader() throws ServletException, IOException {
        // given a request without an authorization header
        when(request.getHeader("Authorization")).thenReturn(null);

        // when the "doFilterInternal" method is called
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then the "getHeader" method is called
        verify(request).getHeader("Authorization");

        // and the chain continues its flow without setting any credentials
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testNonBearerHeader() throws ServletException, IOException {
        // given a request without an authorization header
        when(request.getHeader("Authorization")).thenReturn("Basic dXNlcjpwYXNz");

        // when the "doFilterInternal" method is called
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then the "getHeader" method is called
        verify(request).getHeader("Authorization");

        // and the chain continues its flow without setting any credentials
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testInvalidToken() throws ServletException, IOException {
        // given an invalid token
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXhpbXVzNjEwQGhvdG1haWwuY29tIiwiaWF0IjoxNzM5MjgzNzcwLCJleHAi" +
                "OjE3MzkyODM3ODB9.eQE9wOdAKiGvfipkRtOhgzapDDzEMzaW0DhLafpwx48";

        when(jwtUtils.validateToken(token)).thenReturn(false);

        // and a request with an authorization header that contains the given token
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        // when the "doFilterInternal" method is called
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then the "getHeader" method is called
        verify(request).getHeader("Authorization");

        // and the token is validated
        verify(jwtUtils).validateToken(token);

        // and the chain continues its flow without setting any credentials
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testValidToken() throws ServletException, IOException {
        try (MockedStatic<SecurityContextHolder> mockedStatic = Mockito.mockStatic(SecurityContextHolder.class)) {
            // given a valid token
            String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXhpbXVzNjEwQGhvdG1haWwuY29tIiwiaWF0IjoxNzM5MjgzNzcwLCJl" +
                    "eHAiOjE3MzkyODM3ODB9.eQE9wOdAKiGvfipkRtOhgzapDDzEMzaW0DhLafpwx48";

            when(jwtUtils.validateToken(token)).thenReturn(true);

            // and a request with an authorization header that contains the given token
            when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

            // and the username that can be extracted from the given token
            String username = "john.doe@email.com";

            when(jwtUtils.extractUsername(token)).thenReturn(username);

            // and the authentication token that will be set
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null,
                    List.of());

            // and the security context
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            // when the "doFilterInternal" method is called
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // then the "getHeader" method is called
            verify(request).getHeader("Authorization");

            // and the token is validated
            verify(jwtUtils).validateToken(token);

            // and the username is extracted
            verify(jwtUtils).extractUsername(token);

            // and the security context is retrieved
            mockedStatic.verify(SecurityContextHolder::getContext);

            // and the authentication token is set
            verify(securityContext).setAuthentication(authentication);

            // and the chain continues its flow after setting the credentials
            verify(filterChain).doFilter(request, response);
        }
    }
}
