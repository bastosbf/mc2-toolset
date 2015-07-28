<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.bsFormAlerts.min.js"></script>
<script type="text/javascript" src="js/bootstrap.js"></script>
<script type="text/javascript" src="js/angular.min.js"></script>
<script type="text/javascript" src="js/navbar.js"></script>
<script type="text/javascript" src="js/angular.min.js"></script>
<script type="text/javascript" src="js/navbar.js"></script>
<script type="text/javascript" src="js/sinapad.js"></script>
<title><s:text name="jsp.applications.title" /></title>
<script type="text/javascript">
	function select(choice) {
		window.location = "getAppInfo?app=" + choice.value;
	}
</script>
</head>
<body>
  <div class="container">
    <%@ include file="jspf/header.jspf"%>
    <s:if test="%{#application.multipleApp}">
      <form id="applicationsForm" action="changeApp" method="POST">
        <div class="modal-body">
          <label for="application">
            <s:text name="jsp.applications.choose.application.to.run" />
          </label>
          <select name="app" multiple="multiple" class="form-control" onchange="select(this);">
            <s:iterator var="e" value="%{#application.apps}">
              <s:if test="%{#session.app == name}">
                <option value="<s:property value='name'/>:<s:property value='defaultVersion'/>:<s:property value='info'/>" selected="selected"><s:property value="name" /></option>
              </s:if>
              <s:else>
                <option value="<s:property value='name'/>:<s:property value='defaultVersion'/>:<s:property value='info'/>"><s:property value="name" /></option>
              </s:else>
            </s:iterator>
          </select>
          <span data-alertid="applications"></span>
        </div>
        <div class="modal-footer">
          <button id="applicationsButton" type="button" class="btn btn-primary">
            <s:text name="jsp.applications.choose.btn" />
          </button>
        </div>
      </form>
      <s:if test="%{info != '' && info != null}">
        <div style="border-style: dotted; border-color: black; display: block;">${info}</div>
      </s:if>
    </s:if>
    <s:else>
      <%
      	response.sendRedirect("changeApp");
      %>
    </s:else>
    <%@ include file="jspf/footer.jspf"%>
  </div>
  <script type="text/javascript">
			$("#applicationsButton").on("click", function() {
				$(document).trigger("clear-alert-id.applications");
				var id = $("#id").val();
				if (id == "") {
					$(document).trigger("set-alert-id-applications", [ {
					message : "<s:text name='jsp.applications.warning' />",
					priority : "<s:text name='jsp.applications.error' />"
					} ]);
				} else {
					$("#applicationsForm").submit();
				}
			});
		</script>
</body>
</html>
