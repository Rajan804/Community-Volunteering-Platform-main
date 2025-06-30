package com.cvp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "task_signup", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "volunteerName", "taskName", "signupDate" })
})
@Data
public class TaskSignup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Provide a value for volunteer name")
    @Size(max = 50, message = "Volunteer name must be at most 50 characters")
    @Column(length = 50)
    private String volunteerName;

    @NotEmpty(message = "Provide a value for task name")
    @Size(max = 100, message = "Task name must be at most 100 characters")
    @Column(length = 100)
    private String taskName;

    @FutureOrPresent(message = "Signup date should be either current or future date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false, updatable = false)
    private LocalDate signupDate = LocalDate.now();
    
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-taskSignup")
    private User user;
    
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    @JsonBackReference("task-taskSignup")
    private Task task;
}
