package br.lncc.sinapad.cli.utils;

public class CLILogger {

	private Class _class;

	private CLILogger(Class _class) {
		this._class = _class;
	}

	public void error(String msn) {
		System.err.println("ERROR: [" + _class.getCanonicalName() + "]: " + msn);
	}

	public void error(String msn, Exception e) {
		error(msn);
		e.printStackTrace();
	}

	public static CLILogger getCLILogger(Class _class) {
		return new CLILogger(_class);
	}

}
