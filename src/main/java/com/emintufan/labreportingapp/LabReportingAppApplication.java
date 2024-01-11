package com.emintufan.labreportingapp;

import com.emintufan.labreportingapp.entity.Laborant;
import com.emintufan.labreportingapp.entity.Patient;
import com.emintufan.labreportingapp.entity.PatientReport;
import com.emintufan.labreportingapp.entity.Role;
import com.emintufan.labreportingapp.entity.enums.RoleEnum;
import com.emintufan.labreportingapp.repository.LaborantRepository;
import com.emintufan.labreportingapp.repository.PatientReportRepository;
import com.emintufan.labreportingapp.repository.PatientRepository;
import com.emintufan.labreportingapp.repository.RoleRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;


@SpringBootApplication
public class LabReportingAppApplication implements CommandLineRunner {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private LaborantRepository laborantRepository;

    @Autowired
    private PatientReportRepository patientReportRepository;

    public static void main(String[] args) {
        SpringApplication.run(LabReportingAppApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // Save roles
        Role userRole = saveRole(RoleEnum.ROLE_USER);
        Role adminRole = saveRole(RoleEnum.ROLE_ADMIN);

        // Save patients
        List<Patient> patients = Arrays.asList(
                createPatient("1234567890", "Burak", "Şahin", "burak_s@example.com", "parola123", List.of(userRole)),
                createPatient("2345678901", "Ayşe", "Demir", "ayse_d@example.com", "parola567", List.of(userRole)),
                createPatient("3456789012", "Mehmet", "Aydın", "mehmet_a@example.com", "parola123", List.of(userRole)),
                createPatient("4567890123", "Zeynep", "Kaya", "zeynep_k@example.com", "parolam456", List.of(userRole)),
                createPatient("5678901234", "Mustafa", "Öztürk", "mustafa_o@example.com", "parola123", List.of(userRole)),
                createPatient("6789012345", "Elif", "Yılmaz", "elif_y@example.com", "parola789", List.of(userRole)),
                createPatient("7890123456", "Ahmet", "Can", "ahmet_c@example.com", "parola123", List.of(userRole)),
                createPatient("8901234567", "Zehra", "Çelik", "zehra_c@example.com", "parola012", List.of(userRole)),
                createPatient("9012345678", "Hüseyin", "Demir", "huseyin_d@example.com", "parola123", List.of(userRole)),
                createPatient("0123456789", "Gamze", "Türk", "gamze_t@example.com", "parola567", List.of(userRole)),
                createPatient("1122334455", "Cem", "Şahin", "cem_s@example.com", "parola123", List.of(userRole)),
                createPatient("2233445566", "Deniz", "Güneş", "deniz_g@example.com", "parola789", List.of(userRole)),
                createPatient("3344556677", "Sevgi", "Yıldız", "sevgi_y@example.com", "parola123", List.of(userRole)),
                createPatient("4455667788", "Efe", "Aksoy", "efe_a@example.com", "parola012", List.of(userRole)),
                createPatient("5566778899", "Gizem", "Korkmaz", "gizem_k@example.com", "parola123", List.of(userRole)),
                createPatient("6677889900", "Okan", "Yılmaz", "okan_y@example.com", "parola567", List.of(userRole)),
                createPatient("7788990011", "Esra", "Ergin", "esra_e@example.com", "parola123", List.of(userRole)),
                createPatient("8899001122", "Umut", "Kara", "umut_k@example.com", "parola789", List.of(userRole)),
                createPatient("9900112233", "Selma", "Yıldırım", "selma_y@example.com", "parola123", List.of(userRole))
        );
        patientRepository.saveAll(patients);

        // Save laborant
        List<Laborant> laborants = Arrays.asList(
                createLaborant("Ali", "Yılmaz", "ali_y@example.com", "parola123", "0385ES213DFK21", List.of(adminRole)),
                createLaborant("Selin", "Karaçalı", "selin_k@example.com", "parola123", "3SLSFV35023SK1", List.of(adminRole)));
        laborantRepository.saveAll(laborants);

        // Save patient report with an example lab image
        try {
            String imagePath1 = "src/main/resources/labReportImage.jpg";
            String imagePath2 = "src/main/resources/labreport2.png";

            byte[] byteImage1 = readImageFile(imagePath1);
            byte[] byteImage2 = readImageFile(imagePath2);

            String encodedImage1 = encodeImageToBase64(byteImage1);
            String encodedImage2 = encodeImageToBase64(byteImage2);

            patients.get(0).setReportRequest(false);
            patients.get(1).setReportRequest(false);
            List<PatientReport> patientReports = List.of(
                    createPatientReport("Acute Bronchitis", "KS4C35KS", "The patient has been diagnosed with acute bronchitis, an inflammation of the airways in the lungs.", LocalDateTime.now(), patients.get(0), laborants.get(0), encodedImage1),
                    createPatientReport("COVID-19", "SLV83ALD", "The patient has tested positive for COVID-19, a respiratory illness caused by the SARS-CoV-2 virus.", LocalDateTime.now(), patients.get(1), laborants.get(1), encodedImage2));

            patientReportRepository.saveAll(patientReports);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] readImageFile(String imagePath) throws Exception {
        File file = new File(imagePath);
        return FileUtils.readFileToByteArray(file);
    }

    private String encodeImageToBase64(byte[] imageBytes) {
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
    }

    private Role saveRole(RoleEnum roleEnum) {
        return roleRepository.save(Role.builder().role(roleEnum).build());
    }

    private Patient createPatient(String identityNumber, String name, String surName, String userName, String password, List<Role> roles) {
        return Patient.builder()
                .identityNumber(identityNumber)
                .name(name)
                .surName(surName)
                .userName(userName)
                .password(passwordEncoder.encode(password))
                .roles(roles)
                .reportRequest(true)
                .build();
    }

    private Laborant createLaborant(String name, String surName, String userName, String password, String hospitalId, List<Role> roles) {
        return Laborant.builder()
                .name(name)
                .surName(surName)
                .userName(userName)
                .password(passwordEncoder.encode(password))
                .hospitalId(hospitalId)
                .roles(roles)
                .build();
    }

    private PatientReport createPatientReport(String diagnosisTitle, String fileNumber, String diagnosisDetail, LocalDateTime issueDate, Patient patient, Laborant laborant, String reportImage) {
        return PatientReport.builder()
                .diagnosisTitle(diagnosisTitle)
                .fileNumber(fileNumber)
                .diagnosisDetail(diagnosisDetail)
                .issueDate(issueDate)
                .patient(patient)
                .laborant(laborant)
                .reportImage(reportImage)
                .build();
    }
}
