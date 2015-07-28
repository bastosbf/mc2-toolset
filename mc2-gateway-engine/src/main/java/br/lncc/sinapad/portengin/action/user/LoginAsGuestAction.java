package br.lncc.sinapad.portengin.action.user;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.core.exception.FileServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.config.ModulesConfig.GuestUserModule;
import br.lncc.sinapad.portengin.config.PortalConfig.Application;
import br.lncc.sinapad.portengin.utils.PortEnginConstants;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class LoginAsGuestAction extends BaseAction {

	public static Logger logger = Logger.getLogger(LoginAsGuestAction.class);

	@Action(value = "/loginAsGuest", results = { @Result(name = "success", location = "/applications.jsp"), @Result(name = "error", location = "/index.jsp") })
	public String login() {
		try {
			GuestUserModule guestUserModule = modulesConfig.getGuestUserModule();
			if (guestUserModule.isEnabled()) {
				Application appConfig = portalConfig.getApplications().getApplications().get(0);
				String app = appConfig.getName();
				session.put("app", app);
				String version = appConfig.getDefaultVersion();
				session.put("version", version);
				String info = appConfig.getInfo();
				session.put("info", info);
				session.put("versions", application.get(app + "_versions"));
				session.put("isGuest", true);
				session.put("isVOMS", false);

				String[] guestVersions = guestUserModule.getAllowedVersions().split(",");
				List<String> guestVersionList = Arrays.asList(guestVersions);
				if (!guestVersionList.contains(version) && !guestVersionList.contains("ALL")) {
					session.put("version", guestVersionList.get(0));
				}
				String uuid = getPortEnginUuid();
				String project = getProject();
				if (!fileService.exists(uuid, PortEnginConstants.PORTENGIN_GUEST_PROJECT, null, project)) {
					fileService.create(uuid, PortEnginConstants.PORTENGIN_GUEST_PROJECT, null, project, true);
				}
				// creates a hash for the current guest user
				UUID dir = UUID.randomUUID();
				String hash = dir.toString();
				session.put("hash", hash);
				fileService.create(uuid, PortEnginConstants.PORTENGIN_GUEST_PROJECT, new String[] { project }, hash, true);
				setUuid(uuid);
				return SUCCESS;
			}
		} catch (FileServiceException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (UserNotAuthorizedException e) {
			PortEnginUtils.handleException(e, logger);
		}
		invalid = Boolean.TRUE;
		return ERROR;
	}

	private boolean invalid;

	public boolean isInvalid() {
		return invalid;
	}

	public void setInvalid(boolean invalid) {
		this.invalid = invalid;
	}

}
