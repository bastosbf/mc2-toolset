package br.lncc.sinapad.portengin.action.application;

import java.util.ArrayList;
import java.util.List;

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

public class ApplicationsAction extends BaseAction {

	private static Logger logger = Logger.getLogger(ApplicationsAction.class);

	@Action(value = "/applications", results = { @Result(name = "success", location = "/applications.jsp") })
	public String executables() {
		String uuid = getUuid();
		if (portalConfig.isAllowResourceChoice()) {
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
