package br.lncc.sinapad.portengin.viewer.impl;

import br.lncc.sinapad.portengin.viewer.FileViewer;

public class ImageFileViewer implements FileViewer {

	@Override
	public String createView(String relativePath) {
		return "<img src=\"" + relativePath + "\" />";
	}

}
