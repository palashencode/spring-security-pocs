package com.example.secure.demo.controller;

import com.example.secure.demo.config.JWTIssuer;
import com.example.secure.demo.config.UserPrincipal;
import com.example.secure.demo.model.LoginRequest;
import com.example.secure.demo.model.LoginResponse;
import com.example.secure.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public LoginResponse login(@RequestBody @Validated LoginRequest request){
        return authService.attemptLogin(request.getEmail(), request.getPassword());
    }

    @GetMapping("/secured")
    public String secured(@AuthenticationPrincipal UserPrincipal principal){
        log.info("Call made to /secured endpoint by user - {}, userId - {}", principal.getEmail()
                                        ,principal.getUserId());
        StringBuilder sb = new StringBuilder();
        sb.append("If you see this then you are logged in at ");
        sb.append(Instant.now());
        sb.append(" and as " + principal.getEmail());
        sb.append(" and as " + principal.getUserId());
        return sb.toString();
    }

    @GetMapping("/admin")
    public String admin(@AuthenticationPrincipal UserPrincipal principal){
        StringBuilder sb = new StringBuilder();
        sb.append("If you see this then you are logged in as admin at ");
        sb.append(Instant.now());
        sb.append(" and as " + principal.getEmail());
        sb.append(" and as " + principal.getUserId());
        return sb.toString();
    }
}
