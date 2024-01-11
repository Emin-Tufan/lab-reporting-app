package com.emintufan.labreportingapp.repository;

import com.emintufan.labreportingapp.entity.Role;
import com.emintufan.labreportingapp.entity.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(RoleEnum roleEnum);
}
