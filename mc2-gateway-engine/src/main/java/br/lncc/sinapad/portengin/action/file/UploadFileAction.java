package br.lncc.sinapad.portengin.action.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.core.data.FileData;
import br.lncc.sinapad.core.exception.FileServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class UploadFileAction extends BaseAction {

	private static Logger logger = Logger.getLogger(UploadFileAction.class);

	public static final String FILE = "file";

	private String absolutePath;

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	private String typedName;

	public String getTypedName() {
		return typedName;
	}

	public void setTypedName(String typedName) {
		this.typedName = typedName;
	}

	private String typedContent;

	public String getTypedContent() {
		return typedContent;
	}

	public void setTypedContent(String typedContent) {
		this.typedContent = typedContent;
	}

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private File file;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	private String fileFileName;

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	private boolean fromFile = Boolean.FALSE;

	public boolean getFromFile() {
		return fromFile;
	}

	public void setFromFile(boolean fromFile) {
		this.fromFile = fromFile;
	}

	// if its coming from the arguments when selecting a file the target and
	// onlyDir must be set
	private String target;

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	private boolean onlyDir = Boolean.FALSE;

	public boolean getOnlyDir() {
		return onlyDir;
	}

	public void setOnlyDir(boolean onlyDir) {
		this.onlyDir = onlyDir;
	}

	@Action(value = "/uploadFile", results = { @Result(name = "file", location = "/file.jsp"), @Result(name = "success", location = "/file_manager.jsp") })
	public String uploadFile() {
		String uuid = getUuid();
		String project = getProject();
		boolean isGuest = (Boolean) session.get("isGuest");

		if (typedName != null && !typedName.isEmpty()) {
			try {
				file = File.createTempFile("portengin", "typed");
				PortEnginUtils.convertStringToFile(typedContent, file);
				fileFileName = typedName;
			} catch (IOException e) {
				PortEnginUtils.handleException(e, logger);
			}
		} else {
			if (url != null && !url.isEmpty()) {
				try {
					URL website = new URL(url);
					ReadableByteChannel rbc = Channels.newChannel(website.openStream());
					file = File.createTempFile("portengin", "url");
					FileOutputStream fos = new FileOutputStream(file);
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
					fileFileName = PortEnginUtils.retrieveFileNameFromAbsolutePath(url);
				} catch (Exception e) {
					// if any error occur while download the file from the url
					invalid = Boolean.TRUE;
				}
			}
		}

		String[] parentsArr = PortEnginUtils.convertAbsolutePathToArray(absolutePath, true, false);

		boolean override = Boolean.FALSE;
		if (isGuest) {
			fileFileName = "guest_file";
			override = Boolean.TRUE;
		}

		try {
			if (fileFileName.contains(" ")) {
				fileFileName = fileFileName.replaceAll(" ", "_");
				replaced = true;
			}
			if (!fileService.upload(uuid, project, parentsArr, fileFileName, file, override)) {
				exist = Boolean.TRUE;
			}
			replaced = replaced & !exist;
			// the first file in the absolute path is the project itself
			FileData last = new FileData(null, project, null, true);
			parents = new ArrayList<FileData>();
			if (parentsArr != null) {
				for (String p : parentsArr) {
					FileData file = new FileData(null, p, last, true);
					last = file;
					parents.add(file);
				}
			}
			FileData data = fileService.find(uuid, project, parentsArr, null);
			files = data.getChildren();
			uploaded = true;
		} catch (FileServiceException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (UserNotAuthorizedException e) {
			PortEnginUtils.handleException(e, logger);
		}
		if (fromFile) {
			return FILE;
		}
		return SUCCESS;
	}

	private boolean exist = Boolean.FALSE;

	public boolean getExist() {
		return exist;
	}

	public void setExist(boolean exist) {
		this.exist = exist;
	}

	private boolean replaced = Boolean.FALSE;

	public boolean getReplaced() {
		return replaced;
	}

	private boolean uploaded = Boolean.FALSE;

	public boolean getUploaded() {
		return uploaded;
	}

	public void setUploaded(boolean uploaded) {
		this.uploaded = uploaded;
	}

	private boolean invalid = Boolean.FALSE;

	public boolean getInvalid() {
		return invalid;
	}

	public void setInvalid(boolean invalid) {
		this.invalid = invalid;
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
