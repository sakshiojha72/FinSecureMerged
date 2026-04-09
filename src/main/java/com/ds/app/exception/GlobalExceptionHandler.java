package com.ds.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// ----- Tarushi Code Started

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {

		/* using error response , instead of Edxception response */
		ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceNotFoundException2.class)
	public ResponseEntity<ErrorResponse> handleRequestNotFound_2(ResourceNotFoundException2 ex) {

		ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	// --- Tarushi Code end -----

	@ExceptionHandler(HrResourceNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleNotFound(HrResourceNotFoundException ex) {
		return build(ex.getStatus(), ex.getErrorCode(), ex.getMessage());
	}

	// Handles logic-specific errors defined by our business rules(Bhawna)
	@ExceptionHandler(HrBusinessRuleException.class)
	public ResponseEntity<Map<String, Object>> handleBusinessRule(HrBusinessRuleException ex) {
		return build(ex.getStatus(), ex.getErrorCode(), ex.getMessage());
	}

	// Prevents duplicate entries (e.g., trying to use an existing employee
	// code)(Bhawna)
	@ExceptionHandler(HrDuplicateResourceException.class)
	public ResponseEntity<Map<String, Object>> handleDuplicate(HrDuplicateResourceException ex) {
		return build(ex.getStatus(), ex.getErrorCode(), ex.getMessage());
	}

	@ExceptionHandler(BlacklistedBankException.class)
	public ResponseEntity<String> handleBlacklistedBankException(BlacklistedBankException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(SalaryJobException.class)
	public ResponseEntity<String> handleSalaryJobException(SalaryJobException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(BankAccountAlreadyRegistered.class)
	public ResponseEntity<String> handleBankAccountAlreadyRegistered(BankAccountAlreadyRegistered ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(ResourceAlreadyExistException.class)
	public ResponseEntity<String> handleResourceAlreadyExist(ResourceAlreadyExistException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}

	@ExceptionHandler(AuthorizationDeniedException.class)
	public ResponseEntity<String> handleAuthorizationDenied(AuthorizationDeniedException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body("Access denied. You are not authorized to perform this action.");
	}

	@ExceptionHandler(BankAccountLockedException.class)
	public ResponseEntity<String> handleBankAccountLocked(BankAccountLockedException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(InvestmentComplianceException.class)
	public ResponseEntity<String> handleInvestmentComplianceException(InvestmentComplianceException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	private ResponseEntity<Map<String, Object>> build(
            HttpStatus status, String errorCode, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status",    status.value());
        body.put("errorCode", errorCode);
        body.put("message",   message);
        return new ResponseEntity<>(body, status);
    }

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<String> handleEnumMismatch(MethodArgumentTypeMismatchException ex) {
		String paramName = ex.getName();
		String invalidValue = String.valueOf(ex.getValue());

		String message;
		if (ex.getRequiredType() != null && ex.getRequiredType().isEnum()) {
			Object[] enumValues = ex.getRequiredType().getEnumConstants();
			message = "Invalid value '" + invalidValue + "' for '" + paramName + "'. Accepted values: "
					+ Arrays.toString(enumValues);
		} else {
			message = "Invalid value '" + invalidValue + "' for parameter '" + paramName + "'";
		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGenericException(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<String> handleConflict(ConflictException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}


	@ExceptionHandler(BusinessRuleException.class)
	public ResponseEntity<String> handleBusinessRule(BusinessRuleException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}


  
 
 

    
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<String> handleEmployeeNotFound(EmployeeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
 
  @ExceptionHandler(TrainingNotFoundException.class)
    public ResponseEntity<String> handleTrainingNotFound(TrainingNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(TrainingNotCompleteException.class)
    public ResponseEntity<String> handleTrainingNotCompleted(TrainingNotCompleteException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
 
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponse> handleCustomException(CustomException ex) {
        ExceptionResponse response = new ExceptionResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }



     

}