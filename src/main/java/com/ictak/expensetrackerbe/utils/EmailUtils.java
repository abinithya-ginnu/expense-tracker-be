package com.ictak.expensetrackerbe.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

@Component
public class EmailUtils {

    public boolean sendResetPasswordMail(String recipientEmail, String encryptedEmail) throws UnsupportedEncodingException {
        final String username = "ayoola.customercare@gmail.com";
        final String password = "holbhfqhfvtwldob";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        String subject = "Reset Password";
        String body = "Dear Ayoola User,\n\nPlease reset your password by clicking the below link\n" +
                "http://localhost:3000/resetpassword?key=" + URLEncoder.encode(encryptedEmail, StandardCharsets.UTF_8) + "\n\nBest regards,\nAyoola";

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Support email sent successfully.");
            return true;

        } catch (MessagingException e) {
            System.out.println("Exception when support mail is sent: {}" + Arrays.toString(e.getStackTrace()));
        }
        return false;
    }
}