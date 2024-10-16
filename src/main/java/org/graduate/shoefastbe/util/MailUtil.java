package org.graduate.shoefastbe.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.graduate.shoefastbe.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Value;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;
@AllArgsConstructor
@Getter
@Setter
public class MailUtil {
    private static final String email = "pphuc9122002@gmail.com";

    private static final String password = "efxxykccrzktdmmv";

    public static void sendEmailOrder(OrderEntity order) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(email, false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(order.getEmail()));
        StringBuilder sb = new StringBuilder()
                .append("Đơn hàng " + order.getId()).append("<br/>")
                .append("Tổng tiền: " + order.getTotal()).append("<br/>")
                .append("Ngày tạo: " + order.getCreateDate()).append("<br/>")
                .append("Người nhận:" + order.getFullName()).append("<br/>")
                .append("SDT: " + order.getPhone()).append("<br/>")
                .append("Địa chỉ: " + order.getAddress()).append("<br/>")
                .append("Theo dõi trạng thái đơn hàng tại đây: ")
                        .append("http://localhost:3000/order/detail/");
        msg.setSubject("Cửa hàng giày ShoeFast thông báo");
        msg.setContent(sb.toString(), "text/html; charset=utf-8");
        msg.setSentDate(new Date());
        Transport.send(msg);
    }

    public static void sendmailForgotPassword(String receive, String password) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(email, false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receive));
        msg.setSubject("ShoeFast thông báo");
        msg.setContent("New Pasword: " + password, "text/html");
        msg.setSentDate(new Date());

        Transport.send(msg);
    }
}

