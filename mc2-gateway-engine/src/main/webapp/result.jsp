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
<title><s:text name="jsp.result.title" /></title>
<script type="text/javascript">
	function select(choice) {
		$("#jobId").val(choice.value);
	}
</script>
</head>
<body>
  <div class="container">
    <%@ include file="jspf/header.jspf"%>
    <form id="viewResultForm" action="viewResult" target="viewResultIFrame" method="POST">
      <div class="modal-body">
        <label for="jobId">
          <s:text name="jsp.result.identifier" />
        </label>
        <input type="text" class="form-control" name="jobId" id="jobId" value="${jobId}" placeholder="Job Id">
        <s:if test='%{#session.isGuest == false}'>
          <select id="viewResultSelect" multiple="multiple" class="form-control" onchange="select(this);">
            <s:iterator var="r" value="%{results}">
              <option value="<s:property value='jobId'/>">
                <s:property value="jobId" /> -
                <s:property value="status" />
              </option>
            </s:iterator>
          </select>
        </s:if>
        <span data-alertid="result"></span>
        <div class="modal-footer">
          <button id="viewResultButton" type="button" class="btn btn-primary" onclick="javascript:showLoading()">
            <s:text name="jsp.result.view" />
          </button>
          <s:if test='%{#session.isGuest == false}'>
            <button id="removeResultButton" type="button" class="btn btn-red">
              <s:text name="jsp.result.remove" />
            </button>
          </s:if>
        </div>
      </div>
    </form>
    <iframe id="viewResultIFrame" name="viewResultIFrame" width="700" height="500" frameBorder="0" style="display: none" onload="javascript:hideLoading()"></iframe>
    <%@ include file="jspf/footer.jspf"%>
  </div>
  <!-- Remove result -->
  <div id="removeModal" class="modal fade">
    <div class="modal-dialog">
      <div class="modal-content">
        <form id="removeForm" action="deleteResult" method="POST">
          <div class="modal-body">
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <input type="hidden" name="jobId" id="removeJobId" value="" />
            <p>
              <s:text name="jsp.result.delete.hist.confirm" />
            </p>
          </div>
          <div class="modal-footer">
            <button id="removeYesButton" type="button" class="btn btn-primary" onclick="javascript:showLoading()">
              <s:text name="jsp.result.yes" />
            </button>
            <button id="removeNoButton" type="button" class="btn btn-red">
              <s:text name="jsp.result.no" />
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
  <script type="text/javascript">
			function click() {
				$(document).trigger("clear-alert-id.result");
				var id = $("#jobId").val();
				if (id == "") {
					$(document).trigger("set-alert-id-result", [ {
					message : '<s:text name="jsp.result.select.job.id" />',
					priority : '<s:text name="jsp.result.error" />'
					} ]);
				} else {
					$("#viewResultIFrame").show();
					$("#viewResultForm").submit();
				}
			}
			$("#viewResultSelect").on("dblclick", click);
			$("#viewResultButton").on("click", click);
			
			$("#removeResultButton").on("click", function() {
				$(document).trigger("clear-alert-id.result");
				var id = $("#jobId").val();
				if (id == "") {
					$(document).trigger("set-alert-id-result", [ {
					message : '<s:text name="jsp.result.select.job.id" />',
					priority : '<s:text name="jsp.result.error" />'
					} ]);
				} else {
					showRemoveModal(id);
				}
			});
			
			$("#removeModal").on("show", function() {
				$("#removeModal a.btn").on("click", function(e) {
					$("#removeModal").modal('hide');
				});
			});
			
			$("#removeModal").on("hide", function() {
				$("#removeModal a.btn").off("click");
			});
			
			$("#removeModal").on("hidden", function() {
				$("#removeModal").remove();
			});
			
			$("#removeYesButton").on("click", function() {
				$("#removeForm").submit();
			});
			$("#removeNoButton").on("click", function() {
				$("#removeModal").modal('hide');
			});
			function showRemoveModal(jobId) {
				$("#removeJobId").val(jobId);
				$("#removeModal").modal({
				"backdrop" : "static",
				"keyboard" : true,
				"show" : true
				});
			}
		</script>
  <s:if test="%{jobId != null}">
    <script type="text/javascript">
					var id = $("#jobId").val();
					$("#viewResultIFrame").show();
					$("#viewResultForm").submit();
				</script>
  </s:if>
</body>
</html>
