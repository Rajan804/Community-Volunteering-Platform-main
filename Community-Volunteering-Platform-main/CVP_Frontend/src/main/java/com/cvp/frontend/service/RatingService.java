package com.cvp.frontend.service;

import com.cvp.frontend.model.Rating;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class RatingService {

    private final RestTemplate restTemplate;
    private final String backendUrl = "http://localhost:7777/ratings";

    public RatingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void submitRating(Rating rating) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Rating> request = new HttpEntity<>(rating, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    backendUrl + "/submit", request, String.class
            );
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to submit rating");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public List<Map<String, Object>> getDetailedRatingsByUser(Long userId) {
        String url = backendUrl + "/rated-tasks/" + userId;
        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            return Collections.emptyList(); 
        }
    }

    public List<Long> getRatedTaskIdsByUser(Long userId) {
        String url = backendUrl + "/rated-task-ids/" + userId; // ✅ corrected endpoint
        try {
            ResponseEntity<List<Long>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            return List.of();
        }
    }
}