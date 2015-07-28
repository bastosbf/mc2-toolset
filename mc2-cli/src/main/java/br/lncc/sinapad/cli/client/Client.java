package br.lncc.sinapad.cli.client;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.util.Arrays;

import br.lncc.sinapad.cli.server.Command;
import br.lncc.sinapad.cli.utils.CLILogger;
import br.lncc.sinapad.cli.utils.CLIResultCodes;
import br.lncc.sinapad.cli.utils.CLIUtils;

public class Client {

	private static CLILogger logger = CLILogger.getCLILogger(Client.class);

	public static void main(String[] args) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}

		try {
			String type = args[0];
			String user = args[1];
			Command cmd = (Command) Naming.lookup(type);

			if (args.length > 2 && "USAGE".equals(args[2])) {
				System.out.println(cmd.usage(user, Arrays.copyOfRange(args, 3, args.length)));
			} else {
				System.out.println(cmd.execute(user, Arrays.copyOfRange(args, 2, args.length)));
			}
		} catch (Exception e) {
			e.printStackTrace();
			CLIUtils.handleException(e, logger);
			System.out.println(CLIResultCodes.INTERNAL_SERVER_ERROR + "::RMI error");
			System.exit(-1);
		}
	}

}
