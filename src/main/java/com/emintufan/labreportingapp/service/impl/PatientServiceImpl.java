package com.emintufan.labreportingapp.service.impl;

import com.emintufan.labreportingapp.dto.request.PatientReportSaveRequest;
import com.emintufan.labreportingapp.dto.request.PatientReportUpdateRequest;
import com.emintufan.labreportingapp.dto.request.PatientRequest;
import com.emintufan.labreportingapp.dto.response.PatientReportRequestForPatient;
import com.emintufan.labreportingapp.dto.response.PatientReportResponse;
import com.emintufan.labreportingapp.dto.response.PatientResponse;
import com.emintufan.labreportingapp.entity.Laborant;
import com.emintufan.labreportingapp.entity.Patient;
import com.emintufan.labreportingapp.entity.PatientReport;
import com.emintufan.labreportingapp.entity.Role;
import com.emintufan.labreportingapp.entity.enums.RoleEnum;
import com.emintufan.labreportingapp.exception.ReportNotFoundException;
import com.emintufan.labreportingapp.exception.UnauthorizedReportAccessException;
import com.emintufan.labreportingapp.exception.UserNotFoundException;
import com.emintufan.labreportingapp.mapper.ModelMapperConfig;
import com.emintufan.labreportingapp.repository.LaborantRepository;
import com.emintufan.labreportingapp.repository.PatientReportRepository;
import com.emintufan.labreportingapp.repository.PatientRepository;
import com.emintufan.labreportingapp.repository.RoleRepository;
import com.emintufan.labreportingapp.service.PatientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ModelMapperConfig modelMapperConfig;
    private final PatientReportRepository patientReportRepository;
    private final LaborantRepository laborantRepository;

    @Override
    public PatientResponse patientRegister(PatientRequest patientRequest) {

        Role userRole = roleRepository.findByRole(RoleEnum.ROLE_USER)
                .orElseGet(() -> roleRepository.save(Role.builder().role(RoleEnum.ROLE_USER).build()));

        Patient patient = Patient.builder()
                .name(patientRequest.getName())
                .surName(patientRequest.getSurName())
                .userName(patientRequest.getUserName())
                .password(passwordEncoder.encode(patientRequest.getPassword()))
                .identityNumber(patientRequest.getIdentityNumber())
                .reportRequest(false)
                .roles(List.of(userRole))
                .build();
        patientRepository.save(patient);
        return modelMapperConfig.modelMapper().map(patient, PatientResponse.class);
    }

    @Override
    public PatientResponse getPatientById(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new UserNotFoundException("Patient not found"));
        return modelMapperConfig.modelMapper().map(patient, PatientResponse.class);
    }

    @Override
    public Page<PatientResponse> getPatients(String name, String surName, Pageable pageable) {
        ModelMapper modelMapper = modelMapperConfig.modelMapper();
        return patientRepository.getPatientsFiltered(name, surName, pageable)
                .map(patient -> modelMapper.map(patient, PatientResponse.class));
    }

    @Override
    public Page<PatientReportResponse> getPatientReports(String name, String surName, String fileNumber, Long laborantId, Pageable pageable) {
        Page<PatientReport> pageResult = patientReportRepository.getReportsFilteredAndSorted(name, surName, fileNumber, laborantId, pageable);
        return pageResult.map(this::mapPatientReportToPatientReportResponse);

    }

    @Transactional
    @Override
    public PatientReportResponse addPatientReport(PatientReportSaveRequest patientReportSaveRequest)  {
        Long patientId = patientReportSaveRequest.getPatientId();
        Long laborantId = patientReportSaveRequest.getLaborantId();

        Patient patient = patientRepository.findById(patientId).orElseThrow(
                () -> new UserNotFoundException("Patient not found!"));

        Laborant laborant = laborantRepository.findById(laborantId).orElseThrow(
                () -> new UserNotFoundException("Laborant not found!"));

        patient.setReportRequest(false);

        PatientReport report = PatientReport.builder()
                .diagnosisTitle(patientReportSaveRequest.getDiagnosisTitle())
                .diagnosisDetail(patientReportSaveRequest.getDiagnosisDetail())
                .fileNumber(generateFileNumber())
                .issueDate(LocalDateTime.now())
                .patient(patient)
                .reportImage(patientReportSaveRequest.getReportImage())
                .laborant(laborant)
                .build();
        patientReportRepository.save(report);

        ModelMapper modelMapper = modelMapperConfig.modelMapper();
        return modelMapper.map(report, PatientReportResponse.class);
    }

    @Override
    public PatientReportResponse updatePatientReport(PatientReportUpdateRequest updateRequest) {

        PatientReport patientReport = patientReportRepository.findById(updateRequest.getReportId())
                .orElseThrow(
                        ()-> new ReportNotFoundException("Report not found!")
                );

        Laborant laborant = patientReport.getLaborant();
        if (!laborant.getId().equals(updateRequest.getLaborantId())) {
            throw new UserNotFoundException("Laborant id not equal request laborant id!");
        }

        patientReport.setDiagnosisTitle(updateRequest.getDiagnosisTitle());
        patientReport.setDiagnosisDetail(updateRequest.getDiagnosisDetail());
        patientReport.setIssueDate(LocalDateTime.now());
        if (updateRequest.getReportImage() != null) {
            patientReport.setReportImage(updateRequest.getReportImage());
        }
        patientReportRepository.save(patientReport);

        ModelMapper modelMapper = modelMapperConfig.modelMapper();
        return modelMapper.map(patientReport, PatientReportResponse.class);
    }

    @Override
    public Page<PatientReportResponse> getPatientReportById(Long reportId, Pageable pageable, String fileNumber) {
        Page<PatientReport> patientReport = patientReportRepository.findPatientReportByPatientId(reportId, fileNumber, pageable);
        return patientReport.map(this::mapPatientReportToPatientReportResponse);
    }

    @Override
    public void createRequestForReport(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(
                ()-> new UserNotFoundException("Patient not found!")
        );
        patient.setReportRequest(true);
        patientRepository.save(patient);
    }

    @Override
    public void deletePatientReport(Long patientReportId, Long laborantId) {
        PatientReport patientReport = patientReportRepository.findById(patientReportId)
                .orElseThrow(() -> new ReportNotFoundException("Report not found!"));

        if (patientReport.getLaborant().getId().equals(laborantId)) {
            patientReportRepository.deleteById(patientReportId);
        } else {
            throw new UnauthorizedReportAccessException("This report does not belong to the specified laborant");        }
    }

    @Override
    public PatientReportRequestForPatient isReportEnable(Long patientId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(
                ()-> new UserNotFoundException("Patient not found!")
        );
        return PatientReportRequestForPatient.
                builder()
                .reportRequest(patient.isReportRequest())
                .build();
    }

    private String generateFileNumber() {
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int length = 7;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public PatientReportResponse mapPatientReportToPatientReportResponse(PatientReport patientReport) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");
        return PatientReportResponse.builder()
                .patientName(patientReport.getPatient().getName())
                .reportId(patientReport.getId())
                .patientSurName(patientReport.getPatient().getSurName())
                .reportImage(patientReport.getReportImage())
                .identityNumber(patientReport.getPatient().getIdentityNumber())
                .laborantName(patientReport.getLaborant().getName())
                .diagnosisDetail(patientReport.getDiagnosisDetail())
                .diagnosisTitle(patientReport.getDiagnosisTitle())
                .laborantId(patientReport.getLaborant().getId())
                .issueDate(patientReport.getIssueDate().format(dateTimeFormatter))
                .laborantSurName(patientReport.getLaborant().getSurName())
                .fileNumber(patientReport.getFileNumber())
                .build();
    }

}
