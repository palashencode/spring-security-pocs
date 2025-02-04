package com.java.app.usermgmt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin")
public class AdminController {


    @GetMapping("info")
    public Map<String, String> info(Authentication authentication){
        return Map.of("app", "spring-user-management",
                "msg", "Admin functionalities that a normal user should not have access to",
                "user", authentication.getName(),
                "currtime", Instant.now().toString());
    }

}
