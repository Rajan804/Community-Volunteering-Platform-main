package com.cvp.frontend.controller;

import com.cvp.frontend.model.User;
import com.cvp.frontend.service.UserService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private RestTemplate restTemplate;

    private final String BASE_URL = "http://localhost:7777";

    @Autowired
    private UserService userService;

    // Home Page
    @GetMapping("/")
    public String home() {
        return "homepage";
    }

    // Show Registration Form
    @GetMapping("/registerForm")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    // Register User
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            ResponseEntity<User> response = restTemplate.postForEntity(
                    BASE_URL + "/users/register", user, User.class);
            model.addAttribute("message", "User added successfully: " + response.getBody().getFirstName());
        } catch (HttpClientErrorException e) {
            handleException(e, model);
        }
        return "volunteer-login";
    }
    

    // View User Profile
    @GetMapping("/profile/{id}")
    public String viewProfile(@PathVariable Long id, Model model) {
        try {
            String apiUrl = BASE_URL + "/users/" + id;
            ResponseEntity<User> response = restTemplate.getForEntity(apiUrl, User.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("user", response.getBody());
            } else {
                model.addAttribute("errorMessage", "User not found");
            }
        } catch (HttpClientErrorException e) {
            handleException(e, model);
        }
        return "user_profile";
    }

    // Show Edit User Page
    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        try {
            String apiUrl = BASE_URL + "/users/" + id;
            ResponseEntity<User> response = restTemplate.getForEntity(apiUrl, User.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("user", response.getBody());
            } else {
                model.addAttribute("errorMessage", "User not found");
            }
        } catch (HttpClientErrorException e) {
            handleException(e, model);
        }
        return "edit_user";
    }

    // Process Update User
    @PutMapping("/updateUser/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user, Model model) {
    	
    	  try {
       
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<User> requestEntity = new HttpEntity<>(user, headers);
     
            ResponseEntity<User> response = restTemplate.exchange(
                BASE_URL + "/users/edit/" + id,
                HttpMethod.PUT,
                requestEntity,
                User.class
            );
            if (response.getStatusCode() == HttpStatus.OK) {
                model.addAttribute("newuser", "User Updated Successfully");
            } else {
                model.addAttribute("error", "Update failed.");
            }
            
    	  } catch (HttpClientErrorException e) {
              handleException(e, model);
          }
       
        return "edit_user";
    }


    // Delete User
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, Model model) {
        try {
            restTemplate.delete(BASE_URL + "/users/delete/" + id);
            model.addAttribute("message", "User deleted successfully");
        } catch (HttpClientErrorException e) {
            handleException(e, model);
        }
        return "redirect:/users/";
    }
    
    

    // Helper method to handle exceptions
    private void handleException(HttpClientErrorException e, Model model) {
        try {
            Map<String, String> errors = new ObjectMapper().readValue(
                    e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {});
            model.addAttribute("errorMessage", errors.get("message"));
        } catch ( JsonProcessingException ex) {
            model.addAttribute("errorMessage", "An error occurred while processing the request.");
        }
    }
}