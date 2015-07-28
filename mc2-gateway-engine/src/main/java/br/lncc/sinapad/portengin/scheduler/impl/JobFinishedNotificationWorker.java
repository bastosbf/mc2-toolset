package br.lncc.sinapad.portengin.scheduler.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.lncc.sinapad.core.data.ResultData;
import br.lncc.sinapad.core.exception.MonitoringServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.core.service.monitoring.JobMonitoringService;
import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.config.EmailConfig;
import br.lncc.sinapad.portengin.config.EmailConfig.JobFinishedNotificationMessage;
import br.lncc.sinapad.portengin.scheduler.PortEnginSchedulerWorker;
import br.lncc.sinapad.portengin.utils.EmailUtils;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class JobFinishedNotificationWorker implements PortEnginSchedulerWorker {

	private String email;
	private String uuid;
	private String project;
	private String jobId;

	public JobFinishedNotificationWorker(String email, String uuid, String project, String jobId) {
		this.email = email;
		this.uuid = uuid;
		this.project = project;
		this.jobId = jobId;
	}

	@Override
	public boolean canExecute() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-" + BaseAction.FACADE + ".xml");
		JobMonitoringService service = (JobMonitoringService) applicationContext.getBean("jobMonitoringService");
		try {
			ResultData status = service.get(uuid, project, jobId);
			if (status.getStatus() == ResultData.Status.DONE || status.getStatus() == ResultData.Status.FAILED || status.getStatus() == ResultData.Status.UNDETERMINED) {
				return true;
			}
		} catch (MonitoringServiceException e) {
			PortEnginUtils.handleException(e, null);
		} catch (UserNotAuthorizedException e) {
			PortEnginUtils.handleException(e, null);
		}
		return false;
	}

	@Override
	public void execute() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-" + BaseAction.FACADE + ".xml");
		JobMonitoringService service = (JobMonitoringService) applicationContext.getBean("jobMonitoringService");
		EmailConfig config = (EmailConfig) applicationContext.getBean("emailConfig");
		JobFinishedNotificationMessage message = config.getJobFinishedNotificationMessage();
		try {
			ResultData status = service.get(uuid, project, jobId);
			String info = "\n\nJob Id: " + status.getJobId() + "\nStatus: " + status.getStatus() + "\nStart Date: " + status.getStartDate() + "\nEnd Date: " + status.getEndDate() + "\nQueue: " + status.getResource();
			EmailUtils.send(null, email, message.getSubject(), message.getBody() + info);
		} catch (MonitoringServiceException e) {
			PortEnginUtils.handleException(e, null);
		} catch (UserNotAuthorizedException e) {
			PortEnginUtils.handleException(e, null);
		}
	}
}
