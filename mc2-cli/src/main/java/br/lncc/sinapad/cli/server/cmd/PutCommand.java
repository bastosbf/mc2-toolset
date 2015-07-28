package br.lncc.sinapad.cli.server.cmd;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import br.lncc.sinapad.cli.server.Command;
import br.lncc.sinapad.cli.utils.CLILogger;
import br.lncc.sinapad.cli.utils.CLIResultCodes;
import br.lncc.sinapad.cli.utils.CLIUtils;
import br.lncc.sinapad.core.exception.FileServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.core.service.file.FileService;

public class PutCommand extends UnicastRemoteObject implements Command {

	private static CLILogger logger = CLILogger.getCLILogger(PutCommand.class);

	private FileService fileService;

	public PutCommand(FileService fileService) throws RemoteException {
		this.fileService = fileService;
	}

	@Override
	public String execute(String uuid, String... params) throws RemoteException {
		try {
			LongOpt[] longopts = new LongOpt[4];
			longopts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
			longopts[1] = new LongOpt("project", LongOpt.REQUIRED_ARGUMENT, null, 'p');
			longopts[2] = new LongOpt("file", LongOpt.REQUIRED_ARGUMENT, null, 'f');
			longopts[3] = new LongOpt("dest", LongOpt.REQUIRED_ARGUMENT, null, 'd');

			Getopt g = new Getopt("put", params, "", longopts);
			g.setOpterr(false);

			String project = null;
			String path = null;
			String[] parents = null;
			String dest = null;
			int c;
			while ((c = g.getopt()) != -1) {
				switch (c) {
				case 'p':
					project = g.getOptarg();
					break;
				case 'f':
					path = g.getOptarg();
					break;
				case 'd':
					dest = g.getOptarg();
					if (!".".equals(dest)) {
						parents = CLIUtils.convertAbsolutePathToArray(dest, false, false);
					}
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

			if (path == null) {
				return CLIResultCodes.USER_ERROR + "::--file parameter is required!";
			}

			if (dest == null) {
				return CLIResultCodes.USER_ERROR + "::--dest parameter is required!";
			}

			if (!fileService.exists(uuid, project, null, null)) {
				return CLIResultCodes.PROJECT_NOT_FOUND + "::Could not find project [" + project + "] for the current user";
			}

			File src = new File(path);

			if (!src.exists()) {
				return CLIResultCodes.FILE_DOES_NOT_EXIST + "::File [" + src.getName() + "] does not exist!";
			}

			String fileName = src.getName();
			if (src.isDirectory()) {
				fileService.create(uuid, project, parents, fileName, true);
				String childrenPath = fileName;
				if (parents != null) {
					childrenPath = dest + "/" + fileName;
				}
				createDir(uuid, project, childrenPath, src.listFiles());
				return CLIResultCodes.OK + "::Put the directory [" + src.getName() + "] to the destination [" + dest + "]";
			} else {
				fileService.upload(uuid, project, parents, fileName, src, true);
				return CLIResultCodes.OK + "::Put the file [" + src.getName() + "] to the destination [" + dest + "]";
			}
		} catch (UserNotAuthorizedException e) {
			CLIUtils.handleException(e, logger);
			return CLIResultCodes.USER_NOT_AUTHORIZED + "::User not authorized!";
		} catch (Exception e) {
			CLIUtils.handleException(e, logger);
			return CLIResultCodes.OK + "::Could not execute the command \n " + usage(uuid);
		}
	}

	@Override
	public String usage(String uuid, String... params) throws RemoteException {
		return "Usage: put --project <project name> --file <local file path> --dest <remote destination>";
	}

	private void createDir(String uuid, String project, String path, File[] children) throws FileServiceException, IOException, UserNotAuthorizedException {
		if (children != null && children.length > 0) {
			String[] parents = CLIUtils.convertAbsolutePathToArray(path, false, false);
			for (File child : children) {
				if (child.isDirectory()) {
					fileService.create(uuid, project, parents, child.getName(), true);
					createDir(uuid, project, path + "/" + child.getName(), child.listFiles());
				} else {
					fileService.upload(uuid, project, parents, child.getName(), child, true);
				}
			}
		}
	}
}
