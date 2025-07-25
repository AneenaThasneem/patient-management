package com.pm.patientservice.repositories;

import com.pm.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    Boolean existsByEmail(String email);
    Boolean existsByEmailAndIdNot(String email,UUID id);

}
