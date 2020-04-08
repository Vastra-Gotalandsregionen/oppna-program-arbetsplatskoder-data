package se.vgregion.arbetsplatskoder.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

@Service
public class MailMan {

    @Value("${mail.smtp.host}")
    private String host;

    @Value("${mail.smtp.port}")
    private String port;

    public MailMan() {
        super();
    }

    public MailMan(String host, String port) {
        this();
        this.host = host;
        this.port = port;
    }


    public void send(String from, String withSubject, String andText, List<String> toReceivers, List<String> andCopiesTo) {
        if (andCopiesTo == null) {
            andCopiesTo = Collections.EMPTY_LIST;
        }
        send(from, withSubject, andText, toReceivers.toArray(new String[toReceivers.size()]), andCopiesTo.toArray(new String[andCopiesTo.size()]));
    }

    public void send(String from, String withSubject, String andText, String[] toReceivers, String[] andCopiesTo) {

        // Get system properties
        Properties properties = new Properties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);

        System.out.println("Host is " + host);
        System.out.println("Port is " + port);

        // Get the default Session object.
        Session session = Session.getInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            for (String receiver : toReceivers) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            }

            for (String carbonCopy : andCopiesTo) {
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(carbonCopy));
            }

            // Set Subject: header field
            message.setSubject(withSubject);

            // Now set the actual message
            message.setText(andText, "UTF-8","html");

            message.setText(andText);

            // Send message
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
