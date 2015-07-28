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

@XmlRootElement(name = "modules")
public class ModulesConfig implements Serializable {

	private static Logger logger = Logger.getLogger(ModulesConfig.class);

	public ModulesConfig() {

	}

	public ModulesConfig(String xml) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ModulesConfig.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			InputStream is = null;
			File file = new File(xml);
			if (file.exists()) {
				is = new FileInputStream(file);
			} else {
				is = getClass().getResourceAsStream(xml);
			}

			ModulesConfig config = (ModulesConfig) jaxbUnmarshaller.unmarshal(is);

			this.setSharedFilesModule(config.getSharedFilesModule());
			this.setPublicFilesModule(config.getPublicFilesModule());
			this.setGuestUserModule(config.getGuestUserModule());
			this.setBulkJobsModule(config.getBulkJobsModule());
		} catch (JAXBException e) {
			PortEnginUtils.handleException(e, logger);
		} catch (FileNotFoundException e) {
			PortEnginUtils.handleException(e, logger);
		}
	}

	private SharedFilesModule sharedFilesModule;
	private PublicFilesModule publicFilesModule;
	private GuestUserModule guestUserModule;
	private BulkJobsModule bulkJobsModule;

	public SharedFilesModule getSharedFilesModule() {
		return sharedFilesModule;
	}

	@XmlElement(name = "shared-files")
	public void setSharedFilesModule(SharedFilesModule sharedFilesModule) {
		this.sharedFilesModule = sharedFilesModule;
	}

	public PublicFilesModule getPublicFilesModule() {
		return publicFilesModule;
	}

	@XmlElement(name = "public-files")
	public void setPublicFilesModule(PublicFilesModule publicFilesModule) {
		this.publicFilesModule = publicFilesModule;
	}

	public GuestUserModule getGuestUserModule() {
		return guestUserModule;
	}

	@XmlElement(name = "guest-user")
	public void setGuestUserModule(GuestUserModule guestUserModule) {
		this.guestUserModule = guestUserModule;
	}

	public BulkJobsModule getBulkJobsModule() {
		return bulkJobsModule;
	}

	@XmlElement(name = "bulk-jobs")
	public void setBulkJobsModule(BulkJobsModule bulkJobsModule) {
		this.bulkJobsModule = bulkJobsModule;
	}

	public static class SharedFilesModule {

		private boolean enabled;

		public boolean isEnabled() {
			return enabled;
		}

		@XmlElement(name = "enabled")
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}

	public static class PublicFilesModule {

		private boolean enabled;

		public boolean isEnabled() {
			return enabled;
		}

		@XmlElement(name = "enabled")
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}

	public static class GuestUserModule {

		private boolean enabled;
		private String allowedVersions;

		public boolean isEnabled() {
			return enabled;
		}

		@XmlElement(name = "enabled")
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public String getAllowedVersions() {
			return allowedVersions;
		}

		@XmlElement(name = "allowed-versions")
		public void setAllowedVersions(String allowedVersions) {
			this.allowedVersions = allowedVersions;
		}
	}

	public static class BulkJobsModule {

		private boolean enabled;
		private BulkJobsValue beginValue;
		private BulkJobsValue endValue;
		private BulkJobsValue stepValue;

		public boolean isEnabled() {
			return enabled;
		}

		@XmlElement(name = "enabled")
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public BulkJobsValue getBeginValue() {
			return beginValue;
		}

		@XmlElement(name = "begin-value")
		public void setBeginValue(BulkJobsValue beginValue) {
			this.beginValue = beginValue;
		}

		public BulkJobsValue getEndValue() {
			return endValue;
		}

		@XmlElement(name = "end-value")
		public void setEndValue(BulkJobsValue endValue) {
			this.endValue = endValue;
		}

		public BulkJobsValue getStepValue() {
			return stepValue;
		}

		@XmlElement(name = "step-value")
		public void setStepValue(BulkJobsValue stepValue) {
			this.stepValue = stepValue;
		}

		public static class BulkJobsValue {

			private boolean _static;
			private Integer min;
			private Integer max;
			private String label;
			private Integer value;

			public boolean isStatic() {
				return _static;
			}

			@XmlAttribute(name = "static")
			public void setStatic(boolean _static) {
				this._static = _static;
			}

			public Integer getMin() {
				return min;
			}

			@XmlAttribute(name = "min")
			public void setMin(Integer min) {
				this.min = min;
			}

			public Integer getMax() {
				return max;
			}

			@XmlAttribute(name = "max")
			public void setMax(Integer max) {
				this.max = max;
			}

			public String getLabel() {
				return label;
			}

			@XmlElement(name = "label")
			public void setLabel(String label) {
				this.label = label;
			}

			public Integer getValue() {
				return value;
			}

			@XmlElement(name = "default-value")
			public void setValue(Integer value) {
				this.value = value;
			}

		}

	}

}
