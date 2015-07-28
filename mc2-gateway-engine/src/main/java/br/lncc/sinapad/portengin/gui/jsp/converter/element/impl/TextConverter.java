package br.lncc.sinapad.portengin.gui.jsp.converter.element.impl;

import java.util.Properties;

import br.lncc.sinapad.core.application.representation.element.Text;
import br.lncc.sinapad.portengin.gui.jsp.converter.element.ElementConverter;
import br.lncc.sinapad.portengin.gui.jsp.converter.utils.EventUtils;

public class TextConverter implements ElementConverter<String, Text> {

	public String convert(Text e, Properties locale) {
		String str = "<div id=\"" + e.getName() + "_tip\" style=\"border-style: dotted; border-color: black; display: none;\"><h6>" + e.getTip(locale) + "</h6></div><div class=\"form-group\" id=\"" + e.getName() + "_form\"><label for=\"" + e.getName() + "\" id=\"" + e.getName() + "_label\">" + e.getLabel(locale);
		if (e.getTip(locale) != null && !e.getTip(locale).isEmpty()) {
			str += " <a href=\"javascript:void(0)\" onclick=\"javascript:showHideDiv('" + e.getName() + "_tip');\"><img src=\"images/question.gif\"></a>";
		}
		str += "</label>" + "<input type=\"text\" class=\"form-control\" name=\"" + VARIABLE + "['" + e.getName() + "']\" id=\"" + e.getName() + "\" placeholder=\"" + "\" onchange=\"" + EventUtils.getEvents(e) + "\" value=\"" + e.getValue() + "\" /></div>";
		return str;
	}
}
