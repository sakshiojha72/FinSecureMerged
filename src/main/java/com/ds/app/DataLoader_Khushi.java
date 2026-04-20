package com.ds.app;

import com.ds.app.entity.AppUser;
import com.ds.app.entity.Employee;
import com.ds.app.entity.EmployeeDocument;
import com.ds.app.entity.EmployeeEducation;
import com.ds.app.entity.EmployeeReward;
import com.ds.app.enums.RewardCategory;
import com.ds.app.enums.RewardType;
import com.ds.app.enums.Status;
import com.ds.app.enums.UserRole;
import com.ds.app.repository.iAppUserRepository;
import com.ds.app.repository.EmployeeDocumentRepository;
import com.ds.app.repository.EmployeeEducationRepository;
import com.ds.app.repository.EmployeeRepository;
import com.ds.app.repository.EmployeeRewardRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class DataLoader_Khushi {

    @Bean
    CommandLineRunner loadUserProfileData(
            iAppUserRepository userRepo,
            EmployeeRepository employeeRepo,
            EmployeeEducationRepository educationRepo,
            EmployeeDocumentRepository documentRepo,
            EmployeeRewardRepository rewardRepo,
            PasswordEncoder passwordEncoder) {

        return args -> {

  

            // ══════════════════════════════════════════════════════
            // ADMIN — AppUser only (no Employee record)
            // ══════════════════════════════════════════════════════
            if (!userRepo.existsByUsername("admin_user")) {
                AppUser admin = new AppUser();
                admin.setUsername("admin_user");
                admin.setPassword(passwordEncoder.encode("admin1234"));
                admin.setRole(UserRole.ADMIN);
                admin.setFailedLoginAttemptsCount(0);
                admin.setIsAccountLocked(false);
                userRepo.save(admin);
                System.out.println( " Admin created — admin_user / admin1234");
            }

            // ══════════════════════════════════════════════════════
            // HR USER
            // ══════════════════════════════════════════════════════
            if (!employeeRepo.existsByUsername("hr_user")) {
                Employee hr = new Employee();
                hr.setUsername("hr_user");
                hr.setPassword(passwordEncoder.encode("hr1234"));
                hr.setRole(UserRole.HR);
                hr.setFirstName("Priya");
                hr.setLastName("Sharma");
                hr.setEmail("priya.sharma@finsecure.com");
                hr.setPhoneNumber("9876500001");
                hr.setDesignation("HR Manager");
                hr.setJoiningDate(LocalDate.of(2018, 3, 1));
                hr.setAddressLine("101 HR Street");
                hr.setCity("Mumbai");
                hr.setState("Maharashtra");
                hr.setCountry("India");
                hr.setPincode("400001");

                // Placeholder — Finance sets real value
                hr.setCurrentSalary(0.0);
                hr.setStatus(Status.ACTIVE);
                hr.setIsDeleted(false);
                hr.setIsEscalated(false);
                hr.setHasActiveAssetEscalation(false);
                hr.setFailedLoginAttemptsCount(0);
                hr.setIsAccountLocked(false);

                Employee savedHr = employeeRepo.save(hr);
                savedHr.setEmployeeCode("EMP" + String.format("%04d", savedHr.getUserId()));
                employeeRepo.save(savedHr);
                System.out.println(" HR created — hr_user / hr1234");
            }

            // ══════════════════════════════════════════════════════
            // EMPLOYEE 1 — john_doe
            // Complete profile + Education + Documents + Reward
            // ══════════════════════════════════════════════════════
            Employee john;
            if (!employeeRepo.existsByUsername("john_doe")) {
                Employee emp = new Employee();
                emp.setUsername("john_doe");
                emp.setPassword(passwordEncoder.encode("emp1234"));
                emp.setRole(UserRole.EMPLOYEE);
                emp.setFirstName("John");
                emp.setLastName("Doe");
                emp.setEmail("john.doe@gmail.com");
                emp.setPhoneNumber("9876543210");
                emp.setDesignation("Senior Developer");
                emp.setJoiningDate(LocalDate.of(2022, 1, 15));
                emp.setAddressLine("123 Main Street");
                emp.setCity("Mumbai");
                emp.setState("Maharashtra");
                emp.setCountry("India");
                emp.setPincode("400001");

                // Placeholder — Finance sets real value
                emp.setCurrentSalary(0.0);
                emp.setStatus(Status.ACTIVE);
                emp.setIsDeleted(false);
                emp.setIsEscalated(false);
                emp.setHasActiveAssetEscalation(false);
                emp.setFailedLoginAttemptsCount(0);
                emp.setIsAccountLocked(false);

                john = employeeRepo.save(emp);
                john.setEmployeeCode("EMP" + String.format("%04d",john.getUserId()));
                john = employeeRepo.save(john);
                System.out.println(" Employee created — " + "john_doe / emp1234");
            } else {
                john = employeeRepo
                    .findByUsername("john_doe")
                    .orElseThrow();
            }

            // Education for john_doe
            if (!educationRepo.existsByEmployeeUserId(john.getUserId())) {

                EmployeeEducation btech = new EmployeeEducation();
                btech.setEmployee(john);
                btech.setDegree("B.Tech");
                btech.setInstitution("Mumbai University");
                btech.setFieldOfStudy("Computer Science");
                btech.setPassingYear(2022);
                btech.setPercentage(85.5);
                btech.setGrade("First Class");
                btech.setLocation("Mumbai");
                educationRepo.save(btech);
                System.out.println(" Education created — " + "john_doe B.Tech");

                EmployeeEducation twelfth = new EmployeeEducation();
                twelfth.setEmployee(john);
                twelfth.setDegree("12th");
                twelfth.setInstitution("Delhi Public School");
                twelfth.setFieldOfStudy("Science");
                twelfth.setPassingYear(2018);
                twelfth.setPercentage(88.0);
                twelfth.setGrade("A");
                twelfth.setLocation("Mumbai");
                educationRepo.save(twelfth);
                System.out.println(" Education created — " + "john_doe 12th");
            }

            // Documents for john_doe
            if (!documentRepo.existsByEmployeeUserId(john.getUserId())) {

                EmployeeDocument aadhaar = new EmployeeDocument();
                aadhaar.setEmployee(john);
                aadhaar.setDocumentType("AADHAAR");
                aadhaar.setDocumentNumber("1234-5678-9012");
                aadhaar.setIsVerified(false);
                documentRepo.save(aadhaar);
                System.out.println(" Document created — " + "john_doe AADHAAR");

                EmployeeDocument pan = new EmployeeDocument();
                pan.setEmployee(john);
                pan.setDocumentType("PAN");
                pan.setDocumentNumber("ABCDE1234F");
                pan.setIsVerified(false);
                documentRepo.save(pan);
                System.out.println(
                    " Document created — john_doe PAN");
            }

            // Reward for john_doe
            if (!rewardRepo.existsByEmployeeUserId(john.getUserId())) {
                EmployeeReward reward = new EmployeeReward();
                reward.setEmployee(john);
                reward.setRewardType(RewardType.EMPLOYEE_OF_MONTH);
                reward.setRewardCategory(
                    RewardCategory.PERFORMANCE);
                reward.setTitle(
                    "Employee of Month - Jan 2024");
                reward.setDescription(
                    "Outstanding performance in Q1");
                reward.setRewardDate(
                    LocalDate.of(2024, 1, 31));
                reward.setGivenBy("hr_user");
                rewardRepo.save(reward);
                System.out.println(
                    " Reward created — john_doe");
            }

            // ══════════════════════════════════════════════════════
            // EMPLOYEE 2 — jane_smith
            // Incomplete profile — no address for testing
            // ══════════════════════════════════════════════════════
            if (!employeeRepo.existsByUsername(
                    "jane_smith")) {
                Employee emp = new Employee();
                emp.setUsername("jane_smith");
                emp.setPassword(
                    passwordEncoder.encode("emp1234"));
                emp.setRole(UserRole.EMPLOYEE);
                emp.setFirstName("Jane");
                emp.setLastName("Smith");
                emp.setEmail("jane.smith@gmail.com");
                emp.setPhoneNumber("9876543211");
                emp.setDesignation("Junior Analyst");
                emp.setJoiningDate(
                    LocalDate.of(2023, 6, 1));
                // No address — incomplete profile

                // Placeholder — Finance sets real value
                emp.setCurrentSalary(0.0);
                emp.setStatus(Status.ACTIVE);
                emp.setIsDeleted(false);
                emp.setIsEscalated(false);
                emp.setHasActiveAssetEscalation(false);
                emp.setFailedLoginAttemptsCount(0);
                emp.setIsAccountLocked(false);

                Employee saved =
                    employeeRepo.save(emp);
                saved.setEmployeeCode("EMP" +
                    String.format("%04d",
                        saved.getUserId()));
                employeeRepo.save(saved);
                System.out.println(
                    " Employee created — jane_smith / emp1234" +
                    " (incomplete profile)");
            }

            // ══════════════════════════════════════════════════════
            // EMPLOYEE 3 — raj_kumar
            // Complete profile + Reward
            // ══════════════════════════════════════════════════════
            Employee raj;
            if (!employeeRepo.existsByUsername(
                    "raj_kumar")) {
                Employee emp = new Employee();
                emp.setUsername("raj_kumar");
                emp.setPassword(
                    passwordEncoder.encode("emp1234"));
                emp.setRole(UserRole.EMPLOYEE);
                emp.setFirstName("Raj");
                emp.setLastName("Kumar");
                emp.setEmail("raj.kumar@gmail.com");
                emp.setPhoneNumber("9876543212");
                emp.setDesignation("Tech Lead");
                emp.setJoiningDate(
                    LocalDate.of(2019, 4, 1));
                emp.setAddressLine("789 Lake Road");
                emp.setCity("Pune");
                emp.setState("Maharashtra");
                emp.setCountry("India");
                emp.setPincode("411001");

                // Placeholder — Finance sets real value
                emp.setCurrentSalary(0.0);
                emp.setStatus(Status.ACTIVE);
                emp.setIsDeleted(false);
                emp.setIsEscalated(false);
                emp.setHasActiveAssetEscalation(false);
                emp.setFailedLoginAttemptsCount(0);
                emp.setIsAccountLocked(false);

                raj = employeeRepo.save(emp);
                raj.setEmployeeCode("EMP" +
                    String.format("%04d",
                        raj.getUserId()));
                raj = employeeRepo.save(raj);
                System.out.println(
                    " Employee created — " +
                    "raj_kumar / emp1234");
            } else {
                raj = employeeRepo
                    .findByUsername("raj_kumar")
                    .orElseThrow();
            }

            // Reward for raj_kumar
            if (!rewardRepo.existsByEmployeeUserId(raj.getUserId())) {
                EmployeeReward reward =  new EmployeeReward();
                reward.setEmployee(raj);
                reward.setRewardType(RewardType.BEST_PERFORMER);
                reward.setRewardCategory(RewardCategory.PERFORMANCE);
                reward.setTitle(
                    "Best Performer Q1 2024");
                reward.setDescription(
                    "Highest delivery rate in team");
                reward.setRewardDate(
                    LocalDate.of(2024, 3, 31));
                reward.setGivenBy("hr_user");
                rewardRepo.save(reward);
                System.out.println(
                    " Reward created — raj_kumar");
            }

            // ══════════════════════════════════════════════════════
            // EMPLOYEE 4 — deleted_emp
            // Soft deleted for testing restore endpoint
            // ══════════════════════════════════════════════════════
            if (!employeeRepo.existsByUsername(
                    "deleted_emp")) {
                Employee emp = new Employee();
                emp.setUsername("deleted_emp");
                emp.setPassword(passwordEncoder.encode("emp1234"));
                emp.setRole(UserRole.EMPLOYEE);
                emp.setFirstName("Deleted");
                emp.setLastName("Employee");
                emp.setEmail("deleted.emp@gmail.com");
                emp.setDesignation("Developer");
                emp.setJoiningDate(LocalDate.of(2021, 1, 1));

                // Placeholder
                emp.setCurrentSalary(0.0);
                emp.setStatus(Status.INACTIVE);
                emp.setIsDeleted(true); // ← soft deleted
                emp.setIsEscalated(false);
                emp.setHasActiveAssetEscalation(false);
                emp.setFailedLoginAttemptsCount(0);
                emp.setIsAccountLocked(false);

                Employee saved = employeeRepo.save(emp);
                saved.setEmployeeCode("EMP" + String.format("%04d",saved.getUserId()));
                employeeRepo.save(saved);
                System.out.println(" Deleted employee created — " + "deleted_emp / emp1234 " + "(isDeleted=true)");
            }

    

            System.out.println(
                "=== UserProfileDataLoader Complete ✅ ===");
            System.out.println("Login Credentials:");
            System.out.println(
                "  Admin:    admin_user / admin1234");
            System.out.println(
                "  HR:       hr_user / hr1234");
            System.out.println(
                "  Employee: john_doe / emp1234");
            System.out.println(
                "  Employee: jane_smith / emp1234");
            System.out.println(
                "  Employee: raj_kumar / emp1234");
            
        };
    }
}