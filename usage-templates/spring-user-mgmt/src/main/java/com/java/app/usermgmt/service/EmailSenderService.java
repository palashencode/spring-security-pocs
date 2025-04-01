package com.java.app.usermgmt.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.Instant;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSenderService {

    @Value("${application.config.email.sendfrom:dummy}")
    private String sendFrom;

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    private void sendEmailWithTemplate(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // Set to true to indicate HTML content
            helper.setFrom(sendFrom);

            mailSender.send(message);
        } catch (Exception e) {
            log.error("Error sending email: {}", e.getMessage());
        }
    }

    private String generateEmailBody(String templateName, Model model) {
        Context context = new Context();
        context.setVariables(model.asMap()); // Add all model attributes to Thymeleaf context
        return templateEngine.process(templateName, context);
    }

    public void verifyRegistrationUserEmailWithTemplate(String to, String token){
        String subject = "Registration verification email : Thymeleaf App";
        String url = "http://localhost:60200/web/verify-user?t="+token;
        // Prepare the Thymeleaf model with the verification URL
        Model model = new ExtendedModelMap();
        model.addAttribute("verificationUrl", url);

        // Generate the email body using the template
        String body = generateEmailBody("email-templates/verification-email", model);

//        String body = String.format("Verify your email at %s", url);
        sendEmailWithTemplate(to, subject, body);
    }

    public void verifyRegistrationUserEmail(String to, String token){
        String subject = "Registration verification email : Thymeleaf App";
        String url = "http://localhost:60200/web/verify-user?t="+token;
        String body = String.format("Verify your email at %s", url);
        sendEmail(to, subject, body);
    }


    public void sendLoginMagicLinkWithTemplateWithImage(String to, String token){
        String subject = "One-time Login URL : Thymeleaf App";
        String magicLoginUrl = "http://localhost:60200/web/login-link?t=" + token;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(sendFrom);

            // Prepare the Thymeleaf model with dynamic values
            Context context = new Context();
            context.setVariable("loginUrl", magicLoginUrl);

            // Generate email body using Thymeleaf
            String body = templateEngine.process("email-templates/login-magic-link", context);
            helper.setText(body, true); // Set true for HTML content

            // Attach inline image (Company Logo)
            ClassPathResource logo = new ClassPathResource("static/img/logo.png");
            helper.addInline("logo", logo);

            // Send email
            mailSender.send(message);
            log.info("Login magic link email sent successfully to {}", to);
        } catch (Exception e) {
            log.error("Error sending magic link email: {}", e.getMessage());
        }
    }

    public void sendLoginMagicLinkWithTemplate(String to, String token){
        String subject = "One-time Login URL : Thymeleaf App";
        String url = "http://localhost:60200/web/login-link?t=" + token;

        // Prepare the Thymeleaf model with the login URL
        Model model = new ExtendedModelMap();
        model.addAttribute("loginUrl", url);

        // Generate the email body using the template
        String body = generateEmailBody("login-magic-link", model);

        sendEmailWithTemplate(to, subject, body);
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
