package com.ds.app;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ds.app.entity.*;
import com.ds.app.enums.*;
import com.ds.app.repository.*;
import com.ds.app.service.AppUserService;

@Configuration
public class AppDataSeeder_Tarushi {

    @Bean
    CommandLineRunner seedAll(
            AppUserService appUserService,
            iAppUserRepository userRepo,
            EmployeeRepository employeeRepo,
            AssetRepository assetRepo,
            AssetAllocationRepository allocationRepo,
            AssetIssueRepository issueRepo,
            AssetEscalationRepository escalationRepo
    ) {
        return args -> {

            /* ================= USERS ================= */

            createUser(userRepo, appUserService, "admin01", "admin@123", UserRole.ADMIN);
            createUser(userRepo, appUserService, "hr01", "hr@123", UserRole.HR);

            Employee emp1 = (Employee) createUser(
                    userRepo, appUserService, "employee01", "emp@123", UserRole.EMPLOYEE);

            Employee emp2 = (Employee) createUser(
                    userRepo, appUserService, "employee02", "emp@123", UserRole.EMPLOYEE);

            /* ================= ASSETS ================= */

            Asset laptop = assetRepo.findByAssetTag("LAP-001")
                    .orElseGet(() -> assetRepo.save(buildAsset(
                            "LAP-001", "Laptop", "HARDWARE", 10,
                            AssetStatus.AVAILABLE)));

            Asset monitor = assetRepo.findByAssetTag("MON-001")
                    .orElseGet(() -> assetRepo.save(buildAsset(
                            "MON-001", "Monitor", "HARDWARE", 6,
                            AssetStatus.AVAILABLE)));

            Asset mouse = assetRepo.findByAssetTag("MOU-001")
                    .orElseGet(() -> assetRepo.save(buildAsset(
                            "MOU-001", "Mouse", "ACCESSORY", 15,
                            AssetStatus.AVAILABLE)));

            /* ================= ALLOCATIONS ================= */

            allocateIfMissing(allocationRepo, laptop, emp1);
            allocateIfMissing(allocationRepo, monitor, emp2);
            allocateIfMissing(allocationRepo, mouse, emp2);

            /* ================= ISSUES ================= */

            createIssueIfMissing(issueRepo, laptop, emp1,
                    "Laptop screen flickering");

            createIssueIfMissing(issueRepo, monitor, emp2,
                    "Monitor power cable loose");

            createIssueIfMissing(issueRepo, mouse, emp2,
                    "Mouse not responding");

            /* ================= ESCALATIONS ================= */

            escalateIfMissing(escalationRepo, laptop, emp1,
                    "Issue unresolved for > 5 days");

            escalateIfMissing(escalationRepo, monitor, emp2,
                    "Repeated issue complaints");

            System.out.println("ASSET MODULE SEEDING COMPLETED");
        };
    }

    /* ================= HELPERS ================= */

    private AppUser createUser(
            iAppUserRepository userRepo,
            AppUserService service,
            String username,
            String password,
            UserRole role) {

        if (userRepo.existsByUsername(username)) {
            return userRepo.findByUsername(username).orElse(null);
        }

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);

        return service.registerAppUser(user);
    }

    private Asset buildAsset(
            String tag,
            String name,
            String category,
            int totalUnits,
            AssetStatus status) {

        Asset asset = new Asset();
        asset.setAssetTag(tag);
        asset.setName(name);
        asset.setCategory(category);
        asset.setTotalUnits(totalUnits);
        asset.setAllocatedUnits(1);
        asset.setAvailableUnits(totalUnits - 1);
        asset.setStatus(status);
        return asset;
    }

    private void allocateIfMissing(
            AssetAllocationRepository repo,
            Asset asset,
            Employee employee) {

        boolean exists = repo.existsByAssetAndEmployeeAndStatus(
                asset, employee, AssetAllocationStatus.ACTIVE);

        if (!exists) {
            AssetAllocation allocation = new AssetAllocation();
            allocation.setAsset(asset);
            allocation.setEmployee(employee);
            allocation.setAllocatedDate(LocalDate.now());
            allocation.setStatus(AssetAllocationStatus.ACTIVE);
            repo.save(allocation);
        }
    }

    private void createIssueIfMissing(
            AssetIssueRepository repo,
            Asset asset,
            Employee employee,
            String description) {

        boolean exists = repo.existsByAssetAndEmployeeAndStatus(
                asset, employee, AssetIssueStatus.OPEN);

        if (!exists) {
            AssetIssue issue = new AssetIssue();
            issue.setAsset(asset);
            issue.setEmployee(employee);
            issue.setDescription(description);
            issue.setCreatedAt(LocalDateTime.now());
            issue.setStatus(AssetIssueStatus.OPEN);
            repo.save(issue);
        }
    }

    private void escalateIfMissing(
            AssetEscalationRepository repo,
            Asset asset,
            Employee employee,
            String reason) {

        boolean exists = repo.existsByAssetAndEmployeeAndStatus(
                asset, employee, AssetEscalationStatus.OPEN);

        if (!exists) {
            AssetEscalation esc = new AssetEscalation();
            esc.setAsset(asset);
            esc.setEmployee(employee);
            esc.setReason(reason);
            esc.setStatus(AssetEscalationStatus.OPEN);
            esc.setCreatedAt(LocalDateTime.now().minusDays(1));
            repo.save(esc);
        }
    }
}



//package com.ds.app.config;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.ds.app.entity.*;
//import com.ds.app.enums.*;
//import com.ds.app.repository.*;
//import com.ds.app.service.AppUserService;
//
////@Configuration
//public class AppDataSeeder {
//
//  //  @Bean
//    CommandLineRunner seedAll(
//            AppUserService appUserService,
//            AssetRepository assetRepository,
//            AssetAllocationRepository allocationRepository,
//            AssetIssueRepository issueRepository,
//            AssetEscalationRepository escalationRepository,
//            EmployeeRepository employeeRepository
//    ) {
//        return args -> {
//
//             AppUser admin = createUser(appUserService, "admin01", "admin@123", UserRole.ADMIN);
//            AppUser hr = createUser(appUserService, "hr01", "hr@123", UserRole.HR);
//
//            Employee emp1 = (Employee) createUser(appUserService, "employee01", "emp@123", UserRole.EMPLOYEE);
//            Employee emp2 = (Employee) createUser(appUserService, "employee02", "emp@123", UserRole.EMPLOYEE);
//
// 
//            Asset laptop = createAsset(assetRepository, "LAP-101", "LaptopPro", "HARDWARE", 14);
//            Asset monitor = createAsset(assetRepository, "MON-201", "MonitorLite", "HARDWARE", 16);
//
//            createAllocation(allocationRepository, laptop, emp1);
//            createAllocation(allocationRepository, monitor, emp2);
//
//            AssetIssue issue1 = createIssue(issueRepository, laptop, emp1,
//                    "Screen Broken", AssetIssueStatus.OPEN);
//
//
//            createEscalation(escalationRepository, laptop, emp1,
//                    "Issue unresolved for 10 days", AssetEscalationStatus.OPEN);
//
//            System.out.println("Application seeded successfully");
//        };
//    }
//
//
//    private AppUser createUser(AppUserService service, String username, String password, UserRole role) {
//        try {
//            AppUser user = new AppUser();
//            user.setUsername(username);
//            user.setPassword(password);
//            user.setRole(role);
//            return service.registerAppUser(user);
//        } catch (RuntimeException ex) {
//            return null;
//        }
//    }
//
//    private Asset createAsset(AssetRepository repo, String tag, String name, String category, int units) {
//        return repo.findByAssetTag(tag)
//                .orElseGet(() -> {
//                    Asset a = new Asset();
//                    a.setAssetTag(tag);
//                    a.setName(name);
//                    a.setCategory(category);
//                    a.setTotalUnits(units);
//                    a.setAllocatedUnits(1);
//                    a.setAvailableUnits(units - 1);
//                    a.setStatus(AssetStatus.AVAILABLE);
//                    return repo.save(a);
//                });
//    }
//
//    private void createAllocation(
//            AssetAllocationRepository repo,
//            Asset asset,
//            Employee employee) {
//
//        if (repo.existsByAssetAndEmployeeAndStatus(
//                asset, employee, AssetAllocationStatus.ACTIVE)) {
//            return;
//        }
//
//        AssetAllocation allocation = new AssetAllocation();
//        allocation.setAsset(asset);
//        allocation.setEmployee(employee);
//        allocation.setAllocatedDate(LocalDate.now().minusDays(3));
//        allocation.setStatus(AssetAllocationStatus.ACTIVE);
//
//        repo.save(allocation);
//    }
//
//    private AssetIssue createIssue(
//            AssetIssueRepository repo,
//            Asset asset,
//            Employee employee,
//            String description,
//            AssetIssueStatus status) {
//
//        AssetIssue issue = new AssetIssue();
//        issue.setAsset(asset);
//        issue.setEmployee(employee);
//        issue.setDescription(description);
//        issue.setStatus(status);
//        issue.setCreatedAt(LocalDateTime.now().minusDays(5));
//
//        return repo.save(issue);
//    }
//
//    private void createEscalation(
//            AssetEscalationRepository repo,
//            Asset asset,
//            Employee employee,
//            String reason,
//            AssetEscalationStatus status) {
//
//        if (repo.existsByAssetAndEmployeeAndStatus(asset, employee, status)) {
//            return;
//        }
//
//        AssetEscalation escalation = new AssetEscalation();
//        escalation.setAsset(asset);
//        escalation.setEmployee(employee);
//        escalation.setReason(reason);
//        escalation.setStatus(status);
//        escalation.setCreatedAt(LocalDateTime.now().minusDays(2));
//
//        repo.save(escalation);
//    }
//}
