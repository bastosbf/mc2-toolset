package br.lncc.sinapad.portengin.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

import br.lncc.sinapad.portengin.utils.PortEnginUtils;

@XmlRootElement(name = "authentication")
public class AuthenticationConfig implements Serializable {

	private static Logger logger = Logger.getLogger(AuthenticationConfig.class);

	public AuthenticationConfig(String xml) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(AuthenticationConfig.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			InputStream is = null;
			File file = new File(xml);
			if (file.exists()) {
				is = new FileInputStream(file);
			} else {
				is = getClass().getResourceAsStream(xml);
			}
			AuthenticationConfig config = (AuthenticationConfig) jaxbUnmarshaller.unmarshal(is);

			this.setLdapAuthentication(config.getLdapAuthentication());
			this.setVomsAuthentication(config.getVomsAuthentication());
			this.setShibbolethAuthentication(config.getShibbolethAuthentication());
		} catch (JAXBException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (FileNotFoundException e) {
			PortEnginUtils.handleException(e, logger);
		}
	}

	private LDAPAuthentication ldapAuthentication;
	private VOMSAuthentication vomsAuthentication;
	private ShibbolethAuthentication shibbolethAuthentication;

	public AuthenticationConfig() {

	}

	public LDAPAuthentication getLdapAuthentication() {
		return ldapAuthentication;
	}

	@XmlElement(name = "ldap")
	public void setLdapAuthentication(LDAPAuthentication ldapAuthentication) {
		this.ldapAuthentication = ldapAuthentication;
	}

	public VOMSAuthentication getVomsAuthentication() {
		return vomsAuthentication;
	}

	@XmlElement(name = "voms")
	public void setVomsAuthentication(VOMSAuthentication vomsAuthentication) {
		this.vomsAuthentication = vomsAuthentication;
	}

	public ShibbolethAuthentication getShibbolethAuthentication() {
		return shibbolethAuthentication;
	}

	@XmlElement(name = "shibboleth")
	public void setShibbolethAuthentication(ShibbolethAuthentication shibbolethAuthentication) {
		this.shibbolethAuthentication = shibbolethAuthentication;
	}

	public static class LDAPAuthentication {

		private boolean enabled;

		public boolean isEnabled() {
			return enabled;
		}

		@XmlAttribute(name = "enabled")
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}

	public static class VOMSAuthentication {
		private boolean enabled;

		public boolean isEnabled() {
			return enabled;
		}

		@XmlAttribute(name = "enabled")
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}

	public static class ShibbolethAuthentication {
		private boolean enabled;
		private String url;
		private String storagePath;

		public boolean isEnabled() {
			return enabled;
		}

		@XmlAttribute(name = "enabled")
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public String getUrl() {
			return url;
		}

		@XmlElement(name = "url")
		public void setUrl(String url) {
			this.url = url;
		}

		public String getStoragePath() {
			return storagePath;
		}

		@XmlElement(name = "storage-path")
		public void setStoragePath(String storagePath) {
			this.storagePath = storagePath;
		}
	}

}
