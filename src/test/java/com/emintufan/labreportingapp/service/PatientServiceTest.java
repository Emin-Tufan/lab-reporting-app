package com.emintufan.labreportingapp.service;

import com.emintufan.labreportingapp.dto.request.PatientReportSaveRequest;
import com.emintufan.labreportingapp.dto.request.PatientReportUpdateRequest;
import com.emintufan.labreportingapp.dto.request.PatientRequest;
import com.emintufan.labreportingapp.dto.response.PatientReportRequestForPatient;
import com.emintufan.labreportingapp.dto.response.PatientReportResponse;
import com.emintufan.labreportingapp.dto.response.PatientResponse;
import com.emintufan.labreportingapp.entity.Laborant;
import com.emintufan.labreportingapp.entity.Patient;
import com.emintufan.labreportingapp.entity.PatientReport;
import com.emintufan.labreportingapp.entity.Role;
import com.emintufan.labreportingapp.entity.enums.RoleEnum;
import com.emintufan.labreportingapp.exception.ReportNotFoundException;
import com.emintufan.labreportingapp.exception.UnauthorizedReportAccessException;
import com.emintufan.labreportingapp.exception.UserNotFoundException;
import com.emintufan.labreportingapp.mapper.ModelMapperConfig;
import com.emintufan.labreportingapp.repository.LaborantRepository;
import com.emintufan.labreportingapp.repository.PatientReportRepository;
import com.emintufan.labreportingapp.repository.PatientRepository;
import com.emintufan.labreportingapp.repository.RoleRepository;
import com.emintufan.labreportingapp.service.impl.PatientServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceTest {
    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ModelMapperConfig modelMapperConfig;

    @Mock
    private PatientReportRepository patientReportRepository;

    @Mock
    private LaborantRepository laborantRepository;
    @InjectMocks
    private PatientServiceImpl patientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void patientRegister_ShouldSuccessfullyRegisterPatientAndReturnResponse() {
        PatientRequest patientRequest = new PatientRequest("Emin", "Tufan", "emin_tufan", "password", "1234567890");
        Role userRole = new Role(1L, RoleEnum.ROLE_USER);
        ModelMapper modelMapper = new ModelMapper();

        when(roleRepository.findByRole(RoleEnum.ROLE_USER)).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(patientRequest.getPassword())).thenReturn("password");
        when(modelMapperConfig.modelMapper()).thenReturn(modelMapper);

        PatientResponse patientResponse = patientService.patientRegister(patientRequest);

        ArgumentCaptor<Patient> patientCaptor = ArgumentCaptor.forClass(Patient.class);
        verify(patientRepository, times(1)).save(patientCaptor.capture());
        Patient capturedPatient = patientCaptor.getValue();

        assertEquals(patientResponse.getName(), capturedPatient.getName());
        assertEquals(patientResponse.getSurName(), capturedPatient.getSurName());
        assertEquals(patientResponse.getUserName(), capturedPatient.getUserName());
        assertEquals("password", capturedPatient.getPassword());
        assertEquals(patientResponse.getIdentityNumber(), capturedPatient.getIdentityNumber());
        assertFalse(capturedPatient.isReportRequest());
        assertTrue(capturedPatient.getRoles().contains(userRole));
    }

    @Test
    void patientRegister_UserRoleNotPresentAndSavedRoleRepository_ShouldCreateRoleAndRegisterPatient() {
        PatientRequest patientRequest = new PatientRequest("Emin", "Tufan", "emin_tufan", "password", "1234567890");
        ModelMapper modelMapper = new ModelMapper();
        Role role = Role.builder().role(RoleEnum.ROLE_USER).build();

        when(roleRepository.findByRole(RoleEnum.ROLE_USER)).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenReturn(role);
        when(modelMapperConfig.modelMapper()).thenReturn(modelMapper);

        patientService.patientRegister(patientRequest);

        ArgumentCaptor<Patient> argumentCaptor = ArgumentCaptor.forClass(Patient.class);
        verify(patientRepository).save(argumentCaptor.capture());

        assertTrue(argumentCaptor.getValue().getRoles().contains(role));
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void getPatients_FilteredByNameOrSurnameReturnPatientResponse() {
        String nameFilter = "testName";
        String surNameFilter = "testSurname";
        Pageable pageable = PageRequest.of(0, 10);

        ModelMapper modelMapper = new ModelMapper();
        when(modelMapperConfig.modelMapper()).thenReturn(modelMapper);

        Patient patient = Patient.builder()
                .build();
        Patient patient1 = Patient.builder()
                .build();

        List<Patient> patientList = new ArrayList<>();
        patientList.add(patient);
        patientList.add(patient1);

        when(patientRepository.getPatientsFiltered(nameFilter, surNameFilter, pageable))
                .thenReturn(new PageImpl<>(patientList, pageable, patientList.size()));

        Page<PatientResponse> response = patientService.getPatients(nameFilter, surNameFilter, pageable);

        Assertions.assertEquals(patientList.size(), response.getTotalElements());
        Assertions.assertEquals(10, response.getSize());
    }

    @Test
    void addPatientReport_PatientAndLaborantExist_ShouldAddReportAndReturnResponse() {
        Long patientId = 1L;
        Long laborantId = 2L;
        String identityNumber = "12345678910";
        Role roleUser = Role.builder().role(RoleEnum.ROLE_USER).build();
        Role roleAdmin = Role.builder().role(RoleEnum.ROLE_ADMIN).build();
        ModelMapper modelMapper = new ModelMapper();

        Patient patient = Patient.builder()
                .id(patientId)
                .name("emin")
                .surName("tufan")
                .userName("emint")
                .password(passwordEncoder.encode("password"))
                .identityNumber(identityNumber)
                .roles(List.of(roleUser))
                .build();

        Laborant laborant = Laborant.builder()
                .id(laborantId)
                .name("test name")
                .surName("test surname")
                .userName("test username")
                .password(passwordEncoder.encode("password"))
                .hospitalId("test hospital")
                .roles(List.of(roleAdmin))
                .build();

        PatientReportSaveRequest patientReportSaveRequest = PatientReportSaveRequest.builder()
                .patientId(patientId)
                .laborantId(laborantId)
                .reportImage("image")
                .diagnosisTitle("test title")
                .diagnosisDetail("test detail")
                .build();

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(laborantRepository.findById(laborantId)).thenReturn(Optional.of(laborant));
        when(passwordEncoder.encode(any(String.class))).thenReturn("password");
        when(modelMapperConfig.modelMapper()).thenReturn(modelMapper);

        PatientReportResponse patientReportResponse = patientService.addPatientReport(patientReportSaveRequest);

        ArgumentCaptor<PatientReport> argumentCaptor = ArgumentCaptor.forClass(PatientReport.class);
        verify(patientReportRepository, times(1)).save(argumentCaptor.capture());
        PatientReport patientReport = argumentCaptor.getValue();

        assertEquals(patientReport.getId(), patientReportResponse.getReportId());
        assertEquals(laborant.getId(), patientReportResponse.getLaborantId());
        assertEquals(patient.getName(), patientReportResponse.getPatientName());
        assertEquals(patient.getSurName(), patientReportResponse.getPatientSurName());
        assertEquals(laborant.getName(), patientReportResponse.getLaborantName());
        assertEquals(laborant.getSurName(), patientReportResponse.getLaborantSurName());
        assertEquals(patientReport.getFileNumber(), patientReportResponse.getFileNumber());
        assertEquals(patientReport.getDiagnosisTitle(), patientReportResponse.getDiagnosisTitle());
        assertEquals(patientReport.getDiagnosisDetail(), patientReportResponse.getDiagnosisDetail());
        assertEquals(patientReport.getReportImage(), patientReportResponse.getReportImage());
        assertEquals(patient.getIdentityNumber(), patientReport.getPatient().getIdentityNumber());
        assertNotNull(patientReportResponse.getIssueDate());
    }

    @Test
    void addPatientReport_PatientNotFound_ShouldThrowUsernameNotFoundException() {
        Long patientId = 1L;
        Long laborantId = 2L;

        PatientReportSaveRequest patientReportSaveRequest = PatientReportSaveRequest.builder()
                .patientId(patientId)
                .laborantId(laborantId)
                .build();
        when(patientRepository.findById(patientId)).thenThrow(
                new UserNotFoundException("Patient not found!"));
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            patientService.addPatientReport(patientReportSaveRequest);
        });
    }

    @Test
    void addPatientReport_LaborantNotFound_ShouldThrowUsernameNotFoundException() {
        Long patientId = 1L;
        Long laborantId = 2L;

        PatientReportSaveRequest patientReportSaveRequest = PatientReportSaveRequest.builder()
                .patientId(patientId)
                .laborantId(laborantId)
                .build();
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(Patient.builder().build()));
        when(laborantRepository.findById(laborantId)).thenThrow(
                new UserNotFoundException("Laborant not found!"));
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            patientService.addPatientReport(patientReportSaveRequest);
        });
    }

    @Test
    void updatePatientReport_SuccessfulUpdate_ShouldReturnUpdatedReportResponse() {
        Long reportId = 1L;
        Long patientId = 1L;
        Long laborantId = 2L;
        LocalDateTime dateTime = LocalDateTime.now();
        String identityNumber = "12345678910";

        Role roleUser = Role.builder().role(RoleEnum.ROLE_USER).build();
        Role roleAdmin = Role.builder().role(RoleEnum.ROLE_ADMIN).build();
        ModelMapper modelMapper = new ModelMapper();

        PatientReportUpdateRequest updateRequest = PatientReportUpdateRequest.builder()
                .reportId(reportId)
                .laborantId(laborantId)
                .diagnosisDetail("test detail")
                .diagnosisTitle("test title")
                .reportImage("image")
                .build();

        Patient patient = Patient.builder()
                .id(patientId)
                .name("emin")
                .surName("tufan")
                .userName("emint")
                .password(passwordEncoder.encode("password"))
                .identityNumber(identityNumber)
                .roles(List.of(roleUser))
                .build();

        Laborant laborant = Laborant.builder()
                .id(laborantId)
                .name("test name")
                .surName("test surname")
                .userName("test username")
                .password(passwordEncoder.encode("password"))
                .hospitalId("test hospital")
                .roles(List.of(roleAdmin))
                .build();

        PatientReport report = PatientReport.builder()
                .id(1L)
                .fileNumber("filenumber")
                .issueDate(dateTime)
                .patient(patient)
                .laborant(laborant)
                .diagnosisTitle("title")
                .diagnosisDetail("detail")
                .reportImage("image")
                .build();

        when(patientReportRepository.findById(1L)).thenReturn(Optional.of(report));
        when(modelMapperConfig.modelMapper()).thenReturn(modelMapper);

        PatientReportResponse patientReportResponse = patientService.updatePatientReport(updateRequest);

        assertEquals(updateRequest.getDiagnosisTitle(), patientReportResponse.getDiagnosisTitle());
        assertEquals(updateRequest.getDiagnosisDetail(), patientReportResponse.getDiagnosisDetail());
        assertEquals(updateRequest.getReportImage(), patientReportResponse.getReportImage());
        assertEquals(patient.getName(), patientReportResponse.getPatientName());
        assertEquals(patient.getSurName(), patientReportResponse.getPatientSurName());
        assertEquals(laborant.getName(), patientReportResponse.getLaborantName());
        assertEquals(laborant.getSurName(), patientReportResponse.getLaborantSurName());
        assertEquals(identityNumber, patient.getIdentityNumber());
        assertEquals(report.getFileNumber(), patientReportResponse.getFileNumber());
        assertNotNull(patientReportResponse.getIssueDate());
    }

    @Test
    void updatePatientReport_InvalidReportId_ShouldThrowReportNotFoundException() {

        Long reportId = 1L;
        PatientReportUpdateRequest updateRequest = PatientReportUpdateRequest.builder().build();

        when(patientReportRepository.findById(reportId)).thenThrow(
                new ReportNotFoundException("Report not found!")
        );

        Assertions.assertThrows(ReportNotFoundException.class, () -> {
            patientService.updatePatientReport(updateRequest);
        });
    }

    @Test
    void updatePatientReport_MismatchedLaborantId_ShouldThrowUserNotFoundException() {
        Long reportId = 1L;
        Long diffrentLaborantId = 2L;
        Long patientId = 1L;
        Long laborantId = 1L;

        PatientReportUpdateRequest updateRequest = PatientReportUpdateRequest.builder()
                .reportId(reportId)
                .laborantId(diffrentLaborantId)
                .build();

        Patient patient = Patient.builder()
                .id(patientId)
                .build();

        Laborant laborant = Laborant.builder()
                .id(laborantId)
                .build();

        PatientReport report = PatientReport.builder()
                .id(reportId)
                .laborant(laborant)
                .patient(patient)
                .build();

        when(patientReportRepository.findById(reportId)).thenReturn(Optional.of(report));

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            patientService.updatePatientReport(updateRequest);
        });
    }

    @Test
    void createRequestForReport_ValidPatientId_ShouldSetReportRequestTrue() {
        Long patientId = 1L;
        Patient patient = new Patient();
        patient.setId(patientId);

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        patientService.createRequestForReport(patientId);
        Assertions.assertTrue(patient.isReportRequest());
    }

    @Test
    void createRequestForReport_InvalidPatientId_ShouldThrowUserNotFoundException() {
        Long patientId = 1L;
        when(patientRepository.findById(patientId)).thenThrow(
                new UserNotFoundException("Patient not found!"));
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            patientService.createRequestForReport(patientId);
        });
    }

    @Test
    void deletePatientReport_ValidReportAndMatchingLaborantId_ShouldDeleteReport() {
        Long laborantId = 1L;
        Long reportId = 1L;

        Laborant laborant = Laborant.builder()
                .id(laborantId)
                .build();

        PatientReport patientReport = PatientReport.builder()
                .laborant(laborant)
                .id(reportId)
                .build();

        when(patientReportRepository.findById(reportId)).thenReturn(Optional.of(patientReport));
        patientService.deletePatientReport(reportId, laborantId);
    }

    @Test
    void deletePatientReport_InvalidReportId_ShouldThrowReportNotFoundException() {
        Long reportId = 1L;
        Long laborantId = 1L;

        when(patientReportRepository.findById(reportId)).thenThrow(
                new ReportNotFoundException("Report not found!"));
        Assertions.assertThrows(ReportNotFoundException.class, () -> {
            patientService.deletePatientReport(reportId, laborantId);
        });
    }

    @Test
    void deletePatientReport_MismatchedLaborantId_ShouldThrowUnauthorizedReportAccessException() {

        Long laborantId = 2L;
        Long differentLaborantId = 3L;
        Long reportId = 1L;

        Laborant laborant = Laborant.builder()
                .id(differentLaborantId)
                .build();

        PatientReport patientReport = PatientReport.builder()
                .laborant(laborant)
                .id(reportId)
                .build();

        when(patientReportRepository.findById(reportId)).thenReturn(Optional.of(patientReport));

        Assertions.assertThrows(UnauthorizedReportAccessException.class, () ->
        {
            patientService.deletePatientReport(reportId, laborantId);
        });
    }

    @Test
    void isReportEnable_ValidPatientId_ShouldReturnReportRequestForPatient() {

        Long patientId = 1L;
        Patient patient = Patient.builder()
                .id(patientId)
                .reportRequest(true)
                .build();

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        PatientReportRequestForPatient response = patientService.isReportEnable(patientId);

        Assertions.assertEquals(patient.isReportRequest(), response.isReportRequest());
    }

    @Test
    void isReportEnable_InvalidPatientId_ShouldThrowUserNotFoundException() {
        Long patientId = 1L;
        when(patientRepository.findById(patientId)).thenThrow(
                new UserNotFoundException("Patient not found!"));

        Assertions.assertThrows(UserNotFoundException.class, () ->
        {
            patientService.isReportEnable(patientId);
        });

    }


}
