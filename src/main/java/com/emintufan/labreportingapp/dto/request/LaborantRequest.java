package com.emintufan.labreportingapp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LaborantRequest {

    private String name;
    private String surName;
    private String userName;
    private String password;
    private String hospitalId;
}
