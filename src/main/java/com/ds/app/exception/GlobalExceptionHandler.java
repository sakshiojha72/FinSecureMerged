package com.ds.app.exception;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<?> handleException(Exception e){
		return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
	} 
	
	 @ExceptionHandler
	    public ResponseEntity<ExceptionResponse> handleEmployeeCodeException(EmployeeCodeAlreadyExistsException ex) {

	        System.out.println("Inside EmployeeCodeAlreadyExistsException");

	        ExceptionResponse response = new ExceptionResponse();

	        response.setDate(LocalDate.now());
	        response.setTime(LocalTime.now());
	        response.setUrl("/finsecure/employee");
	        response.setClassName("EmployeeController");
	        response.setErrorMsg(ex.getMessage());
	        response.setSolution("Verify Employee Code");

	        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    }
	  

	 @ExceptionHandler
	 public ResponseEntity<ExceptionResponse> handleAccountLockedException(AccountLockedException ex) {

	     System.out.println("Inside AccountLockedException");

	     ExceptionResponse response = new ExceptionResponse();

	     response.setDate(LocalDate.now());
	     response.setTime(LocalTime.now());
	     response.setUrl("/finsecure/employee");
	     response.setClassName("EmployeeController");
	     response.setErrorMsg(ex.getMessage());
	     response.setSolution("Please verify userId and try again");

	     return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	 }
	 
	 

	 @ExceptionHandler
	 public ResponseEntity<ExceptionResponse> handleDuplicateEmailException(DuplicateEmailException ex) {

	     System.out.println("Inside DuplicateEmailException");

	     ExceptionResponse response = new ExceptionResponse();

	     response.setDate(LocalDate.now());
	     response.setTime(LocalTime.now());
	     response.setUrl("/finsecure/employee");
	     response.setClassName("EmployeeController");
	     response.setErrorMsg(ex.getMessage());
	     response.setSolution("Please enter different emailId. this email already exists");

	     return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	 }
	 

	 @ExceptionHandler
	 public ResponseEntity<ExceptionResponse> handleDuplicatePhoneException(DuplicatePhoneException ex) {

	     System.out.println("Inside DuplicatePhoneException");

	     ExceptionResponse response = new ExceptionResponse();

	     response.setDate(LocalDate.now());
	     response.setTime(LocalTime.now());
	     response.setUrl("/finsecure/employee");
	     response.setClassName("EmployeeController");
	     response.setErrorMsg(ex.getMessage());
	     response.setSolution("Please enter different phone number. This phone number already exists");

	     return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	 }
	 

	 @ExceptionHandler
	 public ResponseEntity<ExceptionResponse> handleEmployeeNotFoundException(EmployeeNotFoundException ex, HttpServletRequest request) {

	     System.out.println("Inside EmployeeNotFoundException");

	     ExceptionResponse response = new ExceptionResponse();

	     response.setDate(LocalDate.now());
	     response.setTime(LocalTime.now());
	     response.setUrl("/finsecure/employee");
	     response.setClassName("EmployeeController");
	     response.setErrorMsg(ex.getMessage());
	     response.setSolution("Please enter valid username. Employee is not found with entered username");

	     return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	 }
	 

	 @ExceptionHandler
	 public ResponseEntity<ExceptionResponse> handleProfileAlreadyExistsException(ProfileAlreadyExistsException ex) {

	     System.out.println("Inside ProfileAlreadyExistsException");

	     ExceptionResponse response = new ExceptionResponse();

	     response.setDate(LocalDate.now());
	     response.setTime(LocalTime.now());
	     response.setUrl("/finsecure/employee");
	     response.setClassName("EmployeeController");
	     response.setErrorMsg(ex.getMessage());
	     response.setSolution("Please try again with different useriD this profile already exists");

	     return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	 }
	 
	 @ExceptionHandler
	 public ResponseEntity<ExceptionResponse> handleProfileDeletedException(ProfileDeletedException ex) {

	     System.out.println("Inside ProfileDeletedException ");

	     ExceptionResponse response = new ExceptionResponse();

	     response.setDate(LocalDate.now());
	     response.setTime(LocalTime.now());
	     response.setUrl("/finsecure/employee");
	     response.setClassName("EmployeeController");
	     response.setErrorMsg(ex.getMessage());
	     response.setSolution("Please find the valid profile");

	     return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	 }
	 	
	 @ExceptionHandler
	 public ResponseEntity<ExceptionResponse> handleFileStorageException(FileStorageException ex) {

	     System.out.println("Inside FileStorageException");

	     ExceptionResponse response = new ExceptionResponse();

	     response.setDate(LocalDate.now());
	     response.setTime(LocalTime.now());
	     response.setUrl("/finsecure/employee/profile/photo");
	     response.setClassName("EmployeeController");
	     response.setErrorMsg(ex.getMessage());
	     response.setSolution("Please upload a valid image file and try again");

	     return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	 }
	 
}//end class
