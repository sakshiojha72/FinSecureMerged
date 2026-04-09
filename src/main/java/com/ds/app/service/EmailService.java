package com.ds.app.service;

import java.time.LocalDateTime;

import com.ds.app.entity.Employee;
import com.ds.app.entity.Leave;
import com.ds.app.entity.RegularizationRequest;
import com.ds.app.entity.Timesheet;
import com.ds.app.enums.ApprovalStatus;

public interface EmailService {
	
	void sendPlainText(String to, String subject, String body);
	
	void notifyManagerForNewLeave(Employee employee, Leave leave);
    void notifyEmployeeForLeaveDecision(Employee employee, Leave leave);
    void notifyManagerForCancellationRequest(Employee employee, Leave leave);
    void notifyEmployeeForCancellationDecision(Employee employee, Leave leave, ApprovalStatus decision, String reason);

    // Regularization
    void notifyManagerForNewRegularization(Employee employee, RegularizationRequest regularizationRequest);
    void notifyEmployeeForRegularizationDecision(Employee employee, RegularizationRequest regularizationRequest);

    // Timesheet
    void notifyManagerForTimesheetSubmission(Employee employee, Timesheet timesheet);
    void notifyEmployeeForTimesheetDecision(Employee employee, Timesheet timesheet);

    void sendAllocationEmail(String to, String name, String company, String dept, String project);

    void sendDeallocationEmail(String to, String name, String type);

    void sendEscalationEmail(String to, String name);

    void sendAppraisalEmail(String to, String name, Double newSalary);

    void sendStaleEscalationAlert(String to, Long escalationId, String targetName, String targetEmail, long daysOpen);

    void sendSalaryCreditEmail(String toEmail, String employeeName,
                               String month, double netSalary, String maskedAccount);

    void sendSalaryJobCompletedEmail(String toEmail, String jobName,
                                     int total, int success, int failed, int skipped);

    void sendFraudAlertEmailToEmployee(String toEmail, String subject,
                                       Long id, String firstName, String lastName,
                                       Integer modifiedAttempted, LocalDateTime coolDownPeriod);

    void sendFraudAlertEmailToFinance(String toEmail, Long id,
                                      String firstName, String lastName,
                                      Integer modifiedAttempted, LocalDateTime coolDownPeriod);
    
void sendEnrollmentEmail(String to,String employeeName,String trainingName);
	
	void sendTrainingStartEmail(String to,String employeeName,String trainingName,String startDate);
	
	void sendTrainingCompleteEmail(String to,String employeeName,String trainingName);
	
	void sendCertUploadEmail(String hrEmail,String employeeName,String certName);
	
	void sendCertVerificationEmail(String to,String employeeName,String certName);

    
    
}