package controllers;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import models.Conta;
import play.mvc.Controller;
import play.mvc.With;

@With(Seguranca.class)
public class Email extends Controller{

	String from = "italofreitas613@gmail.com";
	String senha = "italo2.0";
	Session session;
	public Email() {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "465");

		session = Session.getDefaultInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, senha);
			}
		});

	}
	
	public void mandaEmail(String texto, String titulo, String email) {
		
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			InternetAddress.parse(email);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			message.setSubject(titulo);
			message.setText(texto);
			Transport.send(message);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
