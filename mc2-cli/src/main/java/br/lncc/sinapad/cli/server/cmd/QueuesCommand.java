package br.lncc.sinapad.cli.server.cmd;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import br.lncc.sinapad.cli.server.Command;
import br.lncc.sinapad.cli.utils.CLILogger;
import br.lncc.sinapad.cli.utils.CLIResultCodes;
import br.lncc.sinapad.cli.utils.CLIUtils;
import br.lncc.sinapad.core.data.ApplicationData;
import br.lncc.sinapad.core.data.ApplicationData.VersionData;
import br.lncc.sinapad.core.data.ResourceData;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.core.service.application.ApplicationService;
import br.lncc.sinapad.core.service.monitoring.ResourceMonitoringService;

public class QueuesCommand extends UnicastRemoteObject implements Command {

	private static CLILogger logger = CLILogger.getCLILogger(QueuesCommand.class);

	private ApplicationService applicationService;
	private ResourceMonitoringService resourceMonitoringService;

	public QueuesCommand(ApplicationService applicationService, ResourceMonitoringService resourceMonitoringService) throws RemoteException {
		this.applicationService = applicationService;
		this.resourceMonitoringService = resourceMonitoringService;
	}

	@Override
	public String execute(String uuid, String... params) throws RemoteException {
		try {
			LongOpt[] longopts = new LongOpt[3];
			longopts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
			longopts[1] = new LongOpt("application", LongOpt.REQUIRED_ARGUMENT, null, 'a');
			longopts[2] = new LongOpt("version", LongOpt.REQUIRED_ARGUMENT, null, 'v');

			Getopt g = new Getopt("queues", params, "", longopts);
			g.setOpterr(false);

			String application = null;
			String version = null;
			int c;
			while ((c = g.getopt()) != -1) {
				switch (c) {
				case 'a':
					application = g.getOptarg();
					break;
				case 'v':
					version = g.getOptarg();
					break;
				case 'h':
					return usage(uuid);
				default:
					return CLIResultCodes.INTERNAL_SERVER_ERROR + "::Could not execute the command \n " + usage(uuid);
				}
			}
			
			if (uuid ==null){
				return CLIResultCodes.USER_NOT_AUTHORIZED + "::--uuid parameter is required!";
			}
			
			if (application == null) {
				return CLIResultCodes.USER_ERROR + "::--application parameter is required!";
			}
			if (version == null) {
				return CLIResultCodes.USER_ERROR + "::--version parameter is required!";
			}

			ApplicationData data = applicationService.get(uuid, application);
			if (data == null) {
				return CLIResultCodes.USER_ERROR + "::Application [" + application + "] was not found";
			}
			VersionData vData = data.getVersion(version);
			if (vData != null) {
				List<String> queues = vData.getQueues();
				String result = "QUEUE\n-----------------------------------------\n";
				for (String queue : queues) {
					ResourceData resource = resourceMonitoringService.get(uuid, queue);
					if (resource != null) {
						result += queue + "(" + resource.getNumOfJobs() + " jobs / " + resource.getNumOfProc() + " cores)\n";
					}
				}
				return CLIResultCodes.OK + "::" + result;
			}
			return CLIResultCodes.USER_ERROR + "::Version [" + version + "] of the application [" + application + "] was not found";
		} catch (UserNotAuthorizedException e) {
			CLIUtils.handleException(e, logger);
			return CLIResultCodes.USER_NOT_AUTHORIZED + "::User not authorized!";
		} catch (Exception e) {
			CLIUtils.handleException(e, logger);
			return CLIResultCodes.INTERNAL_SERVER_ERROR + "::Could not execute the command \n " + usage(uuid);
		}
	}

	@Override
	public String usage(String uuid, String... params) throws RemoteException {
		return "Usage: queues --application <application> --version <version>";
	}
}
