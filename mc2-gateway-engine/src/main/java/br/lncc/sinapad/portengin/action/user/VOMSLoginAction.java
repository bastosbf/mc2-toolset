package br.lncc.sinapad.portengin.action.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.core.data.ApplicationData;
import br.lncc.sinapad.core.data.ApplicationData.VersionData;
import br.lncc.sinapad.core.data.ResourceData;
import br.lncc.sinapad.core.data.UserData;
import br.lncc.sinapad.core.exception.ApplicationServiceException;
import br.lncc.sinapad.core.exception.AuthenticationServiceException;
import br.lncc.sinapad.core.exception.FileServiceException;
import br.lncc.sinapad.core.exception.MonitoringServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.core.service.authentication.AuthenticationService.Domain;
import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.config.AuthenticationConfig.VOMSAuthentication;
import br.lncc.sinapad.portengin.config.PortalConfig.Application;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class VOMSLoginAction extends BaseAction {

	public static Logger logger = Logger.getLogger(VOMSLoginAction.class);

	private File cert;

	public File getCert() {
		return cert;
	}

	public void setCert(File cert) {
		this.cert = cert;
	}

	@Action(value = "/vomsLogin", results = { @Result(name = "success", location = "/applications.jsp"), @Result(name = "error", location = "/index.jsp") })
	public String login() {
		try {

			VOMSAuthentication vomsAuthentication = authenticationConfig.getVomsAuthentication();
			if (vomsAuthentication.isEnabled()) {
				String uuid = authenticationService.login(null, new FileInputStream(cert), Domain.VOMS);
				String project = getProject();
				if (uuid == null || !fileService.exists(uuid, project, null, null)) {
					invalidCert = true;
					return ERROR;
				}
				UserData user = authenticationService.info(uuid);
				if (user != null) {
					session.put("userName", user.getUsername());
				}
				setUuid(uuid);
				Application appConfig = portalConfig.getApplications().getApplications().get(0);
				String app = appConfig.getName();
				session.put("app", app);
				String version = appConfig.getDefaultVersion();
				session.put("version", version);
				String info = appConfig.getInfo();
				session.put("info", info);
				session.put("versions", application.get(app + "_versions"));
				session.put("isGuest", false);
				session.put("isVOMS", true);
				if (portalConfig.isAllowResourceChoice()) {
					try {
						ApplicationData appData = applicationService.get(uuid, app);
						VersionData versionData = appData.getVersion(version);
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
		} catch (AuthenticationServiceException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (FileNotFoundException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (FileServiceException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (UserNotAuthorizedException e) {
			PortEnginUtils.handleException(e, logger);
		}
		invalidCert = Boolean.TRUE;
		return ERROR;
	}

	private List<ResourceData> queues;

	public List<ResourceData> getQueues() {
		return queues;
	}

	public void setQueues(List<ResourceData> queues) {
		this.queues = queues;
	}

	private boolean invalidCert;

	public boolean isInvalidCert() {
		return invalidCert;
	}

	public void setInvalidCert(boolean invalidCert) {
		this.invalidCert = invalidCert;
	}

}
