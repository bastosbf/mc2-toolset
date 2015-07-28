package br.lncc.sinapad.rest;

import java.io.InputStream;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.lncc.sinapad.core.service.application.ApplicationService;
import br.lncc.sinapad.core.service.authentication.AuthenticationService;
import br.lncc.sinapad.core.service.configuration.ConfigurationService;
import br.lncc.sinapad.core.service.file.FileService;
import br.lncc.sinapad.core.service.monitoring.JobMonitoringService;
import br.lncc.sinapad.core.service.monitoring.ResourceMonitoringService;
import br.lncc.sinapad.core.service.submission.JobSubmissionService;

public abstract class RESTOperation {

	protected static ApplicationContext applicationContext;

	protected static ConfigurationService configurationService;
	protected static ApplicationService applicationService;
	protected static AuthenticationService authenticationService;
	protected static FileService fileService;
	protected static JobSubmissionService jobSubmissionService;
	protected static JobMonitoringService jobMonitoringService;
	protected static ResourceMonitoringService resourceMonitoringService;
	protected static String FACADE;
	private static Properties properties = new Properties();

	public static void initServices() {
		InputStream is = RESTOperation.class.getResourceAsStream("/rest.properties");
		try {
			properties.load(is);
			FACADE = properties.getProperty("rest.facade.impl");
			if (applicationContext == null) {
				if (FACADE == null) {
					applicationContext = new ClassPathXmlApplicationContext("spring.xml");
				} else {
					applicationContext = new ClassPathXmlApplicationContext("spring-" + FACADE + ".xml");

				}
			}

			configurationService = (ConfigurationService) applicationContext.getBean("configurationService");
			applicationService = (ApplicationService) applicationContext.getBean("applicationService");
			authenticationService = (AuthenticationService) applicationContext.getBean("authenticationService");
			fileService = (FileService) applicationContext.getBean("fileService");
			jobSubmissionService = (JobSubmissionService) applicationContext.getBean("jobSubmissionService");
			jobMonitoringService = (JobMonitoringService) applicationContext.getBean("jobMonitoringService");
			resourceMonitoringService = (ResourceMonitoringService) applicationContext
					.getBean("resourceMonitoringService");

			configurationService.configure(properties);
		} catch (Exception e) {
			throw new RuntimeException("Could not configure the service", e);
		}

	}

	static {
		initServices();
	}
}
