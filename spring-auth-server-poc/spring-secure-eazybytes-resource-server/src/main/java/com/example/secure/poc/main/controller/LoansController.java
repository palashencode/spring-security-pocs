package com.example.secure.poc.main.controller;

import com.example.secure.poc.main.model.Customer;
import com.example.secure.poc.main.model.Loans;
import com.example.secure.poc.main.repository.CustomerRepository;
import com.example.secure.poc.main.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoansController {

    private final LoanRepository loanRepository;

    private final CustomerRepository customerRepository;
    private long getCustomerIdFromEmail(String email){
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        if(optionalCustomer.isPresent()){
            return optionalCustomer.get().getId();
        }
        return -1;
    }

    @GetMapping("/myLoans")
    public List<Loans> getLoanDetails(@RequestParam String email) {
        long id = getCustomerIdFromEmail(email);
        if(id == -1) return null;
        List<Loans> loans = loanRepository.findByCustomerIdOrderByStartDtDesc(id);
        if (loans != null) {
            return loans;
        } else {
            return null;
        }
    }

}
