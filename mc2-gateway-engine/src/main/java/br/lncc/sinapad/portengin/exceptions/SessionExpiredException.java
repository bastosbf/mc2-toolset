package br.lncc.sinapad.portengin.exceptions;

public class SessionExpiredException extends RuntimeException {

	public SessionExpiredException() {}

	public SessionExpiredException(String msn) {
		super(msn);
	}

}
