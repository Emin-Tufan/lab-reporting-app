package com.emintufan.labreportingapp.controller;

import com.emintufan.labreportingapp.dto.request.PatientReportSaveRequest;
import com.emintufan.labreportingapp.dto.request.PatientReportUpdateRequest;
import com.emintufan.labreportingapp.dto.response.LaborantResponse;
import com.emintufan.labreportingapp.dto.response.PatientReportResponse;
import com.emintufan.labreportingapp.dto.response.PatientResponse;
import com.emintufan.labreportingapp.service.LaborantService;
import com.emintufan.labreportingapp.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/laborant")
public class LaborantController {

    private final LaborantService laborantService;
    private final PatientService patientService;


    @PostMapping("/add-report")
    public ResponseEntity<PatientReportResponse> addReport(@RequestBody
                                                           @Valid PatientReportSaveRequest patientReportSaveRequest) {
        PatientReportResponse response = patientService.addPatientReport(patientReportSaveRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update-report")
    public ResponseEntity<PatientReportResponse> updateReport(@Valid @RequestBody PatientReportUpdateRequest updateRequest) {
        PatientReportResponse response = patientService.updatePatientReport(updateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/patient-reports")
    public ResponseEntity<Page<PatientReportResponse>> getReports(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String surName,
            @RequestParam(defaultValue = "") String fileNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "") Long laborantId,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        String sortField = "issueDate";
        Sort.Direction sort = sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort, sortField));
        return ResponseEntity.status(HttpStatus.OK).body(patientService
                .getPatientReports(name, surName, fileNumber, laborantId, pageable));
    }

    @GetMapping("/patients")
    public ResponseEntity<Page<PatientResponse>> getPatients(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "") String surName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PatientResponse> patients = patientService.getPatients(name, surName, pageable);
        return ResponseEntity.ok(patients);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatient(@PathVariable Long id) {
        PatientResponse response = patientService.getPatientById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/laborant/{id}")
    public ResponseEntity<LaborantResponse> getLaborant(@PathVariable Long id) {
        LaborantResponse response = laborantService.getLaborantById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{reportId}/{laborantId}")
    public void deleteReport(@PathVariable Long reportId,
                             @PathVariable Long laborantId) {
        patientService.deletePatientReport(reportId, laborantId);
    }
}
