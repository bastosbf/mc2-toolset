package br.lncc.sinapad.portengin.gui.jsp.converter.element.impl;

import java.util.List;
import java.util.Properties;

import br.lncc.sinapad.core.application.representation.element.Select;
import br.lncc.sinapad.core.application.representation.element.Select.Item;
import br.lncc.sinapad.portengin.gui.jsp.converter.element.ElementConverter;
import br.lncc.sinapad.portengin.gui.jsp.converter.utils.EventUtils;

public class SelectConverter implements ElementConverter<String, Select> {

	public String convert(Select e, Properties locale) {
		String str = "<div id=\"" + e.getName() + "_tip\" style=\"border-style: dotted; border-color: black; display: none;\"><h6>" + e.getTip(locale) + "</h6></div><div class=\"form-group\" id=\"" + e.getName() + "_form\"><label for=\"" + e.getName() + "\" id=\"" + e.getName() + "_label\">" + e.getLabel(locale);
		if (e.getTip(locale) != null && !e.getTip(locale).isEmpty()) {
			str += " <a href=\"javascript:void(0)\" onclick=\"javascript:showHideDiv('" + e.getName() + "_tip');\"><img src=\"images/question.gif\"></a>";
		}
		str += "</label>";
		if (e.getMultiple()) {
			str += getMultipleSelect(e);
		} else {
			str += getSelect(e);
		}
		str += "</div>";
		return str;
	}

	private String getSelect(Select s) {
		return "<select class=\"form-control\" name=\"" + VARIABLE + "['" + s.getName() + "']\" id=\"" + s.getName() + "\" onchange=\"" + EventUtils.getEvents(s) + "\" >" + getSelectItemsAsString(s) + "</select>";
	}

	private String getMultipleSelect(Select s) {
		return "<input type=\"hidden\" id=\"" + s.getName() + "_data\" value=\"" + getMultipleOptionsData(s) + "\"/><input type=\"hidden\" name=\"" + VARIABLE + "['" + s.getName() + "']\" id=\"" + s.getName() + "\" /> <select multiple=\"multiple\" size=\"10\" name=\"" + s.getName() + "_multiple\"class=\"multipleselect form-control\">" + getSelectItemsAsString(s) + "</select><script>var multiple = $('.multipleselect').bootstrapDualListbox({" + "nonselectedlistlabel: 'Non-selected', selectedlistlabel: 'Selected', preserveselectiononmove: 'moved', moveonselect: false, initialfilterfrom: '' });</script>";
	}

	private String getMultipleOptionsData(Select s) {
		List<Item> items = s.getItems();
		String str = "{";
		for (Item i : items) {
			str += "{id=" + i.getId() + "/label=" + i.getLabel() + "/value=" + i.getValue() + "/description=" + i.getTip() + "/visible=true},";
		}
		str = str.substring(0, str.length() - 1);
		str += "}";
		return str;
	}

	private String getSelectItemsAsString(Select s) {
		List<Item> items = s.getItems();
		String str = "";
		for (Item i : items) {
			String selected = "";
			if (i.getValue().equals(s.getValue())) {
				selected = "selected=\"selected\"";
			}
			str += "<option value=\"" + i.getValue() + "\" " + selected + ">" + i.getLabel() + "</option>";
		}
		return str;
	}
}
