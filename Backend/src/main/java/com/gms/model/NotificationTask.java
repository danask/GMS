package com.gms.model;

import com.sun.mail.smtp.SMTPTransport;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;


public class NotificationTask {
	private static final String SMTP_SERVER = "smtp.gmail.com";
    private static final String USERNAME = "danielahndev";
    private static final String PASSWORD = "!admin1010";

    private static final String EMAIL_FROM = "danielahndev@gmail.com";
    private static final String EMAIL_TO = "danielahndev@gmail.com";
    private static final String EMAIL_TO_CC = "";

    private static final String EMAIL_SUBJECT = "[GMS] Alert message comming from GMS ";
    private static final String EMAIL_TEXT = ""
    		+ "<p><b> Dear System Administrator,</b></p>"
    		+ "<p>This is a message from GMS. <br>"
    		+ "You got the alert from the motion dectection<br> "
    		+ "Please check the current status out from GMS server page.</p>"
    		+ "<p>Sincerely<br>"
    		+ "GMS Manager</p>";

    public static void sendEmail() {

        Properties props = System.getProperties();

        props.put("mail.smtp.user", EMAIL_FROM);
        props.put("mail.smtp.host", SMTP_SERVER);
        props.put("mail.smtp.port", 465);
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.socketFactory.port", 465);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");       
        Session session = Session.getInstance(props, null);
        
        Message msg = new MimeMessage(session);

        try {

            msg.setFrom(new InternetAddress(EMAIL_FROM));

            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(EMAIL_TO, false));

            msg.setSubject(EMAIL_SUBJECT);
			
			// TEXT email
            //msg.setText(EMAIL_TEXT);

			// HTML email
            msg.setDataHandler(new DataHandler(new HTMLDataSource(EMAIL_TEXT)));

            
			SMTPTransport t = (SMTPTransport) session.getTransport("smtp");
			
			// connect
            t.connect(SMTP_SERVER, USERNAME, PASSWORD);
			
			// send
            t.sendMessage(msg, msg.getAllRecipients());

            System.out.println("Response: " + t.getLastServerResponse());

            t.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    static class HTMLDataSource implements DataSource {

        private String html;

        public HTMLDataSource(String htmlString) {
            html = htmlString;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            if (html == null) throw new IOException("html message is null!");
            return new ByteArrayInputStream(html.getBytes());
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new IOException("This DataHandler cannot write HTML");
        }

        @Override
        public String getContentType() {
            return "text/html";
        }

        @Override
        public String getName() {
            return "HTMLDataSource";
        }
    }
}
