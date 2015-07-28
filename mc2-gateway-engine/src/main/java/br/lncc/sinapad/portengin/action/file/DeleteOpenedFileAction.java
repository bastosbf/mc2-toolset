package br.lncc.sinapad.portengin.action.file;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.portengin.action.BaseAction;

public class DeleteOpenedFileAction extends BaseAction {

	private static Logger logger = Logger.getLogger(DeleteOpenedFileAction.class);

	private String relativePath;

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	@Action(value = "/deleteOpenedFile", results = { @Result(name = "success", location = "/file_view.jsp"), @Result(name = "error", location = "/file_view.jsp") })
	public String deleteOpenedFile() {
		if (relativePath != null) {
			String realPath = context.getRealPath("/");
			File parent = new File(realPath);
			File file = new File(parent, relativePath);
			file.delete();
			return SUCCESS;
		}
		return ERROR;
	}
}
