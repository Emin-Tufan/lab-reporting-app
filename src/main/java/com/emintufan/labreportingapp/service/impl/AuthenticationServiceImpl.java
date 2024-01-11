package com.emintufan.labreportingapp.service.impl;

import com.emintufan.labreportingapp.dto.request.LoginRequest;
import com.emintufan.labreportingapp.dto.response.LoginResponse;
import com.emintufan.labreportingapp.security.service.JwtService;
import com.emintufan.labreportingapp.security.service.UserDetailsImpl;
import com.emintufan.labreportingapp.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        String userName = loginRequest.getUserName();
        String password = loginRequest.getPassword();
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userName, password));
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        HashMap<String, Object> extraClaim = new HashMap<>();
        extraClaim.put("id",userDetails.getUser().getId());
        String token = jwtService.generateToken(userDetails,extraClaim);
        return LoginResponse.builder()
                .token(token)
                .build();
    }
}
