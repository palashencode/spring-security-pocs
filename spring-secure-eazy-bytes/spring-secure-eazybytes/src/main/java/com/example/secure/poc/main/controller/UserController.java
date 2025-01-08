package com.example.secure.poc.main.controller;

import com.example.secure.poc.main.model.Customer;
import com.example.secure.poc.main.model.LoginRequestDTO;
import com.example.secure.poc.main.model.LoginResponseDTO;
import com.example.secure.poc.main.repository.CustomerRepository;
import com.example.secure.poc.main.utils.AppConstants;
import com.example.secure.poc.main.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final Environment environment;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        try {
            String hashPwd = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(hashPwd);
            customer.setCreateDt(new Date(System.currentTimeMillis()));
            Customer savedCustomer = customerRepository.save(customer);

            if (savedCustomer.getId() > 0) {
                return ResponseEntity.status(HttpStatus.CREATED).
                        body("Given user details are successfully registered");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                        body("User registration failed");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("An exception occurred: " + ex.getMessage());
        }
    }

    // Creds are sent in the HTTP Header
    @GetMapping("/user")
    public Customer getUserDetailsAfterLogin(Authentication authentication) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(authentication.getName());
        return optionalCustomer.orElse(null);
    }

    @PostMapping("/apiLogin")
    public ResponseEntity<LoginResponseDTO> apiLogin(@RequestBody LoginRequestDTO login){
        String jwt = "";
        String secretKey = environment.getProperty(AppConstants.JWT_SECRET_KEY, AppConstants.JWT_SECRET_VALUE_DEFAULT);
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(login.username(), login.password());
        authentication = authenticationManager.authenticate(authentication); // the main method doing authentication
        if(null!= authentication && authentication.isAuthenticated()){
            // generate jwt
            jwt = JWTUtil.encode(authentication.getName(), authentication.getAuthorities(), secretKey);
        }

        return ResponseEntity.status(HttpStatus.OK).header(AppConstants.JWT_HEADER, jwt)
                .body(new LoginResponseDTO(HttpStatus.OK.getReasonPhrase(), jwt));
    }


}
