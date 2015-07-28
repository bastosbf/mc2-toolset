package br.lncc.sinapad.portengin.action.user;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.core.exception.AuthenticationServiceException;
import br.lncc.sinapad.core.exception.FileServiceException;
import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.exceptions.SessionExpiredException;
import br.lncc.sinapad.portengin.utils.PortEnginConstants;

public class LogoutAction extends BaseAction {

	private static Logger logger = Logger.getLogger(LogoutAction.class);

	@Action(value = "/logout", results = { @Result(name = "success", location = "/index.jsp") })
	public String logout() {
		try {
			session.remove("app");
			session.remove("version");
			session.remove("info");
			session.remove("versions");
			session.remove(PortEnginConstants.USER);
			boolean isGuest = (Boolean) session.remove("isGuest");

			String uuid = getUuid();
			if (isGuest) {
				String hash = (String) session.remove("hash");
				String project = getProject();
				fileService.delete(uuid, PortEnginConstants.PORTENGIN_GUEST_PROJECT, new String[] { project }, hash);
			}
			authenticationService.logout(uuid);
		} catch (SessionExpiredException see) {
			// if user on session is already null do nothing.
		} catch (AuthenticationServiceException e) {
			// if some error occur while loging out do nothing
		} catch (FileServiceException e) {
			// if some error occur while deleting the guest user area do nothing
		} catch (Exception e) {
			// if some error occur do nothing
		}

		return SUCCESS;
	}

}
