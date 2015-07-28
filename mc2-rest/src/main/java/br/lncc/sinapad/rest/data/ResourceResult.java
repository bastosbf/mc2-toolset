package br.lncc.sinapad.rest.data;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.lncc.sinapad.rest.utils.RESTResultCodes;

@XmlRootElement
public class ResourceResult extends Result {

	private String name = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private int numOfJobs = 0;

	public int getNumOfJobs() {
		return numOfJobs;
	}

	public void setNumOfJobs(int numOfJobs) {
		this.numOfJobs = numOfJobs;
	}

	private int numOfProc = 0;

	public int getNumOfProc() {
		return numOfProc;
	}

	public void setNumOfProc(int numOfProc) {
		this.numOfProc = numOfProc;
	}

	private List<NodeResult> nodes;

	@XmlElementWrapper
	@XmlElement(name = "nodes")
	public List<NodeResult> getNodes() {
		return nodes;
	}

	public void setNodes(List<NodeResult> nodes) {
		this.nodes = nodes;
	}

	public void addNode(NodeResult node) {
		this.nodes.add(node);
	}

	// private Map<String, String> properties;
	//
	// public Map<String, String> getProperties() {
	// return properties;
	// }
	//
	// public void setProperties(Map<String, String> properties) {
	// this.properties = properties;
	// }
	//
	// public void putProperty(String key, String value) {
	// this.properties.put(key, value);
	// }

	public ResourceResult() {
		this(RESTResultCodes.OK);
	}

	public ResourceResult(int code) {
		super(code);
	}

	@XmlRootElement
	public static class NodeResult {
		private boolean available = false;

		public boolean isAvailable() {
			return available;
		}

		public void setAvailable(boolean available) {
			this.available = available;
		}

		private int numOfJobs = 0;

		public int getNumOfJobs() {
			return numOfJobs;
		}

		public void setNumOfJobs(int numOfJobs) {
			this.numOfJobs = numOfJobs;
		}

		private int numOfProc = 0;

		public int getNumOfProc() {
			return numOfProc;
		}

		public void setNumOfProc(int numOfProc) {
			this.numOfProc = numOfProc;
		}

		// private Map<String, String> properties;
		//
		// public Map<String, String> getProperties() {
		// return properties;
		// }
		//
		// public void setProperties(Map<String, String> properties) {
		// this.properties = properties;
		// }
		//
		// public void putProperty(String key, String value) {
		// this.properties.put(key, value);
		// }
	}
}
