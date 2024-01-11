package com.emintufan.labreportingapp.service.impl;

import com.emintufan.labreportingapp.dto.response.LaborantResponse;
import com.emintufan.labreportingapp.entity.Laborant;
import com.emintufan.labreportingapp.exception.UserNotFoundException;
import com.emintufan.labreportingapp.mapper.ModelMapperConfig;
import com.emintufan.labreportingapp.repository.LaborantRepository;
import com.emintufan.labreportingapp.service.LaborantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LaborantServiceImpl implements LaborantService {

    private final LaborantRepository laborantRepository;
    private final ModelMapperConfig modelMapperConfig;

    @Override
    public LaborantResponse getLaborantById(Long id) {
        Laborant laborant = laborantRepository.findById(id).orElseThrow(
                ()-> new UserNotFoundException("Laborant not found!"));
        return modelMapperConfig.modelMapper().map(laborant,LaborantResponse.class);
    }
}
