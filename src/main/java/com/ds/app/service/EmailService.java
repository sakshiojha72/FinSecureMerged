package com.ds.app.service;

public interface EmailService {

    void sendAllocationEmail(String to, String name, String company, String dept, String project);

    void sendDeallocationEmail(String to, String name, String type);

    void sendEscalationEmail(String to, String name);

    void sendAppraisalEmail(String to, String name, Double newSalary);

    void sendStaleEscalationAlert(String to, Long escalationId, String targetName, String targetEmail, long daysOpen);
}
