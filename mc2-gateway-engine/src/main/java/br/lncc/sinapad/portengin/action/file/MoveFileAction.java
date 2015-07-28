package br.lncc.sinapad.portengin.action.file;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.core.exception.FileServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class MoveFileAction extends BaseAction {

	private static Logger logger = Logger.getLogger(MoveFileAction.class);

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

	@Action(value = "/moveFile", results = { @Result(name = "success", location = "/project_file.jsp"), @Result(name = "error", location = "/project_file.jsp") })
	public String moveFile() {
		String uuid = getUuid();
		String project = getProject();

		String[] parentsFrom = PortEnginUtils.convertAbsolutePathToArray(from, true, true);
		String file = PortEnginUtils.retrieveFileNameFromAbsolutePath(from);
		String[] parentsTo = PortEnginUtils.convertAbsolutePathToArray(to, true, false);

		try {
			if (fileService.move(uuid, project, parentsTo, parentsFrom, file)) {
				moved = true;
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

	private boolean moved;

	public boolean isMoved() {
		return moved;
	}

	public void setMoved(boolean moved) {
		this.moved = moved;
	}

	private boolean error;

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

}
