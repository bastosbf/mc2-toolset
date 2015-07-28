package br.lncc.sinapad.portengin.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public abstract class PortEnginUtils {

	public static InputStream convertStringToStream(String str) {
		if (str != null) {
			return new ByteArrayInputStream(str.getBytes());
		}
		return null;
	}

	public static String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {
			return "";
		}
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

	public static void convertStringToFile(String str, File file) throws IOException {
		if (str != null && file != null) {
			PrintWriter pw = new PrintWriter(file);
			pw.print(str);
			pw.flush();
			pw.close();
		}
	}

	public static String[] convertAbsolutePathToArray(String absolutePath, boolean ignoreProjectName, boolean ignoreFileName) {
		if (absolutePath == null || absolutePath.equals("")) {
			return null;
		}
		String[] tokens = absolutePath.split("/");
		int size = tokens.length;
		List<String> arr = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			if (ignoreProjectName && i == 0) {
				continue;
			}
			if (ignoreFileName && i == (size - 1)) {
				continue;
			}
			arr.add(tokens[i]);
		}
		if (arr.isEmpty()) {
			return null;
		}
		return arr.toArray(new String[] {});
	}

	public static String convertArrayToAbsolutePath(String[] arr) {
		String path = "";
		if (arr != null) {
			for (int i = 0; i < arr.length; i++) {
				path += arr[i] + "/";
			}
		}
		return path;
	}

	public static String retrieveFileNameFromAbsolutePath(String absolutePath) {
		if (absolutePath == null || absolutePath.equals("")) {
			return null;
		}
		String[] tokens = absolutePath.split("/");
		int size = tokens.length;

		return tokens[(size - 1)];
	}

	public static String retrieveProjectNameFromAbsolutePath(String absolutePath) {
		if (absolutePath == null || absolutePath.equals("")) {
			return null;
		}
		String[] tokens = absolutePath.split("/");
		return tokens[0];
	}

	public static void handleException(Exception e, Logger logger) {
		if (logger == null) {
			logger = Logger.getLogger(PortEnginUtils.class);
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
}
