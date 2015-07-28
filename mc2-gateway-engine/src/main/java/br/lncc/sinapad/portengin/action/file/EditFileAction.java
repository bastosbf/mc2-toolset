package br.lncc.sinapad.portengin.action.file;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.core.exception.FileServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class EditFileAction extends BaseAction {

	private static Logger logger = Logger.getLogger(EditFileAction.class);

	private String absolutePath;

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	private String fileData;

	public String getFileData() {
		return fileData;
	}

	public void setFileData(String fileData) {
		this.fileData = fileData;
	}

	@Action(value = "/editFile", results = { @Result(name = "success", location = "/file_editor.jsp"), @Result(name = "error", location = "/file_editor.jsp") })
	public String editFile() {
		String uuid = getUuid();
		String project = getProject();
		try {
			if (absolutePath != null) {
				String[] parentsArr = PortEnginUtils.convertAbsolutePathToArray(absolutePath, true, true);
				String fileName = PortEnginUtils.retrieveFileNameFromAbsolutePath(absolutePath);

				File tmp = File.createTempFile("sinapad", "edit");
				PrintWriter pw = new PrintWriter(tmp);
				pw.print(fileData);
				pw.flush();
				pw.close();

				fileService.upload(uuid, project, parentsArr, fileName, tmp, true);
				edited = true;
				return SUCCESS;

			}
		} catch (FileServiceException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (IOException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (UserNotAuthorizedException e) {
			PortEnginUtils.handleException(e, logger);
		}
		return ERROR;
	}

	private boolean edited = Boolean.FALSE;

	public boolean isEdited() {
		return edited;
	}

	public void setEdited(boolean edited) {
		this.edited = edited;
	}
}
