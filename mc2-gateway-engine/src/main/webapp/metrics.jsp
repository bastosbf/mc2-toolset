<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<link rel="shortcut icon" href="images/favicon.ico" />
<link rel="stylesheet" href="css/sinapad.css">
<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/jumbotron-narrow.css">
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.bsFormAlerts.min.js"></script>
<script type="text/javascript" src="js/bootstrap.js"></script>
<script type="text/javascript" src="js/angular.min.js"></script>
<script type="text/javascript" src="js/navbar.js"></script>
<script type="text/javascript" src="js/sinapad.js"></script>
<title><s:text name="jsp.metrics.title" />
</title>
<script type="text/javascript">
	function popup_form(formId, mylink, width, height) {
		var form = document.getElementById(formId);
		var type = form.type.value;
		var year = document.getElementById("year");
		var month = document.getElementById("month");
		var href;
		if (typeof (mylink) == 'string') {
			href = mylink;
		} else {
			href = mylink.href;
		}
		
		href = href + '?type=' + type;
		
		if (year != null) {
			href += "&year=" + year.value;
		}
		
		if (month != null) {
			href += "&month=" + month.value;
		}
		
		return popup(href, width, height);
	}

	function load(select) {
		var value = select.value;
		if (value == 'class.MetricsAction.yearly') {
			hideCombos();
		} else if (value == 'class.MetricsAction.monthly') {
			showYearCombo();
		} else if (value == 'class.MetricsAction.daily') {
			showMonthCombo();
		}
	}
	function hideCombos() {
		var year = document.getElementById("year");
		var month = document.getElementById("month");
		year.style.display = "none";
		year.style.disabled = "true";
		month.style.display = "none";
		month.style.disabled = "true";
	}

	function showYearCombo() {
		var year = document.getElementById("year");
		year.style.display = "";
		year.style.disabled = "";
	}

	function showMonthCombo() {
		showYearCombo();
		var month = document.getElementById("month");
		month.style.display = "";
		month.style.disabled = "";
	}
</script>
</head>
<body>
  <div class="container">
    <%@ include file="jspf/header.jspf"%>
    <s:form id="metricsForm">
      <select name="type" id="type" class="form-control" onchange="load(this);">
        <s:iterator var="t" value="%{types}">
          <option value="${t}">
            <s:text name="%{t}" />
          </option>
        </s:iterator>
      </select>
      <br />
      <s:doubleselect list="metrics" id="year" name="year" listKey="year" listValue="year" cssClass="form-control" doubleList="months" doubleId="month" doubleName="month" doubleCssClass="form-control"
        theme="simple"
      />
      <br />
      <input type="button" class="form-control" value="<s:text name='jsp.metrics.submit'/>" onclick="popup_form('metricsForm', 'viewMetricsChart', 600, 500)" />
    </s:form>
    <%@ include file="jspf/footer.jspf"%>
  </div>
  <script type="text/javascript">
			hideCombos();
		</script>
</body>
</html>