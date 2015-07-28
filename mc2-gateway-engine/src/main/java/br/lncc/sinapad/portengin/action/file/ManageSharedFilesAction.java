package br.lncc.sinapad.portengin.action.file;

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

public class ManageSharedFilesAction extends BaseAction {

	private static Logger logger = Logger.getLogger(ManageSharedFilesAction.class);

	private String absolutePath;

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	@Action(value = "/manageSharedFiles", results = { @Result(name = "success", location = "/shared_file_manager.jsp"), @Result(name = "error", location = "/shared_file_manager.jsp") })
	public String manageSharedFiles() {
		String sharedUuid = getPortEnginUuid();
		String project = getProject();
		boolean isGuest = (Boolean) session.get("isGuest");
		try {
			if (!isGuest) {
				if (absolutePath == null) {
					absolutePath = project;
				}
				String[] parentsArr = PortEnginUtils.convertAbsolutePathToArray(absolutePath, true, true);
				fileName = PortEnginUtils.retrieveFileNameFromAbsolutePath(absolutePath);
				// the first file in the absolute path is the project itself
				FileData last = new FileData(null, project, null, true);
				parents = new ArrayList<FileData>();
				if (parentsArr != null) {
					for (String p : parentsArr) {
						FileData file = new FileData(null, p, last, true);
						last = file;
						parents.add(file);
					}
				}

				if (fileService.exists(sharedUuid, PortEnginConstants.PORTENGIN_SHARED_PROJECT, parentsArr, fileName)) {
					FileData data = fileService.find(sharedUuid, PortEnginConstants.PORTENGIN_SHARED_PROJECT, parentsArr, fileName);
					files = data.getChildren();
				}
				return SUCCESS;
			}
		} catch (FileServiceException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (UserNotAuthorizedException e) {
			PortEnginUtils.handleException(e, logger);
		}
		return ERROR;
	}

	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	private List<FileData> files;

	public List<FileData> getFiles() {
		return files;
	}

	public void setFiles(List<FileData> files) {
		this.files = files;
	}

	private List<FileData> parents;

	public List<FileData> getParents() {
		return parents;
	}

	public void setParents(List<FileData> parents) {
		this.parents = parents;
	}

}
