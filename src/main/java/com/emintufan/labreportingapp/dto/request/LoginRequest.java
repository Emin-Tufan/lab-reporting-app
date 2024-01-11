package com.emintufan.labreportingapp.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotEmpty(message = "userName cannot be empty")
    private String userName;

    @NotEmpty(message = "password cannot be empty")
    private String password;
}
