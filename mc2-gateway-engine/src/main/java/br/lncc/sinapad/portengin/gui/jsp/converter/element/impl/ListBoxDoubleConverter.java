package br.lncc.sinapad.portengin.gui.jsp.converter.element.impl;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.lncc.sinapad.core.application.representation.element.ListBoxDouble;
import br.lncc.sinapad.portengin.gui.jsp.converter.element.ElementConverter;

public class ListBoxDoubleConverter implements ElementConverter<String, ListBoxDouble> {

	public String convert(ListBoxDouble e, Properties locale) {
		String str = "<div class=\"alert alert-danger alert-dismissable\" id=\"" + e.getName() + "_err\" style=\"display:none\">" + "<button type=\"button\" id=\"" + e.getName() + "_close\" class=\"close\">&times;</button>" + "<strong><s:text name=\"class.ListBoxDouble.minmax.msn\" /> (min: " + (e.getMin() != null ? e.getMin() : "unbound") + " max: " + (e.getMax() != null ? e.getMax() : "unbound") + ")</strong></div><script type=\"text/javascript\">$('#" + e.getName() + "_close').on('click', function () {$('#" + e.getName() + "_err').hide();});</script>" + "<div class=\"alert alert-danger alert-dismissable\" id=\"" + e.getName() + "_nan\" style=\"display:none\">" + "<button type=\"button\" id=\"" + e.getName() + "_nan_close\" class=\"close\">&times;</button>" + "<strong><s:text name=\"class.ListBoxDouble.nan.msn\" /></strong></div><script type=\"text/javascript\">$('#" + e.getName() + "_nan_close').on('click', function () {$('#" + e.getName() + "_nan').hide();});</script>" + "<div class=\"alert alert-danger alert-dismissable\" id=\"" + e.getName() + "_add_err\" style=\"display:none\">" + "<button type=\"button\" class=\"close\" id=\"" + e.getName() + "_add_err_close\">&times;</button>" + "<strong><s:text name=\"class.ListBoxDouble.add.msn\" /></strong></div><script type=\"text/javascript\">$('#" + e.getName() + "_add_err_close').on('click', function () {$('#" + e.getName() + "_add_err').hide();});</script>" + "<div class=\"alert alert-danger alert-dismissable\" id=\"" + e.getName() + "_remove_err\" style=\"display:none\">" + "<button type=\"button\" class=\"close\" id=\"" + e.getName() + "_remove_err_close\">&times;</button>" + "<strong><s:text name=\"class.ListBoxDouble.remove.msn\" /></strong></div><script type=\"text/javascript\">$('#" + e.getName() + "_remove_err_close').on('click', function () {$('#" + e.getName() + "_remove_err').hide();});</script>" + "<input type=\"hidden\" name=\"" + VARIABLE + "['" + e.getName() + "']\" id=\"" + e.getName() + "\"/><div id=\"" + e.getName() + "_tip\" style=\"border-style: dotted; border-color: black; display: none;\"><h6>" + e.getTip(locale) + "</h6></div><div class=\"form-group\" id=\"" + e.getName() + "_form\"><label for=\"" + e.getName() + "\" id=\"" + e.getName() + "_label\">" + e.getLabel(locale);
		if (e.getTip(locale) != null && !e.getTip(locale).isEmpty()) {
			str += " <a href=\"javascript:void(0)\" onclick=\"javascript:showHideDiv('" + e.getName() + "_tip');\"><img src=\"images/question.gif\"></a>";
		}
		str += "</label><input type=\"text\" class=\"form-control\" id=\"" + e.getName() + "_input\" placeholder=\"" + "\" onkeypress=\"var e = event || evt; var charCode = e.which || e.keyCode; if (charCode > 31 && charCode != 46 && charCode != 45 && (charCode < 48 || charCode > 57)) return false; return true;\"" + " onkeyup=\"var e = event || evt;if(e.keyCode == 110 || e.keyCode == 109 || e.keyCode == 190 || e.keyCode == 189){if(this.value.length == 1){return true;}}var min = " + e.getMin() + "; var max = " + e.getMax() + "; var value = this.value; if(!isNaN(value) && (value >= min || min == null) && (value <= max || max == null)) {$('#" + e.getName() + "_err').hide();return true; } $('#" + e.getName() + "_err').show(); this.value=''; return false;\"><div class=\"input-group\"><select multiple=\"multiple\" class=\"form-control\" name=\"" + e.getName() + "\" id=\"" + e.getName() + "_list\">" + getDefaultOptions(e) + "</select><span class=\"input-group-btn\"><button type=\"button\" id=\"" + e.getName() + "_add\" class=\"btn btn-default\" style=\"width: 80px\" onclick=\"var max = " + e.getSize() + ";var listbox = document.getElementById('" + e.getName() + "_list');var input= document.getElementById('" + e.getName() + "_input');var value = input.value; if(value == ''){$('#" + e.getName() + "_nan').show();return false;}var size = listbox.options.length;if(max != null && size >= max) {$('#" + e.getName() + "_add_err').show();} else {listbox.options[size] = new Option(value, value, false, true);input.value = '';}\" /><s:text name=\"class.ListBoxDouble.add.button\" /></button>" + "<br/><button type=\"button\" id=\"" + e.getName() + "_remove\" class=\"btn btn-default\" style=\"width: 80px\" onclick=\"var listbox = document.getElementById('" + e.getName() + "_list');var selected = listbox.selectedIndex;if(selected == -1) {$('#" + e.getName() + "_remove_err').show();} else {listbox.remove(selected)}\"/><s:text name=\"class.ListBoxDouble.remove.button\" /></button>" + "</span></div></div>";
		return str;
	}

	private String getDefaultOptions(ListBoxDouble l) {
		String options = "";
		Pattern p = Pattern.compile("\\{(.*)\\}");
		Matcher m = p.matcher(l.getValue());
		if (m.matches()) {
			String value = m.group(1);
			String[] values = value.split(",");
			for (String v : values) {
				options += "<option value=\"" + v + "\">" + v + "</option>";
			}
		}
		return options;

	}

}
