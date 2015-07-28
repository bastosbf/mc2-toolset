package br.lncc.sinapad.rest.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.lncc.sinapad.rest.utils.RESTResultCodes;

@XmlRootElement
public class FileResult extends Result {

	/**
	 * The file name.
	 */
	private String name = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * The absolute path of the file.
	 */
	private String absolutePath = "";

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	private String parentPath = "";

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	/**
	 * True if it is a directory, or false if it is a file.
	 */
	private boolean directory = false;

	public boolean isDirectory() {
		return directory;
	}

	public void setDirectory(Boolean directory) {
		this.directory = directory;
	}

	/**
	 * The children of the file.
	 */
	private List<FileResult> children;

	@XmlElementWrapper
	@XmlElement(name = "children")
	public List<FileResult> getChildren() {
		return children;
	}

	public void setChildren(List<FileResult> children) {
		this.children = children;
	}

	public void addChild(FileResult child) {
		this.children.add(child);

	}

	public FileResult() {
		this(RESTResultCodes.OK);
	}

	public FileResult(int code) {
		super(code);
		children = new ArrayList<FileResult>();
	}

}
