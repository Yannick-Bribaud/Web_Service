package com.sendMail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMail {
	
	public static boolean EnvoyerMail(String expediteur,String password,String destinataire,String object,String Corpsmessage) {
		
		System.out.println("Envoi en cours...");
		
		//création de la session de Mail
		Properties propriete = new Properties();
		propriete.put("mail.smtp.auth", "true");
		propriete.put("mail.smtp.starttls.enable", "true");
		propriete.put("mail.smtp.host", "smtp.gmail.com");
		propriete.put("mail.smtp.port", "587");
		
		Session session = Session.getInstance(propriete, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(expediteur, password);	
			}
		});
		
		
		Message message = prepareMessage(session,expediteur,destinataire,Corpsmessage,object);
		try {
			Transport.send(message);
			System.out.println("Mail envoyé");
			return true;
			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;	
	}

	
	private static Message prepareMessage(Session session,String expediteur,String destinataire,String Corpsmessage,String Object) {
		Message message = new MimeMessage(session);
		
		try {
			message.setFrom(new InternetAddress(expediteur));
			message.setRecipient(Message.RecipientType.TO,new InternetAddress(destinataire));
			message.setSubject(Object);
			message.setText(Corpsmessage);
			
			return message;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
