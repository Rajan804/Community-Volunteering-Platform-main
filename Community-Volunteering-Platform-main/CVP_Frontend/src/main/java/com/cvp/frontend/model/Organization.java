package com.cvp.frontend.model;

import lombok.Data;

@Data
public class Organization {
    private Long id;
    private String name;
    private String website;
    private String location;
    private String email;
    private String phoneNumber;
    private String password;
    
}

