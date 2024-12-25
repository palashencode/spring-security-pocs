package com.example.secure.demo.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatusController {

    @GetMapping("/")
    public String greeting(){
        return "Hello World !";
    }

    @GetMapping("/status")
    public String status(){
        return "Status - OK !";
    }

}
