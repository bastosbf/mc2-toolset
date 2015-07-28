package br.lncc.sinapad.portengin.action.user;

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
import br.lncc.sinapad.portengin.config.AuthenticationConfig.LDAPAuthentication;
import br.lncc.sinapad.portengin.config.PortalConfig.Application;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class LDAPLoginAction extends BaseAction {

	public static Logger logger = Logger.getLogger(LDAPLoginAction.class);

	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Action(value = "/ldapLogin", results = { @Result(name = "success", location = "/applications.jsp"), @Result(name = "error", location = "/index.jsp") })
	public String login() {
		try {

			LDAPAuthentication ldapAuthentication = authenticationConfig.getLdapAuthentication();
			if (ldapAuthentication.isEnabled()) {
				String uuid = authenticationService.login(userName, password, Domain.LDAP);
				String project = getProject();
				if (uuid == null || !fileService.exists(uuid, project, null, null)) {
					invalid = true;
					return ERROR;
				}
				UserData user = authenticationService.info(uuid);
				if (user != null) {
					session.put("userName", user.getUsername());
					session.put("email", user.getEmail());
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
				session.put("isVOMS", false);
				if (portalConfig.isAllowResourceChoice()) {
					try {
						ApplicationData appData = applicationService.get(uuid, app);
						if (appData != null) {
							VersionData versionData = appData.getVersion(version);
							if (versionData != null) {
								List<String> versionQueues = versionData.getQueues();
								queues = new ArrayList<ResourceData>();
								for (String vQueue : versionQueues) {
									ResourceData data = resourceMonitoringService.get(uuid, vQueue);
									if (data != null) {
										queues.add(data);
									}
								}
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
		} catch (FileServiceException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (UserNotAuthorizedException e) {
			PortEnginUtils.handleException(e, logger);
		}
		invalid = Boolean.TRUE;
		return ERROR;
	}

	private List<ResourceData> queues;

	public List<ResourceData> getQueues() {
		return queues;
	}

	public void setQueues(List<ResourceData> queues) {
		this.queues = queues;
	}

	private boolean invalid;

	public boolean isInvalid() {
		return invalid;
	}

	public void setInvalid(boolean invalid) {
		this.invalid = invalid;
	}

}
