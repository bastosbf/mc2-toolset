package br.lncc.sinapad.portengin.listener.impl;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.listener.PortEnginListener;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class StartupListener implements PortEnginListener {

	private static Logger logger = Logger.getLogger(StartupListener.class);

	public void shutdown(ServletContext ctxt) {
		String path = ctxt.getRealPath("/tmp-files");
		File tmp = new File(path);
		try {
			FileUtils.deleteDirectory(tmp);
			tmp.mkdir();
		} catch (IOException e) {
			PortEnginUtils.handleException(e, logger);
		}
	}

	public void execute(ServletContext ctxt) {
		String path = ctxt.getRealPath("/NEW");
		File f = new File(path);
		if (!f.exists()) {
			String cert = ctxt.getRealPath("/WEB-INF/cert/PortEngin.key");
			BaseAction.initServices(cert);
		}
	}
}