package com.ds.app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ds.app.entity.AppUser;
import com.ds.app.entity.Employee;
import com.ds.app.enums.Status;
import com.ds.app.enums.UserRole;
import com.ds.app.repository.iAppUserRepository;
import com.ds.app.repository.EmployeeRepository;
import com.ds.app.service.AppUserService;

import jakarta.transaction.Transactional;
@Service
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    private iAppUserRepository appUserRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private static final Logger logger = LoggerFactory.getLogger(AppUserServiceImpl.class);

    
    @Override
    @Transactional
    public AppUser registerAppUser(AppUser user) {

        if (appUserRepository.existsByUsername(
                user.getUsername())) {
            throw new RuntimeException(
                    "Username already exists: " +
                    user.getUsername());
        }

        String encodedPassword = passwordEncoder.encode(
                user.getPassword());

        // FIX — not just EMPLOYEE
        // HR, EMPLOYEE, MANAGER, FINANCE → Employee table
        // ADMIN, SYSTEM → AppUser table only
        if (user.getRole() != UserRole.ADMIN &&
                user.getRole() != UserRole.SYSTEM) {

            Employee employee = new Employee();
            employee.setUsername(user.getUsername());
            employee.setPassword(encodedPassword);
            employee.setRole(user.getRole());
            employee.setFailedLoginAttemptsCount(0);
            employee.setIsAccountLocked(false);
            employee.setIsDeleted(false);
            employee.setIsEscalated(false);

            // Required NOT NULL fields
            employee.setFirstName("");
            employee.setLastName("");
            employee.setEmail(user.getUsername() +
                    "@finsecure.com");
            employee.setCurrentSalary(0.0);
            employee.setStatus(Status.ACTIVE);

            Employee saved =
                    employeeRepository.save(employee);

            logger.info("Employee created: {} role: {}",
                    saved.getUsername(), saved.getRole());
            return saved;

        } else {

            // ADMIN / SYSTEM → AppUser only
            user.setPassword(encodedPassword);
            user.setFailedLoginAttemptsCount(0);
            user.setIsAccountLocked(false);

            AppUser saved = appUserRepository.save(user);

            logger.info("AppUser created: {} role: {}",saved.getUsername(), saved.getRole());
            return saved;
        }
    }
//    @Override
//    public AppUser registerAppUser(AppUser user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        if (user.getRole() == UserRole.EMPLOYEE) {
//
//
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        if (user.getRole() == UserRole.EMPLOYEE) {
//
//            Employee employee = new Employee();
//            employee.setUsername(user.getUsername());
//            employee.setPassword(user.getPassword());
//            employee.setRole(user.getRole());
//            employee.setFailedLoginAttemptsCount(0);
//            employee.setIsAccountLocked(false);
//            employee.setFirstName("");
//            employee.setLastName("");
//
//            
//            
//            employee.setEmail(user.getUsername() + "@finsecure.com"); // placeholder, not null
//            employee.setCurrentSalary(0.0);                           // not null default
//            employee.setStatus(Status.ACTIVE);   
//            
//            return employeeRepository.save(employee);
//        }

//
//        }
//
//     
//   
//        return appUserRepository.save(user);
//    }
}