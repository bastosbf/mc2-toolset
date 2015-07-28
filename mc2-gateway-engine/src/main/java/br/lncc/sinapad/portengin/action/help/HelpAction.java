package br.lncc.sinapad.portengin.action.help;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import br.lncc.sinapad.portengin.action.BaseAction;

public class HelpAction extends BaseAction {

	@Action(value = "/help", results = { @Result(name = "success", location = "/help.jsp") })
	public String help() {
		return SUCCESS;
	}

}
