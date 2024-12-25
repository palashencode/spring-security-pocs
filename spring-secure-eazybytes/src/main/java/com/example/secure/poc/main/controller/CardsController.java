package com.example.secure.poc.main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CardsController {

    @GetMapping("/myCards")
    public String getCardDetails(){
        return "Here are the card details from DB";
    }

}
