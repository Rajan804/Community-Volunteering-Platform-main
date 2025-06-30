package com.cvp.frontend.model;

import lombok.Data;

@Data
public class Rating {
    private Task task;
    private User user;
    private int ratingValue;
    private String review;
}
