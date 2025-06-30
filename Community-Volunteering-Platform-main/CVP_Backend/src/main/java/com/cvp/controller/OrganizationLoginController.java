// package com.cvp.controller;

// import com.cvp.dto.OrganizationLoginDto;
// import com.cvp.model.Organization;
// import com.cvp.service.OrganizationService;
// import jakarta.validation.Valid;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.validation.BindingResult;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/organization")
// @CrossOrigin(origins = "*")
// public class OrganizationLoginController {

//     private final OrganizationService organizationService;

//     @Autowired
//     public OrganizationLoginController(OrganizationService organizationService) {
//         this.organizationService = organizationService;
//     }

//     // Login endpoint for organization
//     @PostMapping("/login")
//     public ResponseEntity<?> loginOrganization(@Valid @RequestBody OrganizationLoginDto loginDto, BindingResult result) {
//         if (result.hasErrors()) {
//             return ResponseEntity.badRequest().body(result.getFieldErrors().get(0).getDefaultMessage());
//         }
//         try {
//             Organization organization = organizationService.login(loginDto.getEmail(), loginDto.getPassword());
//             return ResponseEntity.ok(organization);
//         } catch (RuntimeException e) {
//             // Return 401 Unauthorized if login fails
//             return ResponseEntity.status(401).body(e.getMessage());
//         }
//     }
// }
