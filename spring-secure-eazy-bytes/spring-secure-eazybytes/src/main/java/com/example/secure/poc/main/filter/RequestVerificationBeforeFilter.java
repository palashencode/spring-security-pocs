package com.example.secure.poc.main.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RequestVerificationBeforeFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String authorization = req.getHeader(HttpHeaders.AUTHORIZATION);
        try {
            if(StringUtils.hasText(authorization)){
                if(authorization.trim().startsWith("Basic ")){
                    String email = authorization.trim().substring(6);
                    email = new String(Base64.getDecoder().decode(email.getBytes(StandardCharsets.UTF_8))).split(":")[0];
                    if(email.toLowerCase().contains("test")){
                        res.setStatus(542); // you can send custom status codes
                        res.addHeader("EAZYBANK-CUSTOM-HEADER", "Email "+email+" cannot have 'test'");
                        return; // imp, no more processing
                    }
                }
            }
        }catch (IllegalArgumentException e){
            throw new BadRequestException("Failed to decode authentication token");
        }
        // executed for positive scenarios
        chain.doFilter(request, response);
    }
}
