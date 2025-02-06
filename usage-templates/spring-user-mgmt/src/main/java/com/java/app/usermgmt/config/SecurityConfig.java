package com.java.app.usermgmt.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.server.authentication.SwitchUserWebFilter;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final DataSource dataSource;;

    @Bean
    SecurityFilterChain defaultSecurityFilterChainForSessionManagement(HttpSecurity http
                                        ,CustomLoginSuccessHandler successHandler) throws Exception {
        http.authorizeHttpRequests(r -> r
                .requestMatchers("/v3/api-docs*/**", "/swagger-ui/**").permitAll()
                .requestMatchers("/css/**", "/js/**", "/img/**", "/webjars/**", "/favicon.ico").permitAll() // Allow static resources
                .requestMatchers("/","/web/public/**", "/web/home", "/web/forgot-password"
                                                        , "/web/reset-password", "/web/change-password"
                                                        , "/web/verify-user"
                                                        , "/web/login-link", "/web/login-link-generate"
                                                        ,"/error","/web/about", "/web/contact", "/web/services"
                                                        ,"/web/signup","/web/login", "/login", "/perform_login").permitAll()
                .requestMatchers("/api/public/**", "/api/password/**").permitAll()
                .requestMatchers("/web/secure/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/web/user/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/web/admin/**").hasAnyRole("ADMIN")
                .requestMatchers("/api/app/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/admin/impersonate/exit").hasRole("PREVIOUS_ADMINISTRATOR")
                .requestMatchers("/api/admin/**").hasAnyRole("ADMIN").anyRequest().authenticated())
                .csrf(Customizer.withDefaults())
                .csrf(c -> c.ignoringRequestMatchers("/logout", "/login", "/web/login", "/api/app/csrf-token",
                                                "/api/admin/impersonate", "/api/admin/impersonate/exit",
                                                "/api/password/change"))
                .rememberMe(r -> r
                                // Cookie Based RememberMe, survives server restart
//                                .userDetailsService(userDetailsService)
                                // Persistent RememberMe, survives server restart
                                // The server keeps rotating the remember-me cookie value everytime it is used
                                .tokenRepository(persistentTokenRepository())
                                // common parameters
                                .key("secret-remember-me")
                                .rememberMeCookieName("remember-me-cookie")
                                .tokenValiditySeconds(864000))
                // This additional attribute makes a Persistent Remember Me Cookie
                .formLogin(c -> c.loginPage("/web/login")
                        .successHandler(successHandler)
                        .loginProcessingUrl("/perform_login")
                        .failureUrl("/web/login?error=true"))
                .oauth2Login(o -> o.loginPage("/web/login"))
                .logout(c -> c.logoutUrl("/logout")
                        .logoutSuccessUrl("/web/home"));
//                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    // For Remember-Me
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;

    }

    @Bean
    ClientRegistrationRepository clientRegistrationRepository(){
        ClientRegistration git = githubClientRegistration();
        ClientRegistration google = googleClientRegistration();
        return new InMemoryClientRegistrationRepository(git, google);
    }

    private ClientRegistration githubClientRegistration(){
        return CommonOAuth2Provider.GITHUB.getBuilder("github")
                .clientId("Ov23liY8Xlu3FkrcCgRL")
                .clientSecret("ffc8d8d5a645ba68715b2535cd4d9cc6cb51cd52")
                .scope("read:user", "user:email")
                .build();
    }

    private ClientRegistration googleClientRegistration(){
        return CommonOAuth2Provider.GOOGLE.getBuilder("google")
                .clientId("736116120342-nqevg7h3abrv31gjl8f7n56b1rdvt15q.apps.googleusercontent.com")
                .clientSecret("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")
                .scope("profile", "email")
                .build();
    }



    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SwitchUserFilter switchUserFilter(){
        SwitchUserFilter filter = new SwitchUserFilter();
        filter.setUserDetailsService(userDetailsService);
        filter.setSwitchUserUrl("/api/admin/impersonate");
        filter.setExitUserUrl("/api/admin/impersonate/exit");
        filter.setTargetUrl("/api/app/switch-success");
        return filter;
    }

}
