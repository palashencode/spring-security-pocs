package com.example.secure.poc.main.config;

import com.example.secure.poc.main.entity.CustomerDetailsEntity;
import com.example.secure.poc.main.repository.CustomerDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!prod")
public class EazyBankNonProdUsernamePasswdAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CustomerDetailsRepository customerDetailsRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        if(passwordEncoder.matches(password, userDetails.getPassword())){
            CustomerDetailsEntity cd = customerDetailsRepository.findById(username)
                                        .orElse(new CustomerDetailsEntity());
            log.info("Customer '{}', city = '{}', desc = '{}', logging in.", username, cd.getCity(), cd.getDesc());
            return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
//        }else{
//            throw new BadCredentialsException("Invalid Credentials");
//        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
