package br.lncc.sinapad.portengin.listener.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.lncc.sinapad.core.application.representation.Representation;
import br.lncc.sinapad.core.data.ApplicationData;
import br.lncc.sinapad.core.data.ApplicationData.VersionData;
import br.lncc.sinapad.core.exception.ApplicationConverterException;
import br.lncc.sinapad.core.exception.ApplicationServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.core.service.application.ApplicationService;
import br.lncc.sinapad.core.service.application.converter.ApplicationConverterService;
import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.config.ModulesConfig;
import br.lncc.sinapad.portengin.config.ModulesConfig.BulkJobsModule;
import br.lncc.sinapad.portengin.config.PortalConfig;
import br.lncc.sinapad.portengin.config.PortalConfig.Application;
import br.lncc.sinapad.portengin.config.PortalConfig.Applications;
import br.lncc.sinapad.portengin.gui.jsp.converter.JSPConverter;
import br.lncc.sinapad.portengin.listener.PortEnginListener;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class ParserListener implements PortEnginListener {

	private static Logger logger = Logger.getLogger(ParserListener.class);

	public void shutdown(ServletContext ctxt) {
		String path = ctxt.getRealPath("/NEW");
		File f = new File(path);
		if (!f.exists()) {
			// remove generetade folder
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-" + BaseAction.FACADE + ".xml");
			PortalConfig portalConfig = (PortalConfig) applicationContext.getBean("portalConfig");

			boolean auto = portalConfig.isAutoGenerateInterface();
			if (auto) {
				String dest = ctxt.getRealPath("/generated");
				File dir = new File(dest);
				File[] files = dir.listFiles();
				if (files != null) {
					for (File file : files) {
						file.delete();
					}
				}
			}
		}
	}

	public void execute(ServletContext ctxt) {
		String path = ctxt.getRealPath("/NEW");
		File f = new File(path);
		if (!f.exists()) {
			try {
				ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-" + BaseAction.FACADE + ".xml");
				ModulesConfig modulesConfig = (ModulesConfig) applicationContext.getBean("modulesConfig");
				PortalConfig portalConfig = (PortalConfig) applicationContext.getBean("portalConfig");
				Applications applications = portalConfig.getApplications();

				ApplicationService applicationService = (ApplicationService) applicationContext.getBean("applicationService");

				ApplicationConverterService applicationConverter = (ApplicationConverterService) applicationContext.getBean("applicationConverter");

				VersionComparator versionComparator = new VersionComparator();
				for (Application application : applications.getApplications()) {
					String dest = ctxt.getRealPath("/generated");
					File applicationDir = new File(new File(dest), application.getName());
					if (!applicationDir.exists()) {
						applicationDir.mkdir();
					}

					String uuid = BaseAction.getPortEnginUuid();
					ApplicationData appData = applicationService.get(uuid, application.getName());

					List<VersionData> versionsData = appData.getVersions();

					Collections.sort(versionsData, versionComparator);

					ctxt.setAttribute(application.getName() + "_versions", versionsData);

					for (VersionData v : versionsData) {
						File versionDir = new File(applicationDir, v.getVersion());
						if (!versionDir.exists()) {
							versionDir.mkdir();
						}
						File applicationFile = new File(versionDir, "application.jsp");
						boolean auto = portalConfig.isAutoGenerateInterface();
						// will just regenerate the algorithm if the automatic
						// generated
						// flag is enabled
						if (!applicationFile.exists() || auto) {
							PrintWriter pw = new PrintWriter(applicationFile);
							InputStream cofig = applicationService.config(uuid, appData.getName(), v.getVersion());
							Representation rep = applicationConverter.convert(cofig, null);
							BulkJobsModule bulkJobs = modulesConfig.getBulkJobsModule();
							pw.print(JSPConverter.convert(rep, bulkJobs, null));
							pw.flush();
							pw.close();
						}
					}
				}
			} catch (IOException e) {
				PortEnginUtils.handleException(e, logger);
			} catch (ApplicationServiceException e) {
				PortEnginUtils.handleException(e, logger);
			} catch (ApplicationConverterException e) {
				PortEnginUtils.handleException(e, logger);
			} catch (UserNotAuthorizedException e) {
				PortEnginUtils.handleException(e, logger);
			}
		}
	}

	private static class VersionComparator implements Comparator<VersionData> {

		public int compare(VersionData v1, VersionData v2) {
			return v1.getVersion().compareTo(v2.getVersion());
		}

	}

}