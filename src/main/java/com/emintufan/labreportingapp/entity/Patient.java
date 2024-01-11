package com.emintufan.labreportingapp.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "patients")
public class Patient extends User {

    @Column(name = "identity_number")
    private String identityNumber;

    private boolean reportRequest;
}
