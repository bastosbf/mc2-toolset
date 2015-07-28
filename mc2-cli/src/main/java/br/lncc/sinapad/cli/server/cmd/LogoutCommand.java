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

public class LogoutCommand extends UnicastRemoteObject implements Command {

	private static CLILogger logger = CLILogger.getCLILogger(LogoutCommand.class);

	private AuthenticationService authenticationService;

	public LogoutCommand(AuthenticationService authenticationService) throws RemoteException {
		this.authenticationService = authenticationService;
	}

	@Override
	public String execute(String uuid, String... params) throws RemoteException {
		try {
			LongOpt[] longopts = new LongOpt[2];
			longopts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
			Getopt g = new Getopt("logout", params, "", longopts);
			g.setOpterr(false);

			int c;
			while ((c = g.getopt()) != -1) {
				switch (c) {
				case 'h':
					return usage(uuid);
				default:
					return CLIResultCodes.INTERNAL_SERVER_ERROR + "::Could not execute the command \n " + usage(uuid);
				}
			}
			authenticationService.logout(uuid);
			return CLIResultCodes.OK + "::logout completed";
		} catch (Exception e) {
			CLIUtils.handleException(e, logger);
			return CLIResultCodes.INTERNAL_SERVER_ERROR + "::Could not execute the command \n " + usage(uuid);
		}
	}

	@Override
	public String usage(String uuid, String... params) throws RemoteException {
		return "logout";
	}

}
