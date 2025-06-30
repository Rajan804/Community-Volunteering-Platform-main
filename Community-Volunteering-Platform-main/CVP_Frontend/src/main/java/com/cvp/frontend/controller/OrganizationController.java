package com.cvp.frontend.controller;

import com.cvp.frontend.model.Organization;
import com.cvp.frontend.model.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


@Controller
@RequestMapping("/organization")
public class OrganizationController {

    private final String BASE_URL = "http://localhost:7777/organization";
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/form")
        public String addOrganizationForm(Model model) {
        model.addAttribute("organizationModel", new Organization()); // ✅ Match Thymeleaf variable name
        return "add-organization";
    }


    @PostMapping("/register")
    public String registerOrganization(@ModelAttribute("organizationModel") Organization organization, Model model) {
        System.out.println("Sending Organization to Backend: " + organization);
    
        try {
            String apiUrl = BASE_URL+"/register";
    
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
    
            HttpEntity<Organization> requestEntity = new HttpEntity<>(organization, headers);
    
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);
            System.out.println("Backend Response: " + response.getBody()); // Debugging log
    
            if (response.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("message", "Organization registered successfully!");
                return "redirect:/auth/organizer";
            } else {
                model.addAttribute("message", "Failed to register organization.");
                return "add-organization";
            }
    
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage()); // Print error message
            model.addAttribute("message", "Error occurred while registering organization.");
            return "add-organization";
        }
    }
    


    @GetMapping("/view")
    public String viewOrganizations(Model model) {
        ResponseEntity<Organization[]> response = restTemplate.getForEntity(BASE_URL + "/all", Organization[].class);
        model.addAttribute("organizations", response.getBody());
        return "view-organization";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        ResponseEntity<Organization> response = restTemplate.getForEntity(BASE_URL + "/" + id, Organization.class);
        model.addAttribute("organization", response.getBody());
        return "edit-organization";
    }

    // @PostMapping("/update")
    // public String updateOrganization(@ModelAttribute("organization") Organization organization,
    //                                  BindingResult result, Model model) {
    //     try {
    //         restTemplate.put(BASE_URL + "/update", organization);
    //         return "redirect:/organization/view";
    //     } catch (HttpClientErrorException e) {
    //         handleValidationErrors(e, result);
    //     }
    //     return "edit-organization";
    // }

    @PostMapping("/update")
    public String updateOrganization(@ModelAttribute Organization organization,
                                     BindingResult result,
                                     Model model,
                                     HttpSession session) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Organization> requestEntity = new HttpEntity<>(organization, headers);
            ResponseEntity<Organization> response = restTemplate.exchange(
                    BASE_URL + "/update", HttpMethod.PUT, requestEntity, Organization.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // ✅ Update session attribute
                session.setAttribute("loggedInOrganization", response.getBody());

                model.addAttribute("updatedorg", "Organization Updated Successfully");
                return "redirect:/organization/profile";
            } else {
                model.addAttribute("error", "Update failed.");
            }

        } catch (HttpClientErrorException e) {
            handleValidationErrors(e, result);
        }

        return "edit-organization";
    }


    @GetMapping("/delete/{id}")
    public String deleteOrganization(@PathVariable Long id, Model model) {
        try {
            restTemplate.delete(BASE_URL + "/" + id);
            model.addAttribute("message", "Organization deleted successfully!");
        } catch (HttpClientErrorException e) {
            model.addAttribute("message", "Failed to delete organization: " + e.getMessage());
        }
        return "index";
    }

//     @GetMapping("/{org_id}/tasks/view")
//         public String viewTasksByOrganization(@PathVariable Long org_id, Model model) {
//     try {
//         String apiUrl = BASE_URL + "/" + org_id + "/tasks"; // Call backend API
        
//         ResponseEntity<Task[]> response = restTemplate.getForEntity(apiUrl, Task[].class);
//         Task[] tasks = response.getBody();

//         if (tasks == null || tasks.length == 0) {
//             model.addAttribute("errorMessage", "No tasks found for this organization.");
//         } else {
//             model.addAttribute("tasks", tasks);
//         }

//         model.addAttribute("org_id", org_id);
//         return "getTaskByOrganization"; // Load Thymeleaf template
//     } catch (HttpClientErrorException e) {
//         model.addAttribute("errorMessage", "Error fetching tasks: " + e.getMessage());
//         return "getTaskByOrganization";
//     }
// }

@GetMapping("/{id}/tasks")
public String getTasksByOrganization(@PathVariable Long id, Model model) {
    try {
    	model.addAttribute("org_id", id);
        String apiUrl = BASE_URL + "/" + id + "/tasks"; // Backend API
        ResponseEntity<Task[]> response = restTemplate.getForEntity(apiUrl, Task[].class);
        Task[] tasks = response.getBody();
        
        if (tasks == null || tasks.length == 0) {
            model.addAttribute("errorMessage", "No tasks available for this organization.");
            return "errorOrgTaskNot"; // Redirect to error page if no tasks are found
        } else {
            model.addAttribute("tasks", Arrays.asList(tasks));
        }
    } catch (HttpClientErrorException.NotFound e) { // Handling 404 errors
        model.addAttribute("errorMessage", "No tasks found for the given organization.");
        return "errorOrgTaskNot"; 
    } catch (Exception e) { 
        model.addAttribute("errorMessage", "Something went wrong. Please try again later.");
        return "errorOrgTaskNot";
    }

    return "getTaskByOrganization"; // Redirects to Thymeleaf task page
}


    private void handleValidationErrors(HttpClientErrorException e, BindingResult result) {
        try {
            Map<String, String> errors = new ObjectMapper().readValue(e.getResponseBodyAsString(), new TypeReference<>() {});
            errors.forEach(result::rejectValue);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
    }


    @GetMapping("/profile")
    public String showOrganizerProfile(HttpSession session, Model model) {
    Organization organization = (Organization) session.getAttribute("loggedInOrganization");

    if (organization == null) {
        return "redirect:/organizer"; // Redirect to login if not logged in
    }

    model.addAttribute("organization", organization);
    return "organizerProfile"; // Show profile page
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
    session.invalidate(); // Clear session
    return "redirect:/organizer"; // Redirect to login page
    }
}
