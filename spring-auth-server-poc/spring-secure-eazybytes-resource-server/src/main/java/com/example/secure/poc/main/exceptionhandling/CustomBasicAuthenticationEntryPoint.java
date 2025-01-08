package com.example.secure.poc.main.exceptionhandling;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {
        response.setHeader("eazybank-error-reason", "Authentication Failed");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String path = request.getRequestURI();
        String message = ( ex != null && ex.getMessage() != null ) ? ex.getMessage() : "Unauthorized";

        Map<String, Object> errResponse = new LinkedHashMap<>();
        errResponse.put("timestamp", LocalDateTime.now().toString());
        errResponse.put("status",  HttpStatus.UNAUTHORIZED.value());
        errResponse.put("error",  HttpStatus.UNAUTHORIZED.getReasonPhrase());
        errResponse.put("message", message);
        errResponse.put("path", path);

        ObjectMapper jsonMapper = new ObjectMapper();
        String errRes = jsonMapper.writeValueAsString(errResponse);

        response.getWriter().write(errRes);
    }
}
