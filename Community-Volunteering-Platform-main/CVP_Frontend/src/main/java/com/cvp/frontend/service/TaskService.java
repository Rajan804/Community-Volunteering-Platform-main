package com.cvp.frontend.service;

import com.cvp.frontend.model.Task;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class TaskService {

    private final RestTemplate restTemplate;

    public TaskService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Get tasks the user has signed up for (used for rating eligibility)
    public List<Task> getSignedUpTasksForUser(Long userId) {
        ResponseEntity<Task[]> response = restTemplate.exchange(
            "http://localhost:7777/tasksignup/user/" + userId,
            HttpMethod.GET,
            null,
            Task[].class
        );
        return response.getBody() != null ? Arrays.asList(response.getBody()) : List.of();
    }


   
    
}
