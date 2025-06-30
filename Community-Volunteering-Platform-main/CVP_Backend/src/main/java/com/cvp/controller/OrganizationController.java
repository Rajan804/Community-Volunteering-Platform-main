package com.cvp.controller;

import com.cvp.dto.OrganizationLoginDto;
import com.cvp.exception.InvalidEntityException;
import com.cvp.model.Organization;
import com.cvp.model.Task;
import com.cvp.model.TaskSignup;
import com.cvp.service.OrganizationService;
import com.cvp.service.TaskSignupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organization")
@CrossOrigin(origins = "*")
public class OrganizationController {

    private final OrganizationService organizationService;
    private final TaskSignupService taskSignupService;

    @Autowired
    public OrganizationController(OrganizationService organizationService, TaskSignupService taskSignupService) {
        this.organizationService = organizationService;
        this.taskSignupService = taskSignupService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        return ResponseEntity.ok(organizationService.getAllOrganizations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organization> getOrganization(@PathVariable Long id) throws InvalidEntityException {
        return ResponseEntity.ok(organizationService.getOrganizationById(id));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerOrganization(@Valid @RequestBody Organization organization, BindingResult result)
            throws InvalidEntityException {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldErrors().get(0).getDefaultMessage());
        }
        return ResponseEntity.ok(organizationService.registerOrganization(organization));
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateOrganization(@Valid @RequestBody Organization organization, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldErrors().get(0).getDefaultMessage());
        }
        Organization updated = organizationService.updateOrganization(organization);
        return ResponseEntity.ok(updated); // Return updated organization
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrganization(@PathVariable Long id) throws InvalidEntityException {
        return ResponseEntity.ok(organizationService.deleteOrganization(id));
    }

    @GetMapping("/{org_id}/tasks")
    public ResponseEntity<List<Task>> getTasksByOrganization(@PathVariable Long org_id) {
        List<Task> tasks = organizationService.getTasksByOrganizationId(org_id);
        if (tasks.isEmpty()) {
            throw new InvalidEntityException("No Task found for this Organization : " + org_id);
        }
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{org_id}/volunteers")
    public ResponseEntity<List<TaskSignup>> getVolunteersByOrganization(@PathVariable Long org_id) {
        List<TaskSignup> volunteers = taskSignupService.getVolunteersByOrganization(org_id);
        if (volunteers.isEmpty()) {
            throw new InvalidEntityException("No Volunteers found for this Organization : " + org_id);
        }
        return ResponseEntity.ok(volunteers);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginOrganization(@Valid @RequestBody OrganizationLoginDto loginDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getFieldErrors().get(0).getDefaultMessage());
        }
        try {
            Organization organization = organizationService.login(loginDto.getEmail(), loginDto.getPassword());
            return ResponseEntity.ok(organization);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
