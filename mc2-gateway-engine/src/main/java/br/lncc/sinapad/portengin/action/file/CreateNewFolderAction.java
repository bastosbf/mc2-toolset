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

public class CreateNewFolderAction extends BaseAction {

	private static Logger logger = Logger.getLogger(CreateNewFolderAction.class);

	public static final String FILE = "file";

	private String absolutePath;

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	private boolean fromFile = Boolean.FALSE;

	public boolean isFromFile() {
		return fromFile;
	}

	public void setFromFile(boolean fromFile) {
		this.fromFile = fromFile;
	}

	// if its coming from the arguments when selecting a file the target and
	// onlyDir must be set
	private String target;

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	private boolean onlyDir = Boolean.FALSE;

	public boolean getOnlyDir() {
		return onlyDir;
	}

	public void setOnlyDir(boolean onlyDir) {
		this.onlyDir = onlyDir;
	}

	@Action(value = "/createNewFolder", results = { @Result(name = "file", location = "/file.jsp"), @Result(name = "success", location = "/file_manager.jsp") })
	public String createNewFolder() {
		String uuid = getUuid();
		String project = getProject();
		try {
			String[] parentsArr = PortEnginUtils.convertAbsolutePathToArray(absolutePath, true, false);

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
			fileService.create(uuid, project, parentsArr, fileName, true);
			FileData data = fileService.find(uuid, project, parentsArr, fileName);
			absolutePath = data.getAbsolutePath();
			files = data.getChildren();
		} catch (FileServiceException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (UserNotAuthorizedException e) {
			PortEnginUtils.handleException(e, logger);
		}
		if (fromFile) {
			return FILE;
		}
		return SUCCESS;
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
