package br.lncc.sinapad.portengin.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.lncc.sinapad.core.service.application.ApplicationService;
import br.lncc.sinapad.core.service.authentication.AuthenticationService;
import br.lncc.sinapad.core.service.authentication.AuthenticationService.Domain;
import br.lncc.sinapad.core.service.configuration.ConfigurationService;
import br.lncc.sinapad.core.service.file.FileService;
import br.lncc.sinapad.core.service.monitoring.JobMonitoringService;
import br.lncc.sinapad.core.service.monitoring.ResourceMonitoringService;
import br.lncc.sinapad.core.service.submission.JobSubmissionService;
import br.lncc.sinapad.portengin.config.AuthenticationConfig;
import br.lncc.sinapad.portengin.config.EmailConfig;
import br.lncc.sinapad.portengin.config.ModulesConfig;
import br.lncc.sinapad.portengin.config.PortalConfig;
import br.lncc.sinapad.portengin.exceptions.SessionExpiredException;
import br.lncc.sinapad.portengin.utils.PortEnginConstants;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

import com.opensymphony.xwork2.ActionSupport;

@ParentPackage(value = "default")
public class BaseAction extends ActionSupport implements SessionAware, ApplicationAware, ServletRequestAware, ServletResponseAware, ServletContextAware {

	private static Logger logger = Logger.getLogger(BaseAction.class);

	public static String FACADE = "csgrid";

	protected static ApplicationContext applicationContext;

	protected static ConfigurationService configurationService;
	protected static ApplicationService applicationService;
	protected static AuthenticationService authenticationService;
	protected static FileService fileService;
	protected static JobMonitoringService jobMonitoringService;
	protected static ResourceMonitoringService resourceMonitoringService;
	protected static JobSubmissionService jobSubmissionService;

	protected static EmailConfig emailConfig;
	protected static ModulesConfig modulesConfig;
	protected static PortalConfig portalConfig;
	protected static AuthenticationConfig authenticationConfig;

	protected static String portEnginUuid;

	public static void initServices(String portEnginCert) {
		try {
			InputStream is = BaseAction.class.getResourceAsStream("/portengin.properties");
			Properties properties = new Properties();
			properties.load(is);

			if (properties.containsKey("portengin.facade.impl")) {
				FACADE = properties.getProperty("portengin.facade.impl");
			}

			if (applicationContext == null) {
				applicationContext = new ClassPathXmlApplicationContext("spring-" + FACADE + ".xml");
			}

			configurationService = (ConfigurationService) applicationContext.getBean("configurationService");
			configurationService.configure(properties);

			applicationService = (ApplicationService) applicationContext.getBean("applicationService");
			authenticationService = (AuthenticationService) applicationContext.getBean("authenticationService");
			fileService = (FileService) applicationContext.getBean("fileService");
			jobMonitoringService = (JobMonitoringService) applicationContext.getBean("jobMonitoringService");
			resourceMonitoringService = (ResourceMonitoringService) applicationContext.getBean("resourceMonitoringService");
			jobSubmissionService = (JobSubmissionService) applicationContext.getBean("jobSubmissionService");

			emailConfig = (EmailConfig) applicationContext.getBean("emailConfig");
			modulesConfig = (ModulesConfig) applicationContext.getBean("modulesConfig");
			portalConfig = (PortalConfig) applicationContext.getBean("portalConfig");
			authenticationConfig = (AuthenticationConfig) applicationContext.getBean("authenticationConfig");
			// will use the portengin certificate to make a local login on the
			// provider
			String portEnginUuid = authenticationService.login(PortEnginConstants.PORTENGIN_USER, new FileInputStream(new File(portEnginCert)), Domain.RSA);
			BaseAction.portEnginUuid = portEnginUuid;
		} catch (Exception e) {
			PortEnginUtils.handleException(e, logger);
			throw new RuntimeException("Could not configure the service", e);
		}
	}

	public static String getPortEnginUuid() {
		return portEnginUuid;
	}

	protected String getUuid() throws SessionExpiredException {
		String uuid = (String) session.get(PortEnginConstants.USER);
		if (uuid == null) {
			throw new SessionExpiredException();
		}
		return uuid;
	}

	protected void setUuid(String uuid) {
		session.put(PortEnginConstants.USER, uuid);
	}

	protected String getProject() {
		return (String) application.get(PortEnginConstants.PROJECT);
	}

	protected Map<String, Object> application;

	public void setApplication(Map<String, Object> application) {
		this.application = application;
	}

	protected Map<String, Object> session;

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	protected HttpServletRequest request;
	protected HttpServletResponse response;

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	protected ServletContext context;

	public void setServletContext(ServletContext context) {
		this.context = context;
	}
}
