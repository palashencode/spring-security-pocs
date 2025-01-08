package com.example.secure.poc.main.config;

import com.example.secure.poc.main.exceptionhandling.CustomAccessDeniedHandler;
import com.example.secure.poc.main.filter.CsrfCookieFilter;
import com.example.secure.poc.main.utils.AppConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Profile("!prod")
@Configuration
public class ProjectSecurityConfig {

//    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-uri}")
//    String introspectUri;
//
//    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-client-id}")
//    String clientId;
//
//    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-client-secret}")
//    String clientSecret;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http
                .sessionManagement(smc -> smc
                          .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // for JWT
                ).cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(Arrays.asList("Authorization"));
                        config.setMaxAge(3600L);
                        return config;
                    }
                }))
                .csrf(c -> c.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/contact", "/register", "/apiLogin")) // ignore csrf for these
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure()) // only http
                .authorizeHttpRequests((requests) -> requests  // order matters
                .requestMatchers("/", "/index.html", "/register", "/notices", "/apiLogin",  // these are public
                        "/contact", "/error", "/status", "/invalidsession.html").permitAll()
                .requestMatchers("/v3/api-docs*/**", "/swagger-ui/**").permitAll()
                .requestMatchers("/myAccount").hasRole("USER")
                .requestMatchers("/myBalance").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/myLoans").hasRole("USER")
                .requestMatchers("/myCards").hasRole("USER")
                .requestMatchers("/user").authenticated())
//                .oauth2ResourceServer(resourceServer -> resourceServer
//                        .opaqueToken(opaqueTokenCOnfigurer -> opaqueTokenCOnfigurer
//                                .authenticationConverter(new KeyCloakOpaqueRoleConverter())
//                                .introspectionUri(this.introspectUri)
//                                .introspectionClientCredentials(this.clientId, this.clientSecret)))
                .oauth2ResourceServer(resourceServer -> resourceServer
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .jwtAuthenticationConverter(getJwtAuthenticationConverter())))
        .exceptionHandling(hbc -> hbc.accessDeniedHandler(new CustomAccessDeniedHandler())
        ); // Global Errors for all 401/403
        return http.build();
    }

    private JwtAuthenticationConverter getJwtAuthenticationConverter(){
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeyCloakToSpringRoleConverter());
        return jwtAuthenticationConverter;
    }

    @Bean
    static GrantedAuthorityDefaults grantedAuthorityDefaults(){
        return new GrantedAuthorityDefaults(AppConstants.ROLE_PREFIX);
    }

}
