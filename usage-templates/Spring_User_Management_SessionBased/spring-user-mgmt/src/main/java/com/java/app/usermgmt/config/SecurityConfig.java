package com.java.app.usermgmt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(r -> r
                .requestMatchers("/v3/api-docs*/**", "/swagger-ui/**").permitAll()
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/app/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/admin/**").hasAnyRole("ADMIN").anyRequest().authenticated())
                .csrf(c -> c.disable())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .logout(c -> c.clearAuthentication(true)
                .logoutSuccessUrl("/test")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true));
        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
