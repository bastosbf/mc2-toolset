package br.lncc.sinapad.portengin.viewer.impl;

import br.lncc.sinapad.portengin.viewer.FileViewer;

public class DefaultFileViewer implements FileViewer {
	@Override
	public String createView(String relativePath) {
		return "<textarea id=\"content\" rows=\"30\" class=\"form-control\"></textarea><script>$(function(){$(\"#content\").load(\"" + relativePath + "\"); });</script>";
	}

}
