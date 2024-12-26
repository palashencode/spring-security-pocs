package com.example.secure.poc.main.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "customerdetails")
@Data
public class CustomerDetailsEntity {
    @Id
    private String email;
    private String city;
    private String desc;
}
