package br.lncc.sinapad.portengin.gui.jsp.converter.element.impl;

import java.util.Properties;

import br.lncc.sinapad.core.application.representation.element.File;
import br.lncc.sinapad.core.application.representation.element.OutputFile;
import br.lncc.sinapad.portengin.gui.jsp.converter.element.ElementConverter;

public class OutputFileConverter implements ElementConverter<String, OutputFile> {
	public String convert(OutputFile e, Properties locale) {
		String file = "";
		if (!File.Category.DIR.equals(e.getCategory())) {
			file = "/output";
		}
		String str = "<s:if test='%{#session.isGuest == false}'><div id=\"" + e.getName() + "_tip\" style=\"border-style: dotted; border-color: black; display: none;\"><h6>" + e.getTip(locale) + "</h6></div><div class=\"form-group\" id=\"" + e.getName() + "_form\"><label for=\"" + e.getName() + "\" id=\"" + e.getName() + "_label\">" + e.getLabel(locale);
		if (e.getTip(locale) != null && !e.getTip(locale).isEmpty()) {
			str += " <a href=\"javascript:void(0)\" onclick=\"javascript:showHideDiv('" + e.getName() + "_tip');\"><img src=\"images/question.gif\"></a>";
		}
		str += "</label> <div class=\"input-group\">" + "<input type=\"text\" class=\"form-control\" name=\"" + VARIABLE + "['" + e.getName() + "']\" id=\"" + e.getName() + "\" placeholder=\"" + "\" onclick=\"" + getOutputFileEvent(e) + "\"" + " onchange=\"showHideDiv('" + e.getName() + "_file');\" readonly=\"readonly\"/><span class=\"input-group-btn\"><button type=\"button\" class=\"btn btn-default\" id=\"" + e.getName() + "_button\" onclick=\"" + getOutputFileEvent(e) + "\"><s:text name='class.File.choose.label'/></button></span></div><div id=\"" + e.getName() + "_file\" style=\"display: none\"><iframe id=\"" + e.getName() + "_iframe\" frameBorder=\"0\" style=\"width: 100%; height: 400px\"></iframe></div></div></s:if><s:else><input type=\"hidden\" name=\"" + VARIABLE + "['" + e.getName() + "']\" id=\"" + e.getName() + "\" value=\"${hash}" + file + "\" /></s:else>";
		return str;
	}

	protected String getOutputFileEvent(OutputFile o) {
		String str = "showHideDiv('" + o.getName() + "_file'); var link = 'listFiles?target=" + o.getName() + getCategoryAsString(o) + "';";
		str += "document.getElementById('" + o.getName() + "_iframe').src = link;";
		return str;

	}

	protected String getCategoryAsString(OutputFile o) {
		if (File.Category.DIR.equals(o.getCategory())) {
			return "&onlyDir=true";
		}
		return "";
	}

}
