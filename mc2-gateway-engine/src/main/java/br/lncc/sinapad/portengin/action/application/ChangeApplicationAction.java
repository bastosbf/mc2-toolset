package br.lncc.sinapad.portengin.action.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.core.data.ApplicationData;
import br.lncc.sinapad.core.data.ApplicationData.VersionData;
import br.lncc.sinapad.core.data.ResourceData;
import br.lncc.sinapad.core.exception.ApplicationServiceException;
import br.lncc.sinapad.core.exception.MonitoringServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class ChangeApplicationAction extends BaseAction {

	private static Logger logger = Logger.getLogger(ChangeApplicationAction.class);

	private String app;

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	private Map<String, String> args;

	public Map<String, String> getArgs() {
		return args;
	}

	public void setArgs(Map<String, String> args) {
		this.args = args;
	}

	@Action(value = "/changeApp", results = { @Result(name = "success", location = "/application.jsp") })
	public String changeApp() {
		String uuid = getUuid();
		boolean isGuest = (Boolean) session.get("isGuest");
		if (app != null) {
			// will select the executable and the default version
			String[] data = app.split(":");
			session.put("app", data[0]);
			if (data.length > 1) {
				session.put("version", data[1]);
			}
			if (data.length > 2) {
				session.put("info", data[2]);
			}
			session.put("versions", application.get(data[0] + "_versions"));
		}
		if (portalConfig.isAllowResourceChoice() && !isGuest) {
			try {
				String appValue = (String) session.get("app");
				String versionValue = (String) session.get("version");
				ApplicationData appData = applicationService.get(uuid, appValue);
				VersionData versionData = appData.getVersion(versionValue);
				List<String> versionQueues = versionData.getQueues();
				queues = new ArrayList<ResourceData>();
				for (String vQueue : versionQueues) {
					ResourceData data = resourceMonitoringService.get(uuid, vQueue);
					if (data != null) {
						queues.add(data);
					}
				}
			} catch (ApplicationServiceException e) {
				PortEnginUtils.handleException(e, logger);
			} catch (MonitoringServiceException e) {
				PortEnginUtils.handleException(e, logger);
			} catch (UserNotAuthorizedException e) {
				PortEnginUtils.handleException(e, logger);
			}
		}
		if (args != null) {
			this.args = args;
		}
		return SUCCESS;
	}

	private List<ResourceData> queues;

	public List<ResourceData> getQueues() {
		return queues;
	}

	public void setQueues(List<ResourceData> queues) {
		this.queues = queues;
	}
}
