package br.lncc.sinapad.portengin.action.file;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.core.exception.FileServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class CopyFileAction extends BaseAction {

	private static Logger logger = Logger.getLogger(CopyFileAction.class);

	private String from;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	private String to;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	@Action(value = "/copyFile", results = { @Result(name = "success", location = "/project_file.jsp"), @Result(name = "error", location = "/project_file.jsp") })
	public String copyFile() {
		String uuid = getUuid();
		String project = getProject();

		String[] parentsFrom = PortEnginUtils.convertAbsolutePathToArray(from, true, true);
		String file = PortEnginUtils.retrieveFileNameFromAbsolutePath(from);
		String[] parentsTo = PortEnginUtils.convertAbsolutePathToArray(to, true, false);

		try {
			if (fileService.copy(uuid, project, parentsTo, parentsFrom, file)) {
				copied = true;
				return SUCCESS;
			}
		} catch (FileServiceException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (UserNotAuthorizedException e) {
			PortEnginUtils.handleException(e, logger);
		}
		error = true;
		return ERROR;
	}

	private boolean copied;

	public boolean isCopied() {
		return copied;
	}

	public void setCopied(boolean copied) {
		this.copied = copied;
	}

	private boolean error;

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

}
