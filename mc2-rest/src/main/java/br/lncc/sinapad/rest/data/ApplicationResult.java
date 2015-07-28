package br.lncc.sinapad.rest.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.lncc.sinapad.rest.utils.RESTResultCodes;

@XmlRootElement
public class ApplicationResult extends Result {

	/**
	 * The name of the application.
	 */
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * The information of the application.
	 */
	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * The list of the versions of the application.
	 */
	private List<VersionResult> versions;

	@XmlElementWrapper
	@XmlElement(name = "version")
	public List<VersionResult> getVersions() {
		return versions;
	}

	public void setVersions(List<VersionResult> versions) {
		this.versions = versions;
	}

	public void addVersion(VersionResult version) {
		this.versions.add(version);
	}

	public ApplicationResult() {
		this(RESTResultCodes.OK);
	}

	public ApplicationResult(int code) {
		super(code);
		versions = new ArrayList<VersionResult>();
	}

	@XmlRootElement
	public static class VersionResult {

		/**
		 * A version of an application.
		 */
		private String version;

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		/**
		 * The info of the version.
		 */
		private String info;

		public String getInfo() {
			return info;
		}

		public void setInfo(String info) {
			this.info = info;
		}

		/**
		 * The List of resource of the version
		 */
		private List<String> resource;

		public List<String> getQueues() {
			return resource;
		}

		public void setQueues(List<String> resource) {
			this.resource = resource;
		}

	}
}
