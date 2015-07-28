package br.lncc.sinapad.portengin.viewer;

import br.lncc.sinapad.portengin.viewer.impl.DefaultFileViewer;
import br.lncc.sinapad.portengin.viewer.impl.ImageFileViewer;

public class FileViewerFactory {

	public static FileViewer createFileViewer(String mime) {
		if (mime.startsWith("image/")) {
			return new ImageFileViewer();
		}
		return new DefaultFileViewer();
	}

}
