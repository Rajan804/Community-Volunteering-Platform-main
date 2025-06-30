package com.cvp.service;

import com.cvp.dto.RatingResponseDTO;
import com.cvp.model.Rating;
import com.cvp.model.Task;
import com.cvp.repository.RatingRepository;
import com.cvp.repository.TaskRepository;
import com.cvp.repository.TaskSignupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final TaskRepository taskRepository;
    private final TaskSignupRepository taskSignupRepository;

    public RatingService(RatingRepository ratingRepository, TaskRepository taskRepository, TaskSignupRepository taskSignupRepository) {
        this.ratingRepository = ratingRepository;
        this.taskRepository = taskRepository;
        this.taskSignupRepository = taskSignupRepository;
    }

    public Rating saveRating(Rating rating) {
        Long userId = rating.getUser().getId();
        Long taskId = rating.getTask().getId();

        // ✅ Check if task exists
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        // ✅ Check if task is completed
        if (!"COMPLETED".equals(task.getStatus().name())) {
            throw new IllegalStateException("Rating can only be submitted for completed tasks.");
        }

        // ✅ Check if user already rated this task
        boolean alreadyRated = ratingRepository.existsByUserIdAndTaskId(userId, taskId);
        if (alreadyRated) {
            throw new IllegalStateException("You already rated this task.");
        }

        // ✅ Check if user signed up for this task
        boolean isUserSignedUp = taskSignupRepository.findTasksByUserId(userId)
                .stream()
                .anyMatch(t -> t.getId().equals(taskId));

        if (!isUserSignedUp) {
            throw new RuntimeException("You can only rate tasks you have signed up for.");
        }

        return ratingRepository.save(rating);
    }

    public List<RatingResponseDTO> getRatingsByUserId(Long userId) {
        List<Rating> ratings = ratingRepository.findAll()
                .stream()
                .filter(r -> r.getUser().getId().equals(userId))
                .toList();

        if (ratings.isEmpty()) {
            return List.of();
        }

        return ratings.stream()
                .map(r -> new RatingResponseDTO(
                        r.getTask().getTitle(),
                        r.getRatingValue(),
                        r.getReview() != null && !r.getReview().isBlank() ? r.getReview() : "No review"
                ))
                .toList();
    }
    
    
    public List<Long> getRatedTaskIdsByUserId(Long userId) {
        return ratingRepository.findRatedTaskIdsByUserId(userId);
    }


    public List<Rating> getRatingsForTask(Long taskId) {
        return ratingRepository.findByTaskId(taskId);
    }
}
