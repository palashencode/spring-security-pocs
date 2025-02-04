package com.java.app.usermgmt.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "users_data_list")
public class UserData {

    @Id
    @Column(name = "entry_id")
    private Integer entryId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "entry")
    private String entry;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "created_at")
    private Instant createdAt;

}
