package com.ictak.expensetrackerbe.utils;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailUtils {
    public static void sendResetPasswordEmail(String recipientEmail, String resetLink) {
        final String username = "your_email@example.com"; // Replace with your email
        final String password = "your_password"; // Replace with your password

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.freesmtpservers.com"); // Replace with your SMTP server
        //props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587"); // Replace with your SMTP port (587 is typical for TLS)

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
            message.setSubject("Reset Your Password");
            message.setText("Dear User,"
                    + "\n\nClick the link below to reset your password:"
                    + "\n\n" + resetLink);

            Transport.send(message);

            System.out.println("Password reset email sent successfully.");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
