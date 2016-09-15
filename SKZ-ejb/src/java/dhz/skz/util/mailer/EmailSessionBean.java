package dhz.skz.util.mailer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class EmailSessionBean {

    private int port = 25;
    private String host = "cirus.dhz.hr";
    private String from = "kraljevic@cirus.dhz.hr";
    private boolean auth = true;
    private String username = "kraljevic@cirus.dhz.hr";
    private String password = "cbm@MaIl";
    private Protocol protocol = Protocol.SMTP;
    private boolean debug = true;

    public void sendEmail(URL url, String subject, String body, byte[] attachment, String attachemntMIMEType) throws IOException {
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
            InternetAddress[] address = {new InternetAddress(url.getPath())};
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
}