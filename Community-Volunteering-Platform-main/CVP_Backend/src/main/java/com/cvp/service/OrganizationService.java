package com.cvp.service;

import com.cvp.repository.OrganizationRepository;
import com.cvp.repository.TaskRepository;
import com.cvp.exception.InvalidEntityException;
import com.cvp.model.Organization;
import com.cvp.model.Task;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {
    private static final Logger logger = LoggerFactory.getLogger(OrganizationService.class);
    
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository, PasswordEncoder passwordEncoder) {
        this.organizationRepository = organizationRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public boolean existsById(Long orgId) {
        return organizationRepository.existsById(orgId);
    }
    
    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }
    
    public Organization getOrganizationById(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new InvalidEntityException("Organization with ID " + id + " not found."));
    }
    
    public String registerOrganization(@Valid Organization organization) throws InvalidEntityException {
        boolean alreadyExists = organizationRepository.existsByEmailOrPhoneNumber(
                organization.getEmail(), organization.getPhoneNumber());
        if (alreadyExists) {
            throw new InvalidEntityException("An organization with this email or phone number already exists.");
        } else {
            organization.setPassword(passwordEncoder.encode(organization.getPassword()));
            organizationRepository.save(organization);
            
            String subject = "Welcome to Our Platform!";
            String text = "Dear " + organization.getName() + ",\n\n" +
                    "Thank you for registering your organization with us. " +
                    "We are excited to have you on board.\n\n" +
                    "Best Regards,\n" +
                    "Community Volunteering Platform";
            
            try {
                emailService.sendEmail(organization.getEmail(), subject, text);
            } catch (Exception e) {
                logger.error("Error sending welcome email to {}: {}", organization.getEmail(), e.getMessage());
            }
            
            return "Organization registration successful!";
        }
    }
    
    public Organization updateOrganization(@Valid Organization organization) {
        Optional<Organization> existingOrg = organizationRepository.findById(organization.getId());
        if (existingOrg.isPresent()) {
            if (!organization.getPassword().equals(existingOrg.get().getPassword())) {
                organization.setPassword(passwordEncoder.encode(organization.getPassword()));
            }
            return organizationRepository.save(organization); // Return updated organization
        } else {
            throw new InvalidEntityException("Organization not found.");
        }
    }

    
    public String deleteOrganization(Long id) throws InvalidEntityException {
        if (!organizationRepository.existsById(id)) {
            throw new InvalidEntityException("Organization with ID " + id + " not found.");
        }
        organizationRepository.deleteById(id);
        return "Organization deleted successfully!";
    }
    
    public List<Task> getTasksByOrganizationId(Long orgId) {
        List<Task> tasks = taskRepository.findByOrg_Id(orgId);
        if (tasks.isEmpty()) {
            throw new InvalidEntityException("No tasks found for Organization with ID " + orgId);
        }
        return tasks;
    }
    
    public Organization login(String email, String rawPassword) {
        Organization organization = organizationRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        
        if (!passwordEncoder.matches(rawPassword, organization.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        return organization;
    }
}
