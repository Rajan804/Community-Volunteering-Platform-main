package com.cvp.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "User")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Provide value for First name")
    @Column(length = 50)
    private String firstName;

    @NotEmpty(message = "Provide value for last name")
    @Column(length = 50)
    private String lastName;

    @Column(nullable = false)
    private String userName;

    @NotEmpty(message = "Provide value for password")
    @Size(min = 8, message = "Your Password must be at least 8 character long")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?]).{8,}$", message = "Password must contain at least one letter, one number, and one special character")
    @Column(nullable = false)
    private String password;

    @NotEmpty(message = "Provide value for email")
    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$", message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Provide value for phoneNumber")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile Number must be 10 digits")
    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @NotEmpty(message = "Provide value for email")
    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$", message = "Invalid email format")
    private String alternativeEmail;

    @NotEmpty(message = "Provide value for gender")
    @Column(nullable = false)
    private String gender;

    private String resetToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskSignup> taskSignups;

}
