package com.emintufan.labreportingapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientReportResponse {
    private Long reportId;
    private Long laborantId;
    private String patientName;
    private String patientSurName;
    private String laborantName;
    private String laborantSurName;
    private String identityNumber;
    private String fileNumber;
    private String diagnosisTitle;
    private String diagnosisDetail;
    private String reportImage;
    private String issueDate;
}
