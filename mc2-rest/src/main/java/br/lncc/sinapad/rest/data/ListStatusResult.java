package br.lncc.sinapad.rest.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.lncc.sinapad.rest.utils.RESTResultCodes;

@XmlRootElement(name = "list")
public class ListStatusResult extends Result {
	private List<StatusResult> elements;

	public ListStatusResult() {
		this(RESTResultCodes.OK);
	}

	public ListStatusResult(int code) {
		super(code);
		elements = new ArrayList<StatusResult>();
	}

	@XmlElementWrapper
	@XmlElement(name = "element")
	public List<StatusResult> getElements() {
		return elements;
	}

	public void setElements(List<StatusResult> elements) {
		this.elements = elements;
	}

}
