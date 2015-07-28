package br.lncc.sinapad.rest.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.lncc.sinapad.rest.utils.RESTResultCodes;

@XmlRootElement(name = "list")
public class ListApplicationResult extends Result {
	private List<ApplicationResult> elements;

	public ListApplicationResult() {
		this(RESTResultCodes.OK);
	}

	public ListApplicationResult(int code) {
		super(code);
		elements = new ArrayList<ApplicationResult>();
	}

	@XmlElementWrapper
	@XmlElement(name = "element")
	public List<ApplicationResult> getElements() {
		return elements;
	}

	public void setElements(List<ApplicationResult> elements) {
		this.elements = elements;
	}

}
