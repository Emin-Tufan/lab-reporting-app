package com.emintufan.labreportingapp.controller;

import com.emintufan.labreportingapp.dto.request.LoginRequest;
import com.emintufan.labreportingapp.dto.request.PatientRequest;
import com.emintufan.labreportingapp.dto.response.LoginResponse;
import com.emintufan.labreportingapp.dto.response.PatientResponse;
import com.emintufan.labreportingapp.service.AuthenticationService;
import com.emintufan.labreportingapp.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final PatientService patientService;

    @PostMapping("/register")
    public ResponseEntity<PatientResponse> patientRegister(@Valid @RequestBody PatientRequest patientRequest) {
        PatientResponse patientResponse = patientService.patientRegister(patientRequest);
        return ResponseEntity.ok(patientResponse);
    }

    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> signIn(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authenticationService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
