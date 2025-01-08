package com.example.secure.poc.main.filter;

import com.example.secure.poc.main.utils.AppConstants;
import com.example.secure.poc.main.utils.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                                                                                    throws ServletException, IOException {
        String secretKey = getEnvironment().getProperty(AppConstants.JWT_SECRET_KEY, AppConstants.JWT_SECRET_VALUE_DEFAULT);
        String jwt = request.getHeader(AppConstants.JWT_HEADER);
        if(StringUtils.hasText(jwt)){
            Optional<Authentication> auth = JWTUtil.decodeToAuthenticatedToken(jwt, secretKey);
            if(auth.isPresent()){
                SecurityContextHolder.getContext().setAuthentication(auth.get());
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String servletPath = request.getServletPath();
        return servletPath.equals("/user") || servletPath.equals("/contact") || servletPath.equals("/register");
    }
}
