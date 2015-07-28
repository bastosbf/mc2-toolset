package br.lncc.sinapad.portengin.action.user;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.portengin.action.BaseAction;

public class ShibbolethLogin extends BaseAction {

	public static Logger logger = Logger.getLogger(ShibbolethLogin.class);
	private String ssid;

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	@Action(value = "/shibbolethLogin", results = { @Result(name = "success", location = "/applications.jsp"), @Result(name = "error", location = "/index.jsp") })
	public String shibLogin() {
		//authenticationService.login(userName, ssid, Domain.SIBBOLETH);
		return ERROR;
	}
}
