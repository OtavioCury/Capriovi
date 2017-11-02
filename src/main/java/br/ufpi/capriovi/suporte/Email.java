package br.ufpi.capriovi.suporte;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import br.ufpi.capriovi.entidades.cadastros.Administrador;
import br.ufpi.capriovi.entidades.cadastros.Pecuarista;
import br.ufpi.capriovi.entidades.cadastros.Usuario;


public class Email {	

	private static String urlCapriovi = "http://localhost:8080/";

	/**
	 * Envia email de mudança de senha
	 * @param usuario
	 * @throws RuntimeException
	 */
	public static void confirmaEmail(Usuario usuario) throws RuntimeException {

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream in = classLoader.getResourceAsStream("/resources/mail.properties");				

		final Properties properties = new Properties();
		final MimeMessage mimeMessage;

		Date data = new Date();
		String token;
		String url = null;

		try {
			token = MyEncoder.encriptar(data.toString()+usuario.getEmail());
			url = urlCapriovi + "alteraSenha.xhtml?token="+token;
			usuario.setTokenSenha(token);
		} catch (NoSuchAlgorithmException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}				

		try {
			properties.load(in);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Session session = Session.getInstance(properties,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(properties.getProperty("mail.user"), properties.getProperty("mail.password"));
			}
		});

		session.setDebug(true);

		try
		{
			mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(properties.getProperty("mail.user")));
			mimeMessage.setSentDate(new Date());
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(usuario.getEmail()));
			mimeMessage.setSubject("Sistema Capriovi");
			mimeMessage.setText("Altere sua senha por meio do link: " + url);

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Transport.send(mimeMessage);
					} catch (MessagingException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Envia email para um admin avisando que determinado usuário se cadastrou no sistema
	 * @param administrador
	 * @param pecuarista
	 */
	public static void avisaAdmin(Administrador administrador, Pecuarista pecuarista){
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream in = classLoader.getResourceAsStream("/resources/mail.properties");	

		final Properties properties = new Properties();
		final MimeMessage mimeMessage;

		try {
			properties.load(in);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Session session = Session.getInstance(properties,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(properties.getProperty("mail.user"), properties.getProperty("mail.password"));
			}
		});

		session.setDebug(true);

		try
		{
			mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(properties.getProperty("mail.user")));
			mimeMessage.setSentDate(new Date());
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(administrador.getEmail()));
			mimeMessage.setSubject("Solicitação de registro");
			mimeMessage.setText("O usuário " + pecuarista.getNome()+" realizou o cadastro no sistema Capriovi, acesse o sistema para habilitá-lo.\n"
					+"Nome: "+ pecuarista.getNome()+"\n"
					+"Email: "+ pecuarista.getEmail()+"\n"
					+"Celular: "+ pecuarista.getCelular()+"\n"
					+"Endereço: "+ pecuarista.getEndereco()+"\n");

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Transport.send(mimeMessage);
					} catch (MessagingException e) {
						e.printStackTrace();
					}
				}
			}).start();			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método que envia o email no cadastro
	 * @param pecuarista
	 */
	public static void cadastro(Pecuarista pecuarista){
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream in = classLoader.getResourceAsStream("/resources/mail.properties");				

		final Properties properties = new Properties();
		final MimeMessage mimeMessage;

		Date data = new Date();
		String token;
		String url = null;

		try {
			token = MyEncoder.encriptar(data.toString()+pecuarista.getEmail());
			url = urlCapriovi + "confirmaCadastro.xhtml?token="+token;
			pecuarista.setToken(token);
		} catch (NoSuchAlgorithmException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}				

		try {
			properties.load(in);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Session session = Session.getInstance(properties,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(properties.getProperty("mail.user"), properties.getProperty("mail.password"));
			}
		});

		session.setDebug(true);

		try
		{
			mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(properties.getProperty("mail.user")));
			mimeMessage.setSentDate(new Date());
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(pecuarista.getEmail()));
			mimeMessage.setSubject("Sistema Capriovi");
			mimeMessage.setText("Confirme seu cadastro cliquando no link: " + url);

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Transport.send(mimeMessage);
					} catch (MessagingException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Método que envia o email de acesso ao sistema
	 * @param pecuarista
	 */
	public static void acesso(Usuario pecuarista, boolean acessoUsuario){
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream in = classLoader.getResourceAsStream("/resources/mail.properties");				

		final Properties properties = new Properties();
		final MimeMessage mimeMessage;

		try {
			properties.load(in);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Session session = Session.getInstance(properties,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(properties.getProperty("mail.user"), properties.getProperty("mail.password"));
			}
		});

		session.setDebug(true);

		try
		{
			mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(properties.getProperty("mail.user")));
			mimeMessage.setSentDate(new Date());
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(pecuarista.getEmail()));
			mimeMessage.setSubject("Acesso concedido pelos administradores");
			if (acessoUsuario == true) {
				mimeMessage.setText("Seu cadastro foi confirmado pelos administradores do sistema! Você já pode acessar sua conta pelo link: "+urlCapriovi);
			}else{
				mimeMessage.setText("Seu cadastro foi confirmado pelos administradores do sistema! Mas você ainda precisa confirmar seu cadastro para acessar o Capriovi");
			}


			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Transport.send(mimeMessage);
					} catch (MessagingException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Método para enviar email da página inicial 
	 * @param nome
	 * @param email
	 * @param assunto
	 * @param mensagem
	 * @throws RuntimeException
	 */
	public static void enviarMenssagem(String nome, String email,String assunto, String mensagem) throws RuntimeException{

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream in = classLoader.getResourceAsStream("/resources/mail.properties");				

		final Properties properties = new Properties();
		final MimeMessage mimeMessage;

		try {
			properties.load(in);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Session session = Session.getInstance(properties,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(properties.getProperty("mail.user"), properties.getProperty("mail.password"));
			}
		});

		session.setDebug(true);

		try
		{
			mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(properties.getProperty("mail.user")));

			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(properties.getProperty("mail.user")));
			mimeMessage.setSentDate(new Date());
			mimeMessage.setReplyTo(new InternetAddress[] 
					{new InternetAddress(email)});
			mimeMessage.setSubject(assunto);
			mimeMessage.setText(mensagem);

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Transport.send(mimeMessage);
					} catch (MessagingException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}

