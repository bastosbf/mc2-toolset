package br.lncc.sinapad.rest.authentication;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import br.lncc.sinapad.core.exception.AuthenticationServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.core.service.authentication.AuthenticationService.Domain;
import br.lncc.sinapad.rest.RESTOperation;
import br.lncc.sinapad.rest.data.AuthenticationResult;
import br.lncc.sinapad.rest.data.Result;
import br.lncc.sinapad.rest.utils.RESTResultCodes;
import br.lncc.sinapad.rest.utils.RESTUtils;

@Path("/authentication")
public class AuthenticationRESTOperation extends RESTOperation {

	private static Logger logger = Logger.getLogger(AuthenticationRESTOperation.class);

	/**
	 * 
	 * Authenticate an use given an userName and password
	 * 
	 * @param username
	 *          The name of the user;
	 * @param password
	 *          The password of the user
	 * @return A result containing the uuid of the authenticated user and the code
	 *         of the result. <br>
	 *         - {@link RESTResultCodes#OK} if success; <br>
	 *         - {@link RESTResultCodes#USER_NOT_AUTHORIZED} if the user is not
	 *         valid; <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if username or password is
	 *         null; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/login")
	public AuthenticationResult login(@FormParam("username") String username, @FormParam("password") String password) {
		try {

			if (username == null || username.isEmpty()) {
				return new AuthenticationResult(RESTResultCodes.USER_ERROR);
			}

			if (password == null || username.isEmpty()) {
				return new AuthenticationResult(RESTResultCodes.USER_ERROR);
			}

			String uuid = authenticationService.login(username, password, Domain.LDAP);
			if (uuid != null) {
				AuthenticationResult result = new AuthenticationResult();
				result.setUuid(uuid);
				result.setCode(RESTResultCodes.OK);
				return result;
			} else {
				return new AuthenticationResult(RESTResultCodes.USER_NOT_AUTHORIZED);
			}
		} catch (AuthenticationServiceException e) {
			RESTUtils.handleException(e, logger);
		}
		return new AuthenticationResult(RESTResultCodes.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 
	 * Logout an user
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @return A result containing the code of the result. <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if the uuid is null; <br>
	 *         - {@link RESTResultCodes#USER_NOT_AUTHORIZED} if the user is not logged; <br>
	 *         - {@link RESTResultCodes#OK} if success; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/logout")
	public Result logout(@FormParam("uuid") String uuid) {
		try {
			if (uuid != null) {
				authenticationService.logout(uuid);
				return new Result(RESTResultCodes.OK);
			}
			return new Result(RESTResultCodes.USER_ERROR);
		} catch (AuthenticationServiceException e) {
			RESTUtils.handleException(e, logger);
		} catch (UserNotAuthorizedException e) {
			return new Result(RESTResultCodes.USER_NOT_AUTHORIZED);
		}
		return new Result(RESTResultCodes.INTERNAL_SERVER_ERROR);
	}
}
