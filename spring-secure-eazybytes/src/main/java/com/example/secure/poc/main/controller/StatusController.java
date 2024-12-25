package com.example.secure.poc.main.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@Slf4j
@RestController
public class StatusController {

    @GetMapping({"/welcome"})
    public String welcome(){
        return "Welcome to Spring Application with security";
    }

    @GetMapping("/status")
    public Map<String, String> status(){
        return Map.of("status", "up",
                "server", "spring-security-starter",
                "ts", Instant.now().toString());
    }

}
