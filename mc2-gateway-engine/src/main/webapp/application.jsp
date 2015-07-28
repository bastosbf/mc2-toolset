<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="net.tanesha.recaptcha.ReCaptcha"%>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<link rel="shortcut icon" href="images/favicon.ico" />
<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/sinapad.css">
<link rel="stylesheet" href="css/jumbotron-narrow.css">
<link rel="stylesheet" href="css/bootstrap-duallistbox.css">
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.bsFormAlerts.min.js"></script>
<script type="text/javascript" src="js/bootstrap.js"></script>
<script type="text/javascript" src="js/angular.min.js"></script>
<script type="text/javascript" src="js/navbar.js"></script>
<script type="text/javascript" src="js/jquery.bootstrap-duallistbox.js"></script>
<script type="text/javascript" src="js/sinapad.js"></script>
<title><s:text name="jsp.application.title" /></title>
<script type="text/javascript">
	function fillForm(key, value) {
		var el = document.getElementById(key);
		if (el != null) {
			if (el.tagName == "INPUT") {
				var type = el.type;
				if (type == "text") {
					el.value = value;
				} else if (type == "hidden") {
					//check if its a checkbox or a select with multiple choices
					var cb = document.getElementById("cb_" + key);
					var list = document.getElementById(key + "_list");
					var dual_list_l = document
							.getElementById("bootstrap-duallistbox-nonselected-list_" + key + "_multiple");
					var dual_list_r = document
							.getElementById("bootstrap-duallistbox-selected-list_" + key + "_multiple");
					var table = document.getElementById(key + "_table");
					if (cb != null) {
						var cb_value = document.getElementById("cb_value_" + key);
						cb_value.value = value;
						if (value == "true") {
							cb.checked = "checked";
						} else {
							cb.checked = "";
						}
						cb.onchange();
					} else if (list != null) {
						var regex = /\{(.*)\}/;
						if (regex.test(value)) {
							//gets only the content of {}
							var content = regex.exec(value)[1];
							var elements = content.split(",");
							list.options.length = 0;
							for (var i = 0; i < elements.length; i++) {
								list.options[i] = new Option(elements[i], elements[i], false, true);
							}
						}
					} else if (dual_list_l != null && dual_list_r != null) {
						var regex = /\{(.*)\}/;
						if (regex.test(value)) {
							//gets only the content of {}
							var content = regex.exec(value)[1];
							var elements = content.split(",");
							dual_list_r.options.length = 0;
							var count = 0;
							for (var i = 0; i < elements.length; i++) {
								for (var j = 0; j < dual_list_l.options.length; j++) {
									if (elements[i] == dual_list_l.options[j].label) {
										dual_list_r.options[count] = new Option(dual_list_l.options[j].label, dual_list_l.options[j].value, false, true);
										count++;
										dual_list_l.remove(dual_list_l.options[j].index);
									}
								}
							}
						}
					} else if (table != null) {
						var regex = /\{(.*)\}/;
						if (regex.test(value)) {
							var content = regex.exec(value)[1];
							var content = content.replace("},", "},,");
							var elements = content.split(",,");
							for (var i = 0; i < elements.length; i++) {
								if (i > 0) {
									var row = table.insertRow(table.rows.length - 1);
								}
								var element = elements[i];
								if (regex.test(element)) {
									var ccontent = regex.exec(element)[1];
									var celements = ccontent.split(",");
									for (var j = 0; j < celements.length; j++) {
										var cell = table.rows[(i + 1)].cells[j];
										if (cell == null) {
											var cell = row.insertCell(-1);
											var input = document.createElement('input');
											input.className = 'form-control';
											input.id = table.id + '_' + (table.rows.length - 3) + '_' + i;
											cell.appendChild(input);
										} else {
											var input = cell.getElementsByTagName('input')[0];
										}
										if (celements[j] != 'null') {
											input.value = celements[j];
										}
									}
								}
							}
						}
					}
				}
			} else if (el.tagName == "SELECT") {
				el.value = value;
				el.onchange();
			}
		}
	}

	function canChange() {
		var select = document.getElementById("select");
		if (select.value == '${session.version}') {
			$('#version_err').show();
			return false;
		}
		return true;
	}
</script>
</head>
<body>
  <div class="container">
    <%@ include file="jspf/header.jspf"%>
    <s:if test="%{error}">
      <div class="alert alert-danger alert-dismissable" id="app_err">
        <button type="button" id="app_err_close" class="close">&times;</button>
        <strong>
          <s:text name="jsp.application.submit.err" />
        </strong>
      </div>
      <script type="text/javascript">
							$('#app_err_close').on('click', function() {
								$('#app_err').hide();
							});
						</script>
    </s:if>
    <fieldset class="scheduler-border">
      <legend class="scheduler-border">${session.info}</legend>
      <div class="alert alert-danger alert-dismissable" id="version_err" style="display: none">
        <button type="button" id="version_close" class="close">&times;</button>
        <strong>
          <s:text name="jsp.application.version.already.selected" />
        </strong>
      </div>
      <script type="text/javascript">
							$('#version_close').on('click', function() {
								$('#version_err').hide();
							});
						</script>
      <form action="changeVersion">
        <label for="version">
          <s:text name="jsp.application.change.version.label" />
        </label>
        <div class="input-group">
          <select name="version" id="select" class="form-control">
            <s:if test="%{#application.allowMultipleVersions}">
              <s:iterator value="%{#session.versions}" var="v">
                <s:if test="%{#application.guestEnabled  && !#application.guestVersionList.contains('ALL') && #session.isGuest}">
                  <s:if test="%{#application.guestVersionList.contains(#v.version)}">
                    <s:if test="%{#session.version == #v.version}">
                      <option value="${v.version}" selected="selected">${v.version}</option>
                    </s:if>
                    <s:else>
                      <option value="${v.version}">${v.version}</option>
                    </s:else>
                  </s:if>
                </s:if>
                <s:else>
                  <s:if test="%{#session.version == #v.version}">
                    <option value="${v.version}" selected="selected">${v.version}</option>
                  </s:if>
                  <s:else>
                    <option value="${v.version}">${v.version}</option>
                  </s:else>
                </s:else>
              </s:iterator>
            </s:if>
            <s:else>
              <option value="${session.version}">${session.version}</option>
            </s:else>
          </select>
          <span class="input-group-btn">
            <button type="submit" class="btn btn-default" style="width: 80px" onclick="return canChange();">
              <s:text name="jsp.application.change.version.button" />
            </button>
          </span>
        </div>
      </form>
    </fieldset>
    <br />
    <div class="alert alert-danger alert-dismissable" style="display: none">
      <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
      <strong>
        <s:text name="jsp.application.parameters.warning" />
      </strong>
    </div>
    <form id="execute" name="execute" action="execute" method="post">
      <s:include value="generated/%{#session.app}/%{#session.version}/application.jsp" />
      <s:if test="%{#session.isGuest == false}">
        <s:if test="%{#application.allowResourceChoice}">
          <fieldset class="scheduler-border">
            <legend class="scheduler-border">
              <s:text name="jsp.application.resource.choice.label" />
            </legend>
            <select name="resource" id="resource" class="form-control">
              <option value="" selected="selected">
                <s:text name="jsp.application.resource.choice.default.option" />
              </option>
              <s:iterator value="%{#request.queues}" var="q">
                <option value="${q.name}">${q.name}-${q.numOfProc}
                  <s:text name="jsp.application.resource.queue.cores.msn.1" /> - ${q.numOfJobs}
                  <s:text name="jsp.application.resource.queue.cores.msn.2" />
                </option>
              </s:iterator>
            </select>
          </fieldset>
        </s:if>
      </s:if>
      <div class="modal-footer">
        <button id="executeButton" type="submit" class="btn btn-primary" onclick="return callValidate();">
          <s:text name="jsp.application.submit" />
        </button>
      </div>
    </form>
    <%@ include file="jspf/footer.jspf"%>
  </div>
  <s:iterator var="map" value="args">
    <script type="text/javascript">
					fillForm('${map.key}', '${map.value}');
				</script>
  </s:iterator>
  <script type="text/javascript">
			var select = document.getElementById("select");
			select.value = '${session.version}';
		</script>
  <script type="text/javascript">
			var task = '${task}';
			if (task != null && task != '') {
				post("listResults?task=" + task);
			}
		</script>
</body>
</html>