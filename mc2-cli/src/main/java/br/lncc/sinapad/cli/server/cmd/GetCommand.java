package br.lncc.sinapad.cli.server.cmd;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import br.lncc.sinapad.cli.server.Command;
import br.lncc.sinapad.cli.utils.CLILogger;
import br.lncc.sinapad.cli.utils.CLIResultCodes;
import br.lncc.sinapad.cli.utils.CLIUtils;
import br.lncc.sinapad.core.data.FileData;
import br.lncc.sinapad.core.exception.FileServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.core.service.file.FileService;

public class GetCommand extends UnicastRemoteObject implements Command {

	private static CLILogger logger = CLILogger.getCLILogger(GetCommand.class);

	private FileService fileService;

	public GetCommand(FileService fileService) throws RemoteException {
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

			Getopt g = new Getopt("get", params, "", longopts);
			g.setOpterr(false);

			String project = null;
			String path = null;
			String[] parents = null;
			String file = null;
			String dest = null;
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
				case 'd':
					dest = g.getOptarg();
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

			if (dest == null) {
				return CLIResultCodes.USER_ERROR + "::--dest parameter is required!";
			}

			if (!fileService.exists(uuid, project, null, null)) {
				return CLIResultCodes.PROJECT_NOT_FOUND + "::Could not find project [" + project + "] for the current user";
			}
			
			if(!fileService.exists(uuid, project, parents, file)){
				return CLIResultCodes.FILE_DOES_NOT_EXIST + "::Could not find file [" + file + "] for the current parents";

			}
			File dir = new File(dest);
			if (!dir.exists()) {
				return CLIResultCodes.FILE_DOES_NOT_EXIST + "::Destination directory does not exist! [" + dest + "]";
			}
			FileData data = fileService.find(uuid, project, parents, file);
			if (data.isDirectory()) {
				File f = new File(dir, file);
				f.mkdir();
				createDir(uuid, project, parents, file, f, file);

			} else {
				InputStream is = fileService.download(uuid, project, parents, file);
				CLIUtils.convertInputStreamToFile(is, new File(dir, file));
			}
			return CLIResultCodes.OK + "::Got file / directory " + file;
		} catch (IOException e) {
			e.printStackTrace(System.err);
		} catch(UserNotAuthorizedException e){
			return CLIResultCodes.USER_NOT_AUTHORIZED + "::User not authorized!";
		} catch (Exception e) {
			CLIUtils.handleException(e, logger);
			return CLIResultCodes.INTERNAL_SERVER_ERROR + "::Could not execute the command \n " + usage(uuid);
		}
		return CLIResultCodes.INTERNAL_SERVER_ERROR + "::Error getting file \n " + usage(uuid);
	}

	@Override
	public String usage(String uuid, String... params) throws RemoteException {
		return "Usage get --project <project name> --file <remote file path> --dest <local destination>";
	}

	private void createDir(String uuid, String project, String[] parents, String fileName, File parentDir, String parentName) throws FileServiceException, IOException, UserNotAuthorizedException {
		FileData data = fileService.find(uuid, project, parents, fileName);
		List<FileData> children = data.getChildren();
		for (FileData child : children) {
			String[] p = CLIUtils.convertAbsolutePathToArray(child.getAbsolutePath(), true, true);
			String n = child.getName();
			if (child.isDirectory()) {
				File f = new File(parentDir, n);
				f.mkdir();
				createDir(uuid, project, p, n, f, parentName + "/" + n);
			} else {
				InputStream is = fileService.download(uuid, project, p, n);
				File f = new File(parentDir, n);
				CLIUtils.convertInputStreamToFile(is, f);
			}
		}
	}

}
