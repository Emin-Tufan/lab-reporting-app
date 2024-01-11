package com.emintufan.labreportingapp.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientReportSaveRequest {
    @NotNull(message = "patientId cannot be null")
    private Long patientId;

    @NotNull(message = "laborantId cannot be null")
    private Long laborantId;

    @Size(min = 1, message = "diagnosisTitle cannot be empty")
    private String diagnosisTitle;

    @Size(min = 1, message = "diagnosisDetail cannot be empty")
    private String diagnosisDetail;

    @NotNull(message = "report Image cannot be null")
    private String reportImage;
}

