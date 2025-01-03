package com.example.secure.poc.main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NoticesController {

    @GetMapping("/notices")
    public String getNotices(){
        return "Here are notice details from DB";
    }

}
