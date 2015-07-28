package br.lncc.sinapad.rest.data;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import br.lncc.sinapad.core.data.ResultData.Status;
import br.lncc.sinapad.rest.utils.RESTResultCodes;

@XmlRootElement
public class StatusResult extends Result {

	public StatusResult() {
		this(RESTResultCodes.OK);
	}

	public StatusResult(int code) {
		super(code);
	}

	/**
	 * The id of the job
	 */
	private String jobId;

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	/**
	 * The resource where the job was executed
	 */
	private String resource;

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	/**
	 * The start date of the job.
	 */
	private Date startDate;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * The end date of the job.
	 */
	private Date endDate;

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * The status of the job
	 */
	private Status status;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
