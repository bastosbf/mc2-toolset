package br.lncc.sinapad.portengin.action.result;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.core.exception.MonitoringServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;
import br.lncc.sinapad.portengin.viewer.FileViewer;
import br.lncc.sinapad.portengin.viewer.FileViewerFactory;

public class ViewResultFileAction extends BaseAction {

	private static Logger logger = Logger.getLogger(ViewResultFileAction.class);

	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

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

	@Action(value = "/viewResultFile", results = { @Result(name = "success", location = "/file_view.jsp"), @Result(name = "error", location = "/file_view.jsp") })
	public String viewFileData() {
		String uuid = getUuid();
		String project = getProject();
		if (absolutePath != null) {
			try {
				String realPath = context.getRealPath("/tmp-files");
				String id = UUID.randomUUID().toString();
				File dir = new File(realPath, id);
				dir.mkdir();

				String[] parentsArr = PortEnginUtils.convertAbsolutePathToArray(absolutePath, true, true);
				fileName = PortEnginUtils.retrieveFileNameFromAbsolutePath(absolutePath);
				InputStream is = jobMonitoringService.download(uuid, project, jobId, parentsArr, fileName);
						
				File file = new File(dir, fileName);
				PortEnginUtils.convertInputStreamToFile(is, file);
				String mime = Files.probeContentType(file.toPath());
				FileViewer viewer = FileViewerFactory.createFileViewer(mime);
				relativePath = "tmp-files/" + id + "/" + fileName;
				view = viewer.createView(relativePath);
				file.deleteOnExit();
			} catch (MonitoringServiceException e) {
				PortEnginUtils.handleException(e, logger);
			} catch (IOException e) {
				PortEnginUtils.handleException(e, logger);
			} catch (UserNotAuthorizedException e) {
				PortEnginUtils.handleException(e, logger);
			}
			return SUCCESS;
		}
		close = true;
		return ERROR;
	}

	private boolean close = Boolean.FALSE;

	public boolean isClose() {
		return close;
	}

	public void setClose(boolean close) {
		this.close = close;
	}

	private String view;

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	private String relativePath;

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

}
