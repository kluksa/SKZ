package dhz.skz.util.mailer;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class EmailSessionBean {

    private final int port = 25;
    private final String host = "cirus.dhz.hr";
    private final String from = "kraljevic@cirus.dhz.hr";
    private final boolean auth = true;
    private final String username = "kraljevic@cirus.dhz.hr";
    private final String password = "cbm@MaIl";
    private final Protocol protocol = Protocol.SMTP;
    private final boolean debug = false;

    public void sendEmail(URL[] urlovi, String subject, String body, byte[] attachment, String attachemntMIMEType) throws IOException {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        switch (protocol) {
            case SMTPS:
                props.put("mail.smtp.ssl.enable", true);
                break;
            case TLS:
                props.put("mail.smtp.starttls.enable", true);
                break;
        }

        Authenticator authenticator = null;
        if (auth) {
            props.put("mail.smtp.auth", true);
            authenticator = new Authenticator() {
                private PasswordAuthentication pa = new PasswordAuthentication(username, password);
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return pa;
                }
            };
        }

        Session session = Session.getInstance(props, authenticator);
        session.setDebug(debug);

        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] address = getAdrese(urlovi); 
            message.setRecipients(Message.RecipientType.TO, address);
            message.setSubject(subject);
            message.setSentDate(new Date());
         
            Multipart multipart = new MimeMultipart("alternative");
            
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);
            MimeBodyPart attachFilePart = new MimeBodyPart();
            attachFilePart.setDataHandler(new DataHandler(new ByteArrayDataSource(attachment,attachemntMIMEType)));
            attachFilePart.setFileName("data.csv");
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachFilePart);
            
            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }
    
    public InternetAddress[] getAdrese(URL[] urlovi) throws AddressException {
        InternetAddress[] adrese = new InternetAddress[urlovi.length];
        for ( int i = 0; i< urlovi.length; i++) {
            adrese[i] = new InternetAddress(urlovi[i].getPath());
        }
        return adrese;
    }

    public void sendEmail(URL[] urlovi, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        switch (protocol) {
            case SMTPS:
                props.put("mail.smtp.ssl.enable", true);
                break;
            case TLS:
                props.put("mail.smtp.starttls.enable", true);
                break;
        }

        Authenticator authenticator = null;
        if (auth) {
            props.put("mail.smtp.auth", true);
            authenticator = new Authenticator() {
                private PasswordAuthentication pa = new PasswordAuthentication(username, password);
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return pa;
                }
            };
        }

        Session session = Session.getInstance(props, authenticator);
        session.setDebug(debug);

        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] address = getAdrese(urlovi); 
            
            message.setRecipients(Message.RecipientType.TO, address);
            message.setSubject(subject);
            message.setSentDate(new Date());
            
            message.setText(body);
            Transport.send(message);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }
}