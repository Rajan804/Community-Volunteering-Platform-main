package com.cvp.dto;

public class RatingResponseDTO {
    private String taskName;
    private int ratingValue;
    private String review;

    public RatingResponseDTO(String taskName, int ratingValue, String review) {
        this.taskName = taskName;
        this.ratingValue = ratingValue;
        this.review = (review == null || review.trim().isEmpty()) ? "No review" : review;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = (review == null || review.trim().isEmpty()) ? "No review" : review;
    }
}
