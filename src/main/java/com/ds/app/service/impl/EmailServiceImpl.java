package com.ds.app.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ds.app.entity.Employee;
import com.ds.app.entity.Leave;
import com.ds.app.entity.RegularizationRequest;
import com.ds.app.entity.Timesheet;
import com.ds.app.enums.ApprovalStatus;
import com.ds.app.service.EmailService;

import org.springframework.beans.factory.annotation.Value;

@Service
@Async
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String from;

    private void send(String to, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);
        msg.setFrom("sharmayatin0882@gmail.com");
        mailSender.send(msg);
    }
    
    @Override
    public void sendPlainText(String to, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);
        mailSender.send(msg);
    }

    @Override
    @Async
    public void notifyManagerForNewLeave(Employee employee, Leave leave) {
        Employee manager = employee.getManager();
        if (manager == null || manager.getEmail() == null || manager.getEmail().isBlank()) return;

        String subject = "New Leave Request - " + employee.getFirstName() + " " + employee.getLastName();
        String body = "Hello " + manager.getFirstName() + ",\n\n"
                + employee.getFirstName() + " " + employee.getLastName() + " has applied for leave.\n\n"
                + "Type: " + leave.getLeaveType() + "\n"
                + "Dates: " + leave.getStartDate() + " to " + leave.getEndDate() + "\n"
                + "Days: " + leave.getTotalDays() + "\n"
                + "Reason: " + leave.getReasonForLeave() + "\n"
                + "Status: " + leave.getStatus() + "\n\n"
                + "Please review it in the portal.";
        sendPlainText(manager.getEmail(), subject, body);
    }

    @Override
    @Async
    public void notifyEmployeeForLeaveDecision(Employee employee, Leave leave) {
        if (employee.getEmail() == null || employee.getEmail().isBlank()) return;

        String subject = "Leave Request " + leave.getStatus();
        String body = "Hello " + employee.getFirstName() + ",\n\n"
                + "Your leave request has been " + leave.getStatus() + ".\n\n"
                + "Type: " + leave.getLeaveType() + "\n"
                + "Dates: " + leave.getStartDate() + " to " + leave.getEndDate() + "\n"
                + "Days: " + leave.getTotalDays() + "\n"
                + (leave.getStatus().name().equals("REJECTED")
                ? "Reason: " + (leave.getRejectionReason() == null ? "" : leave.getRejectionReason()) + "\n"
                : "")
                + "\nRegards,\nHR Team";
        sendPlainText(employee.getEmail(), subject, body);
    }

    @Override
    @Async
    public void notifyManagerForCancellationRequest(Employee employee, Leave leave) {
        Employee manager = employee.getManager();
        if (manager == null || manager.getEmail() == null || manager.getEmail().isBlank()) return;

        String subject = "Leave Cancellation Request - " + employee.getFirstName() + " " + employee.getLastName();
        String body = "Hello " + manager.getFirstName() + ",\n\n"
                + employee.getFirstName() + " " + employee.getLastName()
                + " has requested cancellation of an approved leave.\n\n"
                + "Type: " + leave.getLeaveType() + "\n"
                + "Dates: " + leave.getStartDate() + " to " + leave.getEndDate() + "\n"
                + "Days: " + leave.getTotalDays() + "\n"
                + "Current Status: " + leave.getStatus() + "\n\n"
                + "Please review the cancellation request in the portal.";
        sendPlainText(manager.getEmail(), subject, body);
    }

    @Override
    @Async
    public void notifyEmployeeForCancellationDecision(Employee employee, Leave leave, ApprovalStatus decision, String reason) {
        if (employee.getEmail() == null || employee.getEmail().isBlank()) return;

        String subject = "Leave Cancellation Request " + leave.getStatus();
        String body = "Hello " + employee.getFirstName() + ",\n\n"
                + "Your leave cancellation request has been " + leave.getStatus() + ".\n\n"
                + "Type: " + leave.getLeaveType() + "\n"
                + "Dates: " + leave.getStartDate() + " to " + leave.getEndDate() + "\n"
                + "Days: " + leave.getTotalDays() + "\n"
                + (decision == ApprovalStatus.REJECTED ? "Reason: " + (reason == null ? "" : reason) + "\n" : "")
                + "\nRegards,\nHR Team";
        sendPlainText(employee.getEmail(), subject, body);
    }

    // Regularization notifications

    @Override
    @Async
    public void notifyManagerForNewRegularization(Employee employee, RegularizationRequest regularizationRequest) {
        Employee manager = employee.getManager();
        if (manager == null || manager.getEmail() == null || manager.getEmail().isBlank()) return;

        String subject = "New Regularization Request - " + employee.getFirstName() + " " + employee.getLastName();
        String body = "Hello " + manager.getFirstName() + ",\n\n"
                + employee.getFirstName() + " " + employee.getLastName() + " has applied for regularization.\n\n"
                + "Date: " + regularizationRequest.getDate() + "\n"
                + "Punch In: " + regularizationRequest.getPunchInTime() + "\n"
                + "Punch Out: " + regularizationRequest.getPunchOutTime() + "\n"
                + "Reason: " + regularizationRequest.getReason() + "\n"
                + "Status: " + regularizationRequest.getStatus() + "\n\n"
                + "Please review it in the portal.";
        sendPlainText(manager.getEmail(), subject, body);
    }

    @Override
    @Async
    public void notifyEmployeeForRegularizationDecision(Employee employee, RegularizationRequest regularizationRequest) {
        if (employee.getEmail() == null || employee.getEmail().isBlank()) return;

        String subject = "Regularization Request " + regularizationRequest.getStatus();
        String body = "Hello " + employee.getFirstName() + ",\n\n"
                + "Your regularization request has been " + regularizationRequest.getStatus() + ".\n\n"
                + "Date: " + regularizationRequest.getDate() + "\n"
                + "Punch In: " + regularizationRequest.getPunchInTime() + "\n"
                + "Punch Out: " + regularizationRequest.getPunchOutTime() + "\n"
                + "Reason: " + regularizationRequest.getReason() + "\n"
                + (regularizationRequest.getStatus().name().equals("REJECTED")
                ? "Reason for Rejection: "
                + (regularizationRequest.getRejectionReason() == null ? "" : regularizationRequest.getRejectionReason()) + "\n"
                : "")
                + "\nRegards,\nHR Team";
        sendPlainText(employee.getEmail(), subject, body);
    }

    // Timesheet notifications

    @Override
    @Async
    public void notifyManagerForTimesheetSubmission(Employee employee, Timesheet timesheet) {
        Employee manager = employee.getManager();
        if (manager == null || manager.getEmail() == null || manager.getEmail().isBlank()) return;

        String subject = "Timesheet Submitted - " + employee.getFirstName() + " " + employee.getLastName();
        String body = "Hello " + manager.getFirstName() + ",\n\n"
                + employee.getFirstName() + " " + employee.getLastName() + " has submitted a timesheet.\n\n"
                + "Month/Year: " + timesheet.getMonth() + "/" + timesheet.getYear() + "\n"
                + "Total Hours: " + timesheet.getTotalMonthlyMinutes()/60 + "\n"
                + "Status: " + timesheet.getStatus() + "\n\n"
                + "Please review it in the portal.";
        sendPlainText(manager.getEmail(), subject, body);
    }

    @Override
    @Async
    public void notifyEmployeeForTimesheetDecision(Employee employee, Timesheet timesheet) {
        if (employee.getEmail() == null || employee.getEmail().isBlank()) return;

        String subject = "Timesheet " + timesheet.getStatus();
        String body = "Hello " + employee.getFirstName() + ",\n\n"
                + "Your timesheet has been " + timesheet.getStatus() + ".\n\n"
                + "Month/Year: " + timesheet.getMonth() + "/" + timesheet.getYear() + "\n"
                + "Total Hours: " + timesheet.getTotalMonthlyMinutes()/60 + "\n"
                + (timesheet.getStatus().name().equals("REJECTED")
                ? "Reason: " + (timesheet.getRejectionReason() == null ? "" : timesheet.getRejectionReason()) + "\n"
                : "")
                + "\nRegards,\nHR Team";
        sendPlainText(employee.getEmail(), subject, body);
    }

    @Override
    public void sendAllocationEmail(String to, String name, String company, String dept, String project) {
        send(to, "FinSecure — You have been assigned",
                "Dear " + name + ",\n\n"
                        + "You have been assigned to:\n"
                        + "  Company    : " + company + "\n"
                        + "  Department : " + dept + "\n"
                        + "  Project    : " + (project != null ? project : "Not assigned yet") + "\n\n"
                        + "Regards,\nFinSecure HR Team");
    }

    @Override
    public void sendDeallocationEmail(String to, String name, String type) {
        send(to, "FinSecure — Allocation Update",
                "Dear " + name + ",\n\n"
                        + "Your " + type.toLowerCase() + " allocation has been removed.\n"
                        + "Please contact HR for further information.\n\n"
                        + "Regards,\nFinSecure HR Team");
    }

    @Override
    public void sendEscalationEmail(String to, String name) {
        send(to, "FinSecure — Escalation Notice",
                "Dear " + name + ",\n\n"
                        + "An escalation has been raised on your profile.\n"
                        + "Please log in to your dashboard for details.\n\n"
                        + "Regards,\nFinSecure HR Team");
    }

    @Override
    public void sendAppraisalEmail(String to, String name, Double newSalary) {
        send(to, "FinSecure — Salary Appraisal Completed",
                "Dear " + name + ",\n\n"
                        + "Your yearly appraisal is complete.\n"
                        + "Updated salary: " + newSalary + "\n\n"
                        + "Regards,\nFinSecure HR Team");
    }

    @Override
    public void sendStaleEscalationAlert(String to, Long escalationId,
                                         String targetName, String targetEmail,
                                         long daysOpen) {
        send(to,
                "FinSecure — URGENT: Escalation SLA Breached (#" + escalationId + ")",
                "This is an automated alert.\n\n"
                        + "Escalation #" + escalationId + " against " + targetName
                        + " (" + targetEmail + ") has been OPEN for " + daysOpen + " days "
                        + "without action.\n\n"
                        + "SLA requirement: Escalations must be moved to IN_PROGRESS within 7 days.\n\n"
                        + "Please review immediately.\n\n"
                        + "Regards,\nFinSecure Automated Alert System"
        );
    }

    @Override
    public void sendSalaryCreditEmail(String toEmail, String employeeName,
                                      String month, double netSalary, String maskedAccount) {

        send(toEmail,
                "Salary Credited — " + month,
                "Dear " + employeeName + ",\n\n"
                        + "Your salary for " + month + " has been credited.\n\n"
                        + "Amount: INR " + netSalary + "\n"
                        + "Account: " + maskedAccount + "\n\n"
                        + "Regards,\nFinSecure Finance Team");
    }

    @Override
    public void sendSalaryJobCompletedEmail(String toEmail,
                                            String jobName,
                                            int total,
                                            int success,
                                            int failed,
                                            int skipped) {

        send(toEmail,
                "Salary Job Completed — " + jobName,
                "Salary Job: " + jobName + " completed.\n\n"
                        + "Total Employees : " + total + "\n"
                        + "Successfully Credited : " + success + "\n"
                        + "Skipped Credited : " + skipped + "\n"
                        + "Failed : " + failed + "\n\n"
                        + "Regards,\nFinSecure System");
    }

    @Override
    public void sendFraudAlertEmailToEmployee(String toEmail,
                                              String subject,
                                              Long id,
                                              String firstName,
                                              String lastName,
                                              Integer modifiedAttempted,
                                              LocalDateTime coolDownPeriod) {

        send(toEmail,
                "🚨 Suspicious Bank Account Activity Detected",
                "Employee ID: " + id + "\n"
                        + "Name: " + firstName + " " + lastName + "\n"
                        + "Changed bank account " + modifiedAttempted + " times in 24 hours.\n"
                        + "Account is now in cooldown until: " + coolDownPeriod + "\n\n"
                        + "Please review immediately.");
    }

    @Override
    public void sendFraudAlertEmailToFinance(String toEmail,
                                             Long id,
                                             String firstName,
                                             String lastName,
                                             Integer modifiedAttempted,
                                             LocalDateTime coolDownPeriod) {

        send(toEmail,
                "🚨 Fraud Monitoring Alert – Bank Account Change Activity",
                "Fraud monitoring systems have detected unusual activity related to bank account modifications.\n\n"
                        + "Employee Details:\n"
                        + "Employee ID   : " + id + "\n"
                        + "Employee Name : " + firstName + " " + lastName + "\n\n"
                        + "Incident Summary:\n"
                        + "• Number of bank account change attempts (last 24 hours): " + modifiedAttempted + "\n"
                        + "• Automated cooldown enforced until: " + coolDownPeriod + "\n\n"
                        + "Action Required:\n"
                        + "Please review this activity for potential fraud risk.\n\n"
                        + "This is a system-generated alert.");
    }
}