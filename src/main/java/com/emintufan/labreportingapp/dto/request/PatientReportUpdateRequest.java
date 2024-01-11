package com.emintufan.labreportingapp.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientReportUpdateRequest {
    @NotNull(message = "reportId cannot be null")
    private Long reportId;

    @NotNull(message = "laborantId cannot be null")
    private Long laborantId;

    @Size(min = 1, message = "diagnosisTitle cannot be empty")
    private String diagnosisTitle;

    @Size(min = 1, message = "diagnosisDetail cannot be empty")
    private String diagnosisDetail;

    private String reportImage;
}
