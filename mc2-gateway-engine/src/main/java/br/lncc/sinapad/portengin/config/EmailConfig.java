package br.lncc.sinapad.portengin.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

import br.lncc.sinapad.portengin.utils.PortEnginUtils;

@XmlRootElement(name = "email")
public class EmailConfig implements Serializable {

	private static Logger logger = Logger.getLogger(EmailConfig.class);

	public EmailConfig(String xml) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(EmailConfig.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			InputStream is = null;
			File file = new File(xml);
			if (file.exists()) {
				is = new FileInputStream(file);
			} else {
				is = getClass().getResourceAsStream(xml);
			}
			EmailConfig config = (EmailConfig) jaxbUnmarshaller.unmarshal(is);

			this.setSmtpHost(config.getSmtpHost());
			this.setEmailFrom(config.getEmailFrom());
			this.setEmailTo(config.getEmailTo());
			this.setJobFinishedNotificationMessage(config.getJobFinishedNotificationMessage());
		} catch (JAXBException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (FileNotFoundException e) {
			PortEnginUtils.handleException(e, logger);
		}
	}

	private String smtpHost;
	private String emailFrom;
	private String emailTo;
	private JobFinishedNotificationMessage jobFinishedNotificationMessage;

	public EmailConfig() {

	}

	public String getSmtpHost() {
		return smtpHost;
	}

	@XmlElement(name = "smtp-host")
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public String getEmailFrom() {
		return emailFrom;
	}

	@XmlElement(name = "email-from")
	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}

	public String getEmailTo() {
		return emailTo;
	}

	@XmlElement(name = "email-to")
	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public JobFinishedNotificationMessage getJobFinishedNotificationMessage() {
		return jobFinishedNotificationMessage;
	}

	@XmlElement(name = "job-finished-notification-message")
	public void setJobFinishedNotificationMessage(JobFinishedNotificationMessage jobFinishedNotificationMessage) {
		this.jobFinishedNotificationMessage = jobFinishedNotificationMessage;
	}

	public static class JobFinishedNotificationMessage {
		private String subject;
		private String body;

		public String getSubject() {
			return subject;
		}

		@XmlElement(name = "subject")
		public void setSubject(String subject) {
			this.subject = subject;
		}

		public String getBody() {
			return body;
		}

		@XmlElement(name = "body")
		public void setBody(String body) {
			this.body = body;
		}
	}

}
