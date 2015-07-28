package br.lncc.sinapad.portengin.action.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class DownloadFile extends BaseAction {

	private static Logger logger = Logger.getLogger(DownloadFile.class);

	private String downloadPath;

	public String getDownloadPath() {
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	@Action(value = "/downloadFile", results = { @Result(name = "success", type = "stream", params = { "contentType", "application/octet-stream", "inputName", "fileInputStream", "contentDisposition", "attachment;filename=\"${fileName}\"", "bufferSize", "1024" }, location = "/download.jsp"), @Result(name = "error", location = "/download.jsp") })
	public String downloadFile() {
		try {
			if (downloadPath != null) {
				File file = new File(downloadPath);
				if (!file.exists()) {				
					//if the file does not exists in the local directory will try to find it on tmp-files dir
					String realPath = context.getRealPath("/");
					File parent = new File(realPath);
					file = new File(parent, downloadPath);
				}
				fileName = file.getName();
				fileInputStream = new FileInputStream(file);
				return SUCCESS;
			}
		} catch (IOException e) {
			PortEnginUtils.handleException(e, logger);
		}
		return ERROR;
	}

	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	private InputStream fileInputStream;

	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public void setFileInputStream(InputStream fileInputStream) {
		this.fileInputStream = fileInputStream;
	}
}
