package com.java.app.usermgmt.service;

import com.java.app.usermgmt.config.CustomUserDetails;
import com.java.app.usermgmt.entity.*;
import com.java.app.usermgmt.model.ResetPasswordData;
import com.java.app.usermgmt.model.ActionResult;
import com.java.app.usermgmt.repository.*;
import com.java.app.usermgmt.utils.CryptoUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordManagementService {

    private final UserRepository userRepository;
    private final UserRolesRepository userRolesRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final LoginMagicLinkRepository loginMagicLinkRepository;
    private final PendingUserVerificationRepository pendingUserVerificationRepository;
    private final EmailSenderService emailSenderService;
    private final UserDetailsService userDetailsService;

    private static final String EMAIL_SENT = "Email sent. Please check inbox for reset link.";
    private static final String TOKEN_EXPIRED = "Token expired, please try again.";
    private static final String PASSWORDS_DONOT_MATCH = "Passwords donot match, please try again.";
    private static final String PASSWORDS_CHANGE_SUCCESS = "Password changed successfully, please try to login.";

    public String getAuthenticityToken(){
        return CryptoUtils.authenticityToken();
    }

    public String processForgotPassword(String email){
        if(userRepository.findByUsername(email).isEmpty()){
            log.info("Forgot Password submitted, Invalid Email - {}", email);
            return EMAIL_SENT;
        }
        log.info("Forgot Password submitted, Valid Email - {}", email);
        String token = CryptoUtils.resetPasswordToken();
        sendPasswordResetEmail(email, token);
        return EMAIL_SENT;
    }

    private void sendPasswordResetEmail(String email, String token){
        User user = userRepository.findByUsername(email).get();
        PasswordReset passwordReset = passwordResetRepository.findByUser(user)
                .orElse(new PasswordReset());
        passwordReset.setUser(user);
        passwordReset.setToken(token);
        passwordReset.setCreatedDate(Instant.now());
        passwordReset.setExpiryDate(Instant.now().plus(5, ChronoUnit.MINUTES));
        passwordResetRepository.save(passwordReset);
        emailSenderService.sendForgetPasswordEmail(email, token);
        log.info("======= TOKEN -> {}, for email {}", token, email);
    }

    public ActionResult changePasswordWithResponseObject(ResetPasswordData resetPasswordData){
        if(!resetPasswordData.getPassword().equals(resetPasswordData.getRepeatPassword())){
            return ActionResult.builder().success(false).message(PASSWORDS_DONOT_MATCH).build();
        }

        if(isTokenInvalid(resetPasswordData)){
            return ActionResult.builder().success(false).message(TOKEN_EXPIRED).build();
        }

        Optional<User> optionalUser = userRepository.findByUsername(resetPasswordData.getEmail());
        User user = optionalUser.get();
        PasswordEncoder encoder = getPasswordEncoder();
        user.setPassword(encoder.encode(resetPasswordData.getPassword()));
        userRepository.save(user);
        PasswordReset passwordReset = passwordResetRepository.findByToken(resetPasswordData.getToken()).get();
        passwordResetRepository.delete(passwordReset);

        emailSenderService.sendPasswordChangeSuccess(resetPasswordData.getEmail());
        return ActionResult.builder().success(true).message(PASSWORDS_CHANGE_SUCCESS).build();
    }

    public String changePassword(ResetPasswordData resetPasswordData){
        return changePasswordWithResponseObject(resetPasswordData).getMessage();
    }

    private PasswordEncoder getNoopPasswordEncoder(){
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("noop", NoOpPasswordEncoder.getInstance());  // No-Op encoder
        encoders.put("bcrypt", new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder());

        // Create DelegatingPasswordEncoder with default encoder
        return new DelegatingPasswordEncoder("noop", encoders);
    }

    private PasswordEncoder getDelegatingPasswordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    private PasswordEncoder getPasswordEncoder(){
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("noop", NoOpPasswordEncoder.getInstance());  // No-Op encoder
        encoders.put("bcrypt", new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder());

        // Create DelegatingPasswordEncoder with default encoder
        return new DelegatingPasswordEncoder("noop", encoders);
    }

    public boolean generateLoginMagicLink(String username){
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isEmpty()){
            return false;
        }
        LoginMagicLink link = null;
        Optional<LoginMagicLink> optionalLink = loginMagicLinkRepository
                                                .findByUserId(userOptional.get().getUserId());

        String token = CryptoUtils.getLoginMagicLink();
        if(optionalLink.isPresent()){
            link = optionalLink.get();
        }else{
            link = LoginMagicLink.builder().build();
            link.setUserId(userOptional.get().getUserId());
            link.setUsername(userOptional.get().getUsername());
        }
            link.setCreatedDate(Instant.now());
            link.setExpiryDate(Instant.now().plus(5, ChronoUnit.MINUTES));
            link.setToken(token);
        loginMagicLinkRepository.save(link);
        emailSenderService.sendLoginMagicLink(username, token);
        return true;
    }

    public boolean loginUserWithMagicLink(String token, HttpSession session){
        Optional<LoginMagicLink> optionalLink = loginMagicLinkRepository
                .findByToken(token);

        if(optionalLink.isEmpty() || Instant.now().isAfter(optionalLink.get().getExpiryDate())){
            return false;
        }
        Optional<User> userOptional = userRepository.findById((long)optionalLink.get().getUserId());
        if(userOptional.isEmpty()){
            return false;
        }
        User user = userOptional.get();
        authenticateUser(user.getUsername(), session);
        loginMagicLinkRepository.delete(optionalLink.get());
        return true;
    }

    private void authenticateUser(String username, HttpSession session){
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService
                .loadUserByUsername(username);
        // authenticate the user
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authToken);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
    }

    public String registerUser(String username, String password){

        PasswordEncoder encoder = getNoopPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        String verificationToken = CryptoUtils.emailVerificationToken();

        Optional<PendingUserVerification> pendingUserVerificationOptional = pendingUserVerificationRepository
                                                    .findByUsername(username);

        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            return "User is already registered with email id " + username;
        }

        PendingUserVerification pendingUser = null;
        if(pendingUserVerificationOptional.isPresent()){
                pendingUser = pendingUserVerificationOptional.get();
        }else{
            pendingUser = PendingUserVerification.builder().build();
            pendingUser.setUsername(username);
        }
        pendingUser.setPassword(encodedPassword);
        pendingUser.setRegistrationTime(Instant.now());
        pendingUser.setVerificationToken(verificationToken);
        pendingUser.setVerificationTime(null);
        pendingUser.setVerified(false);
        pendingUserVerificationRepository.save(pendingUser);
        emailSenderService.verifyRegistrationUserEmail(username, verificationToken);
        return "Success! Now check your email to confirm your registration.";
    }

    public boolean verifyAndCreateUser(String verificationToken){
        Optional<PendingUserVerification> pendingUserVerificationOptional = pendingUserVerificationRepository
                                    .findByVerificationToken(verificationToken);
        if(pendingUserVerificationOptional.isEmpty()){
            return false;
        }
        PendingUserVerification pendingUserVerification = pendingUserVerificationOptional.get();

        String username = pendingUserVerification.getUsername();
        String password = pendingUserVerification.getPassword();

        // Add in User
        User user = User.builder()
                .username(username)
                .password(password)
                .firstname("New User")
                .active(true)
                .build();

        user = userRepository.save(user);
        UserRoles userRole = userRolesRepository.save(UserRoles.builder()
                .userId(user.getUserId())
                .role("ROLE_USER").build());

        pendingUserVerification.setVerified(true);
        pendingUserVerification.setVerificationTime(Instant.now());
        pendingUserVerificationRepository.save(pendingUserVerification);
        emailSenderService.userVerificationSuccess(username);
        return true;
    }

    private boolean getPendingVerification(String token){
        Optional<PendingUserVerification> pendingUserVerification = pendingUserVerificationRepository
                .findByVerificationToken(token);
        if(pendingUserVerification.isEmpty()){
            return false;
        }
        return true;
    }

    public boolean isTokenValid(String token){
        return !isTokenInvalid(token);
    }

    private boolean isTokenInvalid(String token){
        Optional<PasswordReset> optionalPasswordReset = passwordResetRepository
                                                        .findByToken(token);
        if(optionalPasswordReset.isEmpty()){
            return true;
        }

        Instant now = Instant.now();
        if(now.isAfter(optionalPasswordReset.get().getExpiryDate())){
            return true;
        }
        return false;
    }

    private boolean isTokenInvalid(ResetPasswordData resetPasswordData){
        Optional<User> optionalUser = userRepository.findByUsername(resetPasswordData.getEmail());
        Optional<PasswordReset> optionalPasswordReset = passwordResetRepository
                .findByToken(resetPasswordData.getToken());

        if(optionalUser.isEmpty()){
            return true;
        }
        if(optionalPasswordReset.isEmpty()){
            return true;
        }
        if(!optionalUser.get().getUsername()
                .equalsIgnoreCase(optionalPasswordReset.get()
                        .getUser().getUsername())){
            return true;
        }

        Instant now = Instant.now();
        if(now.isAfter(optionalPasswordReset.get().getExpiryDate())){
            return true;
        }
        return false;
    }

}
