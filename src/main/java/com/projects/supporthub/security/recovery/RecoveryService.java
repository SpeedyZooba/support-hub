package com.projects.supporthub.security.recovery;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class RecoveryService 
{
    private final JavaMailSender sender;

    public RecoveryService(JavaMailSender sender)
    {
        this.sender = sender;
    }

    public void sendAccountRecoveryMail(String receiver, String recoveryLink)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(receiver);
        message.setSubject("Password Recovery Request");
        message.setText("Click the link below to reset your password.\nThis link will expire in 15 minutes.\n" + recoveryLink);
        sender.send(message);
    }
}