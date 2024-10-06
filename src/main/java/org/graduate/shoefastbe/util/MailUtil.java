package org.graduate.shoefastbe.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;

public class MailUtil {
    public static void sendmailForgotPassword(String receive, String password) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("pphuc9122002@gmail.com", "efxxykccrzktdmmv");
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("pphuc9122002@gmail.com", false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receive));
        msg.setSubject("ShoeFast thông báo");
        msg.setContent("New Pasword: " + password, "text/html");
        msg.setSentDate(new Date());

        Transport.send(msg);
    }
}

