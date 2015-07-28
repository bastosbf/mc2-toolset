package br.lncc.sinapad.rest.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.lncc.sinapad.rest.utils.RESTResultCodes;

@XmlRootElement(name = "list")
public class ListResourceResult extends Result {
	private List<ResourceResult> elements;

	public ListResourceResult() {
		this(RESTResultCodes.OK);
	}

	public ListResourceResult(int code) {
		super(code);
		elements = new ArrayList<ResourceResult>();
	}

	@XmlElementWrapper
	@XmlElement(name = "element")
	public List<ResourceResult> getElements() {
		return elements;
	}

	public void setElements(List<ResourceResult> elements) {
		this.elements = elements;
	}

}
