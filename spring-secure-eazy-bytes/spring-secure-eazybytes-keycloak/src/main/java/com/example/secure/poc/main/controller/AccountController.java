package com.example.secure.poc.main.controller;

import com.example.secure.poc.main.model.Accounts;
import com.example.secure.poc.main.model.Customer;
import com.example.secure.poc.main.repository.AccountsRepository;
import com.example.secure.poc.main.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountsRepository accountsRepository;
    private final CustomerRepository customerRepository;

    private long getCustomerIdFromEmail(String email){
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        if(optionalCustomer.isPresent()){
            return optionalCustomer.get().getId();
        }
        return -1;
    }

    @GetMapping("/myAccount")
    public Accounts getAccountDetails(@RequestParam String email) {
        long id = getCustomerIdFromEmail(email);
        if(id == -1) return null;
        Accounts accounts = accountsRepository.findByCustomerId(id);
        if (accounts != null) {
            return accounts;
        } else {
            return null;
        }
    }

}
