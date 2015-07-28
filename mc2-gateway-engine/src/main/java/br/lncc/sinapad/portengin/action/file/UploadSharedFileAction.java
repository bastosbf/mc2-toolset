package br.lncc.sinapad.portengin.action.file;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.core.data.FileData;
import br.lncc.sinapad.core.exception.FileServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.utils.PortEnginConstants;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class UploadSharedFileAction extends BaseAction {

	private static Logger logger = Logger.getLogger(UploadSharedFileAction.class);

	private String absolutePath;

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	private boolean directory = Boolean.FALSE;

	public boolean isDirectory() {
		return directory;
	}

	public void setDirectory(boolean directory) {
		this.directory = directory;
	}

	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Action(value = "/uploadSharedFile", results = { @Result(name = "success", location = "/shared_file.jsp"), @Result(name = "error", location = "/shared_file.jsp") })
	public String uploadSharedFile() {
		String uuid = getUuid();
		String sharedUuid = getPortEnginUuid();
		String project = getProject();
		try {
			if (absolutePath != null) {
				String[] parentsArr = PortEnginUtils.convertAbsolutePathToArray(absolutePath, true, true);
				String originalFileName = PortEnginUtils.retrieveFileNameFromAbsolutePath(absolutePath);
				if (fileService.exists(uuid, project, parentsArr, originalFileName)) {
					if (!fileService.exists(sharedUuid, PortEnginConstants.PORTENGIN_SHARED_PROJECT, null, project)) {
						fileService.create(sharedUuid, PortEnginConstants.PORTENGIN_SHARED_PROJECT, null, project, true);
					}
					if (fileService.exists(sharedUuid, PortEnginConstants.PORTENGIN_SHARED_PROJECT, new String[] { project }, fileName)) {
						exists = Boolean.TRUE;
						return ERROR;
					}
					if (directory) {
						// will create the directory
						fileService.create(sharedUuid, PortEnginConstants.PORTENGIN_SHARED_PROJECT, new String[] { project }, fileName, true);
						// will recursively get all the children, including
						// others
						// directories
						shareDir(uuid, project, parentsArr, fileName, sharedUuid, new String[] { project, fileName });
					} else {
						InputStream is = fileService.download(uuid, project, parentsArr, originalFileName);
						fileService.upload(sharedUuid, PortEnginConstants.PORTENGIN_SHARED_PROJECT, new String[] { project }, fileName, is, true);
					}
				}
			}
			fileName = fileName;
			shared = Boolean.TRUE;
			return SUCCESS;
		} catch (FileServiceException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (UserNotAuthorizedException e) {
			PortEnginUtils.handleException(e, logger);
		}
		return ERROR;
	}

	private void shareDir(String uuid, String project, String[] parents, String fileName, String sharedUuid, String[] publicParents) throws FileServiceException, UserNotAuthorizedException {
		FileData data = fileService.find(uuid, project, parents, fileName);
		List<FileData> children = data.getChildren();
		for (FileData child : children) {
			String[] p = PortEnginUtils.convertAbsolutePathToArray(child.getAbsolutePath(), true, true);
			String n = child.getName();
			if (child.isDirectory()) {
				fileService.create(sharedUuid, PortEnginConstants.PORTENGIN_SHARED_PROJECT, publicParents, n, true);
				// will create the new parents hierarchy
				List<String> l = new ArrayList<String>();
				for (String s : publicParents) {
					l.add(s);
				}
				l.add(n);
				shareDir(uuid, project, p, n, sharedUuid, l.toArray(new String[] {}));
			} else {
				InputStream is = fileService.download(uuid, project, p, n);
				fileService.upload(sharedUuid, PortEnginConstants.PORTENGIN_SHARED_PROJECT, publicParents, n, is, true);
			}
		}
	}

	private boolean exists = Boolean.FALSE;

	public boolean isExists() {
		return exists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}

	private boolean shared = Boolean.FALSE;

	public boolean isShared() {
		return shared;
	}

	public void setShared(boolean shared) {
		this.shared = shared;
	}

}
