package com.example.secure.poc.main.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "CUSTOMER")
public class CustomerEntity {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    public String email;
    private String pwd;
    @Column(name = "role")
    private String role;
}


