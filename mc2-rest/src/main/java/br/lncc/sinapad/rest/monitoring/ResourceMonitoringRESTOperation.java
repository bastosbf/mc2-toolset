package br.lncc.sinapad.rest.monitoring;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import br.lncc.sinapad.core.data.ResourceData;
import br.lncc.sinapad.core.exception.MonitoringServiceException;
import br.lncc.sinapad.core.exception.UserNotAuthorizedException;
import br.lncc.sinapad.rest.RESTOperation;
import br.lncc.sinapad.rest.data.JobSubmissonResult;
import br.lncc.sinapad.rest.data.ListResourceResult;
import br.lncc.sinapad.rest.data.ResourceResult;
import br.lncc.sinapad.rest.data.StatusResult;
import br.lncc.sinapad.rest.utils.RESTResultCodes;
import br.lncc.sinapad.rest.utils.RESTUtils;

@Path("/resource-monitoring")
public class ResourceMonitoringRESTOperation extends RESTOperation {

	private static Logger logger = Logger.getLogger("resource-monitoring");

	/**
	 * 
	 * List all the resources available for the user.
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @return A result containing the list of the resources available for the
	 *         user and the code of the result. <br>
	 *         - {@link RESTResultCodes#USER_NOT_AUTHORIZED} if the user is not
	 *         valid; <br>
	 *         - {@link RESTResultCodes#USER_ERROR} if the uuid is null; <br>
	 *         - {@link RESTResultCodes#OK} if success; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/list")
	public ListResourceResult list(@FormParam("uuid") String uuid) {
		try {
			if (uuid == null || uuid.isEmpty()) {
				return new ListResourceResult(RESTResultCodes.USER_ERROR);
			}

			List<ResourceData> resources;
			resources = resourceMonitoringService.list(uuid);

			List<ResourceResult> result = RESTUtils.convertResourceDataListToResourceResultList(resources);
			ListResourceResult listResult = new ListResourceResult(RESTResultCodes.OK);
			listResult.setElements(result);
			return listResult;
		} catch (UserNotAuthorizedException e) {
			return new ListResourceResult(RESTResultCodes.USER_NOT_AUTHORIZED);
		} catch (MonitoringServiceException e) {
			RESTUtils.handleException(e, logger);
		}
		return new ListResourceResult(RESTResultCodes.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 
	 * Gets a resource information
	 * 
	 * @param uuid
	 *          Universally unique identifier of the user.
	 * @param resource
	 *          The name of the resource.
	 * @return A result containing the resource information and the code of the
	 *         result. <br>
	 *         - {@link RESTResultCodes#USER_NOT_AUTHORIZED} if the user is not
	 *         valid; <br>
	 *         - {@link RESTResultCodes#OK} if success; <br>
	 *         - {@link RESTResultCodes#USER_ERROR if the resource or the uuid is null; <br>
	 *         - {@link RESTResultCodes#FILE_DOES_NOT_EXIST  if the resource does not exist; <br>
	 *         - {@link RESTResultCodes#INTERNAL_SERVER_ERROR} if some error
	 *         occurs.
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/get")
	public ResourceResult get(@FormParam("uuid") String uuid, @FormParam("resource") String resource) {
		try {
			if ((uuid == null || uuid.isEmpty()) || (resource == null || resource.isEmpty())) {
				return new ResourceResult(RESTResultCodes.USER_ERROR);
			}
			
			ResourceData resourceData = resourceMonitoringService.get(uuid, resource);
			if(resourceData != null){
			ResourceResult result = RESTUtils.convertResourceDataToResourceResult(resourceData);
			result.setCode(RESTResultCodes.OK);
			return result;
			}
			return new ResourceResult(RESTResultCodes.FILE_DOES_NOT_EXIST);
		} catch (UserNotAuthorizedException e) {
			return new ResourceResult(RESTResultCodes.USER_NOT_AUTHORIZED);
		} catch (MonitoringServiceException e) {
			RESTUtils.handleException(e, logger);
		}
		return new ResourceResult(RESTResultCodes.INTERNAL_SERVER_ERROR);

	}

}
