package br.lncc.sinapad.portengin.listener;

import javax.servlet.ServletContext;

public interface PortEnginListener {

	void shutdown(ServletContext ctxt);

	void execute(ServletContext ctxt);

}
