package smarteasycontracts.smarteasycontracts;

import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;

import it.ozimov.springboot.mail.model.InlinePicture;
import it.ozimov.springboot.mail.service.EmailService;
import it.ozimov.springboot.mail.service.exception.CannotSendEmailException;
/**
 * @author Javier Iglesias Sanz
 * @version 1.0
 * @since 2019-02-21s
 * This Class manages the Email connections.
 * */
public class EmailConnector {
	String from, host, port, password;
	Properties properties;
	@Autowired
	public EmailService emailService = new EmailService() {
		
		@Override
		public MimeMessage send(it.ozimov.springboot.mail.model.Email mimeEmail, String template,
				Map<String, Object> modelObject, InlinePicture... inlinePictures) throws CannotSendEmailException {
			return null;
		}
		
		@Override
		public MimeMessage send(it.ozimov.springboot.mail.model.Email mimeEmail) {
			return null;
		}
	};
	public EmailConnector() {
		this.from = "javiglesias18@gmail.com";
		this.host = "smtp.gmail.com";
		this.port = "587";
		this.password = "Einstein1955";
		properties = new Properties();
		properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
	}
	public boolean Send(String to_Email, String body) {
		Session session;
		MimeMessage message;
		session = Session.getInstance(properties,new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
		});
		message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(this.from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to_Email));
			message.setSubject("SNEC password recovery");
			message.setText(body);
			Transport.send(message);
			System.out.println("Email sent.");
		} catch(Exception ex) {
			System.err.println("Error sending email.");
			ex.printStackTrace();
		}
		return true;
	}
}
