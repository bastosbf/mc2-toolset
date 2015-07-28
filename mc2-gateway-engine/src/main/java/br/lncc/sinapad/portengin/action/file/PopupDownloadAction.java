package br.lncc.sinapad.portengin.action.file;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class PopupDownloadAction extends BaseAction {

	private String label;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	private String action;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	private String absolutePath;

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	private boolean directory = Boolean.FALSE;

	public boolean isDirectory() {
		return directory;
	}

	public void setDirectory(boolean directory) {
		this.directory = directory;
	}

	private List<String> selectedFiles;

	public List<String> getSelectedFiles() {
		return selectedFiles;
	}

	public void setSelectedFiles(List<String> selectedFiles) {
		this.selectedFiles = selectedFiles;
	}

	private String jobId;

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	@Action(value = "/popupDownload", results = { @Result(name = "success", location = "/download.jsp") })
	public String popupDownload() {
		if (label == null) {
			label = PortEnginUtils.retrieveFileNameFromAbsolutePath(absolutePath);
		}
		// will prepare the multiple files if it exists
		if (selectedFiles != null && !selectedFiles.isEmpty()) {
			for (String file : selectedFiles) {
				files += file + ";";
			}
			// removes the last ';' character
			files.substring(0, files.length() - 1);
		}
		return SUCCESS;
	}

	private String files;

	public String getFiles() {
		return files;
	}

	public void setFiles(String files) {
		this.files = files;
	}
}
