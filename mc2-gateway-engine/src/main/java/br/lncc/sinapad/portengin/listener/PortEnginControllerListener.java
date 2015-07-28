package br.lncc.sinapad.portengin.listener;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import br.lncc.sinapad.portengin.listener.impl.ParserListener;
import br.lncc.sinapad.portengin.listener.impl.SchedulerListener;
import br.lncc.sinapad.portengin.listener.impl.StartupListener;

public class PortEnginControllerListener implements ServletContextListener {

	private static List<PortEnginListener> listeners;

	static {
		listeners = new ArrayList<PortEnginListener>();

		StartupListener startup = new StartupListener();
		ParserListener parser = new ParserListener();
		SchedulerListener scheduler = new SchedulerListener();

		listeners.add(startup);
		listeners.add(parser);
		listeners.add(scheduler);
	}

	public static void executeListeners(ServletContext ctxt) {
		for (PortEnginListener listener : listeners) {
			listener.execute(ctxt);
		}
	}

	public static void shutdownListeners(ServletContext ctxt) {
		for (PortEnginListener listener : listeners) {
			listener.shutdown(ctxt);
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent ctxt) {
		executeListeners(ctxt.getServletContext());
	}

	@Override
	public void contextDestroyed(ServletContextEvent ctxt) {
		shutdownListeners(ctxt.getServletContext());
	}

}
