package br.lncc.sinapad.portengin.action.result;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.core.exception.MonitoringServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class DownloadResultLogAction extends BaseAction {

	private static Logger logger = Logger.getLogger(DownloadResultLogAction.class);

	private String jobId;

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	@Action(value = "/downloadLog", results = { @Result(name = "success", location = "/download.jsp"), @Result(name = "error", location = "/view_result.jsp") })
	public String downloadLog() {
		String uuid = getUuid();
		String project = getProject();
		try {
			if (jobId != null) {
				File dir = File.createTempFile("files", "download"); //
				// creates a temp directory to avoid duplicated directory // name
				dir.delete();
				dir.mkdir();

				// removes empty spaces from the task id
				jobId = jobId.trim();
				InputStream is = jobMonitoringService.log(uuid, project, jobId);
				label = jobId + ".log";
				File file = new File(dir, label);
				PortEnginUtils.convertInputStreamToFile(is, file);
				downloadPath = file.getAbsolutePath();
				return SUCCESS;
			}
		} catch (MonitoringServiceException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (UserNotAuthorizedException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (IOException e) {
			PortEnginUtils.handleException(e, logger);
		}
		return ERROR;
	}

	private String label;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	private String downloadPath;

	public String getDownloadPath() {
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}
}
