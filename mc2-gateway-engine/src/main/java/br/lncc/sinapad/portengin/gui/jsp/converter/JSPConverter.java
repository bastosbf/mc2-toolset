package br.lncc.sinapad.portengin.gui.jsp.converter;

import java.util.List;
import java.util.Properties;

import br.lncc.sinapad.core.application.representation.Event;
import br.lncc.sinapad.core.application.representation.Group;
import br.lncc.sinapad.core.application.representation.Parameter;
import br.lncc.sinapad.core.application.representation.Representation;
import br.lncc.sinapad.core.application.representation.element.ListBoxDouble;
import br.lncc.sinapad.core.application.representation.element.ListBoxInteger;
import br.lncc.sinapad.core.application.representation.element.OutputFile;
import br.lncc.sinapad.core.application.representation.element.Select;
import br.lncc.sinapad.core.application.representation.element.Table;
import br.lncc.sinapad.portengin.config.ModulesConfig.BulkJobsModule;
import br.lncc.sinapad.portengin.config.ModulesConfig.BulkJobsModule.BulkJobsValue;
import br.lncc.sinapad.portengin.gui.jsp.converter.element.ElementConverter;
import br.lncc.sinapad.portengin.gui.jsp.converter.factory.JSPConverterFactory;

public abstract class JSPConverter {

	public static String convert(Representation source, BulkJobsModule bulkJobs, Properties locale) {
		List<Event> events = source.getEvents();
		List<Group> groups = source.getGroups();

		String str = "<%@ taglib prefix='s' uri='/struts-tags'%><script type=\"text/javascript\">function validate() {var characters = new Array(';', ':', '=', ' ', '#', '|');var valid = true;for(var i = 0; i < arguments.length; i++) {var el = document.getElementById(arguments[i]);if(el == null){continue;}el.style.backgroundColor='';var el_list = document.getElementById(arguments[i]+'_list');if(el_list != null){el_list.style.backgroundColor='';el.value='';if(el_list.length > 0){el.value='{';for(var j=0;j<el_list.length-1;j++){el.value+=el_list.options[j].value+','}el.value+=el_list.options[el_list.length-1].value +'}'}} var el_table = document.getElementById(arguments[i] + '_table');if (el_table != null) {el.value = '{';for (var r = 1, row; row = el_table.rows[r]; r++) {if(el_table.rows.length -1 == r) {continue;}el.value += '{';for (var c = 0, col; col = row.cells[c]; c++) {var table_cell = col.getElementsByTagName('input')[0];if(table_cell.value != '') {el.value += table_cell.value + ',';} else {el.value += 'null,';}if (table_cell.value == '') {var optional = table_cell.getAttribute('optional');if(optional != 'true') {table_cell.style.backgroundColor = '#FA8072';valid = false;}} else {table_cell.style.backgroundColor = '';}}el.value = el.value.substring(0, el.value.length - 1);el.value += '},';}el.value = el.value.substring(0, el.value.length - 1);el.value += '}';}var el_dual_list = document.getElementById('bootstrap-duallistbox-selected-list_'+arguments[i]+'_multiple');if (el_dual_list != null) {var el_dual_list_data = document.getElementById(arguments[i] + '_data');el_dual_list.style.backgroundColor='';el.value = '';if (el_dual_list.length > 0) {el.value = '{';for (var j = 0; j < el_dual_list.length - 1; j++) {el.value += el_dual_list.options[j].label + ','}el.value += el_dual_list.options[el_dual_list.length - 1].label + '}'; }el.value = el_dual_list_data.value + ':' + el.value;}if((el.value == null || el.value == '') && el.innerHTML == '' && el.style.visibility != 'hidden' && !el.disabled) {if(el.tagName == 'DIV'){var link=document.getElementById(el.id + '_link');link.style.backgroundColor='#FA8072';}else{el.style.backgroundColor = '#FA8072';if(el_list != null){el_list.style.backgroundColor = '#FA8072';}}valid = false;$('body').scrollTop(0);}if (arguments[i] == 'email' && valid) {var value = el.value;var reg = /^([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})$/;if (value == null || value == '' || reg.test(value) == false) {$('#email_invalid').show();return false;}}}if(!valid) {$('#parameter_required').show();}else {var els = document.body.getElementsByTagName('input');out: for ( var i = 0; i < els.length; i++) {var el = els[i];if (el.type == 'text') {if (el.value != null && el.value != '' && el.id != 'recaptcha_response_field') {for(var j = 0; j < characters.length; j++) {var c = characters[j];if (el.id != 'radio_label' && el.id != 'checkbox_label' && el.value.indexOf(c) != -1) {el.style.backgroundColor = '#FA8072';valid = false;$('body').scrollTop(0);	$('#input_invalid').show();break out;}}}}}}return valid;}</script>";
		String elements = "";
		str += "<div class=\"alert alert-danger alert-dismissable\" id=\"parameter_required\" style=\"display:none\">" + "<button type=\"button\" id=\"required_close\" class=\"close\">&times;</button>" + "<strong><s:text name=\"class.Algorithm.required.msn\" /></strong></div>" + "<script type=\"text/javascript\">$('#required_close').on('click', function () {$('#parameter_required').hide();});</script>";
		str += "<div class=\"alert alert-danger alert-dismissable\" id=\"input_invalid\" style=\"display:none\">" + "<button type=\"button\" id=\"input_close\" class=\"close\">&times;</button>" + "<strong><s:text name=\"class.Algorithm.invalid.input\" /></strong></div>" + "<script type=\"text/javascript\">$('#input_close').on('click', function () {$('#input_invalid').hide();});</script>";
		for (Group g : groups) {
			str += "<s:if test='%{#session.isGuest == false || " + !isOutputGroup(g) + "}'>" + "<fieldset class=\"scheduler-border\"><legend class=\"scheduler-border\">" + g.getLabel() + "</legend></s:if>";
			List<Parameter> parameters = g.getParameters();
			for (Parameter p : parameters) {
				if (!p.getOptional() || p instanceof ListBoxInteger || p instanceof ListBoxDouble || p instanceof Select || p instanceof Table) {
					elements += "'" + p.getName() + "',";
				}
				ElementConverter converter = JSPConverterFactory.createParameterConverter(p.getClass());
				if (converter != null) {
					str += converter.convert(p, locale);
				}
			}
			str += "<s:if test='%{#session.isGuest == false || " + !isOutputGroup(g) + "}'>" + "</fieldset></s:if>";
		}

		if (bulkJobs != null && bulkJobs.isEnabled()) {
			BulkJobsValue beginValue = bulkJobs.getBeginValue();
			BulkJobsValue endValue = bulkJobs.getEndValue();
			BulkJobsValue stepValue = bulkJobs.getStepValue();

			boolean createFieldSet = !beginValue.isStatic() || !endValue.isStatic() || !stepValue.isStatic();
			if (createFieldSet) {
				str += "<fieldset class=\"scheduler-border\"><legend class=\"scheduler-border\"><s:text name=\"class.Application.bulk.jobs.label\" /></legend><div class=\"form-group\">";
			}
			if (!beginValue.isStatic()) {
				str += "<div class=\"alert alert-danger alert-dismissable\" id=\"bulk_begin_err\" style=\"display:none\"><button type=\"button\" id=\"bulk_begin_close\" class=\"close\" >&times;</button><strong><s:text name=\"class.JSPConverter.begin.minmax.msn\" /> (min: " + (beginValue.getMin() != null ? beginValue.getMin() : "unbound") + " max: " + (beginValue.getMax() != null ? beginValue.getMax() : "unbound") + ")</strong></div><script type=\"text/javascript\">$('#bulk_begin_close').on('click', function () {$('#bulk_begin_err').hide();});</script>";
				str += "<label for=\"beginValue\"><s:text name=\"" + beginValue.getLabel() + "\" /></label>" + "<input type=\"text\" class=\"form-control\" name=\"beginValue\" id=\"beginValue\" value=\"" + beginValue.getValue() + "\" onblur=\"var e = event || evt;if(this.value.length == 0){return true;}if(e.keyCode == 109 || e.keyCode == 189){if(this.value.length == 1){$('#bulk_begin_err').hide();return true;}}var min = " + beginValue.getMin() + "; var max = " + beginValue.getMax() + "; var value = this.value; if(!isNaN(value) && (value >= min || min == null) && (value <= max || max == null)) {$('#bulk_begin_err').hide();return true; } $('#bulk_begin_err').show(); this.value='" + (beginValue.getValue() != null ? beginValue.getValue() : "") + "'; return false;\"/>";
			} else {
				str += "<input type=\"hidden\" class=\"form-control\" name=\"beginValue\" id=\"beginValue\" value=\"" + beginValue.getValue() + "\"/>";
			}
			if (!endValue.isStatic()) {
				str += "<div class=\"alert alert-danger alert-dismissable\" id=\"bulk_end_err\" style=\"display:none\"><button type=\"button\" id=\"bulk_end_close\" class=\"close\" >&times;</button><strong><s:text name=\"class.JSPConverter.end.minmax.msn\" /> (min: " + (endValue.getMin() != null ? endValue.getMin() : "unbound") + " max: " + (endValue.getMax() != null ? endValue.getMax() : "unbound") + ")</strong></div><script type=\"text/javascript\">$('#bulk_end_close').on('click', function () {$('#bulk_end_err').hide();});</script>";
				str += "<label for=\"endValue\"><s:text name=\"" + endValue.getLabel() + "\" /></label>" + "<input type=\"text\" class=\"form-control\" name=\"endValue\" id=\"endValue\" value=\"" + endValue.getValue() + "\" onblur=\"var e = event || evt;if(this.value.length == 0){return true;}if(e.keyCode == 109 || e.keyCode == 189){if(this.value.length == 1){$('#bulk_end_err').hide();return true;}}var min = " + endValue.getMin() + "; var max = " + endValue.getMax() + "; var value = this.value; if(!isNaN(value) && (value >= min || min == null) && (value <= max || max == null)) {$('#bulk_end_err').hide();return true; } $('#bulk_end_err').show(); this.value='" + (endValue.getValue() != null ? endValue.getValue() : "") + "'; return false;\" />";
			} else {
				str += "<input type=\"hidden\" class=\"form-control\" name=\"endValue\" id=\"endValue\" value=\"" + endValue.getValue() + "\"/>";
			}
			if (!stepValue.isStatic()) {
				str += "<div class=\"alert alert-danger alert-dismissable\" id=\"bulk_step_err\" style=\"display:none\"><button type=\"button\" id=\"bulk_step_close\" class=\"close\" >&times;</button><strong><s:text name=\"class.JSPConverter.step.minmax.msn\" /> (min: " + (stepValue.getMin() != null ? stepValue.getMin() : "unbound") + " max: " + (stepValue.getMax() != null ? stepValue.getMax() : "unbound") + ")</strong></div><script type=\"text/javascript\">$('#bulk_step_close').on('click', function () {$('#bulk_step_err').hide();});</script>";
				str += "<label for=\"stepValue\"><s:text name=\"" + stepValue.getLabel() + "\" /></label>" + "<input type=\"text\" class=\"form-control\" name=\"stepValue\" id=\"stepValue\" value=\"" + stepValue.getValue() + "\"  onblur=\"var e = event || evt;if(this.value.length == 0){return true;}if(e.keyCode == 109 || e.keyCode == 189){if(this.value.length == 1){$('#bulk_step_err').hide();return true;}}var min = " + stepValue.getMin() + "; var max = " + stepValue.getMax() + "; var value = this.value; if(!isNaN(value) && (value >= min || min == null) && (value <= max || max == null)) {$('#bulk_step_err').hide();return true; } $('#bulk_step_err').show(); this.value='" + (stepValue.getValue() != null ? stepValue.getValue() : "") + "'; return false;\" />";
			} else {
				str += "<input type=\"hidden\" class=\"form-control\" name=\"stepValue\" id=\"stepValue\" value=\"" + stepValue.getValue() + "\"/>";
			}
			if (createFieldSet) {
				str += "</div></fieldset>";
			}
		}

		elements += "'email'";
		str += "<div class=\"alert alert-danger alert-dismissable\" id=\"email_invalid\" style=\"display:none\">" + "<button type=\"button\" id=\"email_close\" class=\"close\">&times;</button>" + "<strong><s:text name=\"class.Algorithm.guest.email.invalid\" /></strong></div>" + "<script type=\"text/javascript\">$('#email_close').on('click', function () {$('#email_invalid').hide();});</script>";

		str += "<s:if test='%{#session.isVOMS}'>" + "<fieldset class=\"scheduler-border\"><legend class=\"scheduler-border\"><s:text name=\"class.Algorithm.voms.label\" /></legend>" + "<div class=\"form-group\"><label for=\"email\"><s:text name=\"class.Algorithm.email.label\" /></label><input type=\"text\" class=\"form-control\" name=\"email\" id=\"email\" />" + "</div></fieldset></s:if>";
		str += "<s:if test='%{#session.isGuest}'>" + "<fieldset class=\"scheduler-border\"><legend class=\"scheduler-border\"><s:text name=\"class.Algorithm.guest.label\" /></legend>" + "<div class=\"form-group\"><label for=\"email\"><s:text name=\"class.Algorithm.email.label\" /></label><input type=\"text\" class=\"form-control\" name=\"email\" id=\"email\" />" + "<div align=\"center\"><%net.tanesha.recaptcha.ReCaptcha c = net.tanesha.recaptcha.ReCaptchaFactory.newSecureReCaptcha(\"6Lfyf8cSAAAAAAMb_Wm8dQa_p5-Eh11K_gpNg59j\", \"6Lfyf8cSAAAAAJ9qXZpQAvs05FxsTjIYVHFh4mcS\", false);((net.tanesha.recaptcha.ReCaptchaImpl) c).setRecaptchaServer(\"https://www.google.com/recaptcha/api\");out.print(c.createRecaptchaHtml(null, null));%></div>" + "</div></fieldset></s:if>";

		str += "<script type=\"text/javascript\">function callValidate() {return validate(" + elements + ");}</script>";

		ElementConverter eventConverter = JSPConverterFactory.createParameterConverter(Event.class);

		for (Event e : events) {
			str += "<script type=\"text/javascript\">";
			str += eventConverter.convert(e, locale);
			str += "</script>";
		}
		return str;
	}

	private static boolean isOutputGroup(Group group) {
		List<Parameter> parameters = group.getParameters();
		if (!parameters.isEmpty()) {
			for (Parameter p : parameters) {
				if (!(p instanceof OutputFile)) {
					return false;
				}
			}
		}
		return true;
	}

}
