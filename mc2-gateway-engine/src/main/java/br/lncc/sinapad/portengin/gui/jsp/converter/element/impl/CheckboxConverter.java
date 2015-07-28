package br.lncc.sinapad.portengin.gui.jsp.converter.element.impl;

import java.util.Properties;

import br.lncc.sinapad.core.application.representation.element.Checkbox;
import br.lncc.sinapad.portengin.gui.jsp.converter.element.ElementConverter;
import br.lncc.sinapad.portengin.gui.jsp.converter.utils.EventUtils;

public class CheckboxConverter implements ElementConverter<String, Checkbox> {

	public String convert(Checkbox e, Properties locale) {

		String str = "<div class=\"form-group\" id=\"" + e.getName() + "_form\"><div class=\"col-md-12\"><label class=\"checkbox-inline\" for=\"" + e.getName() + "\"><input type=\"checkbox\" name=\"cb_" + e.getName() + "\" id=\"cb_" + e.getName() + "\" onchange=\"" + getCheckboxEvent(e) + EventUtils.getEvents(e) + "\" " + isChecked(e) + "><input type=\"hidden\" id=\"cb_value_" + e.getName() + "\" name=\"" + VARIABLE + "['" + e.getName() + "']\" value=\"" + getValue(e) + "\" /> " + e.getLabel() + "</label></div></div> <input type=\"hidden\" id=\"" + e.getName() + "\" value=\"" + getCheckboxValue(e) + "\"/>";
		return str;
	}

	private String getCheckboxValue(Checkbox c) {
		if (c.getTrueValue().equals(c.getValue())) {
			return c.getTrueValue();
		}
		return c.getFalseValue();
	}

	private String isChecked(Checkbox c) {
		if (c.getTrueValue().equals(c.getValue())) {
			return "checked=\"checked\"";
		}
		return "";
	}

	private String getValue(Checkbox c) {
		if (c.getTrueValue().equals(c.getValue())) {
			return "true";
		}
		return "false";
	}

	private String getCheckboxEvent(Checkbox c) {
		return "var cb = document.getElementById('cb_" + c.getName() + "');" + "var hidden = document.getElementById('" + c.getName() + "');" + "if(cb.checked) {hidden.value = '" + c.getTrueValue() + "'} else {hidden.value = '" + c.getFalseValue() + "'} var cb_value = document.getElementById('cb_value_" + c.getName() + "'); if(cb.checked){cb_value.value = 'true';} else {cb_value.value = 'false';}";
	}
}
