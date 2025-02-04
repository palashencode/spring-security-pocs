package com.java.app.usermgmt.config;

import com.java.app.usermgmt.controller.ViewController;
import com.java.app.usermgmt.utils.SessionUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(assignableTypes = {ViewController.class})
public class ViewControllerAdvice {

    @ModelAttribute
    public void processSessionAttributes(HttpSession session, Model model) {
        SessionUtils.consumeToastsFromSession(session, model);
    }

}
