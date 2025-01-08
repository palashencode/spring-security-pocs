package com.example.secure.poc.main.mapper.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class AccountsDTO {
    private long customerId;
    private long accountNumber;
    private String accountType;
    private String branchAddress;
    private Date createDt;
}
