package br.lncc.sinapad.rest.monitoring;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import br.lncc.sinapad.core.data.FileData;
import br.lncc.sinapad.core.data.ResultData;
import br.lncc.sinapad.core.data.ResultData.Status;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.rest.RESTOperation;
import br.lncc.sinapad.rest.data.ApplicationResult;
import br.lncc.sinapad.rest.data.FileResult;
import br.lncc.sinapad.rest.data.ListStatusResult;
import br.lncc.sinapad.rest.data.Result;
import br.lncc.sinapad.rest.data.StatusResult;
import br.lncc.sinapad.rest.utils.RESTResultCodes;
import br.lncc.sinapad.rest.utils.RESTUtils;

@Path("/job-monitoring")
public class JobMonitoringRESTOperation extends RESTOperation {

	private static Logger logger = Logger.getLogger(JobMonitoringRESTOperation.class);

	/**
	 * 
	 * Lists all results in a given period
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param project
	 *          A project owned by the user.
	 * @param from
	 *          (optional) The minimum start date of the job, or null for dates
	 *          from any period. The used format is 'yyyyMMdd'
	 * @param to
	 *          (optional) The maximum start date of the job, or null for dates to
	 *          any period. The used format is 'yyyyMMdd'
	 * @return A result containing the list of statuses and the code of the
	 *         result. <br>
	 *         - {@link RESTResultCodes#USER_NOT_AUTHORIZED} if the user is not
	 *         valid; <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if the format of one the
	 *         dates(from or to) is not null and is incorrect, or if some required
	 *         parameter is null; <br>
	 *         - {@link RESTResultCodes#OK} if success; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs.
	 */

	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/list")
	public ListStatusResult list(@FormParam("uuid") String uuid, @FormParam("project") String project, @FormParam("from") String from, @FormParam("to") String to) {
		try {
			if ((uuid == null || uuid.isEmpty()) || (project == null || project.isEmpty())) {
				return new ListStatusResult(RESTResultCodes.USER_ERROR);
			}

			if (!fileService.exists(uuid, project, null, null)) {
				return new ListStatusResult(RESTResultCodes.PROJECT_NOT_FOUND);
			}

			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			Date dateFrom = null;
			if (from == null) {
				from = "";
			}

			if (to == null) {
				to = "";
			}

			if (!from.equals("")) {
				try {
					dateFrom = formatter.parse(from);
				} catch (ParseException e) {
					return new ListStatusResult(RESTResultCodes.USER_ERROR);
				}
			}

			Date dateTo = null;
			if (!to.equals("")) {
				try {
					dateTo = formatter.parse(to);
				} catch (ParseException e) {
					return new ListStatusResult(RESTResultCodes.USER_ERROR);
				}
			}
			List<ResultData> list = jobMonitoringService.list(uuid, project, dateFrom, dateTo);
			if (list != null) {
				List<StatusResult> result = RESTUtils.convertListResultDataToListStatusResult(list);
				ListStatusResult results = new ListStatusResult(RESTResultCodes.OK);
				results.setElements(result);
				return results;
			}
		} catch (UserNotAuthorizedException e) {
			return new ListStatusResult(RESTResultCodes.USER_NOT_AUTHORIZED);
		} catch (Exception e) {
			RESTUtils.handleException(e, logger);
		}
		return new ListStatusResult(RESTResultCodes.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 
	 * Gets the job result
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param project
	 *          A project owned by the user.
	 * @param jobId
	 *          The id of the job
	 * @return A result containing the status of the job and the code of the
	 *         result. <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if some required parameter is null; <br>
	 *         - {@link RESTResultCodes#USER_NOT_AUTHORIZED} if the user is not
	 *         valid; <br>
	 *         - {@link RESTResultCodes#PROJECT_NOT_FOUND} if the project is not
	 *         found; <br>
	 *         - {@link RESTResultCodes#OK} if success; <br>
	 *         - {@link RESTResultCodes#FILE_DOES_NOT_EXIST} if the file does not
	 *         exist; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/get")
	public StatusResult get(@FormParam("uuid") String uuid, @FormParam("project") String project, @FormParam("jobId") String jobId) {
		try {
			if ((uuid == null || uuid.isEmpty()) || (project == null || project.isEmpty()) || (jobId == null || jobId.isEmpty())) {
				return new StatusResult(RESTResultCodes.USER_ERROR);
			}
			
			if (!fileService.exists(uuid, project, null, null)) {
				return new StatusResult(RESTResultCodes.PROJECT_NOT_FOUND);
			}

			ResultData data = jobMonitoringService.get(uuid, project, jobId);
			if (data != null && !data.getStatus().equals(Status.UNDETERMINED)) {
				StatusResult result = RESTUtils.convertResultDataToStatusResult(data);
				result.setCode(RESTResultCodes.OK);
				return result;
			}
			return new StatusResult(RESTResultCodes.JOB_DOES_NOT_EXIST);
		} catch (UserNotAuthorizedException e) {
			return new StatusResult(RESTResultCodes.USER_NOT_AUTHORIZED);
		} catch (Exception e) {
			RESTUtils.handleException(e, logger);
		}

		return new StatusResult(RESTResultCodes.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 
	 * Deletes a job result
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param project
	 *          A project owned by the user.
	 * @param jobId
	 *          The id of the job.
	 * @return A result containing the code of the result. <br>
	 *         - {@link RESTResultCodes#USER_NOT_AUTHORIZED} if the user is not
	 *         valid; <br>
	 *         - {@link RESTResultCodes#PROJECT_NOT_FOUND} if the project is not
	 *         found; <br>
	 *         - {@link RESTResultCodes#OK} if success; <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if some required parameter is null; <br>
	 *         - {@link RESTResultCodes#JOB_DOES_NOT_EXIST} if the job result does not exist; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/delete")
	public Result delete(@FormParam("uuid") String uuid, @FormParam("project") String project, @FormParam("jobId") String jobId) {
		try {
			if ((uuid == null || uuid.isEmpty()) || (project == null || project.isEmpty()) || (jobId == null || jobId.isEmpty())) {
				return new StatusResult(RESTResultCodes.USER_ERROR);
			}

			if (!fileService.exists(uuid, project, null, null)) {
				return new StatusResult(RESTResultCodes.PROJECT_NOT_FOUND);
			}

			boolean result = jobMonitoringService.delete(uuid, project, jobId);
			if (result) {
				return new Result(RESTResultCodes.OK);
			}
			return new Result(RESTResultCodes.JOB_DOES_NOT_EXIST);
		} catch (UserNotAuthorizedException e) {
			return new ApplicationResult(RESTResultCodes.USER_NOT_AUTHORIZED);
		} catch (Exception e) {
			RESTUtils.handleException(e, logger);
		}
		return new Result(RESTResultCodes.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 
	 * Gets the log of the job.
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param project
	 *          A project owned by the user.
	 * @param jobId
	 *          The id of the job.
	 * @return The log of the job <br>
	 *         - {@link RESTResultCodes#OK} if success; <br>
	 *         -{@link RESTResultCodes#NO_CONTENT} if the log can not be returned; <br>
	 */
	@POST
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/log")
	public Response log(@FormParam("uuid") String uuid, @FormParam("project") String project, @FormParam("jobId") String jobId) {
		if ((uuid == null || uuid.isEmpty()) || (project == null || project.isEmpty()) || (jobId == null || jobId.isEmpty())) {
			return Response.noContent().build();
		}
		InputStream is = null;
		try {
			is = jobMonitoringService.log(uuid, project, jobId);
		} catch (Exception e) {
			RESTUtils.handleException(e, logger);
		}
		if (is == null) {
			return Response.noContent().build();
		}
		return Response.ok(is, MediaType.APPLICATION_OCTET_STREAM).header("content-disposition", "attachment; filename = " + jobId + ".log").build();
	}

	/**
	 * 
	 * Gets the immutable file history used and returned by the job
	 * 
	 * @param uuid
	 *          The universally unique identifier of the user
	 * @param project
	 *          A project owned by the user
	 * @param jobId
	 *          The id of the job
	 * @param parents
	 *          (optional) The parents directory. Null if there is no parents
	 * @param file
	 *          (optional) The file name. Null if there is not file name
	 * @return A result containing the file representation of the history
	 *         directory of the job and the code of the result. <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if some required parameter is null;<br>
	 *         - {@link RESTResultCodes#USER_NOT_AUTHORIZED} if the user is not
	 *         valid; <br>
	 *         - {@link RESTResultCodes#PROJECT_NOT_FOUND} if the project is not
	 *         found; <br>
	 *         - {@link RESTResultCodes#OK} if the file was found; <br>
	 *         {@link RESTResultCodes#USER_ERROR} if the job is not valid; <br>
	 *         - {@link RESTResultCodes#FILE_DOES_NOT_EXIST} if the file does not
	 *         exist; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/history")
	public FileResult history(@FormParam("uuid") String uuid, @FormParam("project") String project, @FormParam("jobId") String jobId, @FormParam("parents") String parents, @FormParam("file") String file) {
		try {
			if ((uuid == null || uuid.isEmpty()) || (project == null || project.isEmpty()) || (jobId == null || jobId.isEmpty())) {
				return new FileResult(RESTResultCodes.USER_ERROR);
			}
			
			// Checking if the project is valid
			if (!fileService.exists(uuid, project, null, null)) {
				return new FileResult(RESTResultCodes.PROJECT_NOT_FOUND);
			}

			if (parents != null && parents.isEmpty()) {
				parents = null;
			}

			if (file != null && file.isEmpty()) {
				file = null;
			}
			String[] parentsArray = RESTUtils.convertAbsolutePathToArray(parents, true, false);

			// Calling the method
			FileData data = jobMonitoringService.history(uuid, project, jobId, parentsArray, file);
			if (data != null) {
				FileResult result = RESTUtils.convertFileDataToFileResult(data);
				result.setCode(RESTResultCodes.OK);
				return result;
			} else {
				return new FileResult(RESTResultCodes.FILE_DOES_NOT_EXIST);
			}
		} catch (UserNotAuthorizedException e) {
			return new FileResult(RESTResultCodes.USER_NOT_AUTHORIZED);
		} catch (Exception e) {
			RESTUtils.handleException(e, logger);
		}
		return new FileResult(RESTResultCodes.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 
	 * Downloads a file from the immutable file history
	 * 
	 * @param uuid
	 *          The universally unique identifier of the user
	 * @param project
	 *          A project owned by the user
	 * @param jobId
	 *          The if of the job
	 * @param parents
	 *          (optional) The parents directory. Null if there is no parents
	 * @param file
	 *          The file name
	 * @return The file content or null if the file was not found and a result
	 *         containing the code of the result. <br>
	 *         - {@link RESTResultCodes#OK} if the file download was successful; <br>
	 *         -{@link RESTResultCodes#NO_CONTENT} if the file was not found; <br>
	 */
	@POST
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/download")
	public Response download(@FormParam("uuid") String uuid, @FormParam("project") String project, @FormParam("jobId") String jobId, @FormParam("parents") String parents, @FormParam("file") String file) {
		InputStream is = null;
		try {

			if (parents != null && parents.isEmpty()) {
				parents = null;
			}

			String[] parentsArray = RESTUtils.convertAbsolutePathToArray(parents, true, false);

			// Calling the method
			is = jobMonitoringService.download(uuid, project, jobId, parentsArray, file);
		} catch (Exception e) {
			RESTUtils.handleException(e, logger);
		}
		if (is == null) {
			return Response.noContent().build();
		}
		return Response.ok(is, MediaType.APPLICATION_OCTET_STREAM).header("content-disposition", "attachment; filename = " + file).build();
	}
}
