package com.templatemanager.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "loginrbm")
public class LoginRbm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Username")
    private String username;

    @Column(name = "Pass")
    private String pass;

    @Column(name = "Status")
    private Integer status;

    @Column(name = "UserType")
    private String userType;

    @Column(name = "Mobile_number")
    private String mobileNumber;

    @Column(name = "OtpEnable")
    private String otpEnable;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "parentuser")
    private String parentuser;

    @Column(name = "botid")
    private String botid;

    @Column(name = "CreatedBy")
    private String createdBy;
}
