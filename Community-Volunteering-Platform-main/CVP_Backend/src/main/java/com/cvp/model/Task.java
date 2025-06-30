package com.cvp.model;

import java.time.*;

import com.cvp.enums.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "task")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Provide value for title")
    @Column(length = 50, nullable = false)
    private String title;

    @NotEmpty(message = "Provide value for description")
    @Column(length = 255, nullable = false)
    private String description;

    @NotEmpty(message = "Provide value for location")
    @Column(length = 100, nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Provide value for status")
    private Status status = Status.PENDING; // Enum for pending, in progress, completed

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Provide value for priority")
    private Priority priority; // Enum for low, medium, high

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Provide value for category")
    private Category category; // Enum for education, health, environment

    @NotNull(message = "Provide value for event date")
    @Column(nullable = false)
    @FutureOrPresent(message = "Event start date should be either current or future date")
    private LocalDate eventDate;

    @Column(nullable = false)
    @FutureOrPresent(message = "Event End date should be either current or future date")
    private LocalDate endDate;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id")
    @JsonBackReference 
    private Organization org;

    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (eventDate != null && endDate != null && endDate.isBefore(eventDate)) {
            throw new IllegalArgumentException("End date cannot be before event date.");
        }
    }
}
