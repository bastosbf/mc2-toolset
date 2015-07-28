package br.lncc.sinapad.portengin.action.editor;

import java.io.File;
import java.io.PrintWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.config.AuthenticationConfig;
import br.lncc.sinapad.portengin.config.AuthenticationConfig.LDAPAuthentication;
import br.lncc.sinapad.portengin.config.AuthenticationConfig.ShibbolethAuthentication;
import br.lncc.sinapad.portengin.config.AuthenticationConfig.VOMSAuthentication;
import br.lncc.sinapad.portengin.config.EmailConfig;
import br.lncc.sinapad.portengin.config.EmailConfig.JobFinishedNotificationMessage;
import br.lncc.sinapad.portengin.config.ModulesConfig;
import br.lncc.sinapad.portengin.config.ModulesConfig.BulkJobsModule;
import br.lncc.sinapad.portengin.config.ModulesConfig.GuestUserModule;
import br.lncc.sinapad.portengin.config.ModulesConfig.PublicFilesModule;
import br.lncc.sinapad.portengin.config.ModulesConfig.SharedFilesModule;
import br.lncc.sinapad.portengin.config.PortalConfig;
import br.lncc.sinapad.portengin.config.PortalConfig.Application;
import br.lncc.sinapad.portengin.config.PortalConfig.Applications;
import br.lncc.sinapad.portengin.listener.PortEnginControllerListener;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class Editor3rdStepAction extends BaseAction {

	public static Logger logger = Logger.getLogger(Editor3rdStepAction.class);

	private String acronymName;
	private String fullName;
	private String about;
	private Boolean allowMultipleVersions;
	private Boolean allowResourceChoice;
	private String applicationName;
	private String applicationDefaultVersion;

	public String getAcronymName() {
		return acronymName;
	}

	public void setAcronymName(String acronymName) {
		this.acronymName = acronymName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public Boolean getAllowMultipleVersions() {
		return allowMultipleVersions;
	}

	public void setAllowMultipleVersions(Boolean allowMultipleVersions) {
		this.allowMultipleVersions = allowMultipleVersions;
	}

	public Boolean getAllowResourceChoice() {
		return allowResourceChoice;
	}

	public void setAllowResourceChoice(Boolean allowResourceChoice) {
		this.allowResourceChoice = allowResourceChoice;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getApplicationDefaultVersion() {
		return applicationDefaultVersion;
	}

	public void setApplicationDefaultVersion(String applicationDefaultVersion) {
		this.applicationDefaultVersion = applicationDefaultVersion;
	}

	private Boolean sharedFilesModule;
	private Boolean publicFilesModule;
	private Boolean guestUserModule;
	private Boolean ldapUserModule;
	private Boolean vomsUserModule;
	private Boolean shibboletUserModule;

	public Boolean getSharedFilesModule() {
		return sharedFilesModule;
	}

	public void setSharedFilesModule(Boolean sharedFilesModule) {
		this.sharedFilesModule = sharedFilesModule;
	}

	public Boolean getPublicFilesModule() {
		return publicFilesModule;
	}

	public void setPublicFilesModule(Boolean publicFilesModule) {
		this.publicFilesModule = publicFilesModule;
	}

	public Boolean getGuestUserModule() {
		return guestUserModule;
	}

	public void setGuestUserModule(Boolean guestUserModule) {
		this.guestUserModule = guestUserModule;
	}

	public Boolean getLdapUserModule() {
		return ldapUserModule;
	}

	public void setLdapUserModule(Boolean ldapUserModule) {
		this.ldapUserModule = ldapUserModule;
	}

	public Boolean getVomsUserModule() {
		return vomsUserModule;
	}

	public void setVomsUserModule(Boolean vomsUserModule) {
		this.vomsUserModule = vomsUserModule;
	}

	private String subject;
	private String body;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Action(value = "/editor3rdStep", results = { @Result(name = "success", location = "/editor/3rd.jsp") })
	public String step() {

		try {
			{
				JAXBContext jaxbContext = JAXBContext.newInstance(AuthenticationConfig.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

				AuthenticationConfig authenticationConfig = new AuthenticationConfig();
				LDAPAuthentication ldapAuthentication = new LDAPAuthentication();
				ldapAuthentication.setEnabled(ldapUserModule);
				authenticationConfig.setLdapAuthentication(ldapAuthentication);

				VOMSAuthentication vomsAuthentication = new VOMSAuthentication();
				vomsAuthentication.setEnabled(vomsUserModule);
				authenticationConfig.setVomsAuthentication(vomsAuthentication);
				
				ShibbolethAuthentication shibbolethAuthentication = new ShibbolethAuthentication();
				shibbolethAuthentication.setEnabled(false);
				shibbolethAuthentication.setUrl("");
				shibbolethAuthentication.setStoragePath("");
				authenticationConfig.setShibbolethAuthentication(shibbolethAuthentication);

				File output = new File(BaseAction.class.getResource("/conf/authentication.xml").getFile());
				jaxbMarshaller.marshal(authenticationConfig, output);
			}

			{
				PrintWriter pw = new PrintWriter(getClass().getResource("/../../static/about.html").getPath());
				pw.print(about);
				pw.flush();
				pw.close();
			}
			{
				JAXBContext jaxbContext = JAXBContext.newInstance(ModulesConfig.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

				ModulesConfig modulesConfig = new ModulesConfig();
				GuestUserModule guest = new GuestUserModule();
				guest.setEnabled(guestUserModule);
				modulesConfig.setGuestUserModule(guest);
				PublicFilesModule _public = new PublicFilesModule();
				_public.setEnabled(publicFilesModule);
				modulesConfig.setPublicFilesModule(_public);
				SharedFilesModule shared = new SharedFilesModule();
				shared.setEnabled(sharedFilesModule);
				modulesConfig.setSharedFilesModule(shared);
				BulkJobsModule bulk = new BulkJobsModule();
				bulk.setEnabled(false);
				modulesConfig.setBulkJobsModule(bulk);

				File output = new File(BaseAction.class.getResource("/conf/modules.xml").getFile());
				jaxbMarshaller.marshal(modulesConfig, output);
			}
			{
				JAXBContext jaxbContext = JAXBContext.newInstance(PortalConfig.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

				PortalConfig portalConfig = new PortalConfig();
				portalConfig.setAcronymName(acronymName);
				portalConfig.setFullName(fullName);
				portalConfig.setAllowMultipleVersions(allowMultipleVersions);
				portalConfig.setAllowResourceChoice(allowResourceChoice);
				portalConfig.setAutoGenerateInterface(true);
				portalConfig.setResultCheckInterval(40000);
				portalConfig.setProjectName(acronymName);

				Application application = new Application();
				application.setName(applicationName);
				application.setDefaultVersion(applicationDefaultVersion);
				application.setInfo("");

				Applications applications = new Applications();
				applications.addApplication(application);

				portalConfig.setApplications(applications);

				File output = new File(BaseAction.class.getResource("/conf/portal.xml").getFile());
				jaxbMarshaller.marshal(portalConfig, output);
			}
			{
				JAXBContext jaxbContext = JAXBContext.newInstance(EmailConfig.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

				EmailConfig emailConfig = new EmailConfig();
				emailConfig.setSmtpHost("smtp.sinapad.lncc.br");
				emailConfig.setEmailFrom("sinapad-sup@lncc.br");
				emailConfig.setEmailTo("sinapad-sup@lncc.br");

				JobFinishedNotificationMessage msn = new JobFinishedNotificationMessage();
				msn.setSubject(subject);
				msn.setBody(body);

				emailConfig.setJobFinishedNotificationMessage(msn);

				File output = new File(BaseAction.class.getResource("/conf/email.xml").getFile());
				jaxbMarshaller.marshal(emailConfig, output);
			}			
			String path = context.getRealPath("/NEW");
			File f = new File(path);
			f.delete();
			
			PortEnginControllerListener.shutdownListeners(context);
			PortEnginControllerListener.executeListeners(context);
		} catch (Exception e) {
			PortEnginUtils.handleException(e, logger);
		}
		return SUCCESS;
	}

}
