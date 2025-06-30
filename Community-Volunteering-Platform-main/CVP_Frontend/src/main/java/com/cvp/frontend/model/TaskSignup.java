package com.cvp.frontend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskSignup {
    private Long id;
    private String volunteerName;
    private String taskName;
    private LocalDate signupDate;
    private User user; 
}
