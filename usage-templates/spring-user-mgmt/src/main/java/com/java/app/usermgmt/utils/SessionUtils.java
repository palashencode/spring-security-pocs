package com.java.app.usermgmt.utils;

import com.java.app.usermgmt.config.CustomUserDetails;
import com.java.app.usermgmt.model.ToastData;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.List;

public class SessionUtils {

    private static final String TOAST_LIST_ATTRIBUTE_KEY = "toastList";
    public static final String AUTHENTICITY_TOKEN_KEY = "authenticityToken";
    public static final String PASSWORD_RESET_TOKEN_KEY = "passwordResetToken";

    private SessionUtils(){}

    public static boolean consumeVerifyAuthenticityToken(HttpSession session, Model model, String expectedValue){
        String authenticityToken = (String) session.getAttribute(AUTHENTICITY_TOKEN_KEY);

        // Check if token is valid
        if (authenticityToken == null || !authenticityToken.equals(expectedValue)) {
            return false;
        }

        // Token is valid -> Proceed with forgot password logic
        session.removeAttribute(AUTHENTICITY_TOKEN_KEY); // Invalidate token after use
        return true;
    }

    public static void addAuthenticityTokenToSessionAndModel(HttpSession session, Model model){
        String authenticityToken = CryptoUtils.authenticityToken();
        session.setAttribute("authenticityToken", authenticityToken); // Store it in session
        model.addAttribute("authenticityToken", authenticityToken); // Pass to Thymeleaf
    }

    public static void addPasswordResetTokenToSessionAndModel(HttpSession session, Model model, String token){
        session.setAttribute(PASSWORD_RESET_TOKEN_KEY, token); // Store it in session
        model.addAttribute(PASSWORD_RESET_TOKEN_KEY, token); // Pass to Thymeleaf
    }

    public static void addLoggedInToastsToSession(HttpSession session, CustomUserDetails userDetails){

        List<ToastData> toastList = ToastData.toastLists(ToastData.justLoggedIn(userDetails.getFirstName()),
                                ToastData.info("Just Now", "Important Info", "You are awesome !")
        );
        session.setAttribute(TOAST_LIST_ATTRIBUTE_KEY, toastList);
    }

    public static void showErrorToast(Model model, String body){
        List<ToastData> toastList = ToastData.toastLists(ToastData.error("Just Now", "Info", body));
        model.addAttribute(TOAST_LIST_ATTRIBUTE_KEY, toastList);
    }

    public static void showSuccessToast(Model model, String body){
        List<ToastData> toastList = ToastData.toastLists(ToastData.success("Just Now", "Info", body));
        model.addAttribute(TOAST_LIST_ATTRIBUTE_KEY, toastList);
    }

    public static void addSuccessToastToSession(HttpSession session, String body){
        List<ToastData> toastList = ToastData.toastLists(ToastData.success("Just Now", "Info", body));
        session.setAttribute(TOAST_LIST_ATTRIBUTE_KEY, toastList);
    }

    public static void consumeToastsFromSession(HttpSession session, Model model){
        // Retrieve message from session (if available) and remove it
        @SuppressWarnings("unchecked")
        List<ToastData> toastList = (List<ToastData>) session.getAttribute(TOAST_LIST_ATTRIBUTE_KEY);
        if (toastList != null && !toastList.isEmpty()) {
            model.addAttribute(TOAST_LIST_ATTRIBUTE_KEY, toastList);
            session.removeAttribute(TOAST_LIST_ATTRIBUTE_KEY); // Remove after use
        }
    }
}
