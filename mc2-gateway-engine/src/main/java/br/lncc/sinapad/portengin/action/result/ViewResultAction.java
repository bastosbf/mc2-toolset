package br.lncc.sinapad.portengin.action.result;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.core.data.FileData;
import br.lncc.sinapad.core.data.ResultData;
import br.lncc.sinapad.core.exception.MonitoringServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class ViewResultAction extends BaseAction {

	private static Logger logger = Logger.getLogger(ViewResultAction.class);

	private String jobId;

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	private String absolutePath;

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	@Action(value = "/viewResult", results = { @Result(name = "success", location = "/result_view.jsp"), @Result(name = "error", location = "/result_view.jsp") })
	public String viewResult() {
		String uuid = getUuid();
		String project = getProject();
		try {
			if (jobId != null) {
				// removes empty spaces from the task id
				jobId = jobId.trim();
				result = jobMonitoringService.get(uuid, project, jobId);

				// will ignore the history directory
				String[] parentsArr = PortEnginUtils.convertAbsolutePathToArray(absolutePath, true, true);
				fileName = PortEnginUtils.retrieveFileNameFromAbsolutePath(absolutePath);
				String parent = PortEnginUtils.retrieveProjectNameFromAbsolutePath(absolutePath);

				// the first file in the absolute path is the project itself
				FileData last = null;
				if (parent != null) {
					last = new FileData(null, parent, null, true);
				}
				parents = new ArrayList<FileData>();
				if (parentsArr != null) {
					for (String p : parentsArr) {
						FileData file = new FileData(null, p, last, true);
						last = file;
						parents.add(file);
					}
				}

				FileData data = jobMonitoringService.history(uuid, project, jobId, parentsArr, fileName);
				files = data.getChildren();
				return SUCCESS;
			}
		} catch (MonitoringServiceException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (UserNotAuthorizedException e) {
			PortEnginUtils.handleException(e, logger);
		}
		return ERROR;
	}

	private ResultData result;

	public ResultData getResult() {
		return result;
	}

	public void setResult(ResultData result) {
		this.result = result;
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
