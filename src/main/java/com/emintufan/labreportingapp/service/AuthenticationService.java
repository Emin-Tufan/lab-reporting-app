package com.emintufan.labreportingapp.service;

import com.emintufan.labreportingapp.dto.request.LoginRequest;
import com.emintufan.labreportingapp.dto.response.LoginResponse;

public interface AuthenticationService {
    LoginResponse login(LoginRequest loginRequest);
}
