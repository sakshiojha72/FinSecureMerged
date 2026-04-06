package com.ds.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ds.app.entity.AppUser;
import com.ds.app.entity.Employee;
import com.ds.app.enums.UserRole;
import com.ds.app.repository.IEmployeeRepository;
import com.ds.app.repository.iAppUserRepository;

import jakarta.transaction.Transactional;

@Service
public class AppUserServiceImpl implements AppUserService{

	@Autowired
	private iAppUserRepository appUserRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	 @Autowired
	 private IEmployeeRepository iEmployeeRepo;

	 
	 @Override
	 @Transactional
	 public AppUser registerAppUser(AppUser user) {

	     // Check duplicate username
	     if (appUserRepository.existsByUsername(user.getUsername())) {
	         throw new RuntimeException("Username already exists");
	     }

	     // Encode password
	     String encodedPassword = passwordEncoder.encode(
	             user.getPassword());

	     // FIX: replace isEmployeeType() with direct comparison
	     if (user.getRole() != UserRole.ADMIN &&
	         user.getRole() != UserRole.SYSTEM) {

	         // EMPLOYEE / HR / FINANCE → create Employee object
	         Employee employee = new Employee();

	         // AppUser fields
	         employee.setUsername(user.getUsername());
	         employee.setPassword(encodedPassword);  // ← use encodedPassword
	         employee.setRole(user.getRole());
	         employee.setFailedLoginAttemptsCount(0);
	         employee.setIsAccountLocked(false);

	         // Default flags
	         employee.setIsDeleted(false);
	         employee.setIsEscalated(false);

	         // Personal fields are null by default
	         // No need to set them to null explicitly
	         // JPA handles it automatically

	         // Save — get auto-generated userId
	         Employee saved = iEmployeeRepo.save(employee);

	         // Auto-generate employeeCode
	         String employeeCode = "EMP" +
	                 String.format("%04d", saved.getUserId());
	         saved.setEmployeeCode(employeeCode);
	         iEmployeeRepo.save(saved);

	         return saved;

	     } else {

	         // ADMIN / SYSTEM → AppUser only
	         user.setPassword(encodedPassword);
	         user.setFailedLoginAttemptsCount(0);
	         user.setIsAccountLocked(false);
	         return appUserRepository.save(user);
	     }
	 }
	
//	@Override
//	@Transactional
//	public AppUser registerAppUser(AppUser user) {
//		
//        if (appUserRepository.existsByUsername(user.getUsername())) {
//            throw new RuntimeException("Username already exists");
//        }
//        
//        String encodedPassword = passwordEncoder.encode(user.getPassword());
//       // user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        if (user.getRole().isEmployeeType()) {
//
//            Employee employee = new Employee();
//
//            // inherited AppUser fields
//            employee.setUsername(user.getUsername());
//            employee.setPassword(user.getPassword());
//            employee.setRole(user.getRole());
//
//            // default employee/system fields
//            employee.setIsDeleted(false);
//            employee.setIsEscalated(false);
//
//            // optional profile fields can remain null for now
//            employee.setEmployeeCode(null);
//            employee.setFirstName(null);
//            employee.setLastName(null);
//            employee.setEmail(null);
//            employee.setPhoneNumber(null);
//            employee.setDateOfBirth(null);
//            employee.setGender(null);
//            employee.setDepartment(null);
//            employee.setDesignation(null);
//            employee.setJoiningDate(null);
//            employee.setEmploymentType(null);
//            employee.setEmployeeExperience(null);
//            employee.setCertificationName(null);
//            employee.setAddressLine(null);
//            employee.setCity(null);
//            employee.setState(null);
//            employee.setCountry(null);
//            employee.setPincode(null);
//            employee.setProfilePhotoUrl(null);
//
//            return iEmployeeRepo.save(employee);
//        }
//        
//        user.setPassword(encodedPassword);
//        return appUserRepository.save(user);
//
//		
//	}	
		
		
		
		
		
		
		
		
		
		
		/*user.setPassword(passwordEncoder.encode(user.getPassword()));
		AppUser savedUser = appUserRepository.save(user);
		return savedUser;*/
	

	@Override
	public AppUser findByUsername(String username) {
		 return appUserRepository.findByUsername(username)
		            .orElseThrow(() -> new RuntimeException("User not found: " + username));
	}
}
