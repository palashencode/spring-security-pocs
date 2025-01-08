package com.example.secure.poc.main.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table( name = "authorities")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
