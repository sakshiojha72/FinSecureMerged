package com.ds.app.service.impl;

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
@Service
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    private iAppUserRepository appUserRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public AppUser registerAppUser(AppUser user) {
<<<<<<< HEAD

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == UserRole.EMPLOYEE) {

=======
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == UserRole.EMPLOYEE) {
>>>>>>> 388aecd46cb67e0f22d0bb0c6ec3262d3d9c866e
            Employee employee = new Employee();
            employee.setUsername(user.getUsername());
            employee.setPassword(user.getPassword());
            employee.setRole(user.getRole());
<<<<<<< HEAD

=======
>>>>>>> 388aecd46cb67e0f22d0bb0c6ec3262d3d9c866e
            employee.setFailedLoginAttemptsCount(0);
            employee.setIsAccountLocked(false);
            employee.setFirstName("");
            employee.setLastName("");
<<<<<<< HEAD
            
            
            employee.setEmail(user.getUsername() + "@finsecure.com"); // placeholder, not null
            employee.setCurrentSalary(0.0);                           // not null default
            employee.setStatus(Status.ACTIVE);   
            
            return employeeRepository.save(employee);
        }

=======
            employee.setEmail(user.getUsername() + "@finsecure.com"); // placeholder, not null
            employee.setCurrentSalary(0.0);                           // not null default
            employee.setStatus(Status.ACTIVE);                // not null default

            return employeeRepository.save(employee);
        }
>>>>>>> 388aecd46cb67e0f22d0bb0c6ec3262d3d9c866e
        return appUserRepository.save(user);
    }
}