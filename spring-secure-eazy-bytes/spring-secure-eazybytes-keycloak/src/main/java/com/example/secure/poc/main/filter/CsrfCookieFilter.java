package com.example.secure.poc.main.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CsrfCookieFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
//        Spring Security automatically puts the CsrfToken object into the request as
//        an attribute during request processing. Using the class name as the attribute key is a common convention
//        in Java to avoid hardcoding string literals and to ensure strong typing.
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        // Render the token value to a cookie by causing the deferred token to be loaded
        csrfToken.getToken(); // this will create the csrf token
        filterChain.doFilter(request, response);
    }
}
