package br.lncc.sinapad.portengin.action.index;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.config.AuthenticationConfig.LDAPAuthentication;
import br.lncc.sinapad.portengin.config.AuthenticationConfig.ShibbolethAuthentication;
import br.lncc.sinapad.portengin.config.AuthenticationConfig.VOMSAuthentication;
import br.lncc.sinapad.portengin.config.ModulesConfig.BulkJobsModule;
import br.lncc.sinapad.portengin.config.ModulesConfig.GuestUserModule;
import br.lncc.sinapad.portengin.config.ModulesConfig.PublicFilesModule;
import br.lncc.sinapad.portengin.config.ModulesConfig.SharedFilesModule;
import br.lncc.sinapad.portengin.config.PortalConfig.Application;
import br.lncc.sinapad.portengin.utils.PortEnginConstants;

public class IndexAction extends BaseAction {

	private static Logger logger = Logger.getLogger(IndexAction.class);

	private Boolean configured = Boolean.FALSE;

	private void configure() {
		this.application.put("projectAcronym", portalConfig.getAcronymName());
		this.application.put("portalFullName", portalConfig.getFullName());

		GuestUserModule guestUserConfig = modulesConfig.getGuestUserModule();

		this.application.put("guestEnabled", guestUserConfig.isEnabled());

		String[] guestVersions = guestUserConfig.getAllowedVersions().split(",");
		List<String> guestVersionList = Arrays.asList(guestVersions);

		this.application.put("guestVersionList", guestVersionList);

		this.application.put(PortEnginConstants.PROJECT, portalConfig.getProjectName());
		this.application.put("allowMultipleVersions", portalConfig.isAllowMultipleVersions());
		this.application.put("allowResourceChoice", portalConfig.isAllowResourceChoice());

		SharedFilesModule sharedFilesConfig = modulesConfig.getSharedFilesModule();

		this.application.put("sharedFilesEnabled", sharedFilesConfig.isEnabled());

		PublicFilesModule publicFilesConfig = modulesConfig.getPublicFilesModule();

		this.application.put("publicFilesEnabled", publicFilesConfig.isEnabled());

		BulkJobsModule bulkJobsModule = modulesConfig.getBulkJobsModule();
		this.application.put("isBulkJobs", bulkJobsModule.isEnabled());

		List<Application> applicationsConfig = portalConfig.getApplications().getApplications();
		Boolean multipleApp = applicationsConfig.size() > 1;

		this.application.put("multipleApp", multipleApp);
		if (multipleApp) {
			this.application.put("apps", applicationsConfig);
		}

		LDAPAuthentication ldapAuthentication = authenticationConfig.getLdapAuthentication();
		this.application.put("ldapEnabled", ldapAuthentication.isEnabled());
		VOMSAuthentication vomsAuthentication = authenticationConfig.getVomsAuthentication();
		this.application.put("vomsEnabled", vomsAuthentication.isEnabled());
		ShibbolethAuthentication shibbolethAuthentication = authenticationConfig.getShibbolethAuthentication();
		this.application.put("shibbolethEnabled", shibbolethAuthentication.isEnabled());
		this.application.put("shibbolethURL", shibbolethAuthentication.getUrl());

		configured = Boolean.TRUE;
	}

	@Action(value = "/index", results = { @Result(name = "success", location = "/index.jsp") })
	public String index() {
		String newPortal = context.getRealPath("/NEW");
		File f = new File(newPortal);
		if (f.exists()) {
			request.setAttribute("PortEditor", true);
		} else {
			if (!configured) {
				configure();
			}
		}

		return SUCCESS;
	}

	@Action(value = "/about", results = { @Result(name = "success", location = "/about.jsp") })
	public String about() {
		return SUCCESS;
	}
}
