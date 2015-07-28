package br.lncc.sinapad.portengin.action.editor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class Editor1stStepAction extends BaseAction {

	public static Logger logger = Logger.getLogger(Editor1stStepAction.class);

	@Action(value = "/editor", results = { @Result(name = "success", location = "/editor/index.jsp") })
	public String index() {
		return SUCCESS;
	}

	private static final String PORTENGIN_KEY = "PortEngin.key";

	private File privateKey;
	private String privateKeyFileName;

	public File getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(File privateKey) {
		this.privateKey = privateKey;
	}

	public String getPrivateKeyFileName() {
		return privateKeyFileName;
	}

	public void setPrivateKeyFileName(String privateKeyFileName) {
		this.privateKeyFileName = privateKeyFileName;
	}

	@Action(value = "/editor1stStep", results = { @Result(name = "success", location = "/editor/1st.jsp") })
	public String step() {
		String path = context.getRealPath("WEB-INF/cert");
		File dir = new File(path);
		try {
			Files.copy(privateKey.toPath(), new File(dir, PORTENGIN_KEY).toPath());
		} catch (IOException e) {
			PortEnginUtils.handleException(e, logger);
		}
		return SUCCESS;
	}

}
