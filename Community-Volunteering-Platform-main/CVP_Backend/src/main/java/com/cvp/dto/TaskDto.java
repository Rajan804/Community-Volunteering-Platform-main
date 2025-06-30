package com.cvp.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.cvp.enums.Category;
import com.cvp.enums.Priority;
import com.cvp.enums.Status;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    @NotEmpty(message = "Provide value for title")
    private String title;

    @NotEmpty(message = "Provide value for description")
    private String description;

    @NotEmpty(message = "Provide value for location")
    private String location;

    @NotNull(message = "Provide value for status")
    private Status status;

    @NotNull(message = "Provide value for priority")
    private Priority priority;

    @NotNull(message = "Provide value for category")
    private Category category;

    @NotNull(message = "Provide value for event date")
    @FutureOrPresent(message = "Event start date should be either current or future date")
    private LocalDate eventDate;

    @NotNull(message = "Provide value for end date")
    @FutureOrPresent(message = "Event end date should be either current or future date")
    private LocalDate endDate;
}

