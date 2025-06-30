package com.cvp.frontend.model;

import lombok.Data;

@Data
public class User {
	
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private String phoneNumber;
    private String alternativeEmail;
    private String gender;

}
