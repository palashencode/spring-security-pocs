package com.example.secure.sociallogin.poc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AppConfig {

    @Bean
    public SecurityFilterChain defaultSecurityChainFilter(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(r -> r.requestMatchers("/secure").authenticated()
                .anyRequest().permitAll())
                .oauth2Login(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());
        return httpSecurity.build();
    }

    @Bean
    ClientRegistrationRepository clientRegistrationRepository(){
        ClientRegistration git = githubClientRegistration();
        ClientRegistration google = googleClientRegistration();
        return new InMemoryClientRegistrationRepository(git, google);
    }
    private ClientRegistration githubClientRegistration(){
        return CommonOAuth2Provider.GITHUB.getBuilder("github")
                .clientId("Ov23lix8qZHJWeBmZ0jR")
                .clientSecret("afa1d55ace90cfea9dc8eb38a510fd5a137e7ae1")
                .build();
    }

    private ClientRegistration googleClientRegistration(){
        return CommonOAuth2Provider.GOOGLE.getBuilder("google")
                .clientId("736116120342-2quk77bfefb49ar6dia70b787e4feda6.apps.googleusercontent.com")
                .clientSecret("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")
                .build();
    }

}
