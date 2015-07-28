package br.lncc.sinapad.portengin.gui.jsp.converter.element.impl;

import java.util.List;
import java.util.Properties;

import br.lncc.sinapad.core.application.representation.element.Table;
import br.lncc.sinapad.core.application.representation.element.Table.Column;
import br.lncc.sinapad.portengin.gui.jsp.converter.element.ElementConverter;

public class TableConverter implements ElementConverter<String, Table> {

	public String convert(Table e, Properties locale) {
		String str = "<div class=\"alert alert-danger alert-dismissable\" id=\"" + e.getName() + "_err\" style=\"display:none\">" + "<button type=\"button\" id=\"" + e.getName() + "_close\" class=\"close\">&times;</button>" + "<strong>You must have at least one row!</strong></div><script type=\"text/javascript\">$('#" + e.getName() + "_close').on('click', function () {$('#" + e.getName() + "_err').hide();});</script><div id=\"" + e.getName() + "_tip\" style=\"border-style: dotted; border-color: black; display: none;\"><h6>" + e.getTip(locale) + "</h6></div><div class=\"form-group\" id=\"" + e.getName() + "_form\"><label for=\"" + e.getName() + "\" id=\"" + e.getName() + "_label\">" + e.getLabel(locale);
		if (e.getTip(locale) != null && !e.getTip(locale).isEmpty()) {
			str += " <a href=\"javascript:void(0)\" onclick=\"javascript:showHideDiv('" + e.getName() + "_tip');\"><img src=\"images/question.gif\"></a>";
		}

		str += "</label><div><input type=\"hidden\" name=\"" + VARIABLE + "['" + e.getName() + "']\" id=\"" + e.getName() + "\"/><table id=\"" + e.getName() + "_table\" class=\"table\"><thead><tr>" + getTHead(e) + "</tr> </thead>" + getTableRows(e) + "<tr align=\"right\"><td colspan=\"2\"><button type=\"button\" class=\"btn btn-default\" id=\"input_button\" " + getAddEvent(e) + ">Add Row</button>" + "<button type=\"button\" class=\"btn btn-default\" id=\"input_button\"" + "onclick=\"var table = document.getElementById('" + e.getName() + "_table');if(table.rows.length > 3){table.deleteRow(table.rows.length -2);} else{$('#" + e.getName() + "_err').show();}\">Remove Last Row</button></td></tr></table></div></div>";

		return str;
	}

	private String getAddEvent(Table e) {
		List<Column> columns = e.getColumns();
		String str = "onclick=\"var table = document.getElementById('" + e.getName() + "_table');var row = table.insertRow(table.rows.length -1); " + "for(var i=0; i<" + columns.size() + "; i++){var cell = row.insertCell(-1); " + "var input = document.createElement('input'); input.className = 'form-control'; input.id='" + e.getName() + "_'+(table.rows.length-3)+'_'+i;cell.appendChild(input);}\"";
		return str;
	}

	private String getTableRows(Table e) {
		List<Column> columns = e.getColumns();
		String optional = "";
		if (e.getOptional()) {
			optional = "optional=\"true\"";
		}
		String str = "<tr>";
		for (int i = 0; i < columns.size(); i++) {
			Column col = columns.get(i);
			if (col.getOptional()) {
				optional = "optional=\"true\"";
			}
			str += "<td><input type=\"text\" class=\"form-control\" id=\"" + e.getName() + "_0_" + i + "\" " + optional + " /></td>";
		}
		str += "</tr>";
		return str;
	}

	private String getTHead(Table e) {
		List<Column> columns = e.getColumns();
		String str = "";
		for (Column c : columns) {
			str += "<td>" + c.getLabel() + "</td>";
		}
		return str;
	}
}
