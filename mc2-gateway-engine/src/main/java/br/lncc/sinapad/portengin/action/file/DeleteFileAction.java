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
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class DeleteFileAction extends BaseAction {

	private static Logger logger = Logger.getLogger(DeleteFileAction.class);

	private String absolutePath;

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	@Action(value = "/deleteFile", results = { @Result(name = "success", location = "/file_manager.jsp"), @Result(name = "error", location = "/file_manager.jsp") })
	public String deleteFile() {
		String uuid = getUuid();
		String project = getProject();
		try {
			if (absolutePath != null) {
				String[] parentsArr = PortEnginUtils.convertAbsolutePathToArray(absolutePath, true, true);
				String fileName = PortEnginUtils.retrieveFileNameFromAbsolutePath(absolutePath);
				if (fileService.delete(uuid, project, parentsArr, fileName)) {

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

					if (fileService.exists(uuid, project, parentsArr, null)) {
						FileData data = fileService.find(uuid, project, parentsArr, null);
						files = data.getChildren();
					}
					return SUCCESS;
				}
			}
		} catch (FileServiceException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (UserNotAuthorizedException e) {
			PortEnginUtils.handleException(e, logger);
		}
		return ERROR;
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
