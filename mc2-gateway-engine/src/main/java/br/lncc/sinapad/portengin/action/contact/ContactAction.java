package br.lncc.sinapad.portengin.action.contact;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.utils.EmailUtils;

public class ContactAction extends BaseAction {

	private String name;
	private String email;
	private String subject;
	private String msn;

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getSubject() {
		return subject;
	}

	public String getMsn() {
		return msn;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	private Boolean sent = Boolean.FALSE;

	public Boolean getSent() {
		return sent;
	}

	public void setSent(Boolean sent) {
		this.sent = sent;
	}

	private Boolean invalid = Boolean.FALSE;

	public Boolean getInvalid() {
		return invalid;
	}

	public void setInvalid(Boolean invalid) {
		this.invalid = invalid;
	}

	@Action(value = "/contact", results = { @Result(name = "success", location = "/contact.jsp") })
	public String contact() {
		return SUCCESS;
	}

	@Action(value = "/sendMessage", results = { @Result(name = "success", location = "/contact.jsp"), @Result(name = "error", location = "/contact.jsp") })
	public String sendMessage() {
		String project = getProject();
		boolean isGuest = (Boolean) session.get("isGuest");

		if (isGuest) {
			String remoteAddr = request.getRemoteAddr();
			ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
			reCaptcha.setPrivateKey("6Lfyf8cSAAAAAJ9qXZpQAvs05FxsTjIYVHFh4mcS");

			String challenge = request.getParameter("recaptcha_challenge_field");
			String uresponse = request.getParameter("recaptcha_response_field");
			if (uresponse != null) {
				ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);
				if (!reCaptchaResponse.isValid()) {
					invalid = Boolean.TRUE;
					return ERROR;
				}
			}
		}
		String info = "\nUsername: " + session.get("userName") + "\nProject: " + project + "\n\n";

		String to = emailConfig.getEmailTo();
		String subject = "Mail from: " + this.name + " subject: " + this.subject;
		EmailUtils.send(String.valueOf(session.get("email")), to, subject, info + msn);
		sent = Boolean.TRUE;
		return SUCCESS;
	}

}
