package br.lncc.sinapad.portengin.action.result;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.core.data.ResultData;
import br.lncc.sinapad.core.exception.MonitoringServiceException;
import br.lncc.sinapad.core.exception.SubmissionServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class CancelJobAction extends BaseAction {

	private static Logger logger = Logger.getLogger(CancelJobAction.class);

	private String jobId;

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	@Action(value = "/cancelJob", results = { @Result(name = "success", location = "/result_view.jsp"), @Result(name = "error", location = "/result_view.jsp") })
	public String stopTask() {
		String uuid = getUuid();
		String project = getProject();
		if (jobId != null) {
			jobId = jobId.trim();
			try {

				jobSubmissionService.cancel(uuid, project, jobId);
				result = jobMonitoringService.get(uuid, project, jobId);
				cancelled = true;
				return SUCCESS;
			} catch (SubmissionServiceException e) {
				PortEnginUtils.handleException(e, logger);
			} catch (MonitoringServiceException e) {
				PortEnginUtils.handleException(e, logger);
			} catch (UserNotAuthorizedException e) {
				PortEnginUtils.handleException(e, logger);
			}
		}
		return ERROR;
	}

	private boolean cancelled = Boolean.FALSE;

	public boolean getCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	private ResultData result;

	public ResultData getResult() {
		return result;
	}

	public void setResult(ResultData result) {
		this.result = result;
	}

}
