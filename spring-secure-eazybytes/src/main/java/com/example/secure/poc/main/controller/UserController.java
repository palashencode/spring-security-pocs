package com.example.secure.poc.main.controller;

import com.example.secure.poc.main.entity.CustomerEntity;
import com.example.secure.poc.main.mapper.CustomerEntityToViewMapper;
import com.example.secure.poc.main.model.CustomerView;
import com.example.secure.poc.main.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final CustomerEntityToViewMapper mapper;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody CustomerView cView){
        CustomerEntity c = mapper.mapToEntity(cView);
        try{
            if(customerRepository.findByEmail(c.getEmail()).isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User already exists.");
            }
            c.setPwd(passwordEncoder.encode(c.getPwd()));
            c = customerRepository.save(c);
            if(c.getId() > 0){
                return ResponseEntity.status(HttpStatus.OK)
                        .body("User created successfully.");
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("User registration failed.");
            }

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Exception occured "+ e.getMessage());
        }
    }

}
