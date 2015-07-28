package br.lncc.sinapad.cli.server;

import java.io.FileInputStream;
import java.io.InputStream;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Properties;

import br.lncc.sinapad.cli.server.cmd.CreateDirCommand;
import br.lncc.sinapad.cli.server.cmd.GetCommand;
import br.lncc.sinapad.cli.server.cmd.ListCommand;
import br.lncc.sinapad.cli.server.cmd.LoginCommand;
import br.lncc.sinapad.cli.server.cmd.LogoutCommand;
import br.lncc.sinapad.cli.server.cmd.PutCommand;
import br.lncc.sinapad.cli.server.cmd.QueuesCommand;
import br.lncc.sinapad.cli.server.cmd.RemoveCommand;
import br.lncc.sinapad.cli.server.cmd.RunCommand;
import br.lncc.sinapad.cli.server.cmd.StatsCommand;
import br.lncc.sinapad.cli.server.cmd.StopCommand;
import br.lncc.sinapad.cli.utils.CLIResultCodes;
import br.lncc.sinapad.cli.utils.CLILogger;
import br.lncc.sinapad.cli.utils.CLIUtils;
import br.lncc.sinapad.core.service.application.ApplicationService;
import br.lncc.sinapad.core.service.application.converter.ApplicationConverterService;
import br.lncc.sinapad.core.service.authentication.AuthenticationService;
import br.lncc.sinapad.core.service.configuration.ConfigurationService;
import br.lncc.sinapad.core.service.file.FileService;
import br.lncc.sinapad.core.service.monitoring.JobMonitoringService;
import br.lncc.sinapad.core.service.monitoring.ResourceMonitoringService;
import br.lncc.sinapad.core.service.submission.JobSubmissionService;

public class Server {

	private static CLILogger logger = CLILogger.getCLILogger(Server.class);

	protected static ConfigurationService configurationService;
	protected static ApplicationService applicationService;
	protected static ApplicationConverterService applicationConverterService;
	protected static AuthenticationService authenticationService;
	protected static FileService fileService;
	protected static JobMonitoringService jobMonitoringService;
	protected static ResourceMonitoringService resourceMonitoringService;
	protected static JobSubmissionService jobSubmissionService;

	public static void connect(String path) {
		try {
			InputStream is = null;
			if (path != null) {
				is = new FileInputStream(path);
			} else {
				is = Server.class.getResourceAsStream("/cli.properties");
			}
			Properties properties = new Properties();
			properties.load(is);

			{
				String service = properties.getProperty("application.service");
				applicationService = (ApplicationService) Class.forName(service).newInstance();
			}
			{
				String service = properties.getProperty("application.converter.service");
				applicationConverterService = (ApplicationConverterService) Class.forName(service).newInstance();
			}
			{
				String service = properties.getProperty("authentication.service");
				authenticationService = (AuthenticationService) Class.forName(service).newInstance();
			}
			{
				String service = properties.getProperty("file.service");
				fileService = (FileService) Class.forName(service).newInstance();
			}
			{
				String service = properties.getProperty("job.monitoring.service");
				jobMonitoringService = (JobMonitoringService) Class.forName(service).newInstance();
			}
			{
				String service = properties.getProperty("resource.monitoring.service");
				resourceMonitoringService = (ResourceMonitoringService) Class.forName(service).newInstance();
			}
			{
				String service = properties.getProperty("job.submission.service");
				jobSubmissionService = (JobSubmissionService) Class.forName(service).newInstance();
			}
			{
				String service = properties.getProperty("configuration.service");
				configurationService = (ConfigurationService) Class.forName(service).newInstance();
				configurationService.configure(properties);
			}

		} catch (Exception e) {
			e.printStackTrace();
			CLIUtils.handleException(e, logger);
			System.err.println(CLIResultCodes.INTERNAL_SERVER_ERROR + "::A error occuried while trying to start the CLI proccess.");
			System.exit(-1);
		}
	}

	public static void main(String[] args) {
		String path = null;
		if (args.length > 0) {
			path = args[0];
		}
		connect(path);
		// System.setSecurityManager(new RMISecurityManager());

		try {
			// LocateRegistry.createRegistry(1099);
			LoginCommand login = new LoginCommand(authenticationService);
			LogoutCommand logout = new LogoutCommand(authenticationService);
			CreateDirCommand create = new CreateDirCommand(fileService);
			ListCommand list = new ListCommand(fileService);
			GetCommand getFile = new GetCommand(fileService);
			PutCommand putFile = new PutCommand(fileService);
			StatsCommand stats = new StatsCommand(fileService, jobMonitoringService);
			RunCommand run = new RunCommand(fileService, applicationService, applicationConverterService, resourceMonitoringService, jobSubmissionService);
			RemoveCommand remove = new RemoveCommand(fileService);
			QueuesCommand queues = new QueuesCommand(applicationService, resourceMonitoringService);
			StopCommand stop = new StopCommand(fileService, jobSubmissionService);

			Naming.bind("LOGIN", login);
			Naming.bind("LOGOUT", logout);
			Naming.bind("CREATE_DIR", create);
			Naming.bind("LIST", list);
			Naming.bind("GET", getFile);
			Naming.bind("PUT", putFile);
			Naming.bind("STATS", stats);
			Naming.bind("RUN", run);
			Naming.bind("REMOVE", remove);
			Naming.bind("QUEUES", queues);
			Naming.bind("STOP", stop);
		} catch (Exception e) {
			CLIUtils.handleException(e, logger);
			System.err.println(CLIResultCodes.INTERNAL_SERVER_ERROR + "::A error occuried while trying to start the CLI proccess.");
			System.exit(-1);
		}
	}
}
