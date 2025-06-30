package com.cvp.dto;

import jakarta.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotEmpty(message = "Provide value for First name")
    private String firstName;

    @NotEmpty(message = "Provide value for last name")
    private String lastName;

    @NotEmpty(message = "Provide value for User Name")
    private String userName;

    @NotEmpty(message = "Provide value for password")
    private String password;

    @NotEmpty(message = "Provide value for email")
    private String email;

    @NotEmpty(message = "Provide value for phoneNumber")
    private String phoneNumber;

    @NotEmpty(message = "Provide value for email")
    private String alternativeEmail;

    @NotEmpty(message = "Provide value for gender")
    private String gender;

}

