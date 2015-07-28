package br.lncc.sinapad.portengin.action.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.core.exception.FileServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class DownloadMultipleFilesAction extends BaseAction {

	private static Logger logger = Logger.getLogger(DownloadMultipleFilesAction.class);

	private String files;

	public String getFiles() {
		return files;
	}

	public void setFiles(String files) {
		this.files = files;
	}

	@Action(value = "/downloadMultipleFiles", results = { @Result(name = "success", location = "/download.jsp"), @Result(name = "error", location = "/file_manager.jsp") })
	public String downloadMultipleFiles() {
		String uuid = getUuid();
		String project = getProject();
		try {
			// creates the unique directory to store the multiple files
			File dir = File.createTempFile("files", "download");
			// creates a temp directory
			dir.delete();
			dir.mkdir();

			String zip = dir.getAbsolutePath() + ".zip";
			FileOutputStream fos = new FileOutputStream(zip);
			ZipOutputStream zos = new ZipOutputStream(fos);
			byte[] buffer = new byte[1024];
			String[] selectedFiles = files.split(";");
			for (String absolutePath : selectedFiles) {
				String[] parentsArr = PortEnginUtils.convertAbsolutePathToArray(absolutePath, true, true);
				String fileName = PortEnginUtils.retrieveFileNameFromAbsolutePath(absolutePath);

				InputStream is = fileService.download(uuid, project, parentsArr, fileName);
				File file = new File(dir, fileName);
				PortEnginUtils.convertInputStreamToFile(is, file);

				ZipEntry ze = new ZipEntry(file.getName());
				zos.putNextEntry(ze);
				FileInputStream in = new FileInputStream(file);

				int len;
				while ((len = in.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}
				in.close();
				zos.closeEntry();
			}
			zos.close();
			downloadPath = zip;
			label = dir.getName() + ".zip";
		} catch (FileServiceException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (IOException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (UserNotAuthorizedException e) {
			PortEnginUtils.handleException(e, logger);
		}
		return SUCCESS;
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
