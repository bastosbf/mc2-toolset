package br.lncc.sinapad.portengin.action.editor;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.core.data.ApplicationData;
import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class Editor2ndStepAction extends BaseAction {

	public static Logger logger = Logger.getLogger(Editor2ndStepAction.class);

	private File properties;

	public File getProperties() {
		return properties;
	}

	public void setProperties(File properties) {
		this.properties = properties;
	}

	private String propertiesFileName;

	public String getPropertiesFileName() {
		return propertiesFileName;
	}

	public void setPropertiesFileName(String propertiesFileName) {
		this.propertiesFileName = propertiesFileName;
	}

	@Action(value = "/editor2ndStep", results = { @Result(name = "success", location = "/editor/2nd.jsp"), @Result(name = "error", location = "/editor/1st.jsp") })
	public String step() {
		try {
			String path = context.getRealPath("/WEB-INF/classes/portengin.properties");
			File dest = new File(path);
			if (dest.exists()) {
				dest.delete();
			}
			FileUtils.copyFile(properties, dest);
			String cert = context.getRealPath("/WEB-INF/cert/PortEngin.key");
			BaseAction.initServices(cert);

			applications = applicationService.list(getPortEnginUuid());
			return SUCCESS;
		} catch (Exception e) {
			PortEnginUtils.handleException(e, logger);
		}
		error = Boolean.TRUE;
		return ERROR;
	}

	private List<ApplicationData> applications;

	public List<ApplicationData> getApplications() {
		return applications;
	}

	public void setApplications(List<ApplicationData> applications) {
		this.applications = applications;
	}

	private boolean error = Boolean.FALSE;

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

}
