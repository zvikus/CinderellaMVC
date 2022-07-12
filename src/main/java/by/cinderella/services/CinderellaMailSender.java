package by.cinderella.services;

import by.cinderella.model.organizer.Organizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.Properties;

@Service
public class CinderellaMailSender {
    @Autowired
    private JavaMailSender mailSender;

    @Value("{spring.mail.username}")
    private String username;

    public  void send(String emailTo, String subject, String message) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        //get Session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("cinderella.minsk.service@gmail.com","lhiepbmdvdqrgwxi");
                    }
                });
        //compose message
        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO,new InternetAddress(emailTo));
            mimeMessage.setSubject(subject, "UTF-8");
            mimeMessage.setText(message, "text/html; charset=UTF-8");
            //send message
            Transport.send(mimeMessage);
        } catch (MessagingException e) {throw new RuntimeException(e);}

    }

}
