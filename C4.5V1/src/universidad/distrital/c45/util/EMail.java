package universidad.distrital.c45.util;


import java.util.Properties;
/*
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
*/
public class EMail {

	/*public void enviar(String empece, String termine, String segundos, String conjuntoDatos){

		final String username = "nikolucas@gmail.com";
		final String password = "Dihidrocodeina1";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("nikolucas@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("nikolucas@gmail.com"));
			message.setSubject("Terminacion conjunto de datos " + conjuntoDatos);
			message.setText(empece +
				 "\n\n " +
				 termine + 
				 "\n\n " +
				 "Duración total en segundos: " + segundos);

			Transport.send(message);

			//System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}*/
}