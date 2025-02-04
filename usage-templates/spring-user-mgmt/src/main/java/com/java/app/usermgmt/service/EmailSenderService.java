package com.java.app.usermgmt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.Instant;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSenderService {

    @Value("${application.config.email.sendfrom:dummy}")
    private String sendFrom;

    private final JavaMailSender mailSender;

    public void verifyRegistrationUserEmail(String to, String token){
        String subject = "Registration verification email : Thymeleaf App";
        String url = "http://localhost:60200/web/verify-user?t="+token;
        String body = String.format("Verify your email at %s", url);
        sendEmail(to, subject, body);
    }

    public void sendLoginMagicLink(String to, String token){
        String subject = "One-time Login url : Thymeleaf App";
        String url = "http://localhost:60200/web/login-link?t="+token;
        String body = String.format("Your one-time login link valid for 5 minutes is %s", url);
        sendEmail(to, subject, body);
    }

    public void userVerificationSuccess(String to){
        String subject = "User Verified Successfully : Thymeleaf App";
        String url = "http://localhost:60200";
        String body = String.format("Your email has been verified successfully at %s, please login here %s", Instant.now().toString(), url);
        sendEmail(to, subject, body);
    }


    public void sendPasswordChangeSuccess(String to){
        String subject = "Password Changed Successfully : Thymeleaf App";
        String url = "http://localhost:60200";
        String body = String.format("Your password was successfully changed at %s, please login here %s", Instant.now().toString(), url);
        sendEmail(to, subject, body);
    }

    public void sendForgetPasswordEmail(String to, String token){
        String subject = "Reset Password Link : Thymeleaf App";
        String url = "http://localhost:60200/web/reset-password?t="+token;
        String body = String.format("Your password reset link is as follows : %s", url);
        sendEmail(to, subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom(sendFrom); // Dummy sender address

            mailSender.send(message);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
