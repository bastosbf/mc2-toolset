package br.lncc.sinapad.portengin.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;

import br.lncc.sinapad.portengin.utils.PortEnginUtils;

/**
 * @author bastosbf
 *
 */
@XmlRootElement(name = "portal")
public class PortalConfig implements Serializable {

	private static Logger logger = Logger.getLogger(PortalConfig.class);

	public PortalConfig(String xml) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(PortalConfig.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			InputStream is = null;
			File file = new File(xml);
			if (file.exists()) {
				is = new FileInputStream(file);
			} else {
				is = getClass().getResourceAsStream(xml);
			}

			PortalConfig config = (PortalConfig) jaxbUnmarshaller.unmarshal(is);

			this.setAllowMultipleVersions(config.isAllowMultipleVersions());
			this.setAllowResourceChoice(config.isAllowResourceChoice());
			this.setAutoGenerateInterface(config.isAutoGenerateInterface());
			this.setResultCheckInterval(config.getResultCheckInterval());
			this.setAcronymName(config.getAcronymName());
			this.setFullName(config.getFullName());
			this.setProjectName(config.getProjectName());
			this.setApplications(config.getApplications());
		} catch (JAXBException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (FileNotFoundException e) {
			PortEnginUtils.handleException(e, logger);
		}
	}

	private boolean allowMultipleVersions;
	private boolean allowResourceChoice;
	private boolean autoGenerateInterface;
	private int resultCheckInterval;
	private String acronymName;
	private String fullName;
	private String projectName;
	private Applications applications;

	public PortalConfig() {

	}

	public boolean isAllowMultipleVersions() {
		return allowMultipleVersions;
	}

	@XmlAttribute(name = "allow-multiple-versions")
	public void setAllowMultipleVersions(boolean allowMultipleVersions) {
		this.allowMultipleVersions = allowMultipleVersions;
	}

	public boolean isAllowResourceChoice() {
		return allowResourceChoice;
	}

	@XmlAttribute(name = "allow-resource-choice")
	public void setAllowResourceChoice(boolean allowResourceChoice) {
		this.allowResourceChoice = allowResourceChoice;
	}

	public boolean isAutoGenerateInterface() {
		return autoGenerateInterface;
	}

	@XmlAttribute(name = "auto-generate-interface")
	public void setAutoGenerateInterface(boolean autoGenerateInterface) {
		this.autoGenerateInterface = autoGenerateInterface;
	}

	public int getResultCheckInterval() {
		return resultCheckInterval;
	}

	@XmlAttribute(name = "result-check-interval")
	public void setResultCheckInterval(int resultCheckInterval) {
		this.resultCheckInterval = resultCheckInterval;
	}

	public String getAcronymName() {
		return acronymName;
	}

	@XmlElement(name = "acronym-name")
	public void setAcronymName(String acronymName) {
		this.acronymName = acronymName;
	}

	public String getFullName() {
		return fullName;
	}

	@XmlElement(name = "full-name")
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getProjectName() {
		return projectName;
	}

	@XmlElement(name = "project-name")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Applications getApplications() {
		return applications;
	}

	@XmlElement(name = "applications")
	public void setApplications(Applications applications) {
		this.applications = applications;
	}

	public static class Applications {

		@XmlElement(name = "application", type = Application.class)
		private List<Application> applications;

		public Applications() {
			applications = new ArrayList<Application>();
		}

		@XmlTransient
		public void setApplications(List<Application> applications) {
			this.applications = applications;
		}
		
		public void addApplication(Application application) {
			this.applications.add(application);
		}

		public List<Application> getApplications() {
			return applications;
		}

	}

	public static class Application {
		private String name;
		private String defaultVersion;
		private String info;

		public String getName() {
			return name;
		}

		@XmlElement(name = "name")
		public void setName(String name) {
			this.name = name;
		}

		public String getDefaultVersion() {
			return defaultVersion;
		}

		@XmlElement(name = "default-version")
		public void setDefaultVersion(String defaultVersion) {
			this.defaultVersion = defaultVersion;
		}

		public String getInfo() {
			return info;
		}

		@XmlElement(name = "info")
		public void setInfo(String info) {
			this.info = info;
		}
	}
}
