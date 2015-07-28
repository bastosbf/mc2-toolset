package br.lncc.sinapad.portengin.action.contact;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.utils.EmailUtils;

public class ReportErrorAction extends BaseAction {

	private String type;
	private String msn;
	private String exception;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMsn() {
		return msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	@Action(value = "/reportError", results = { @Result(name = "success", location = "/index.jsp") })
	public String reportError() {
		StringBuilder sb = new StringBuilder("Error Type: ");
		sb.append(type);
		sb.append("\n");
		sb.append("User Name: ");
		sb.append(session.get("userName"));
		sb.append("\n");
		sb.append("Message: \n");
		sb.append(msn);
		sb.append("\n");
		sb.append("Exception: \n");
		sb.append(exception);
		String subject = "PortEngin - ERROR REPORT";
		EmailUtils.send(null, null, subject, sb.toString());
		return SUCCESS;
	}

}
