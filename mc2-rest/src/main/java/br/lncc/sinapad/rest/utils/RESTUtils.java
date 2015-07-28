package br.lncc.sinapad.rest.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import br.lncc.sinapad.core.data.ApplicationData;
import br.lncc.sinapad.core.data.ApplicationData.VersionData;
import br.lncc.sinapad.core.data.FileData;
import br.lncc.sinapad.core.data.ResourceData;
import br.lncc.sinapad.core.data.ResourceData.NodeData;
import br.lncc.sinapad.core.data.ResultData;
import br.lncc.sinapad.rest.data.ApplicationResult;
import br.lncc.sinapad.rest.data.ApplicationResult.VersionResult;
import br.lncc.sinapad.rest.data.FileResult;
import br.lncc.sinapad.rest.data.ResourceResult;
import br.lncc.sinapad.rest.data.ResourceResult.NodeResult;
import br.lncc.sinapad.rest.data.StatusResult;

public class RESTUtils {

	public static void handleException(Exception e, Logger logger) {
		if (logger == null) {
			logger = Logger.getLogger(RESTUtils.class);
		}
		String message = "";
		Class<?> c = e.getClass();
		Field[] fields = c.getDeclaredFields();
		for (Field f : fields) {
			if ("message".equals(f.getName())) {
				try {
					message = String.valueOf(f.get(e));
				} catch (IllegalArgumentException e1) {
					logger.error("Could not get message field value", e1);
				} catch (IllegalAccessException e1) {
					logger.error("Could not get message field value", e1);
				}
			}
		}
		logger.error(message, e);
	}

	public static FileResult convertFileDataToFileResult(FileData data) {
		if (data == null) {
			return null;
		}
		FileResult fileResult = new FileResult();
		fileResult.setName(data.getName());
		FileData parent = data.getParent();
		if(parent != null) {
			fileResult.setParentPath(parent.getAbsolutePath());
		}
		fileResult.setAbsolutePath(data.getAbsolutePath());
		fileResult.setDirectory(data.isDirectory());
		if (data.hasChildren()) {
			for (FileData child : data.getChildren()) {
				FileResult c = convertFileDataToFileResult(child);
				fileResult.addChild(c);
			}
		}
		return fileResult;
	}

	public static ApplicationResult convertApplicationDataToApplicationResult(ApplicationData data) {

		ApplicationResult result = new ApplicationResult();
		result.setInfo(data.getInfo());
		result.setName(data.getName());

		List<VersionResult> vResult = new ArrayList<VersionResult>();
		for (VersionData vData : data.getVersions()) {
			VersionResult version = new VersionResult();
			version.setVersion(vData.getVersion());
			version.setInfo(vData.getInfo());
			version.setQueues(vData.getQueues());
			vResult.add(version);
		}
		result.setVersions(vResult);

		return result;
	}

	public static List<ApplicationResult> convertApplicationDataListToApplicationResultList(List<ApplicationData> list) {

		List<ApplicationResult> results = new ArrayList<ApplicationResult>();
		for (ApplicationData l : list) {
			ApplicationResult result = convertApplicationDataToApplicationResult(l);
			results.add(result);
		}
		return results;
	}

	public static ResourceResult convertResourceDataToResourceResult(ResourceData data) {

		ResourceResult result = new ResourceResult();
		result.setName(data.getName());
		result.setNumOfJobs(data.getNumOfJobs());
		result.setNumOfProc(data.getNumOfProc());
		// result.setProperties(data.getProperties());

		List<NodeResult> vResult = new ArrayList<NodeResult>();
		for (NodeData nData : data.getNodes()) {
			NodeResult node = new NodeResult();
			node.setAvailable(nData.isAvailable());
			node.setNumOfJobs(nData.getNumOfJobs());
			node.setNumOfProc(nData.getNumOfProc());
			// node.setProperties(nData.getProperties());
			vResult.add(node);
		}
		result.setNodes(vResult);

		return result;
	}

	public static List<ResourceResult> convertResourceDataListToResourceResultList(List<ResourceData> list) {

		List<ResourceResult> results = new ArrayList<ResourceResult>();
		for (ResourceData l : list) {
			ResourceResult result = convertResourceDataToResourceResult(l);
			results.add(result);
		}
		return results;
	}

	public static StatusResult convertResultDataToStatusResult(ResultData data) {

		StatusResult result = new StatusResult();
		result.setJobId(data.getJobId());
		result.setResource(data.getResource());
		result.setStartDate(data.getStartDate());
		result.setEndDate(data.getEndDate());
		result.setStatus(data.getStatus());

		return result;
	}

	public static List<StatusResult> convertListResultDataToListStatusResult(List<ResultData> list) {

		List<StatusResult> results = new ArrayList<StatusResult>();
		for (ResultData l : list) {
			StatusResult result = convertResultDataToStatusResult(l);
			results.add(result);
		}
		return results;
	}

	public static String[] convertAbsolutePathToArray(String absolutePath, boolean ignoreProjectName, boolean ignoreFileName) {
		if (absolutePath == null || absolutePath.equals("")) {
			return null;
		}
		String[] tokens = absolutePath.split("/");
		int size = tokens.length;
		List<String> parents = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			if (ignoreProjectName && i == 0) {
				continue;
			}
			if (ignoreFileName && i == (size - 1)) {
				continue;
			}
			parents.add(tokens[i]);
		}
		return parents.toArray(new String[] {});
	}

	public static String[] getParentsFromAbsoluteFilePath(String project, String filePath) {
		String[] tokens = filePath.split("/");
		boolean contains = tokens[0].equals(project);
		int size = tokens.length;
		String[] parents = null;
		if (!contains) {
			parents = new String[size - 1];
			for (int i = 0; i < size - 1; i++) {
				parents[i] = tokens[i];
			}
			return parents;
		}
		// ignores the project name and file name
		if (size > 1) {
			parents = new String[size - 2];
			for (int i = 1; i < size - 1; i++) {
				parents[i - 1] = tokens[i];
			}
		}
		return parents;
	}

	public static String retrieveFileNameFromAbsolutePath(String absolutePath) {
		String[] tokens = absolutePath.split("/");
		int size = tokens.length;

		return tokens[(size - 1)];
	}

	public static Map<String, String> parseParameters(String parameters) {
		if (parameters == null) {
			return null;
		}
		Map<String, String> params = new HashMap<String, String>();
		String[] parts = parameters.split(";");
		for (String p : parts) {
			String[] data = p.split("::");
			if (data.length == 2) {
				params.put(data[0], data[1]);
			}
		}
		return params;
	}

	public static void convertInputStreamToFile(InputStream is, File file) throws IOException {
		if (is != null && file != null) {
			if (!file.exists()) {
				file.createNewFile();
			}
			OutputStream out = new FileOutputStream(file);

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = is.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			is.close();
			out.flush();
			out.close();
		}
	}
}
