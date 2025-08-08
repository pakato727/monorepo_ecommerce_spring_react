package com.example.ecommercespring.service;

import com.example.ecommercespring.model.Account;
import com.example.ecommercespring.model.VerificationToken;
import com.example.ecommercespring.repo.TokenRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.util.UUID;

@Service
public class VerificationService {

    @Autowired
    private TokenRepository tr;

    @Autowired
    private JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    public VerificationService(TemplateEngine templateEngine, JavaMailSender mailSender) {
        this.templateEngine = templateEngine;
        this.mailSender = mailSender;
    }

    public String generateVerificationToken(Account account) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(account, token);
        tr.save(verificationToken);
        return token;
    }

    public boolean sendVerificationEmail(Account account, String token) throws MessagingException {
        String token1 = token;
        String name=account.getUser().getName();

        String verificationUrl="http://localhost:5173/accountVerify?token="+token;
        String emailContent = "<div style=\"font-family: Arial, sans-serif; text-align: center;\">" +
                "<h2>Benvenuto, " + name + "!</h2>" +
                "<p>Grazie per esserti registrato. Per completare la registrazione, clicca sul pulsante qui sotto:</p>" +
                "<a href=\"" + verificationUrl + "\" style=\"" +
                "display: inline-block; padding: 10px 20px; background-color: #007BFF; color: white;" +
                "text-decoration: none; border-radius: 5px;\">Conferma la Registrazione</a>" +
                "</div>";

        // Creazione del messaggio email HTML
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(account.getEmail());
        helper.setSubject("Conferma la tua registrazione su My Website");
        helper.setText(emailContent, true); // `true` indica che è HTML

        mailSender.send(message);
        return true;
    }
}

