package com.cvp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cvp.model.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    boolean existsByEmailOrPhoneNumber(String email, String phoneNumber);
    Optional<Organization> findByEmail(String email);

}
