package com.emintufan.labreportingapp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientReportRequest {

    @NotNull(message = "laborantId cannot be null")
    private Long laborantId;
    private boolean reportRequest;
    
}
