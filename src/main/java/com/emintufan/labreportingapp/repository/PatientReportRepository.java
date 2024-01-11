package com.emintufan.labreportingapp.repository;

import com.emintufan.labreportingapp.entity.PatientReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PatientReportRepository extends JpaRepository<PatientReport, Long> {
    @Query("SELECT pr FROM PatientReport pr " +
            "WHERE " +
            "(pr.laborant.id = :laborantId) " +
            "AND (:name = '' OR UPPER(pr.patient.name) LIKE UPPER(CONCAT('%', :name, '%'))) " +
            "AND (:surName = '' OR UPPER(pr.patient.surName) LIKE UPPER(CONCAT('%', :surName, '%'))) " +
            "AND (:fileNumber = '' OR UPPER(pr.fileNumber) LIKE UPPER(CONCAT('%', :fileNumber, '%'))) ")
    Page<PatientReport> getReportsFilteredAndSorted(
            @Param("name") String name,
            @Param("surName") String surName,
            @Param("fileNumber") String fileNumber,
            @Param("laborantId") Long laborantId,
            Pageable pageable
    );

    @Query("SELECT pr FROM PatientReport pr " +
            "WHERE " +
            "(pr.patient.id = :patientId) " +
            "AND (:fileNumber = '' OR UPPER(pr.fileNumber) LIKE UPPER(CONCAT('%', :fileNumber, '%')))")
    Page<PatientReport> findPatientReportByPatientId(
            @Param("patientId") Long patientId,
            @Param("fileNumber") String fileNumber,
            Pageable pageable);

}
