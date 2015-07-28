package br.lncc.sinapad.portengin.action.result;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.core.data.ResultData;
import br.lncc.sinapad.core.exception.MonitoringServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class DeleteResultAction extends BaseAction {

	private static Logger logger = Logger.getLogger(DeleteResultAction.class);

	// comparator used to order the results by the newest to the oldest
	private Comparator<ResultData> comparator = new Comparator<ResultData>() {
		public int compare(ResultData d1, ResultData d2) {
			return d2.getStartDate().compareTo(d1.getStartDate());
		}
	};

	private String jobId;

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	@Action(value = "/deleteResult", results = { @Result(name = "success", location = "/result.jsp"), @Result(name = "error", location = "/result.jsp") })
	public String deleteTaskHistory() {
		String uuid = getUuid();
		String project = getProject();
		boolean isGuest = (Boolean) session.get("isGuest");

		if (jobId != null) {
			jobId = jobId.trim();
			try {
				if (!isGuest) {
					jobMonitoringService.delete(uuid, project, jobId);
					results = jobMonitoringService.list(uuid, project, null, null);
					if (results != null) {
						Collections.sort(results, comparator);
					}
				}
				jobId = null;
				return SUCCESS;
			} catch (MonitoringServiceException e) {
				PortEnginUtils.handleException(e, logger);
			} catch (UserNotAuthorizedException e) {
				PortEnginUtils.handleException(e, logger);
			}
		}
		return ERROR;
	}

	private List<ResultData> results;

	public List<ResultData> getResults() {
		return results;
	}

	public void setResults(List<ResultData> results) {
		this.results = results;
	}

}
