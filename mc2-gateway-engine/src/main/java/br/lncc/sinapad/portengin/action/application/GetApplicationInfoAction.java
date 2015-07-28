package br.lncc.sinapad.portengin.action.application;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.core.data.ApplicationData;
import br.lncc.sinapad.core.exception.ApplicationServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class GetApplicationInfoAction extends BaseAction {

	private static Logger logger = Logger.getLogger(GetApplicationInfoAction.class);

	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	private String app;

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	@Action(value = "/getAppInfo", results = { @Result(name = "success", location = "/applications.jsp") })
	public String getApplicationInfo() {
		String uuid = getUuid();
		if (app != null) {
			// will select the executable and the default version
			String[] data = app.split(":");
			try {
				session.put("app", data[0]);
				ApplicationData application = applicationService.get(uuid, data[0]);
				info = application.getInfo();
			} catch (ApplicationServiceException e) {
				PortEnginUtils.handleException(e, logger);
			} catch (UserNotAuthorizedException e) {
				PortEnginUtils.handleException(e, logger);
			}
		}
		return SUCCESS;
	}

}
