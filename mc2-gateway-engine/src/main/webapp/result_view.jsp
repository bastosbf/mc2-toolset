<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<style type="text/css">
legend.scheduler-border {
	width: inherit; /* Or auto */
	padding: 0 10px; /* To give a bit of padding on the left and right */
	border-bottom: none;
}

fieldset.scheduler-border {
	border: 1px groove #ddd !important;
	padding: 0 1.4em 1.4em 1.4em !important;
	margin: 0 0 1.5em 0 !important;
	-webkit-box-shadow: 0px 0px 0px 0px #000;
	box-shadow: 0px 0px 0px 0px #000;
}

legend.scheduler-border {
	font-size: 1.2em !important;
	font-weight: bold !important;
	text-align: left !important;
}
</style>
<link rel="shortcut icon" href="images/favicon.ico" />
<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/sinapad.css">
<link rel="stylesheet" href="css/jumbotron-narrow.css">
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.bsFormAlerts.min.js"></script>
<script type="text/javascript" src="js/bootstrap.js"></script>
<script type="text/javascript" src="js/angular.min.js"></script>
<script type="text/javascript" src="js/navbar.js"></script>
<script type="text/javascript" src="js/sinapad.js"></script>
<script type="text/javascript">
	function resubmit(divname) {
		var form = document.createElement("form");
		form.setAttribute("method", "post");
		form.setAttribute("action", "changeApp");

		var div = document.getElementById(divname);
		var elements = div.getElementsByTagName("input");
		for (var i = 0; i < elements.length; i++) {
			var hiddenField = document.createElement("input");
			hiddenField.setAttribute("type", "hidden");
			hiddenField.setAttribute("name", "args['"+elements[i].name+"']");
			hiddenField.setAttribute("value", elements[i].value);			

			form.appendChild(hiddenField);
		}

		var name = Math.floor(Math.random() * 11);

		var target = null;
		if (window.opener != null) {
			target = window.opener;
		} else {
			target = window.parent;
		}

		target.name = name
		form.target = name;

		document.body.appendChild(form);
		form.submit();
	}

	function reload() {
		window.location = "viewResult?jobId=${jobId}";
	}
</script>
<title><s:text name="jsp.result_view.title" /></title>
</head>
<body>
  <div class="container">
    <div id="iframe-loading-indicator" class="loading" style="display: none; z-index: 1">
      <img src="images/miscellaneous/loading.gif" />
    </div>
    <s:if test="%{cancelled}">
      <div class="alert alert-info alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        <strong>
          <s:text name="jsp.result_view.cancelled" />
        </strong>
      </div>
    </s:if>
    <div style="float: left">
      <h3>${result.jobId}</h3>
    </div>
    <div style="float: right">
      <h3>${result.status}</h3>
    </div>
    <div class="btn-group btn-group-justified">
      <s:if test="%{result.status.toString() != 'FAILED' && result.status.toString() != 'DONE' && result.status.toString() != 'CANCELLED'}">
        <a role="button" class="btn btn-default" onclick="javascript:reload();">
          <s:text name="jsp.result_view.refresh" />
        </a>
      </s:if>
      <a role="button" class="btn btn-default" onclick="javascript:resubmit('values');">
        <s:text name="jsp.result_view.resubmit" />
      </a>
      <s:if test="%{result.status.toString() != 'FAILED' && result.status.toString() != 'DONE' && result.status.toString() != 'CANCELLED'}">
        <a role="button" class="btn btn-default" onclick="javascript:showCancelModal('${jobId}');">
          <s:text name="jsp.result_view.cancel" />
        </a>
      </s:if>
    </div>
    <div align="right">
      <a href="javascript:void(0)" onclick='javascript:startDownload("popupDownload?action=downloadLog&jobId=${jobId}");'>
        <s:text name="jsp.result_view.download.full.log" />
      </a>
    </div>
    <div class="tabbable">
      <ul class="nav nav-tabs">
        <li class="active">
          <a href="#files" data-toggle="tab">
            <s:text name="jsp.result_view.files.link" />
          </a>
        </li>
        <li>
          <a href="#data" data-toggle="tab">
            <s:text name="jsp.result_view.data.link" />
          </a>
        </li>
        <li>
          <a href="#values" data-toggle="tab">
            <s:text name="jsp.result_view.values.link" />
          </a>
        </li>
      </ul>
      <div class="tab-content">
        <div id="files" class="tab-pane active">
          <h4>Files</h4>
          <ol class="breadcrumb">
            <li class="active">
              <a href="viewResult?jobId=${result.jobId}" onclick="javascript:showLoading();">HISTORY</a>
            </li>
            <s:iterator var="p" value="parents">
              <li>
                <a href="viewResult?jobId=${result.jobId}&absolutePath=${p.absolutePath}" onclick="javascript:showLoading();">${p.name}</a>
              </li>
            </s:iterator>
            <li class="active">${fileName}</li>
          </ol>
          <table class="table">
            <tr>
              <td align="left">
                <b>
                  <s:text name="jsp.result_view.file" />
                </b>
              </td>
              <td align="left">
                <b>
                  <s:text name="jsp.result_view.options" />
                </b>
              </td>
              <td align="center">
                <b>
                  <s:text name="jsp.result_view.modification.date" />
                </b>
              </td>
              <td align="right">
                <b>
                  <s:text name="jsp.result_view.size" />
                </b>
              </td>
            </tr>
            <s:iterator var="f" value="files">
              <tr>
                <td align="left">
                  <s:if test="%{directory}">
                    <a href="viewResult?jobId=${result.jobId}&absolutePath=${f.absolutePath}" onclick="javascript:showLoading();">
                      <img src="images/files/folder.png" width="30px" />
                      <s:property value="name" />
                    </a>
                  </s:if>
                  <s:else>
                    <img src="images/files/file.png" width="30px" />
                    <s:property value="name" />
                  </s:else>
                </td>
                <td align="left">
                  <div class="dropdown">
                    <button id="menu" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                      <span class="caret"></span>
                    </button>
                    <ul class="dropdown-menu" role="menu" aria-labelledby="menu">
                      <s:if test="%{directory}">
                        <li>
                          <a href="javascript:void(0)"
                            onclick='javascript:startDownload("popupDownload?action=downloadResultFile&jobId=${result.jobId}&directory=true&absolutePath=<s:property value="absolutePath"/>")'
                          >
                            <s:text name="jsp.result_view.download" />
                          </a>
                        </li>
                      </s:if>
                      <s:else>
                        <li>
                          <a href="javascript:void(0)" onclick='javascript:popup("viewResultFile?jobId=${result.jobId}&absolutePath=<s:property value="absolutePath"/>", 800, 600);'>
                            <s:text name="jsp.result_view.view" />
                          </a>
                        </li>
                        <li>
                          <a href="javascript:void(0)"
                            onclick='javascript:startDownload("popupDownload?action=downloadResultFile&jobId=${result.jobId}&absolutePath=<s:property value="absolutePath"/>")'
                          >
                            <s:text name="jsp.result_view.download" />
                          </a>
                        </li>
                      </s:else>
                    </ul>
                  </div>
                </td>
                <td align="center">
                  <s:property value="modificationDate" />
                </td>
                <td align="right">
                  <s:property value="size" />
                </td>
              </tr>
            </s:iterator>
          </table>
        </div>
        <div id="data" class="tab-pane">
          <h4>
            <s:text name="jsp.result_view.data" />
          </h4>
          <fieldset class="scheduler-border">
            <legend class="scheduler-border">
              <s:text name="jsp.result_view.data" />
            </legend>
            <label for="queue">
              <s:text name="jsp.result_view.data.queue" />
            </label>
            <input type='text' class='form-control' name='queue' id='queue' value='${result.resource}' disabled='disabled'>
            <label for="startDate">
              <s:text name="jsp.result_view.data.startDate" />
            </label>
            <input type='text' class='form-control' name='startDate' id='startDate' value='${result.startDate}' disabled='disabled'>
            <label for="endDate">
              <s:text name="jsp.result_view.data.endDate" />
            </label>
            <input type='text' class='form-control' name='endDate' id='endDate' value='${result.endDate}' disabled='disabled'>
          </fieldset>
        </div>
        <div id="values" class="tab-pane">
          <h4>
            <s:text name="jsp.result_view.values" />
          </h4>
          <fieldset class="scheduler-border">
            <legend class="scheduler-border">
              <s:text name="jsp.result_view.values" />
            </legend>
            <s:iterator value="result.parameters">
              <label>
                <s:property value="key" />
              </label>
              <input type="text" class='form-control' name='<s:property value="key" />' id='<s:property value="key" />' value='<s:property value="value" />' disabled='disabled'>
              <br>
            </s:iterator>
          </fieldset>
        </div>
      </div>
    </div>
  </div>
  <!-- Remove file -->
  <div id="cancelModal" class="modal fade">
    <div class="modal-dialog">
      <div class="modal-content">
        <form id="cancelForm" action="cancelJob" method="POST">
          <div class="modal-body">
            <div id="cancel-loading-indicator" class="loading" style="display: none; z-index: 1">
              <img src="images/miscellaneous/loading.gif" />
            </div>
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <input type="hidden" name="jobId" id="jobId" value="" />
            <p>
              <s:text name="jsp.result_view.question.sure.cancel.job" />
            </p>
          </div>
          <div class="modal-footer">
            <button id="cancelYesButton" type="button" class="btn btn-primary" onclick="javascript:showLoading('cancel-loading-indicator')">
              <s:text name="jsp.result_view.yes" />
            </button>
            <button id="cancelNoButton" type="button" class="btn btn-red">
              <s:text name="jsp.result_view.no" />
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
  <script type="text/javascript">
		$("#cancelModal").on("show", function() {
			$("#cancelModal a.btn").on("click", function(e) {
				$("#cancelModal").modal('hide');
			});
		});

		$("#cancelModal").on("hide", function() {
			$("#cancelModal a.btn").off("click");
		});

		$("#cancelModal").on("hidden", function() {
			$("#cancelModal").remove();
		});

		$("#cancelYesButton").on("click", function() {
			$("#cancelForm").submit();
		});
		$("#cancelNoButton").on("click", function() {
			$("#cancelModal").modal('hide');
		});
		function showCancelModal(jobId) {
			$("#jobId").val(jobId);
			$("#cancelModal").modal({
				"backdrop" : "static",
				"keyboard" : true,
				"show" : true
			});
		}
	</script>
</body>
</html>