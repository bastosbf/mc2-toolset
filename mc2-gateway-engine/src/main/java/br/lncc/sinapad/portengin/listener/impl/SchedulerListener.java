package br.lncc.sinapad.portengin.listener.impl;

import java.io.File;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.config.PortalConfig;
import br.lncc.sinapad.portengin.listener.PortEnginListener;
import br.lncc.sinapad.portengin.scheduler.PortEnginScheduler;

public class SchedulerListener implements PortEnginListener {

	public void shutdown(ServletContext ctxt) {
		PortEnginScheduler.stop();
	}

	public void execute(ServletContext ctxt) {
		String path = ctxt.getRealPath("/NEW");
		File f = new File(path);
		if (!f.exists()) {
			ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-" + BaseAction.FACADE + ".xml");
			PortalConfig portalConfig = (PortalConfig) applicationContext.getBean("portalConfig");
			final int interval = portalConfig.getResultCheckInterval();

			String storagePath = ctxt.getRealPath("/storage");

			File dir = new File(storagePath);
			if (!dir.exists()) {
				dir.mkdir();
			}
			File storage = new File(dir, "scheduler.data");
			PortEnginScheduler.start(storage, interval);
		}
	}
}
