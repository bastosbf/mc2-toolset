package br.lncc.sinapad.rest.data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Result {

	/**
	 * The result code.
	 */
	private int code;

	public Result() {

	}

	public Result(int code) {
		this();
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public boolean equals(Object o) {
		return ((Result) o).code == code;
	}

	public int hashCode() {
		return code;
	}

}
