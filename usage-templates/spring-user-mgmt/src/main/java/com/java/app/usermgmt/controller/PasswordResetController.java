package com.java.app.usermgmt.controller;

import com.java.app.usermgmt.model.ResetPasswordData;
import com.java.app.usermgmt.service.PasswordManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping(value = "/api/password")
@RestController
public class PasswordResetController {

    private final PasswordManagementService passwordManagementService;

    public PasswordResetController(PasswordManagementService passwordManagementService) {
        this.passwordManagementService = passwordManagementService;
    }

    @GetMapping("forgot-password")
    public String resetPassword(@RequestParam(name = "email") String email){
        return passwordManagementService.processForgotPassword(email);
    }

    @PostMapping("change")
    public String changePassword(@RequestBody ResetPasswordData resetPasswordData){
        return passwordManagementService.changePassword(resetPasswordData);
    }

}
