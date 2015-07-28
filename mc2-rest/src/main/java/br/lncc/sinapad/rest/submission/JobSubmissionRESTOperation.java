package br.lncc.sinapad.rest.submission;

import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import br.lncc.sinapad.core.exception.FileServiceException;
import br.lncc.sinapad.core.exception.SubmissionServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.rest.RESTOperation;
import br.lncc.sinapad.rest.data.JobSubmissonResult;
import br.lncc.sinapad.rest.data.Result;
import br.lncc.sinapad.rest.data.StatusResult;
import br.lncc.sinapad.rest.utils.RESTResultCodes;
import br.lncc.sinapad.rest.utils.RESTUtils;

@Path("/job-submission")
public class JobSubmissionRESTOperation extends RESTOperation {

	private static Logger logger = Logger.getLogger(JobSubmissionRESTOperation.class);

	/**
	 * 
	 * Runs a job
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param project
	 *          A project owned by the user.
	 * @param application
	 *          The name of the application.
	 * @param version
	 *          The version of the application.
	 * @param args
	 *          The arguments of the job. The used format is
	 *          'ARGUMENT1::VALUE1;ARGUMENT2::VALUE2;ARGUMENT3::VALUE3'
	 * @return A result containing the id of the job and the code of the result. <br>
	 *         - {@link RESTResultCodes#USER_NOT_AUTHORIZED} if the user is not
	 *         valid; <br>
	 *         - {@link RESTResultCodes#PROJECT_NOT_FOUND} if the project is not
	 *         found; <br>
	 *         - {@link RESTResultCodes#OK} if success; <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if some required inputed value is not
	 *         valid or is null; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/run")
	public JobSubmissonResult run(@FormParam("uuid") String uuid, @FormParam("project") String project, @FormParam("application") String application, @FormParam("version") String version, @FormParam("args") String args) {
		try {
			if ((uuid == null || uuid.isEmpty()) || (project == null || project.isEmpty()) || (application == null || application.isEmpty()) || (version == null || version.isEmpty()) || (args == null || args.isEmpty()) ) {
				return new JobSubmissonResult(RESTResultCodes.USER_ERROR);
			}

			if (!fileService.exists(uuid, project, null, null)) {
				return new JobSubmissonResult(RESTResultCodes.PROJECT_NOT_FOUND);
			}

			Map<String, String> argsMap = RESTUtils.parseParameters(args);

			String jobId = jobSubmissionService.run(uuid, project, application, version, null, argsMap);

			if (jobId != null) {
				JobSubmissonResult result = new JobSubmissonResult();
				result.setJobId(jobId);
				result.setCode(RESTResultCodes.OK);
				return result;
			}
			return new JobSubmissonResult(RESTResultCodes.USER_ERROR);
		} catch (UserNotAuthorizedException e) {
			return new JobSubmissonResult(RESTResultCodes.USER_NOT_AUTHORIZED);
		} catch (SubmissionServiceException e) {
			RESTUtils.handleException(e, logger);
			return new JobSubmissonResult(RESTResultCodes.USER_ERROR);
		} catch (FileServiceException e) {
			RESTUtils.handleException(e, logger);

		}
		return new JobSubmissonResult(RESTResultCodes.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 
	 * Cancels a job
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param project
	 *          A project owned by the user.
	 * @param jobId
	 *          The id of the job
	 * @return A result containing the code of the result. <br>
	 *         - {@link RESTResultCodes#USER_NOT_AUTHORIZED} if the user is not
	 *         valid; <br>
	 *         - {@link RESTResultCodes#PROJECT_NOT_FOUND} if the project is not
	 *         found; <br>
	 *         - {@link RESTResultCodes#OK} if success; <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if some required parameter is
	 *         null; <br>
	 *         - {@link RESTResultCodes#JOB_CANNOT_BE_CANCELLED} if the job can
	 *         not be cancelled. - {@link RESTResultCodes#INTERNAL_SERVER_ERROR}
	 *         if some error occurs.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/cancel")
	public Result cancel(@FormParam("uuid") String uuid, @FormParam("project") String project, @FormParam("jobId") String jobId) {
		try {
			if ((uuid == null || uuid.isEmpty()) || (project == null || project.isEmpty()) || (jobId == null || jobId.isEmpty())) {
				return new JobSubmissonResult(RESTResultCodes.USER_ERROR);
			}

			if (!fileService.exists(uuid, project, null, null)) {
				return new JobSubmissonResult(RESTResultCodes.PROJECT_NOT_FOUND);
			}

			if (jobSubmissionService.cancel(uuid, project, jobId)) {
				return new Result(RESTResultCodes.OK);
			}
			return new Result(RESTResultCodes.JOB_CANNOT_BE_CANCELLED);
		} catch (UserNotAuthorizedException e) {
			return new JobSubmissonResult(RESTResultCodes.USER_NOT_AUTHORIZED);
		} catch (Exception e) {
			RESTUtils.handleException(e, logger);
		}
		return new Result(RESTResultCodes.INTERNAL_SERVER_ERROR);
	}
}
