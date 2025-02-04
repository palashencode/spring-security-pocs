package com.java.app.usermgmt.controller;

import com.java.app.usermgmt.model.ActionResult;
import com.java.app.usermgmt.model.ForgotPasswordRequest;
import com.java.app.usermgmt.model.ResetPasswordData;
import com.java.app.usermgmt.service.PasswordManagementService;
import com.java.app.usermgmt.utils.SessionUtils;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
public class ViewController {

    private final PasswordManagementService passwordManagementService;

    private static final String SUCCESS_MSG_KEY = "successMsg";
    private static final String ERROR_MSG_KEY = "errorMsg";
    private static final String INFO_MSG_KEY = "infoMsg";

    public ViewController(PasswordManagementService passwordManagementService){
        this.passwordManagementService = passwordManagementService;
    }

    @RequestMapping("/web/public/thyme")
    public String getThymeDemoPage(Model model){
        model.addAttribute("msg", "from controller : this is a thymeleaf demo page");
        model.addAttribute("list", List.of("this is something", "anotherthing", "a new thing", "another val in list"));
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("username", "janice@example.com");
        return "/thyme.html";
    }

    @RequestMapping("/")
    public String baseHomePage(Model model){
        return "redirect:/web/home"; // Redirects to /dashboard
    }

    @GetMapping("/web/login")
    public String getLoginPage(Model model,
                               @RequestParam(name = "error", required = false) String error){
        if(error != null && !error.isEmpty()){
            model.addAttribute("loginError", true);
        }
        return "login";
    }

    @RequestMapping("/web/home")
    public String getHomePage(HttpSession session, Model model){
//        SessionUtils.consumeToastsFromSession(session, model);
        return "home";
    }

    @RequestMapping("/web/about")
    public String getAboutPage(HttpSession session, Model model){
//        SessionUtils.consumeToastsFromSession(session, model);
        return "about";
    }

    @RequestMapping("/web/contact")
    public String getContactPage(HttpSession session, Model model){
//        SessionUtils.consumeToastsFromSession(session, model);
        return "contact-us";
    }

    @RequestMapping("/web/services")
    public String getServicePage(HttpSession session, Model model){
        return "services";
    }

    // GET will prepare the page for submit with custom tokens
    @GetMapping("/web/forgot-password")
    public String getForgotPassword(HttpSession session, Model model){
        SessionUtils.addAuthenticityTokenToSessionAndModel(session, model);
        return "forgot-password";
    }

    @PostMapping("/web/forgot-password")
    public String processForgotPassword(@ModelAttribute ForgotPasswordRequest forgotPasswordRequest, HttpSession session, Model model){

        boolean isInvalid = !SessionUtils.consumeVerifyAuthenticityToken(session, model, forgotPasswordRequest.getAuthenticityToken());
        // Check if token is valid
        if (isInvalid) {
            return "redirect:forgot-password"; // Invalid request
        }
        String response = passwordManagementService.processForgotPassword(forgotPasswordRequest.getEmail());

        model.addAttribute(SUCCESS_MSG_KEY, response);
        SessionUtils.showSuccessToast(model, response);
        return "forgot-password";
    }


    @GetMapping("/web/verify-user")
    public String verifyUserRegistration(@RequestParam( name = "t") String token, HttpSession session, Model model){
        boolean isVerified = passwordManagementService.verifyAndCreateUser(token);
        if(isVerified){
            model.addAttribute(SUCCESS_MSG_KEY, "User verified successfully, please login.");
            return "success";
        }else{
            model.addAttribute(ERROR_MSG_KEY, "Link expired or incorrect. Verification failed");
            return "error-blank";
        }
    }

    // GET will prepare the page for submit with custom tokens
    @GetMapping("/web/reset-password")
    public String resetPassword(@RequestParam( name = "t") String token, HttpSession session, Model model){

        boolean isTokenValid = passwordManagementService.isTokenValid(token);
        if(!isTokenValid){
            String msg = "Invalid or Expired Token, Please request again";
            model.addAttribute(ERROR_MSG_KEY, msg);
            SessionUtils.showErrorToast(model, msg);
            return "error-blank";
        }

        SessionUtils.addPasswordResetTokenToSessionAndModel(session, model, token);
        SessionUtils.addAuthenticityTokenToSessionAndModel(session, model);
        return "reset-password";
    }

    @PostMapping("/web/reset-password")
    public String resetPasswordPost(@RequestParam( name = SessionUtils.AUTHENTICITY_TOKEN_KEY) String authenticityToken,
                                    @RequestParam( name = SessionUtils.PASSWORD_RESET_TOKEN_KEY) String passwordResetToken,
                                    HttpSession session, Model model){

        boolean isAuthenticityTokenInvalid = !SessionUtils.consumeVerifyAuthenticityToken(session, model, authenticityToken);
        boolean isTokenInvalid = !passwordManagementService.isTokenValid(passwordResetToken);
        // Check if token is valid
        if (isAuthenticityTokenInvalid || isTokenInvalid) {
            String msg = "Invalid or Expired Token, Please request again";
            model.addAttribute(ERROR_MSG_KEY, msg);
            SessionUtils.showErrorToast(model, msg);
            return "error-blank";
        }

        SessionUtils.addPasswordResetTokenToSessionAndModel(session, model, passwordResetToken);
        SessionUtils.addAuthenticityTokenToSessionAndModel(session, model);
        return "change-password";
    }


    @GetMapping("/web/login-link")
    public String loginWithMagicLink(@RequestParam(name = "t") String token,
                         HttpSession session, Model model) {
        boolean result = passwordManagementService.loginUserWithMagicLink(token, session);
        if(result){
            model.addAttribute(SUCCESS_MSG_KEY, "you are logged in !");
            return "home";
        }else{
            model.addAttribute(ERROR_MSG_KEY, "Error : Login link expired or invalid !");
            return "error-blank";
        }
    }

    @PostMapping("/web/login-link-generate")
    public String signup(@RequestParam(name = "username") String username,
                         HttpSession session, Model model) {
        boolean result = passwordManagementService.generateLoginMagicLink(username);
        if(result){
            model.addAttribute(SUCCESS_MSG_KEY, "One time Login Link emailed.");
           return "success";
        }else{
            model.addAttribute(ERROR_MSG_KEY, "One time Login Link generation failed.");
            return "error-blank";
        }
    }


    @PostMapping("/web/signup")
    public String signup(@RequestParam(name = "username") String username,
                                 @RequestParam(name = "password") String password,
                                 HttpSession session, Model model) {

        String responseMsg = passwordManagementService.registerUser(username, password);
        model.addAttribute(SUCCESS_MSG_KEY, responseMsg);
        return "success";
    }

    @PostMapping("/web/change-password")
    public String changePassword(@RequestParam( name = SessionUtils.AUTHENTICITY_TOKEN_KEY) String authenticityToken,
                                    @RequestParam( name = SessionUtils.PASSWORD_RESET_TOKEN_KEY) String passwordResetToken,
                                     @RequestParam( name = "email") String email,
                                     @RequestParam( name = "password") String password,
                                     @RequestParam( name = "repeat-password") String repeatPassword,
                                    HttpSession session, Model model){

        boolean isAuthenticityTokenInvalid = !SessionUtils.consumeVerifyAuthenticityToken(session, model, authenticityToken);
        boolean isTokenInvalid = !passwordManagementService.isTokenValid(passwordResetToken);
        // Check if token is valid
        if (isAuthenticityTokenInvalid || isTokenInvalid) {
            String msg = "Invalid or Expired Token, Please request again";
            model.addAttribute(ERROR_MSG_KEY, msg);
            SessionUtils.showErrorToast(model, msg);
            return "error-blank";
        }

        ActionResult result = passwordManagementService.changePasswordWithResponseObject(ResetPasswordData.builder()
                .email(email)
                .password(password)
                .repeatPassword(repeatPassword)
                .token(passwordResetToken)
                .build());


        model.addAttribute(result.isSuccess() ? SUCCESS_MSG_KEY : ERROR_MSG_KEY, result.getMessage());
        if(result.isSuccess()){
            SessionUtils.showSuccessToast(model, result.getMessage());
        }else{
            SessionUtils.showErrorToast(model, result.getMessage());
        }
        return "change-password";
    }


}
