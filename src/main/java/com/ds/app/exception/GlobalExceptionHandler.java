package com.ds.app.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
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

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException2.class)
    public ResponseEntity<ErrorResponse> handleRequestNotFound_2(ResourceNotFoundException2 ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HrResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(HrResourceNotFoundException ex) {
        return build(ex.getStatus(), ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(HrBusinessRuleException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessRule(HrBusinessRuleException ex) {
        return build(ex.getStatus(), ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(HrDuplicateResourceException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(HrDuplicateResourceException ex) {
        return build(ex.getStatus(), ex.getErrorCode(), ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> build(HttpStatus status, String errorCode, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("errorCode", errorCode);
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(BlacklistedBankException.class)
    public ResponseEntity<String> handleBlacklistedBankException(BlacklistedBankException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
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

    @ExceptionHandler(BankAccountLockedException.class)
    public ResponseEntity<String> handleBankAccountLocked(BankAccountLockedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(InvestmentComplianceException.class)
    public ResponseEntity<String> handleInvestmentComplianceException(InvestmentComplianceException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
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
        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse2> buildErrorResponse2(
            HttpStatus status, String message, String errorCode,
            Map<String, String> fieldErrors, String path) {
        ErrorResponse2 body = ErrorResponse2.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .errorCode(errorCode)
                .fieldErrors(fieldErrors)
                .build();
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse2> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse2(HttpStatus.NOT_FOUND, ex.getMessage(), "RESOURCE_NOT_FOUND", null, request.getRequestURI());
    }

    @ExceptionHandler(InsufficientLeaveBalanceException.class)
    public ResponseEntity<ErrorResponse2> handleInsufficientLeaveBalance(InsufficientLeaveBalanceException ex, HttpServletRequest request) {
        return buildErrorResponse2(HttpStatus.BAD_REQUEST, ex.getMessage(), "INSUFFICIENT_LEAVE_BALANCE", null, request.getRequestURI());
    }

    @ExceptionHandler(InvalidLeaveStateException.class)
    public ResponseEntity<ErrorResponse2> handleInvalidLeaveState(InvalidLeaveStateException ex, HttpServletRequest request) {
        return buildErrorResponse2(HttpStatus.CONFLICT, ex.getMessage(), "INVALID_LEAVE_STATE", null, request.getRequestURI());
    }

    @ExceptionHandler(InvalidTimesheetStateException.class)
    public ResponseEntity<ErrorResponse2> handleInvalidTimesheetState(InvalidTimesheetStateException ex, HttpServletRequest request) {
        return buildErrorResponse2(HttpStatus.CONFLICT, ex.getMessage(), "INVALID_TIMESHEET_STATE", null, request.getRequestURI());
    }

    @ExceptionHandler(DuplicateRegularizationException.class)
    public ResponseEntity<ErrorResponse2> handleDuplicateRegularization(DuplicateRegularizationException ex, HttpServletRequest request) {
        return buildErrorResponse2(HttpStatus.CONFLICT, ex.getMessage(), "DUPLICATE_REGULARIZATION", null, request.getRequestURI());
    }

    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<ErrorResponse2> handleInvalidDateRange(InvalidDateRangeException ex, HttpServletRequest request) {
        return buildErrorResponse2(HttpStatus.BAD_REQUEST, ex.getMessage(), "INVALID_DATE_RANGE", null, request.getRequestURI());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse2> handleUnauthorized(ForbiddenException ex, HttpServletRequest request) {
        return buildErrorResponse2(HttpStatus.FORBIDDEN, ex.getMessage(), "UNAUTHORIZED", null, request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse2> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return buildErrorResponse2(HttpStatus.BAD_REQUEST, "Validation failed", "VALIDATION_FAILED", fieldErrors, request.getRequestURI());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse2> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return buildErrorResponse2(HttpStatus.BAD_REQUEST, ex.getMessage(), "BAD_REQUEST", null, request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse2> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = "Invalid value '" + ex.getValue() + "' for parameter '" + ex.getName() + "'";
        return buildErrorResponse2(HttpStatus.BAD_REQUEST, message, "INVALID_PARAMETER", null, request.getRequestURI());
    }

    @ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
    public ResponseEntity<ErrorResponse2> handleAccessDenied(Exception ex) {
        ErrorResponse2 error = ErrorResponse2.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Forbidden")
                .message("Access Denied")
                .errorCode("ACCESS_DENIED")
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred: " + ex.getMessage());
    }
    
    @ExceptionHandler(EmployeeNotFoundException1.class)
    public ResponseEntity<String> handleEmployeeNotFoundException(EmployeeNotFoundException1 ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(EmployeeCodeAlreadyExistsException.class)
    public ResponseEntity<String> handleEmployeeCodeAlreadyExists(EmployeeCodeAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<String> handleDuplicateEmail(DuplicateEmailException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(DuplicatePhoneException.class)
    public ResponseEntity<String> handleDuplicatePhone(DuplicatePhoneException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(ProfileDeletedException.class)
    public ResponseEntity<String> handleProfileDeleted(ProfileDeletedException ex) {
        return ResponseEntity.status(HttpStatus.GONE).body(ex.getMessage());
    }

    @ExceptionHandler(ProfileAlreadyExistsException.class)
    public ResponseEntity<String> handleProfileAlreadyExists(ProfileAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<String> handleAccountLocked(AccountLockedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<String> handleFileStorage(FileStorageException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

}