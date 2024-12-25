package com.example.secure.poc.main.model;

import lombok.Data;

@Data
public class CustomerView {
    public String email;
    private String pwd;
    private String role;
}
