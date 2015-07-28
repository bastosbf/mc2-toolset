package br.lncc.sinapad.cli.server.cmd;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import br.lncc.sinapad.cli.server.Command;
import br.lncc.sinapad.cli.utils.CLILogger;
import br.lncc.sinapad.cli.utils.CLIResultCodes;
import br.lncc.sinapad.cli.utils.CLIUtils;
import br.lncc.sinapad.core.exception.FileServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.core.service.file.FileService;

public class CreateDirCommand extends UnicastRemoteObject implements Command {

	private static CLILogger logger = CLILogger.getCLILogger(CreateDirCommand.class);

	private FileService fileService;

	public CreateDirCommand(FileService fileService) throws RemoteException {
		this.fileService = fileService;
	}

	@Override
	public String execute(String uuid, String... params) throws RemoteException {
		try {
			LongOpt[] longopts = new LongOpt[3];
			longopts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
			longopts[1] = new LongOpt("project", LongOpt.REQUIRED_ARGUMENT, null, 'p');
			longopts[2] = new LongOpt("dir", LongOpt.REQUIRED_ARGUMENT, null, 'd');

			Getopt g = new Getopt("create_dir", params, "", longopts);
			g.setOpterr(false);

			String project = null;
			String[] parents = null;
			String directory = null;
			int c;
			while ((c = g.getopt()) != -1) {
				switch (c) {
				case 'p':
					project = g.getOptarg();
					break;
				case 'd':
					String path = g.getOptarg();
					parents = CLIUtils.convertAbsolutePathToArray(path, false, true);
					directory = CLIUtils.retrieveFileNameFromAbsolutePath(path);
					break;
				case 'h':
					return usage(uuid);
				default:
					return CLIResultCodes.INTERNAL_SERVER_ERROR + "::Could not execute the command \n " + usage(uuid);
				}
			}
			
			if(uuid==null){
				return CLIResultCodes.USER_NOT_AUTHORIZED + "::--uuid parameter is required!";
			}
			
			if (project == null) {
				return CLIResultCodes.USER_ERROR + "::--project parameter is required!";
			}

			if (directory == null) {
				return CLIResultCodes.USER_ERROR + "::--dir parameter is required!";
			}
			if (!fileService.exists(uuid, project, null, null)) {
				return CLIResultCodes.PROJECT_NOT_FOUND + "::Could not find project [" + project + "] for the current user";
			}

			Boolean ret = fileService.create(uuid, project, parents, directory, true);
			if (ret) {
				return CLIResultCodes.OK + "::Directory [" + directory + "] succesfully created";
			}
			return CLIResultCodes.INTERNAL_SERVER_ERROR + "::Error while creating directory [" + directory + "]";
		} catch (FileServiceException e) {
			CLIUtils.handleException(e, logger);
			return CLIResultCodes.INTERNAL_SERVER_ERROR + "::Error while accessing the file service.";
		} catch (UserNotAuthorizedException e) {
			CLIUtils.handleException(e, logger);
			return CLIResultCodes.USER_NOT_AUTHORIZED + "::User not authorized!" + usage(uuid);
		} catch (Exception e) {
			CLIUtils.handleException(e, logger);
			return CLIResultCodes.INTERNAL_SERVER_ERROR + "Could not execute the command \n " + usage(uuid);
		}
	}

	@Override
	public String usage(String uuid, String... params) throws RemoteException {
		return "Usage: create_dir --project <project name> --dir <directory absolute path>";
	}
}
