package br.lncc.sinapad.rest.data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import br.lncc.sinapad.rest.utils.RESTResultCodes;

@XmlType
public class LongResult extends Result {

	@XmlElement(name = "value")
	private long value;

	public LongResult() {
		this(RESTResultCodes.OK);
	}

	public LongResult(Integer code) {
		super(code);

	}

	public LongResult(Long value) {
		this();
		this.value = value;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

}
