package com.java.app.usermgmt.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResetPasswordData {
    private String email;
    private String password;
    private String repeatPassword;
    private String token;
}
