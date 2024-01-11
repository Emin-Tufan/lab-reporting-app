package com.emintufan.labreportingapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "patient_reports")
public class PatientReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String diagnosisTitle;

    @Column(nullable = false, unique = true)
    private String fileNumber;

    @Column(nullable = false)
    private String diagnosisDetail;

    @Column(nullable = false)
    private LocalDateTime issueDate;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "laborant_id", nullable = false)
    private Laborant laborant;

    @Column(nullable = false)
    @Lob
    private String reportImage;
}