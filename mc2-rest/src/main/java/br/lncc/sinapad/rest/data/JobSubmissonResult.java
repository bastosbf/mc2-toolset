package br.lncc.sinapad.rest.data;

import javax.xml.bind.annotation.XmlRootElement;

import br.lncc.sinapad.rest.utils.RESTResultCodes;

@XmlRootElement
public class JobSubmissonResult extends Result {

	/**
	 * The id of the job.
	 */
	private String jobId;

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public JobSubmissonResult() {
		this(RESTResultCodes.OK);
	}

	public JobSubmissonResult(int code) {
		super(code);
	}

}
