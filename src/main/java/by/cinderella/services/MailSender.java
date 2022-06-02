package by.cinderella.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class MailSender {

    @Value("{spring.mail.username}")
    private String username;
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.socket.factory.class}")
    private String socketFactoryClass;
    @Value("${spring.mail.smtp.auth}")
    private String isSmptAuth;

    public  void send(String emailTo, String subject, String message) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", socketFactoryClass);
        props.put("mail.smtp.auth", isSmptAuth);
        props.put("mail.smtp.port", port);
        //get Session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        //compose message
        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO,new InternetAddress(emailTo));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);
            //send message
            Transport.send(mimeMessage);
        } catch (MessagingException e) {throw new RuntimeException(e);}

    }
}
