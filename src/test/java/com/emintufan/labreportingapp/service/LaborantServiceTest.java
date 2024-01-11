package com.emintufan.labreportingapp.service;

import com.emintufan.labreportingapp.dto.response.LaborantResponse;
import com.emintufan.labreportingapp.entity.Laborant;
import com.emintufan.labreportingapp.entity.Role;
import com.emintufan.labreportingapp.entity.enums.RoleEnum;
import com.emintufan.labreportingapp.exception.UserNotFoundException;
import com.emintufan.labreportingapp.mapper.ModelMapperConfig;
import com.emintufan.labreportingapp.repository.LaborantRepository;
import com.emintufan.labreportingapp.repository.RoleRepository;
import com.emintufan.labreportingapp.service.impl.LaborantServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class LaborantServiceTest {

    @Mock
    private LaborantRepository laborantRepository;

    @Mock
    private ModelMapperConfig modelMapperConfig;

    @InjectMocks
    private LaborantServiceImpl laborantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getLaborantById_LaborantNotFound_ShouldThrowUserNotFoundException() {
        Long laborantId = 1L;

        when(laborantRepository.findById(laborantId)).thenThrow(
                new UserNotFoundException("Laborant not found!"));
        Assertions.assertThrows(UserNotFoundException.class, () ->
        {
            laborantService.getLaborantById(laborantId);
        });
    }

    @Test
    void getLaborantById_ExistingLaborantId_ShouldReturnLaborantResponse() {
        Long laborantId = 1L;
        Role role = Role.builder().role(RoleEnum.ROLE_ADMIN).build();
        ModelMapper modelMapper = new ModelMapper();

        Laborant laborant = Laborant.builder()
                .id(laborantId)
                .name("emin")
                .surName("tufan")
                .roles(List.of(role))
                .password("passowrd")
                .userName("emint")
                .hospitalId("testid")
                .build();

        when(laborantRepository.findById(laborantId)).thenReturn(Optional.of(laborant));
        when(modelMapperConfig.modelMapper()).thenReturn(modelMapper);

        LaborantResponse laborantResponse = laborantService.getLaborantById(laborantId);

        assertNotNull(laborantResponse);
        assertEquals(laborant.getName(), laborantResponse.getName());
        assertEquals(laborant.getSurName(), laborantResponse.getSurName());
    }

}
