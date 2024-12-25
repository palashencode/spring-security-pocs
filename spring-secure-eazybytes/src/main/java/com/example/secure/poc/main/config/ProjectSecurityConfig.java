package com.example.secure.poc.main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests  // order matters
                .requestMatchers("/", "/index.html", "/register", "/notices", "/contact", "/error", "/status").permitAll()
                .requestMatchers("/v3/api-docs*/**", "/swagger-ui/**").permitAll()
                .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated());
//        http.formLogin(AbstractHttpConfigurer::disable); // form login configurer
//        http.httpBasic(AbstractHttpConfigurer::disable); // header as - Basic <Base64Encoded-User:Pass>
        http.formLogin(withDefaults()); // form login configurer
        http.httpBasic(withDefaults()); // header as - Basic <Base64Encoded-User:Pass>
        return http.build();
    }

//    @Bean
//    public UserDetailsService getUserDetail(DataSource dataSource){
//        return new JdbcUserDetailsManager(dataSource);
//    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // makes a call to https://api.pwnedpasswords.com/range/ to check password
//    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker(){
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

}
