package com.java.app.usermgmt.config;

import com.java.app.usermgmt.model.ToastData;
import com.java.app.usermgmt.model.ToastType;
import com.java.app.usermgmt.utils.SessionUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
                                        throws IOException, ServletException {
        // Store success message in session (Flash Attributes alternative)
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        SessionUtils.addLoggedInToastsToSession(request.getSession(), userDetails);

        // Redirect to the home page
        response.sendRedirect("/web/home");
    }
}