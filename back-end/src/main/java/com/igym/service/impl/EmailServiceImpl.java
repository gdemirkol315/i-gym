package com.igym.service.impl;

import com.igym.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Override
    public void sendPasswordEmail(String to, String password) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Welcome to iGym - Your Account Details");
            message.setText("Welcome to iGym!\n\n" +
                    "Your account has been created. Here are your login details:\n\n" +
                    "Email: " + to + "\n" +
                    "Temporary Password: " + password + "\n\n" +
                    "Please log in and change your password immediately for security purposes.\n\n" +
                    "Best regards,\n" +
                    "iGym Team");

            mailSender.send(message);
        } catch (Exception e) {
            // Consider logging the exception or handling it appropriately
        }
    }
}
