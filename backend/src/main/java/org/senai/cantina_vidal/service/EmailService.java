package org.senai.cantina_vidal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    String cantinaEmail;

    public void composeEmail(String code, String toEmail) {
        String subject = "Código de Verificação";

        String message = "Olá, seu código de verificação é: " + code;

        sendEmail(toEmail, subject, message);
    }

    public void sendEmail(String toEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(cantinaEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}
