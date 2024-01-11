package com.emintufan.labreportingapp.service;

import com.emintufan.labreportingapp.dto.request.LoginRequest;
import com.emintufan.labreportingapp.dto.response.LoginResponse;
import com.emintufan.labreportingapp.entity.Patient;
import com.emintufan.labreportingapp.security.service.JwtService;
import com.emintufan.labreportingapp.security.service.UserDetailsImpl;
import com.emintufan.labreportingapp.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_ValidCredentials_ShouldReturnLoginResponseWithToken() {

        LoginRequest loginRequest = new LoginRequest("username", "password");

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .user(Patient.builder().id(1L).build())
                .build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword())))
                .thenReturn(authentication);

        HashMap<String, Object> extraClaim = new HashMap<>();
        extraClaim.put("id", userDetails.getUser().getId());
        String expectedToken = "mockedToken";
        when(jwtService.generateToken(userDetails, extraClaim)).thenReturn(expectedToken);

        LoginResponse loginResponse = authenticationService.login(loginRequest);

        assertEquals(expectedToken, loginResponse.getToken());
    }

}
