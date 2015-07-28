package br.lncc.sinapad.portengin.gui.jsp.converter.element.impl;

import java.util.Properties;

import br.lncc.sinapad.core.application.representation.element.TextDouble;
import br.lncc.sinapad.portengin.gui.jsp.converter.element.ElementConverter;
import br.lncc.sinapad.portengin.gui.jsp.converter.utils.EventUtils;

public class TextDoubleConverter implements ElementConverter<String, TextDouble> {

	public String convert(TextDouble e, Properties locale) {
		String str = "<div class=\"alert alert-danger alert-dismissable\" id=\"" + e.getName() + "_err\" style=\"display:none\">" + "<button type=\"button\" id=\"" + e.getName() + "_close\" class=\"close\">&times;</button>" + "<strong><s:text name=\"class.TextDouble.minmax.msn\" /> (min: " + (e.getMin() != null ? e.getMin() : "unbound") + " max: " + (e.getMax() != null ? e.getMax() : "unbound") + ")</strong></div><script type=\"text/javascript\">$('#" + e.getName() + "_close').on('click', function () {$('#" + e.getName() + "_err').hide();});</script>" + "<div id=\"" + e.getName() + "_tip\" style=\"border-style: dotted; border-color: black; display: none;\"><h6>" + e.getTip(locale) + "</h6></div><div class=\"form-group\" id=\"" + e.getName() + "_form\"><label for=\"" + e.getName() + "\" id=\"" + e.getName() + "_label\">" + e.getLabel(locale);
		if (e.getTip(locale) != null && !e.getTip(locale).isEmpty()) {
			str += " <a href=\"javascript:void(0)\" onclick=\"javascript:showHideDiv('" + e.getName() + "_tip');\"><img src=\"images/question.gif\"></a>";
		}
		str += "</label>" + "<input type=\"text\" class=\"form-control\" name=\"" + VARIABLE + "['" + e.getName() + "']\" id=\"" + e.getName() + "\" placeholder=\"" + "\" onchange=\"" + EventUtils.getEvents(e) + "\" onkeypress=\"var e = event || evt; var charCode = e.which || e.keyCode; if (charCode > 31 && charCode != 46 && charCode != 45 && (charCode < 48 || charCode > 57)) return false; return true;\"" + " onblur=\"var e = event || evt;if(this.value.length == 0){return true;}if(e.keyCode == 110 || e.keyCode == 109 || e.keyCode == 190 || e.keyCode == 189){if(this.value.length == 1){$('#" + e.getName() + "_err').hide();return true;}}var min = " + e.getMin() + "; var max = " + e.getMax() + "; var value = this.value; if(!isNaN(value) && (value >= min || min == null) && (value <= max || max == null)) {$('#" + e.getName() + "_err').hide();return true; } $('#" + e.getName() + "_err').show(); this.value = '" + (e.getValue() != null ? e.getValue() : "") + "'; return false;\"" + " value=\"" + e.getValue() + "\" /></div>";
		return str;
	}
}
