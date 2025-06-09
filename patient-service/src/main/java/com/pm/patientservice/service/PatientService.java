package com.pm.patientservice.service;

import billing.BillingServiceGrpc;
import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exceptions.EmailAlreadyExistsException;
import com.pm.patientservice.exceptions.PatientNotFoundException;
import com.pm.patientservice.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.kafka.KafkaProducer;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repositories.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
public class PatientService {


    private final PatientRepository patientRepository;
   private final BillingServiceGrpcClient billingServiceGrpcClient;
   private final KafkaProducer kafkaProducer;
    public PatientService(PatientRepository patientRepository,
                          BillingServiceGrpcClient billingServiceGrpcClient
                         ,KafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient=billingServiceGrpcClient;
        this.kafkaProducer=kafkaProducer;
    }


    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(patient -> PatientMapper.toDTO(patient)).toList();


    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("a patient with this email " + patientRequestDTO.getEmail() + " already exists");
        }
        Patient patient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        billingServiceGrpcClient.createBillingAccount(patient.getId().toString(),
                patient.getName(), patient.getEmail());
        kafkaProducer.sendEvent(patient);
        return PatientMapper.toDTO(patient);

    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {

        Patient patient=patientRepository.findById(id).orElseThrow(()->
                new PatientNotFoundException("Patient not found with id : "+ id));
        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),id)) {
            throw new EmailAlreadyExistsException("a patient with this email " + patientRequestDTO.getEmail() + " already exists");
        }
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        Patient updatedPatient=patientRepository.save(patient);
        return PatientMapper.toDTO(updatedPatient);


    }

    public void deletePatient(UUID id){
        patientRepository.deleteById(id);
    }

}
