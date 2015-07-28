package br.lncc.sinapad.cli.server.cmd;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import br.lncc.sinapad.cli.server.Command;
import br.lncc.sinapad.cli.utils.CLILogger;
import br.lncc.sinapad.cli.utils.CLIResultCodes;
import br.lncc.sinapad.cli.utils.CLIUtils;
import br.lncc.sinapad.core.service.authentication.AuthenticationService;
import br.lncc.sinapad.core.service.authentication.AuthenticationService.Domain;

public class LoginCommand extends UnicastRemoteObject implements Command {

	private static CLILogger logger = CLILogger.getCLILogger(LoginCommand.class);

	private AuthenticationService authenticationService;

	public LoginCommand(AuthenticationService authenticationService) throws RemoteException {
		this.authenticationService = authenticationService;
	}

	@Override
	public String execute(String uuid, String... params) throws RemoteException {
		try {
			LongOpt[] longopts = new LongOpt[3];
			longopts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
			longopts[1] = new LongOpt("user", LongOpt.REQUIRED_ARGUMENT, null, 'u');
			longopts[2] = new LongOpt("password", LongOpt.REQUIRED_ARGUMENT, null, 'p');

			Getopt g = new Getopt("login", params, "", longopts);
			g.setOpterr(false);

			String user = null;
			String password = null;
			int c;
			while ((c = g.getopt()) != -1) {
				switch (c) {
				case 'u':
					user = g.getOptarg();
					break;
				case 'p':
					password = g.getOptarg();
					break;
				case 'h':
					return usage(user);
				default:
					return CLIResultCodes.INTERNAL_SERVER_ERROR + "::Could not execute the command \n " + usage(user);
				}
			}
			if (user == null) {
				return CLIResultCodes.USER_ERROR + "::--user parameter is required!";
			}
			if (password == null) {
				return CLIResultCodes.USER_ERROR + "::--password parameter is required!";
			}
			String result = authenticationService.login(user, password, Domain.LDAP);
			if (result == null) {
				return CLIResultCodes.USER_NOT_AUTHORIZED + "::The username or password are incorrect!";
			}
			return CLIResultCodes.OK + "::" + result;
		} catch (Exception e) {
			CLIUtils.handleException(e, logger);
			return CLIResultCodes.INTERNAL_SERVER_ERROR + "::Could not execute the command \n " + usage(uuid);
		}
	}

	@Override
	public String usage(String uuid, String... params) throws RemoteException {
		return "Usage: login --user <user name> --password <user passsword>";
	}

}
