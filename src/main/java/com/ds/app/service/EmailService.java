package com.ds.app.service;

import java.time.LocalDateTime;

public interface EmailService {

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
}