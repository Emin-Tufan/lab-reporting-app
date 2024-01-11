package com.emintufan.labreportingapp.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequest {
    @NotEmpty(message = "name cannot be empty")
    private String name;

    @NotEmpty(message = "surName cannot be empty")
    private String surName;

    @NotEmpty(message = "userName cannot be empty")
    private String userName;

    @NotEmpty(message = "password cannot be empty")
    private String password;

    @NotEmpty(message = "identityNumber cannot be empty")
    @Size(min = 11, max = 11)
    private String identityNumber;
}
