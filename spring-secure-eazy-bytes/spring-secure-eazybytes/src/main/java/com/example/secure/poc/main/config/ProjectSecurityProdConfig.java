package com.example.secure.poc.main.config;

import com.example.secure.poc.main.exceptionhandling.CustomAccessDeniedHandler;
import com.example.secure.poc.main.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import com.example.secure.poc.main.filter.CsrfCookieFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Profile("prod")
@Configuration
public class ProjectSecurityProdConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.securityContext(c -> c.requireExplicitSave(false))
                .sessionManagement(smc -> smc.invalidSessionUrl("/invalidsession.html")
                                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
//                        .maximumSessions(5).maxSessionsPreventsLogin(false)
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
                .requestMatchers("/", "/index.html", "/register", "/notices",
                        "/contact", "/error", "/status", "/invalidsession.html").permitAll()
                .requestMatchers("/v3/api-docs*/**", "/swagger-ui/**").permitAll()
                .requestMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
                .requestMatchers("/myBalance").hasAnyAuthority("VIEWBALANCE", "VIEWACCOUNT")
                .requestMatchers("/myLoans").hasAuthority("VIEWLOANS")
                .requestMatchers("/myCards").hasAuthority("VIEWCARDS")
                .requestMatchers("/user").authenticated());
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
