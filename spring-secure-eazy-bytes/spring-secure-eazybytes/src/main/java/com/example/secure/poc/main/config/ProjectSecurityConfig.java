package com.example.secure.poc.main.config;

import com.example.secure.poc.main.exceptionhandling.CustomAccessDeniedHandler;
import com.example.secure.poc.main.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import com.example.secure.poc.main.filter.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Profile("!prod")
@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http
//                .securityContext(c -> c.requireExplicitSave(false))
                .sessionManagement(smc -> smc
//                                .invalidSessionUrl("/invalidsession.html")
//                          .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                          .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // for JWT
//                        .maximumSessions(5).maxSessionsPreventsLogin(false)
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
                .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new RequestVerificationBeforeFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class) // Skip basic auth if jwt check fails
                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class) // Generate jwt only after basic auth
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
//                .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards", "/user").authenticated())
//        http.formLogin(AbstractHttpConfigurer::disable); // form login configurer
//        http.httpBasic(AbstractHttpConfigurer::disable); // header as - Basic <Base64Encoded-User:Pass>
        .formLogin(withDefaults()) // form login configurer
        .httpBasic(hbc -> hbc.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint())) // header as - Basic <Base64Encoded-User:Pass>
        .exceptionHandling(hbc -> hbc.accessDeniedHandler(new CustomAccessDeniedHandler())
        ); // Global Errors for all 401/403
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

    @Bean
    static GrantedAuthorityDefaults grantedAuthorityDefaults(){
        return new GrantedAuthorityDefaults("MYPREFIX_");
    }


    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) {
        EazyBankNonProdUsernamePasswdAuthenticationProvider authenticationProvider =
                new EazyBankNonProdUsernamePasswdAuthenticationProvider(userDetailsService, passwordEncoder);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false); // Do not erase password from Authentication, only if you need it.
        return  providerManager;
    }

}
