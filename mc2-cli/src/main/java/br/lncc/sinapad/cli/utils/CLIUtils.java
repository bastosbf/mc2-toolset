package br.lncc.sinapad.cli.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class CLIUtils {

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

	public static String retrieveFileNameFromAbsolutePath(String absolutePath) {
		if (absolutePath == null || absolutePath.equals("")) {
			return null;
		}
		String[] tokens = absolutePath.split("/");
		int size = tokens.length;

		return tokens[(size - 1)];
	}

	public static void handleException(Exception e, CLILogger logger) {
		if (logger == null) {
			logger = CLILogger.getCLILogger(CLIUtils.class);
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
