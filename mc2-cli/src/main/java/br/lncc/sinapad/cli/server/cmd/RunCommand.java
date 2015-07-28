package br.lncc.sinapad.cli.server.cmd;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.lncc.sinapad.cli.server.Command;
import br.lncc.sinapad.cli.utils.CLILogger;
import br.lncc.sinapad.cli.utils.CLIResultCodes;
import br.lncc.sinapad.cli.utils.CLIUtils;
import br.lncc.sinapad.core.application.representation.Group;
import br.lncc.sinapad.core.application.representation.Parameter;
import br.lncc.sinapad.core.application.representation.Representation;
import br.lncc.sinapad.core.application.representation.element.Checkbox;
import br.lncc.sinapad.core.application.representation.element.InputFile;
import br.lncc.sinapad.core.application.representation.element.ListBoxDouble;
import br.lncc.sinapad.core.application.representation.element.ListBoxInteger;
import br.lncc.sinapad.core.application.representation.element.OutputFile;
import br.lncc.sinapad.core.application.representation.element.Select;
import br.lncc.sinapad.core.application.representation.element.Text;
import br.lncc.sinapad.core.application.representation.element.TextDouble;
import br.lncc.sinapad.core.application.representation.element.TextInteger;
import br.lncc.sinapad.core.data.ApplicationData;
import br.lncc.sinapad.core.data.ApplicationData.VersionData;
import br.lncc.sinapad.core.data.ResourceData;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.core.service.application.ApplicationService;
import br.lncc.sinapad.core.service.application.converter.ApplicationConverterService;
import br.lncc.sinapad.core.service.file.FileService;
import br.lncc.sinapad.core.service.monitoring.ResourceMonitoringService;
import br.lncc.sinapad.core.service.submission.JobSubmissionService;

public class RunCommand extends UnicastRemoteObject implements Command {

	private static CLILogger logger = CLILogger.getCLILogger(RunCommand.class);

	private FileService fileService;
	private ApplicationService applicationService;
	private ApplicationConverterService applicationConverterService;
	private ResourceMonitoringService resourceMonitoringService;
	private JobSubmissionService jobSubmissionService;

	public RunCommand(FileService fileService, ApplicationService applicationService, ApplicationConverterService applicationConverterService, ResourceMonitoringService resourceMonitoringService, JobSubmissionService jobSubmissionService) throws RemoteException {
		this.fileService = fileService;
		this.applicationService = applicationService;
		this.applicationConverterService = applicationConverterService;
		this.resourceMonitoringService = resourceMonitoringService;
		this.jobSubmissionService = jobSubmissionService;
	}

	@Override
	public String execute(String uuid, String... params) throws RemoteException {
		try {
			LongOpt[] longopts = new LongOpt[6];
			longopts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
			longopts[1] = new LongOpt("project", LongOpt.REQUIRED_ARGUMENT, null, 'p');
			longopts[2] = new LongOpt("application", LongOpt.REQUIRED_ARGUMENT, null, 'a');
			longopts[3] = new LongOpt("version", LongOpt.REQUIRED_ARGUMENT, null, 'v');			
			longopts[4] = new LongOpt("queue", LongOpt.REQUIRED_ARGUMENT, null, 'q');
			longopts[5] = new LongOpt("wait", LongOpt.REQUIRED_ARGUMENT, null, 'w');

			Getopt g = new Getopt("run", params, "", longopts);
			g.setOpterr(false);

			String project = null;
			String application = null;
			String version = null;
			String queue = null;
			boolean wait = false;
			int c;
			while ((c = g.getopt()) != -1) {
				switch (c) {
				case 'p':
					project = g.getOptarg();
					break;
				case 'a':
					application = g.getOptarg();
					break;
				case 'v':
					version = g.getOptarg();
					break;				
				case 'q':
					queue = g.getOptarg();
					break;
				case 'w':
					wait = Boolean.TRUE;
					break;
				case 'h':
					if (application != null && version != null) {
						return usage(uuid, params);
					} else {
						return "Usage: run --project <project name> --application <application name> --version <application version> [--queue <queue name>] [--wait] <job parameters in the format -KEY=value>";
					}
				}
			}
			
			if (uuid ==null){
				return CLIResultCodes.USER_NOT_AUTHORIZED + "::--uuid parameter is required!";
			}

			if (project == null) {
				return CLIResultCodes.USER_ERROR + "::--project parameter is required!";
			}

			if (application == null || application.isEmpty()) {
				return CLIResultCodes.USER_ERROR + "::--application parameter is required!";
			}
			if (version == null || version.isEmpty()) {
				return CLIResultCodes.USER_ERROR + "::--version parameter is required!";
			}

			if (!fileService.exists(uuid, project, null, null)) {
				return CLIResultCodes.PROJECT_NOT_FOUND + "::Could not find project [" + project + "] for the current user";
			}

			Map<String, String> args = parseParams(params);
			if (queue != null) {
				ResourceData resource = null;
				ApplicationData data = applicationService.get(uuid, application);
				if (data == null) {
					return CLIResultCodes.USER_ERROR + "::Application [" + application + "] was not found";
				}
				VersionData vData = data.getVersion(version);
				if (vData != null) {
					List<String> queues = vData.getQueues();
					for (String q : queues) {
						if (q.equals(queue)) {
							resource = resourceMonitoringService.get(uuid, queue);
							break;
						}
					}
				}
				if (resource == null) {
					return CLIResultCodes.USER_ERROR + "::Application [" + application + "] with version [" + version + "] is not available for the queue [" + queue + "]";
				}
			}

			String jobId = jobSubmissionService.run(uuid, project, application, version, queue, args);
			return CLIResultCodes.OK + "::" + jobId;
		} catch (UserNotAuthorizedException e) {
			CLIUtils.handleException(e, logger);
			return CLIResultCodes.USER_NOT_AUTHORIZED + "::User not authorized!";
		} catch (Exception e) {
			CLIUtils.handleException(e, logger);
			return CLIResultCodes.INTERNAL_SERVER_ERROR + "::Could not execute the command \n " + usage(uuid, params);
		}
	}

	private Map<String, String> parseParams(String[] params) {
		Map<String, String> args = new HashMap<String, String>();
		for (String p : params) {
			if (!p.contains("=")) {
				continue;
			}
			String[] data = p.split("=");
			String key = data[0].replace("-", "");
			if (data.length == 2) {
				args.put(key, data[1]);
			}
		}
		return args;
	}

	@Override
	public String usage(String uuid, String... params) throws RemoteException {
		try {
			LongOpt[] longopts = new LongOpt[3];
			longopts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
			longopts[1] = new LongOpt("application", LongOpt.REQUIRED_ARGUMENT, null, 'a');
			longopts[2] = new LongOpt("version", LongOpt.REQUIRED_ARGUMENT, null, 'v');

			Getopt g = new Getopt("run", params, "", longopts);
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
				}
			}

			if (application == null || version == null) {
				return "Usage: run --project <project name> --application <application name> --version <application version> [--queue <queue name>] [--wait] <job parameters in the format -KEY=value>";
			}

			ApplicationData appData = applicationService.get(uuid, application);
			if (appData == null) {
				return CLIResultCodes.USER_ERROR + "::Application [" + application + "] was not found";
			}			
			VersionData vData = appData.getVersion(version);			
			if (vData == null) {
				return CLIResultCodes.USER_ERROR + "::Application [" + application + "] with version [" + version + "] was not found";
			}
			InputStream is = applicationService.config(uuid, application, version);
			Representation representation = applicationConverterService.convert(is, null);
			return help(representation, application, version);
		} catch (Exception e) {
			CLIUtils.handleException(e, logger);
		}
		return CLIResultCodes.INTERNAL_SERVER_ERROR + "::Error printing application usage.";
	}

	private String help(Representation representation, String appName, String appVersion) {
		String help = "\tjob parameters (for " + appName + " version " + appVersion + "):\n";
		List<Group> groups = representation.getGroups();
		for (Group g : groups) {
			List<Parameter> parameters = g.getParameters();
			for (Parameter p : parameters) {
				String name = p.getName();
				String label = p.getLabel();
				// String tip = p.getTip();

				Boolean optional = p.getOptional();
				help += "\t-" + name + getValueType(p);
				if (optional == Boolean.TRUE) {
					help += " (optional)";
				}
				help += "\n\t" + label;
				if (p instanceof Select) {
					help += " (";
					Select s = (Select) p;
					List<Select.Item> items = s.getItems();
					for (Select.Item i : items) {
						help += i.getId() + " - " + i.getLabel() + ", ";
					}
					help = help.substring(0, help.length() - 2);
					help += ")";
				}
				help += "\n";
			}
		}
		return CLIResultCodes.OK + "::" + help;
	}

	private String getValueType(Parameter p) {
		if (p instanceof Text) {
			return "=TEXT";
		} else if (p instanceof Select) {
			return "=SELECTION";
		} else if (p instanceof InputFile) {
			String category = "FILE";
			if ("diretorio".equals(((InputFile) p).getCategory())) {
				category = "DIRECTORY";
			}
			return "=INPUT " + category;
		} else if (p instanceof OutputFile) {
			String category = "FILE";
			if ("diretorio".equals(((OutputFile) p).getCategory())) {
				category = "DIRECTORY";
			}
			return "=OUTPUT " + category;
		} else if (p instanceof TextInteger) {
			return "=INTEGER";
		} else if (p instanceof TextDouble) {
			return "=DECIMAL";
		} else if (p instanceof ListBoxInteger) {
			return "=INTEGER LIST";
		} else if (p instanceof ListBoxDouble) {
			return "=DOUBLE LIST";
		} else if (p instanceof Checkbox) {
			return "=BOOLEAN";
		}
		return "";
	}
}
