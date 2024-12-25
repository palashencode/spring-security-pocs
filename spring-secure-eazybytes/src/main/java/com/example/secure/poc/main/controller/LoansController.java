package com.example.secure.poc.main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoansController {

    @GetMapping("/myLoans")
    public String getLoanDetails(){
        return "here are loan details from DB";
    }

}
