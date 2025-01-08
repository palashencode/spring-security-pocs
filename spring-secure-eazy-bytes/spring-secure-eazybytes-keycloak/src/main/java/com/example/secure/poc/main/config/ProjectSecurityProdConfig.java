package com.example.secure.poc.main.config;

import com.example.secure.poc.main.exceptionhandling.CustomAccessDeniedHandler;
import com.example.secure.poc.main.filter.CsrfCookieFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Profile("prod")
@Configuration
public class ProjectSecurityProdConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.securityContext(c -> c.requireExplicitSave(false))
                .sessionManagement(smc -> smc.invalidSessionUrl("/invalidsession.html")
                                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                ).cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                }))
                .csrf(c -> c.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/contact", "/register"))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .requiresChannel(rcc -> rcc.anyRequest().requiresSecure()) // only https
                .authorizeHttpRequests((requests) -> requests  // order matters
                        .requestMatchers("/", "/index.html", "/register", "/notices", "/apiLogin",  // these are public
                                "/contact", "/error", "/status", "/invalidsession.html").permitAll()
                        .requestMatchers("/v3/api-docs*/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/myAccount").hasRole("USER")
                        .requestMatchers("/myBalance").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/myLoans").hasRole("USER")
                        .requestMatchers("/myCards").hasRole("USER")
                        .requestMatchers("/user").authenticated());
        http.oauth2ResourceServer(resourceServer -> resourceServer
                .jwt(jwtConfigurer -> jwtConfigurer
                        .jwtAuthenticationConverter(getJwtAuthenticationConverter())));
        http.exceptionHandling(hbc -> hbc.accessDeniedHandler(new CustomAccessDeniedHandler())); // Global Errors for all 401/403
        return http.build();
    }

    private JwtAuthenticationConverter getJwtAuthenticationConverter(){
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeyCloakToSpringRoleConverter());
        return jwtAuthenticationConverter;
    }
}
