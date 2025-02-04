package com.java.app.usermgmt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@Slf4j
@RestController
public class StatusController {
    @GetMapping("/api/public/status")
    public Map<String, String> status(){
        return Map.of("app", "spring-user-management", "status", "UP", "currtime", Instant.now().toString());
    }
}
