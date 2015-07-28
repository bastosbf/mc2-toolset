package br.lncc.sinapad.rest.data;

import javax.xml.bind.annotation.XmlRootElement;

import br.lncc.sinapad.rest.utils.RESTResultCodes;

@XmlRootElement
public class AuthenticationResult extends Result {

	/**
	 * Universally unique identifier of the user.
	 */
	private String uuid;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public AuthenticationResult() {
		this(RESTResultCodes.OK);
	}

	public AuthenticationResult(int code) {
		super(code);
	}
}
