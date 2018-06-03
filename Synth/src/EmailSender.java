import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public final class EmailSender {

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
                new javax.mail.Authenticator() {
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
                new javax.mail.Authenticator() {
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
