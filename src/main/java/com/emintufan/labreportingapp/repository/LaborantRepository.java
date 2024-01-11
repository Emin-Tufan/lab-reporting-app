package com.emintufan.labreportingapp.repository;

import com.emintufan.labreportingapp.entity.Laborant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaborantRepository extends JpaRepository<Laborant, Long> {
}
