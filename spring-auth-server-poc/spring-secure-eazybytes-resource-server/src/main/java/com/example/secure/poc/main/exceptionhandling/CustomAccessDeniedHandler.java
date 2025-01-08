package com.example.secure.poc.main.exceptionhandling;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException, ServletException {
        response.setHeader("eazybank-denied-reason", "Authentication Failed");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String path = request.getRequestURI();
        String message = ( ex != null && ex.getMessage() != null ) ? ex.getMessage() : "Unauthorized";

        Map<String, Object> errResponse = new LinkedHashMap<>();
        errResponse.put("timestamp", LocalDateTime.now().toString());
        errResponse.put("status",  HttpStatus.FORBIDDEN.value());
        errResponse.put("error",  HttpStatus.FORBIDDEN.getReasonPhrase());
        errResponse.put("message", "Custom: " + message);
        errResponse.put("path", path);

        ObjectMapper jsonMapper = new ObjectMapper();
        String errRes = jsonMapper.writeValueAsString(errResponse);
        response.getWriter().write(errRes);
    }
}
