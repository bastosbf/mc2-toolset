package br.lncc.sinapad.portengin.action.application;

import java.util.Map;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.core.exception.SubmissionServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.portengin.action.BaseAction;
import br.lncc.sinapad.portengin.scheduler.PortEnginScheduler;
import br.lncc.sinapad.portengin.scheduler.PortEnginSchedulerWorker;
import br.lncc.sinapad.portengin.scheduler.impl.BulkJobsFinishedNotificationWorker;
import br.lncc.sinapad.portengin.scheduler.impl.JobFinishedNotificationWorker;
import br.lncc.sinapad.portengin.utils.PortEnginUtils;

public class ExecuteApplicationAction extends BaseAction {

	private static Logger logger = Logger.getLogger(ExecuteApplicationAction.class);

	private Map<String, String> args;

	public Map<String, String> getArgs() {
		return args;
	}

	public void setArgs(Map<String, String> args) {
		this.args = args;
	}

	private int beginValue;

	public int getBeginValue() {
		return beginValue;
	}

	public void setBeginValue(int beginValue) {
		this.beginValue = beginValue;
	}

	private int endValue;

	public int getEndValue() {
		return endValue;
	}

	private int stepValue;

	public int getStepValue() {
		return stepValue;
	}

	public void setStepValue(int stepValue) {
		this.stepValue = stepValue;
	}

	public void setEndValue(int endValue) {
		this.endValue = endValue;
	}

	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private String resource;

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	@Override
	@Action(value = "/execute", results = { @Result(name = "success", location = "/confirm.jsp"), @Result(name = "error", location = "/application.jsp") })
	public String execute() {
		String uuid = getUuid();
		String project = getProject();
		String app = (String) session.get("app");
		String version = (String) session.get("version");
		boolean isGuest = (Boolean) session.get("isGuest");
		boolean isBulkJobs = (Boolean) application.get("isBulkJobs");
		if (isGuest) {
			String remoteAddr = request.getRemoteAddr();
			ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
			reCaptcha.setPrivateKey("6Lfyf8cSAAAAAJ9qXZpQAvs05FxsTjIYVHFh4mcS");

			String challenge = request.getParameter("recaptcha_challenge_field");
			String uresponse = request.getParameter("recaptcha_response_field");
			notVerified = Boolean.TRUE;
			if (uresponse != null) {
				ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);
				if (!reCaptchaResponse.isValid()) {
					return ERROR;
				}
				notVerified = Boolean.FALSE;
			} else {
				return ERROR;
			}
		}
		if (!isGuest && !isBulkJobs) {
			email = String.valueOf(session.get("email"));
		}
		try {
			PortEnginSchedulerWorker worker = null;
			if (isBulkJobs) {
				jobIds = jobSubmissionService.runBulk(uuid, project, app, version, resource, args, beginValue, endValue, stepValue);
				worker = new BulkJobsFinishedNotificationWorker(email, uuid, project, jobIds);
			} else {
				jobId = jobSubmissionService.run(uuid, project, app, version, resource, args);
				worker = new JobFinishedNotificationWorker(email, uuid, project, jobId);
			}
			if (worker != null) {
				PortEnginScheduler.addWorker(worker);
			}
		} catch (SubmissionServiceException e) {
			PortEnginUtils.handleException(e, logger);
			error = Boolean.TRUE;
			return ERROR;
		} catch (UserNotAuthorizedException e) {
			PortEnginUtils.handleException(e, logger);
			error = Boolean.TRUE;
		}
		return SUCCESS;
	}

	private String jobId;

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	private String[] jobIds;

	public String[] getJobIds() {
		return jobIds;
	}

	public void setJobIds(String[] jobIds) {
		this.jobIds = jobIds;
	}

	private boolean notVerified = Boolean.FALSE;

	public boolean isNotVerified() {
		return notVerified;
	}

	public void setNotVerified(boolean notVerified) {
		this.notVerified = notVerified;
	}

	private boolean error = Boolean.FALSE;

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}
}
