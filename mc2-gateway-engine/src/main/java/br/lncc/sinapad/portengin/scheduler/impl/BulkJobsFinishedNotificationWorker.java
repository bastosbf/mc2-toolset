package br.lncc.sinapad.portengin.scheduler.impl;

import java.util.ArrayList;
import java.util.List;

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

public class BulkJobsFinishedNotificationWorker implements PortEnginSchedulerWorker {

	private String email;
	private String uuid;
	private String project;
	private String[] jobIds;

	public BulkJobsFinishedNotificationWorker(String email, String uuid, String project, String[] jobIds) {
		this.email = email;
		this.uuid = uuid;
		this.project = project;
		this.jobIds = jobIds;
	}

	@Override
	public boolean canExecute() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-" + BaseAction.FACADE + ".xml");
		JobMonitoringService service = (JobMonitoringService) applicationContext.getBean("jobMonitoringService");
		try {
			boolean result = true;
			for (String jobId : jobIds) {
				ResultData status = service.get(uuid, project, jobId);
				if (!(status.getStatus() == ResultData.Status.DONE || status.getStatus() == ResultData.Status.FAILED || status.getStatus() == ResultData.Status.UNDETERMINED)) {
					result = false;
					break;
				}
			}
			return result;
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
			String info = "\n\nJob Id: $JOBIDS\nStatus: $STATUS";
			List<String> i = new ArrayList<String>();
			List<String> s = new ArrayList<String>();
			for (String jobId : jobIds) {
				ResultData status = service.get(uuid, project, jobId);
				i.add(status.getJobId());
				s.add(String.valueOf(status.getStatus()));
			}
			info = info.replace("$JOBIDS", String.valueOf(i));
			info = info.replace("$STATUS", String.valueOf(s));
			EmailUtils.send(null, email, message.getSubject(), message.getBody() + info);
		} catch (MonitoringServiceException e) {
			PortEnginUtils.handleException(e, null);
		} catch (UserNotAuthorizedException e) {
			PortEnginUtils.handleException(e, null);
		}
	}
}
