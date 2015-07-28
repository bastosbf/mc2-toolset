package br.lncc.sinapad.cli.server.cmd;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import br.lncc.sinapad.cli.server.Command;
import br.lncc.sinapad.cli.utils.CLILogger;
import br.lncc.sinapad.cli.utils.CLIResultCodes;
import br.lncc.sinapad.cli.utils.CLIUtils;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.core.service.file.FileService;

public class RemoveCommand extends UnicastRemoteObject implements Command {

	private static CLILogger logger = CLILogger.getCLILogger(RemoveCommand.class);

	private FileService fileService;

	public RemoveCommand(FileService fileService) throws RemoteException {
		this.fileService = fileService;
	}

	@Override
	public String execute(String uuid, String... params) throws RemoteException {
		try {
			LongOpt[] longopts = new LongOpt[3];
			longopts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
			longopts[1] = new LongOpt("project", LongOpt.REQUIRED_ARGUMENT, null, 'p');
			longopts[2] = new LongOpt("file", LongOpt.REQUIRED_ARGUMENT, null, 'f');

			Getopt g = new Getopt("remove", params, "", longopts);
			g.setOpterr(false);

			String project = null;
			String[] parents = null;
			String path = null;
			String file = null;
			int c;
			while ((c = g.getopt()) != -1) {
				switch (c) {
				case 'p':
					project = g.getOptarg();
					break;
				case 'f':
					path = g.getOptarg();
					parents = CLIUtils.convertAbsolutePathToArray(path, false, true);
					file = CLIUtils.retrieveFileNameFromAbsolutePath(path);
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

			if (file == null) {
				return CLIResultCodes.USER_ERROR + "::--file parameter is required!";
			}

			if (!fileService.exists(uuid, project, null, null)) {
				return CLIResultCodes.PROJECT_NOT_FOUND + "::Could not find project [" + project + "] for the current user";
			}
			
			if (!fileService.exists(uuid, project, parents, file)) {
				return CLIResultCodes.FILE_DOES_NOT_EXIST + "::Could not find file [" + file + "] for the current parents";
			}

			fileService.delete(uuid, project, parents, file);
			return CLIResultCodes.OK + "::The file [" + file +"] was deleted";
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
		return "Usage: remove --project <project name> --file <remote file path>";
	}

}
