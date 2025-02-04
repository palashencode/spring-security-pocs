package com.java.app.usermgmt.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users_meta")
public class UserMeta {

    @Id
    @Column(name = "user_id")
    @JsonIgnore
    private Integer userId;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "profile_pic_url")
    private String profilePic;

    @Column(name = "bio")
    private String bio;
}
