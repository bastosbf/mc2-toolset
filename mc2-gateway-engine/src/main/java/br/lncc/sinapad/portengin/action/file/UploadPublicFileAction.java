package br.lncc.sinapad.portengin.action.file;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.core.data.FileData;
import br.lncc.sinapad.core.exception.FileServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.utils.EmailUtils;
import br.lncc.sinapad.portengin.utils.PortEnginConstants;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class UploadPublicFileAction extends BaseAction {

	private static Logger logger = Logger.getLogger(UploadPublicFileAction.class);

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

	@Action(value = "/uploadPublicFile", results = { @Result(name = "success", location = "/public_file.jsp"), @Result(name = "error", location = "/file_manager.jsp") })
	public String uploadPublicFile() {
		String uuid = getUuid();
		String publicUuid = getPortEnginUuid();
		String project = getProject();

		try {
			if (absolutePath != null) {
				String[] parentsArr = PortEnginUtils.convertAbsolutePathToArray(absolutePath, true, true);
				String fileName = PortEnginUtils.retrieveFileNameFromAbsolutePath(absolutePath);
				if (fileService.exists(uuid, project, parentsArr, fileName)) {
					UUID dir = UUID.randomUUID();
					String id = dir.toString();

					if (!fileService.exists(publicUuid, PortEnginConstants.PORTENGIN_PUBLIC_PROJECT, null, project)) {
						fileService.create(publicUuid, PortEnginConstants.PORTENGIN_PUBLIC_PROJECT, null, project, true);
					}
					fileService.create(publicUuid, PortEnginConstants.PORTENGIN_PUBLIC_PROJECT, new String[] { project }, id, true);

					if (directory) {
						// will create the directory
						fileService.create(publicUuid, PortEnginConstants.PORTENGIN_PUBLIC_PROJECT, new String[] { project, id }, fileName, true);
						// will recursively get all the children, including
						// others
						// directories
						publishDir(uuid, project, parentsArr, fileName, publicUuid, new String[] { project, id, fileName });
					} else {
						InputStream is = fileService.download(uuid, project, parentsArr, fileName);
						fileService.upload(publicUuid, PortEnginConstants.PORTENGIN_PUBLIC_PROJECT, new String[] { project, id }, fileName, is, true);
					}

					url = String.valueOf(request.getRequestURL());
					// remover the actions from the url
					url = url.substring(0, url.lastIndexOf("/"));
					url += "/downloadPublicFile?id=" + id;

					String subject = getText("class.UploadPublicFileAction.email.subject");
					MessageFormat format = new MessageFormat(subject);
					subject = format.format(new String[] { fileName, project });

					String body = getText("class.UploadPublicFileAction.email.body", new String[] { url });
					EmailUtils.send(null, String.valueOf(session.get("email")), subject, body);

				}
			}
			return SUCCESS;
		} catch (FileServiceException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (UserNotAuthorizedException e) {
			PortEnginUtils.handleException(e, logger);
		}
		return ERROR;
	}

	private void publishDir(String uuid, String project, String[] parents, String fileName, String publicUuid, String[] publicParents) throws FileServiceException, UserNotAuthorizedException {
		FileData data = fileService.find(uuid, project, parents, fileName);
		List<FileData> children = data.getChildren();
		for (FileData child : children) {
			String[] p = PortEnginUtils.convertAbsolutePathToArray(child.getAbsolutePath(), true, true);
			String n = child.getName();
			if (child.isDirectory()) {
				fileService.create(publicUuid, PortEnginConstants.PORTENGIN_PUBLIC_PROJECT, publicParents, n, true);
				// will create the new parents hierarchy
				List<String> l = new ArrayList<String>();
				for (String s : publicParents) {
					l.add(s);
				}
				l.add(n);
				publishDir(uuid, project, p, n, publicUuid, l.toArray(new String[] {}));
			} else {
				InputStream is = fileService.download(uuid, project, p, n);
				fileService.upload(publicUuid, PortEnginConstants.PORTENGIN_PUBLIC_PROJECT, publicParents, n, is, true);
			}
		}
	}

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
