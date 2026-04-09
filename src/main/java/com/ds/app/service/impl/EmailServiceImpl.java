package com.ds.app.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ds.app.service.EmailService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@Async

public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender mailSender;


   
    
    @Value("${spring.mail.username}")
    private String fromEmail;


    private void send(String to, String subject, String body) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);
        msg.setFrom("sharmayatin0882@gmail.com");
        mailSender.send(msg);
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
                " Suspicious Bank Account Activity Detected",
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
                " Fraud Monitoring Alert – Bank Account Change Activity",
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
    
    //  COMMON METHOD
  
    public void sendEmail(String to, String subject, String body) {


        log.info("Sending email to: {} | Subject: {}", to, subject);


        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);


            mailSender.send(message);


            log.info("Email sent successfully to: {}", to);


        } catch (MailException e) {


            log.error("Email failed to: {} | Error: {}", to, e.getMessage(), e);
        }
    }


    // ENROLLMENT EMAIL
    @Override
    public void sendEnrollmentEmail(String to, String employeeName, String trainingName) {


        log.info("Preparing enrollment email for employee: {}", employeeName);


        String subject = "Enrolled in Training: " + trainingName;


        String body =
                "Dear " + employeeName + ",\n\n"
                        + "You have been enrolled in the following training:\n"
                        + "Training : " + trainingName + "\n\n"
                        + "Please login to dashboard.\n\n"
                        + "Regards,\nFinSecure HR Team";


        sendEmail(to, subject, body);
    }


    //  TRAINING START EMAIL
    @Override
    public void sendTrainingStartEmail(String to, String employeeName, String trainingName, String startDate) {


        log.info("Preparing training start email for: {}", employeeName);


        String subject = "Training Started: " + trainingName;


        String body =
                "Dear " + employeeName + ",\n\n"
                        + "Your training has started.\n"
                        + "Training: " + trainingName + "\n"
                        + "Start Date: " + startDate + "\n\n"
                        + "Regards,\nFinSecure HR Team";


        sendEmail(to, subject, body);
    }


    //  TRAINING COMPLETE EMAIL
    @Override
    public void sendTrainingCompleteEmail(String to, String employeeName, String trainingName) {


        log.info("Preparing training completion email for: {}", employeeName);


        String subject = "Training Completed: " + trainingName;


        String body =
                "Dear " + employeeName + ",\n\n"
                        + "Your training is completed.\n"
                        + "Training: " + trainingName + "\n\n"
                        + "Upload certification.\n\n"
                        + "Regards,\nFinSecure HR Team";


        sendEmail(to, subject, body);
    }


    //  CERT UPLOAD EMAIL (TO HR)
    @Override
    public void sendCertUploadEmail(String hrEmail, String employeeName, String certName) {


        log.info("Sending certification upload email to HR for employee: {}", employeeName);


        String subject = "Certification Uploaded — " + employeeName;


        String body =
                "Dear HR,\n\n"
                        + employeeName + " uploaded certification:\n"
                        + certName + "\n\n"
                        + "Please verify.\n\n"
                        + "FinSecure System";


        sendEmail(hrEmail, subject, body);
    }


    //  CERT VERIFIED EMAIL
    @Override
    public void sendCertVerificationEmail(String to, String employeeName, String certName) {


        log.info("Sending certification verification email to: {}", employeeName);


        String subject = "Certification Verified — " + certName;


        String body =
                "Dear " + employeeName + ",\n\n"
                        + "Your certification is verified.\n"
                        + "Certificate: " + certName + "\n\n"
                        + "You are eligible for projects.\n\n"
                        + "FinSecure HR Team";


        sendEmail(to, subject, body);
    }
}