package br.lncc.sinapad.portengin.action.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.core.data.FileData;
import br.lncc.sinapad.core.exception.FileServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class DownloadProjectFileAction extends BaseAction {

	private static Logger logger = Logger.getLogger(DownloadProjectFileAction.class);

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

	@Action(value = "/downloadProjectFile", results = { @Result(name = "success", location = "/download.jsp"), @Result(name = "error", location = "/file_manager.jsp") })
	public String downloadFile() {
		String uuid = getUuid();
		String project = getProject();
		try {
			if (absolutePath != null) {
				File dir = File.createTempFile("files", "download"); //
				// creates a temp directory to avoid duplicated directory // name
				dir.delete();
				dir.mkdir();

				String[] parentsArr = PortEnginUtils.convertAbsolutePathToArray(absolutePath, true, true);
				label = PortEnginUtils.retrieveFileNameFromAbsolutePath(absolutePath);
				if (directory) {
					// will create the directory to be zipped
					File file = new File(dir, label);
					file.mkdir();

					String zip = file.getAbsolutePath() + ".zip";
					FileOutputStream fos = new FileOutputStream(zip);
					ZipOutputStream zos = new ZipOutputStream(fos);
					byte[] buffer = new byte[1024];

					ZipEntry ze = new ZipEntry(label + "/");
					zos.putNextEntry(ze);
					zos.closeEntry();

					// will recursively get all the children, including others
					// directories
					createZip(uuid, project, parentsArr, label, file, label, buffer, zos);
					zos.close();
					downloadPath = zip;
					// fileInputStream = new FileInputStream(zip);

				} else {
					InputStream is = fileService.download(uuid, project, parentsArr, label);
					File file = new File(dir, label);
					PortEnginUtils.convertInputStreamToFile(is, file);
					downloadPath = file.getAbsolutePath();
				}
				return SUCCESS;
			}
		} catch (IOException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (FileServiceException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (UserNotAuthorizedException e) {
			PortEnginUtils.handleException(e, logger);
		}
		return ERROR;
	}

	// this method creates a zip file following the hierarchy of the files
	// returned by the fileService
	private void createZip(String uuid, String project, String[] parents, String fileName, File parentDir, String parentName, byte[] buffer, ZipOutputStream zos) throws FileServiceException, IOException, UserNotAuthorizedException {
		FileData data = fileService.find(uuid, project, parents, fileName);
		List<FileData> children = data.getChildren();
		for (FileData child : children) {
			String[] p = PortEnginUtils.convertAbsolutePathToArray(child.getAbsolutePath(), true, true);
			String n = child.getName();
			if (child.isDirectory()) {
				File f = new File(parentDir, n);
				f.mkdir();
				ZipEntry ze = new ZipEntry(parentName + "/" + n + "/");
				zos.putNextEntry(ze);
				zos.closeEntry();
				createZip(uuid, project, p, n, f, parentName + "/" + n, buffer, zos);
			} else {
				InputStream is = fileService.download(uuid, project, p, n);
				File f = new File(parentDir, n);
				PortEnginUtils.convertInputStreamToFile(is, f);
				ZipEntry ze = new ZipEntry(parentName + "/" + n);
				zos.putNextEntry(ze);
				FileInputStream in = new FileInputStream(f);

				int len;
				while ((len = in.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}
				in.close();
				zos.closeEntry();
			}
		}
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
