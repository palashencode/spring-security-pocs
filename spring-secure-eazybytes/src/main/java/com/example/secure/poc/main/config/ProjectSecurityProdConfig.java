package com.example.secure.poc.main.config;

import com.example.secure.poc.main.exceptionhandling.CustomAccessDeniedHandler;
import com.example.secure.poc.main.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import static org.springframework.security.config.Customizer.withDefaults;

@Profile("prod")
@Configuration
public class ProjectSecurityProdConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.sessionManagement(smc -> smc.invalidSessionUrl("/invalidsession.html")
                .maximumSessions(1).maxSessionsPreventsLogin(true))
                .requiresChannel(rcc -> rcc.anyRequest().requiresSecure()) // only https
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests  // order matters
                .requestMatchers("/", "/index.html", "/register", "/notices",
                        "/contact", "/error", "/status", "/invalidsession.html").permitAll()
                .requestMatchers("/v3/api-docs*/**", "/swagger-ui/**").permitAll()
                .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated());
//        http.formLogin(AbstractHttpConfigurer::disable); // form login configurer
//        http.httpBasic(AbstractHttpConfigurer::disable); // header as - Basic <Base64Encoded-User:Pass>
        http.formLogin(withDefaults()); // form login configurer
        http.httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint())); // header as - Basic <Base64Encoded-User:Pass>
        http.exceptionHandling(hbc -> hbc.accessDeniedHandler(new CustomAccessDeniedHandler())); // Global Errors for all 401/403
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
