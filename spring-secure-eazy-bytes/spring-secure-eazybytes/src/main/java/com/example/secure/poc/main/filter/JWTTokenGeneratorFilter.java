package com.example.secure.poc.main.filter;

import com.example.secure.poc.main.utils.AppConstants;
import com.example.secure.poc.main.utils.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * To be called after BasicAuthentication is done, to set the JWT Header
 */
public class JWTTokenGeneratorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(null != authentication){
            String secretKey = getEnvironment().getProperty(AppConstants.JWT_SECRET_KEY, AppConstants.JWT_SECRET_VALUE_DEFAULT);
            String userName = authentication.getName();
            String jwt = JWTUtil.encode(userName, authentication.getAuthorities(), secretKey);
            response.setHeader(AppConstants.JWT_HEADER, jwt);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String servletPath = request.getServletPath();
        return !servletPath.equals("/user");
    }
}
