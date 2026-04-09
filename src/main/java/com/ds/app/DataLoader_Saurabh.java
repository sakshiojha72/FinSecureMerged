package com.ds.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ds.app.entity.AppUser;
import com.ds.app.entity.Employee;
import com.ds.app.enums.CertificationStatus;
import com.ds.app.enums.EmployeeExperience;
import com.ds.app.enums.SkillStatus;
import com.ds.app.enums.UserRole;
import com.ds.app.repository.EmployeeRepository;
import com.ds.app.repository.iAppUserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader_Saurabh implements CommandLineRunner {

    private final iAppUserRepository userRepo;
    private final EmployeeRepository employeeRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {

        // Admin user
        if (!userRepo.existsByUsername("admin")) {
            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(UserRole.ADMIN);

            userRepo.save(admin);
            log.info("✅ Admin user created");
        }

        createEmployeeIfNotExists(
                "suresh", "suresh123",
                "suresh@example.com",
                EmployeeExperience.FRESHER,
                CertificationStatus.NON_CERTIFIED,
                SkillStatus.NON_SKILLED,
                UserRole.EMPLOYEE
        );

        createEmployeeIfNotExists(
                "mahesh", "mahesh123",
                "mahesh@example.com",
                EmployeeExperience.EXPERIENCED,
                CertificationStatus.CERTIFIED,
                SkillStatus.SKILLED,
                UserRole.EMPLOYEE
        );

        createEmployeeIfNotExists(
                "saurabh", "saurabh123",
                "tiwarisaurabhkumar321@gmail.com",
                EmployeeExperience.EXPERIENCED,
                CertificationStatus.NON_CERTIFIED,
                SkillStatus.NON_SKILLED,
                UserRole.EMPLOYEE
        );

        createEmployeeIfNotExists(
                "mayank", "mayank123",
                "mayank@example.com",
                EmployeeExperience.FRESHER,
                CertificationStatus.NON_CERTIFIED,
                SkillStatus.NON_SKILLED,
                UserRole.HR
        );

        createEmployeeIfNotExists(
                "john", "john123",
                "john@example.com",
                EmployeeExperience.EXPERIENCED,
                CertificationStatus.CERTIFIED,
                SkillStatus.NON_SKILLED,
                UserRole.HR
        );
    }

    private void createEmployeeIfNotExists(
            String username,
            String rawPassword,
            String email,
            EmployeeExperience experience,
            CertificationStatus certificationStatus,
            SkillStatus skillStatus,
            UserRole role
    ) {
        if (employeeRepo.existsByUsername(username)) return;

        Employee emp = new Employee();
        emp.setUsername(username);
        emp.setPassword(passwordEncoder.encode(rawPassword));
        emp.setEmail(email);
        emp.setFirstName(username);
        emp.setLastName("User");
        emp.setRole(role);
        emp.setEmployeeExperience(experience);
        emp.setCertificationStatus(certificationStatus);
        emp.setSkillStatus(skillStatus);

        employeeRepo.save(emp);
        log.info(" Employee created: {}", username);
    }
}