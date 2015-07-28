package br.lncc.sinapad.portengin.utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.config.EmailConfig;

public class EmailUtils {

	public static void send(String from, String to, String subject, String body) {
		try {
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-" + BaseAction.FACADE + ".xml");			
			EmailConfig emailConfig = (EmailConfig) applicationContext.getBean("emailConfig");

			String smtpServer = emailConfig.getSmtpHost();
			Properties props = System.getProperties();
			props.put("mail.smtp.host", smtpServer);
			Session session = Session.getDefaultInstance(props, null);
			Message msn = new MimeMessage(session);
			if (from == null) {
				from = emailConfig.getEmailFrom();
			}
			if (to == null) {
				to = emailConfig.getEmailTo();
			}
			msn.setFrom(new InternetAddress(from));
			msn.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
			msn.setSubject(subject);
			msn.setText(body);
			msn.setHeader("SINAPAD", "EMAIL-NOTIFICATION");
			msn.setSentDate(new Date());
			Transport.send(msn);
		} catch (Exception ex) {
			PortEnginUtils.handleException(ex, null);
		}

	}
}
