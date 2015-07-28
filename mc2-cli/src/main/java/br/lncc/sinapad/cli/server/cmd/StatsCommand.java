package br.lncc.sinapad.cli.server.cmd;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.lncc.sinapad.cli.server.Command;
import br.lncc.sinapad.cli.utils.CLILogger;
import br.lncc.sinapad.cli.utils.CLIResultCodes;
import br.lncc.sinapad.cli.utils.CLIUtils;
import br.lncc.sinapad.core.data.ResultData;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.core.service.file.FileService;
import br.lncc.sinapad.core.service.monitoring.JobMonitoringService;

public class StatsCommand extends UnicastRemoteObject implements Command {

	private static CLILogger logger = CLILogger.getCLILogger(StatsCommand.class);

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");

	private FileService fileService;
	private JobMonitoringService jobMonitoringService;

	public StatsCommand(FileService fileService, JobMonitoringService jobMonitoringService) throws RemoteException {
		this.fileService = fileService;
		this.jobMonitoringService = jobMonitoringService;
	}

	@Override
	public String execute(String uuid, String... params) throws RemoteException {
		try {
			LongOpt[] longopts = new LongOpt[5];
			longopts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
			longopts[1] = new LongOpt("project", LongOpt.REQUIRED_ARGUMENT, null, 'p');
			longopts[2] = new LongOpt("job", LongOpt.REQUIRED_ARGUMENT, null, 'j');
			longopts[3] = new LongOpt("begin", LongOpt.REQUIRED_ARGUMENT, null, 'b');
			longopts[4] = new LongOpt("end", LongOpt.REQUIRED_ARGUMENT, null, 'e');

			Getopt g = new Getopt("get", params, "", longopts);
			g.setOpterr(false);

			String project = null;
			String jobId = null;
			String begin = null;
			String end = null;
			int c;
			while ((c = g.getopt()) != -1) {
				switch (c) {
				case 'p':
					project = g.getOptarg();
					break;
				case 'j':
					jobId = g.getOptarg();
					break;
				case 'b':
					begin = g.getOptarg();
					break;
				case 'e':
					end = g.getOptarg();
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

			if (project == null) {
				return CLIResultCodes.USER_ERROR + "::--project parameter is required!";
			}

			if (!fileService.exists(uuid, project, null, null)) {
				return CLIResultCodes.PROJECT_NOT_FOUND + "::Could not find project [" + project + "] for the current user";
			}

			if (jobId != null) {
				ResultData data = jobMonitoringService.get(uuid, project, jobId);

				return CLIResultCodes.OK + "::\n==========================================\n" + "job_ID: " + jobId + "\nstate: " + data.getStatus() + "\nstart_date: " + data.getStartDate() + "\nend_date: " + data.getEndDate() + "\nresource: " + data.getResource();
			}

			Date from = null;
			if (begin != null) {
				try {
					from = sdf.parse(begin);
				} catch (ParseException ex) {
					return CLIResultCodes.USER_ERROR + "::The begin date is not on the format: [yyyyMMddhhmmss]";
				}
			}
			Date to = null;
			if (end != null) {
				try {
					to = sdf.parse(end);
				} catch (ParseException ex) {
					return CLIResultCodes.USER_ERROR + "::The end date is not on the format: [yyyyMMddhhmmss]";
				}
			}
			List<ResultData> results = jobMonitoringService.list(uuid, project, from, to);

			String result = "JOB-ID                                   RESOURCE                                    START DATE                                    END DATE                          STATUS\n" + "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n";
			for (ResultData r : results) {
				result += parseElement(r.getJobId()) + parseElement(r.getResource()) + parseElement(String.valueOf(r.getStartDate())) + parseElement(String.valueOf(r.getEndDate())) + r.getStatus() + "\n";
			}
			return CLIResultCodes.OK + "::" + result;
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
		return "Usage: stats --project <project name> [--begin <yyyyMMddhhmmss>] [--end <yyyyMMddhhmmss>]  [--job <job id>]";
	}

	private String parseElement(String element) {
		if (element != null) {
			if (element.length() < 41) {
				int size = (41 - element.length());
				for (int i = 0; i < size; i++) {
					element += " ";
				}
			}
			return element;
		}
		return "";
	}

}
