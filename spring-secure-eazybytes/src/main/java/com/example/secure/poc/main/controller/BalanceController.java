package com.example.secure.poc.main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BalanceController {

    @GetMapping("/myBalance")
    public String getBalanceDetails(){
        return "Here are the balance details from DB";
    }

}
