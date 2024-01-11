package com.emintufan.labreportingapp.service;

import com.emintufan.labreportingapp.dto.request.PatientReportSaveRequest;
import com.emintufan.labreportingapp.dto.request.PatientReportUpdateRequest;
import com.emintufan.labreportingapp.dto.request.PatientRequest;
import com.emintufan.labreportingapp.dto.response.PatientReportRequestForPatient;
import com.emintufan.labreportingapp.dto.response.PatientReportResponse;
import com.emintufan.labreportingapp.dto.response.PatientResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientService {
    PatientResponse patientRegister(PatientRequest patientRequest);

    PatientResponse getPatientById(Long patientId);

    Page<PatientResponse> getPatients(String name, String surName, Pageable pageable);

    Page<PatientReportResponse> getPatientReports(String name, String surName, String fileNumber, Long laborantId, Pageable pageable);

    PatientReportResponse addPatientReport(PatientReportSaveRequest patientReportSaveRequest);

    PatientReportResponse updatePatientReport(PatientReportUpdateRequest updateRequest);

    Page<PatientReportResponse> getPatientReportById(Long reportId, Pageable pageable, String fileNumber);

    void createRequestForReport(Long patientId);

    void deletePatientReport(Long patientReportId, Long laborantId);

    PatientReportRequestForPatient isReportEnable(Long patientId);

}
