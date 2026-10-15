package com.doritech.microservice.CustomerService.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(String toEmail, String customerName, String customerCode) {
        try {
            // Load template
            String template = loadTemplate("templates/welcome-email-template.html");

            // Replace placeholders
            String body = template
                    .replace("{{customerName}}", customerName)
                    .replace("{{customerCode}}", customerCode);

            String subject = "Welcome to Doritech!";

            // Print email details to console
//            System.out.println("====== Sending Welcome Email ======");
//            System.out.println("To: " + toEmail);
//            System.out.println("Subject: " + subject);
//            System.out.println("Body: ");
//            System.out.println(body);
//            System.out.println("==================================");

            // Send email
            sendHtmlEmail(toEmail, subject, body);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send welcome email to " + toEmail, e);
        }
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); 
            mailSender.send(message);
            System.out.println("Email sent successfully to " + to);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email to " + to, e);
        }
    }

    private String loadTemplate(String path) throws Exception {
        ClassPathResource resource = new ClassPathResource(path);
        return new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);
    }
}

