package com.emintufan.labreportingapp.controller;

import com.emintufan.labreportingapp.dto.response.PatientReportRequestForPatient;
import com.emintufan.labreportingapp.dto.response.PatientReportResponse;
import com.emintufan.labreportingapp.dto.response.PatientResponse;
import com.emintufan.labreportingapp.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/patient")
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<Page<PatientReportResponse>> getPatientReport(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String fileNumber,
            @RequestParam Long patientId) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(patientService.getPatientReportById(patientId, pageable, fileNumber));
    }

    @PostMapping
    public ResponseEntity createRequestForReport(@RequestParam Long patientId) {
        patientService.createRequestForReport(patientId);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping("/isEnable")
    public ResponseEntity<PatientReportRequestForPatient> isReportRequestEnable(@RequestParam Long patientId) {
        PatientReportRequestForPatient patient = patientService.isReportEnable(patientId);
        return ResponseEntity.ok(patient);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatient(@PathVariable Long id) {
        PatientResponse response = patientService.getPatientById(id);
        return ResponseEntity.ok(response);
    }

}
