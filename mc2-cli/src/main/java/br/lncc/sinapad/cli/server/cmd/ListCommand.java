package br.lncc.sinapad.cli.server.cmd;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import br.lncc.sinapad.cli.server.Command;
import br.lncc.sinapad.cli.utils.CLILogger;
import br.lncc.sinapad.cli.utils.CLIResultCodes;
import br.lncc.sinapad.cli.utils.CLIUtils;
import br.lncc.sinapad.core.data.FileData;
import br.lncc.sinapad.core.exception.FileServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.core.service.file.FileService;

public class ListCommand extends UnicastRemoteObject implements Command {

	private static CLILogger logger = CLILogger.getCLILogger(ListCommand.class);

	private FileService fileService;

	public ListCommand(FileService fileService) throws RemoteException {
		this.fileService = fileService;
	}

	@Override
	public String execute(String uuid, String... params) throws RemoteException {
		try {
			LongOpt[] longopts = new LongOpt[3];
			longopts[0] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
			longopts[1] = new LongOpt("project", LongOpt.REQUIRED_ARGUMENT, null, 'p');
			longopts[2] = new LongOpt("dir", LongOpt.REQUIRED_ARGUMENT, null, 'd');

			Getopt g = new Getopt("list", params, "", longopts);
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

			if (uuid ==null){
				return CLIResultCodes.USER_NOT_AUTHORIZED + "::--uuid parameter is required!";
			}
			
			if (project == null) {
				return CLIResultCodes.USER_ERROR + "::--project parameter is required!";
			}

			if (!fileService.exists(uuid, project, null, null)) {
				return CLIResultCodes.PROJECT_NOT_FOUND + "::Could not find project [" + project + "] for the current user";
			}

			if (fileService.exists(uuid, project, parents, directory)) {
				FileData dir = fileService.find(uuid, project, parents, directory);
				StringBuilder sb = new StringBuilder();
				getTreeString(sb, dir);
				return CLIResultCodes.OK + "::" + sb.toString();
			}
			return CLIResultCodes.FILE_DOES_NOT_EXIST + "::Directory does not exist";
		} catch (UserNotAuthorizedException e) {
			CLIUtils.handleException(e, logger);
			return CLIResultCodes.USER_NOT_AUTHORIZED + "::User not authorized!";
		} catch (Exception e) {
			CLIUtils.handleException(e, logger);
			return CLIResultCodes.INTERNAL_SERVER_ERROR + "::Could not execute the command \n " + usage(uuid);
		}
	}

	private void getTreeString(StringBuilder sb, FileData data) throws FileServiceException {
		if (!data.getName().startsWith(".")) {
			if (data.isDirectory()) {
				sb.append("\t");
				sb.append(data.getAbsolutePath());
				sb.append(":\n");
				sb.append("\ttotal ");
				sb.append(data.getSize());
				sb.append("\n");
				if (data.hasChildren()) {
					for (FileData d : data.getChildren()) {
						sb.append("\t");
						sb.append(d.getSize());
						sb.append(" ");
						sb.append(d.getModificationDate());
						sb.append(" ");
						sb.append(d.getName());
						sb.append("\n");
					}
					sb.append("\n");
					for (FileData d : data.getChildren()) {
						getTreeString(sb, d);
					}
				} else {
					sb.append("\n");
				}
			}
		}
	}

	@Override
	public String usage(String uuid, String... params) throws RemoteException {
		return "Usage: list --project <project name> [--dir <dir path>]";
	}
}
