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
<title><s:text name="jsp.project_file.title" /></title>
</head>
<body>
  <div class="container">
    <div id="loading-indicator" class="loading" style="display: none">
      <img src="images/miscellaneous/loading.gif" />
    </div>
    <s:if test="%{error}">
      <div class="alert alert-danger">
        <strong>
          <s:text name="jsp.project_file.alert.could.not.move.for.selected.directory" />
        </strong>
      </div>
    </s:if>
    <s:elseif test="%{moved}">
      <div class="alert alert-success">
        <strong>
          <s:text name="jsp.project_file.alert.file.moved" />
        </strong>
      </div>
    </s:elseif>
    <s:elseif test="%{exist}">
      <div class="alert alert-danger">
        <strong>
          <s:text name="jsp.project_file.alert.file.exists" />
        </strong>
      </div>
    </s:elseif>
    <s:elseif test="%{copied}">
      <div class="alert alert-success">
        <strong>
          <s:text name="jsp.project_file.alert.file.copied" />
        </strong>
      </div>
    </s:elseif>
    <s:else>
      <ol class="breadcrumb">
        <li>
          <a href="listProjectRepository?shared=${shared}&move=${move}&from=${from}" onclick="javascript:showLoading();">${session.USER.projectName}</a>
        </li>
        <s:iterator var="p" value="parents">
          <li>
            <a href="listProjectRepository?shared=${shared}&move=${move}&from=${from}&filePath=${p.absolutePath}" onclick="javascript:showLoading();">${p.name}</a>
          </li>
        </s:iterator>
        <li class="active">${fileName}</li>
        <div class="dropdown" style="display: inline">
          <button id="menu" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
            <span class="caret"></span>
          </button>
          <s:if test="%{move}">
            <ul class="dropdown-menu" role="menu" aria-labelledby="menu">
              <li>
                <a href='moveFile?from=${from}&to=${filePath}' onclick="javascript:showLoading();">
                  <s:text name="jsp.project_file.select" />
                </a>
              </li>
            </ul>
          </s:if>
          <s:if test="%{shared}">
            <ul class="dropdown-menu" role="menu" aria-labelledby="menu">
              <li>
                <a href='downloadSharedFile?from=${from}&to=${filePath}' onclick="javascript:showLoading();">
                  <s:text name="jsp.project_file.select" />
                </a>
              </li>
            </ul>
          </s:if>
        </div>
      </ol>
      <div class="row">
        <div align="right" class="col-md-12">
          <button type="button" class="btn btn-default" onclick="javascript:showCreateDirModal('${filePath}')">
            <s:text name="jsp.file_manager.menu.create" />
          </button>
        </div>
      </div>
      <table class="table">
        <tr>
          <td align="left">
            <b>
              <s:text name="jsp.project_file.file" />
            </b>
          </td>
          <td align="left">
            <b>
              <s:text name="jsp.project_file.opions" />
            </b>
          </td>
          <td align="center">
            <b>
              <s:text name="jsp.project_file.modification.date" />
            </b>
          </td>
          <td align="right">
            <b>
              <s:text name="jsp.project_file.size" />
            </b>
          </td>
        </tr>
        <s:iterator var="f" value="files">
          <s:if test="%{directory}">
            <tr>
              <td align="left">
                <a href="listProjectRepository?shared=${shared}&move=${move}&from=${from}&filePath=${f.absolutePath}" onclick="javascript:showLoading();">
                  <img src="images/files/folder.png" width="30px" />
                  <s:property value="name" />
                </a>
              </td>
              <td align="left">
                <div class="dropdown">
                  <button id="menu" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                    <span class="caret"></span>
                  </button>
                  <s:if test="%{move}">
                    <ul class="dropdown-menu" role="menu" aria-labelledby="menu">
                      <li>
                        <a href='moveFile?from=${from}&to=${f.absolutePath}' onclick="javascript:showLoading();">
                          <s:text name="jsp.project_file.select" />
                        </a>
                      </li>
                    </ul>
                  </s:if>
                  <s:if test="%{shared}">
                    <ul class="dropdown-menu" role="menu" aria-labelledby="menu">
                      <li>
                        <a href='downloadSharedFile?from=${from}&to=${f.absolutePath}' onclick="javascript:showLoading();">
                          <s:text name="jsp.project_file.select" />
                        </a>
                      </li>
                    </ul>
                  </s:if>
                </div>
              </td>
              <td align="center">
                <s:property value="modificationDate" />
              </td>
              <td align="right">
                <s:property value="size" />
              </td>
            </tr>
          </s:if>
        </s:iterator>
      </table>
    </s:else>
  </div>
  <!-- Create new Directory -->
  <div id="createDirModal" class="modal fade">
    <div class="modal-dialog">
      <div class="modal-content">
        <form id="createDirForm" action="createNewFolder" method="POST">
          <div class="modal-body">
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <input type="hidden" name="directory" id="directory" value="true" />
            <input type="hidden" name="filePath" id="createDirFilePath" value="" />
            <input type="hidden" name="fromProject" id="fromProject" value="true" />
            <input type="hidden" name="from" id="from" value="${from}" />
            <input type="hidden" name="move" id="move" value="${move}" />
            <input type="hidden" name="shared" id="shared" value="${shared}" />
            <label for="fileName">
              <s:text name="jsp.project_file.folder.name" />
            </label>
            <input type="text" class="form-control" name="fileName" id="fileName" placeholder="<s:text name="jsp.project_file.instruction.type.folder.name" />">
            <span data-alertid="create"></span>
          </div>
          <div class="modal-footer">
            <button id="createDirButton" type="button" class="btn btn-primary">
              <s:text name="jsp.project_file.create" />
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
  <script type="text/javascript">
			$("#createDirModal").on("show", function() {
				$("#createDirModal a.btn").on("click", function(e) {
					$("#createDirModal").modal('hide');
				});
			});
			
			$("#createDirModal").on("hide", function() {
				$("#createDirModal a.btn").off("click");
			});
			
			$("#createDirModal").on("hidden", function() {
				$("#createDirModal").remove();
			});
			
			$("#createDirButton").on("click", function() {
				var fileName = $("#fileName").val();
				if (fileName == "") {
					$(document).trigger("set-alert-id-create", [ {
					message : '<s:text name="jsp.project_file.enter.directory.name" />',
					priority : '<s:text name="jsp.project_file.error" />'
					} ]);
				} else {
					$("#createDirForm").submit();
				}
			});
			function showCreateDirModal(filePath) {
				$("#createDirFilePath").val(filePath);
				$(document).trigger("clear-alert-id.create");
				$("#createDirModal").modal({
				"backdrop" : "static",
				"keyboard" : true,
				"show" : true
				});
			}
		</script>
</body>
</html>