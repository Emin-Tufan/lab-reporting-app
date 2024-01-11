package com.emintufan.labreportingapp.repository;

import com.emintufan.labreportingapp.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("SELECT pt FROM Patient pt " +
            "WHERE " +
            "(pt.reportRequest = true) " +
            "AND (pt.name = '' OR UPPER(pt.name) LIKE UPPER(CONCAT('%', :name, '%'))) " +
            "AND (pt.surName = '' OR UPPER(pt.surName) LIKE UPPER(CONCAT('%', :surName, '%')))")
    Page<Patient> getPatientsFiltered(
            @Param("name") String name,
            @Param("surName") String surName,
            Pageable pageable);
}