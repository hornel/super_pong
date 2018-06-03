package pong.internet;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

/**
 * Classe qui permet d'envoyer des e-mails.
 * Sources:
 * http://www.mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/
 * http://www.tutorialspoint.com/javamail_api/javamail_api_send_email_with_attachment.htm
 */
public final class EmailSender {

    /**
     * Permet d'envoyer un e-mail par Gmail.
     * @param sender le compte Gmail qui envoit l'e-mail.
     * @param senderPassword le mot de passe du compte qui envoit l'e-mail
     * @param recipient le compte qui doit recevoir l'e-mail.
     * @param subject le titre de l'e-mail.
     * @param content le contenu de l'e-mail.
     */
    public static void sendEmailViaGmail(String sender, final String senderPassword, String recipient, String subject, String content) {

        final String host = "smtp.gmail.com";
        final String port = "587";

        final String username = sender;

        Properties p = System.getProperties();

        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.host", host);
        p.put("mail.smtp.port", port);

        Session session = Session.getInstance(p,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, senderPassword);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet d'envoyer un e-mail par Gmail avec une piece jointe.
     * @param sender le compte Gmail qui envoit l'e-mail.
     * @param senderPassword le mot de passe du compte qui envoit l'e-mail
     * @param recipient le compte qui doit recevoir l'e-mail.
     * @param subject le titre de l'e-mail.
     * @param content le contenu de l'e-mail.
     * @param attachment la piece jointe.
     */
    public static void sendEmailViaGmail(String sender, final String senderPassword, String recipient, String subject, String content, String attachment) {

        final String host = "smtp.gmail.com";
        final String port = "587";

        final String username = sender;

        Properties p = System.getProperties();

        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.host", host);
        p.put("mail.smtp.port", port);

        Session session = Session.getInstance(p,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, senderPassword);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            Multipart multipart = new MimeMultipart();
            message.setFrom(new InternetAddress(sender));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject(subject);

            BodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(content);
            multipart.addBodyPart(textBodyPart);

            BodyPart attachmentBodyPart = new MimeBodyPart();
            DataSource attachmentSource = new FileDataSource(attachment);
            attachmentBodyPart.setDataHandler(new DataHandler(attachmentSource));
            attachmentBodyPart.setFileName("Attachment");
            multipart.addBodyPart(attachmentBodyPart);

            message.setContent(multipart);

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
