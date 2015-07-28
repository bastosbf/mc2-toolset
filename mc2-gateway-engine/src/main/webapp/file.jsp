<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
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
<title><s:text name="jsp.file.title" /></title>
<script type="text/javascript">
	function choose(target, value) {
		var input = null;
		if (self.opener != null) {
			input = self.opener.document.getElementById(target);
		} else {
			input = window.parent.document.getElementById(target);
		}
		if (input != null) {
			//will remove the project name from the value of the path
			var index = value.indexOf("/") + 1;
			value = value.substring(index, value.lenght);
			input.value = value;
			if (self.opener == null) {
				var button = window.parent.document.getElementById(target + "_button");
				button.onclick();
			}
		}
	}
</script>
</head>
<body>
  <div class="container">
    <div id="iframe-loading-indicator" class="loading" style="display: none; z-index: 1">
      <img src="images/miscellaneous/loading.gif" />
    </div>
    <ol class="breadcrumb">
      <li>
        <a href="listFiles?target=${target}&onlyDir=${onlyDir}" onclick="javascript:showLoading();">${application.project}</a>
      </li>
      <s:iterator var="p" value="parents">
        <li>
          <a href="listFiles?target=${target}&onlyDir=${onlyDir}&absolutePath=${p.absolutePath}" onclick="javascript:showLoading();">${p.name}</a>
        </li>
      </s:iterator>
      <li class="active">${fileName}</li>
      <s:if test="%{onlyDir}">
        <s:if test="%{fileName != null}">
          <div class="dropdown" style="display: inline">
            <button id="menu" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
              <span class="caret"></span>
            </button>
            <ul class="dropdown-menu" role="menu" aria-labelledby="menu">
              <li>
                <a href="javascript:void(0)" onclick='javascript:choose("${target}", "${absolutePath}")'>
                  <s:text name="jsp.file.select" />
                </a>
              </li>
            </ul>
          </div>
        </s:if>
      </s:if>
    </ol>
    <div class="row">
      <div align="right" class="col-md-12">
        <button type="button" class="btn btn-default" onclick="javascript:showCreateDirModal('${absolutePath}')">
          <s:text name="jsp.file.create.dir" />
        </button>
        <button type="button" class="btn btn-default" onclick="javascript:showUploadModal('${absolutePath}')">
          <s:text name="jsp.file.upload" />
        </button>
      </div>
    </div>
    <table class="table">
      <tr>
        <td align="left">
          <b>
            <s:text name="jsp.file.file" />
          </b>
        </td>
        <td align="left">
          <b>
            <s:text name="jsp.file.options" />
          </b>
        </td>
        <td align="center">
          <b>
            <s:text name="jsp.file.modification.date" />
          </b>
        </td>
        <td align="right">
          <b>
            <s:text name="jsp.file.size" />
          </b>
        </td>
      </tr>
      <s:iterator var="f" value="files">
        <s:if test="%{onlyDir && directory == false}">
          <!-- will not show -->
        </s:if>
        <s:else>
          <tr>
            <td align="left">
              <s:if test="%{directory}">
                <a href="listFiles?target=${target}&onlyDir=${onlyDir}&absolutePath=${f.absolutePath}" onclick="javascript:showLoading();">
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
                <s:if test="%{onlyDir == false && directory}">
                </s:if>
                <s:else>
                  <button id="menu" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                    <span class="caret"></span>
                  </button>
                  <ul class="dropdown-menu" role="menu" aria-labelledby="menu">
                    <li>
                      <a href="javascript:void(0)" onclick='javascript:choose("${target}", "${f.absolutePath}")'>
                        <s:text name="jsp.file.select" />
                      </a>
                    </li>
                  </ul>
                </s:else>
              </div>
            </td>
            <td align="center">
              <s:property value="modificationDate" />
            </td>
            <td align="right">
              <s:property value="size" />
            </td>
          </tr>
        </s:else>
      </s:iterator>
    </table>
  </div>
  <!-- Create new Directory -->
  <div id="createDirModal" class="modal fade">
    <div class="modal-dialog">
      <div class="modal-content">
        <form id="createDirForm" action="createNewFolder" method="POST">
          <div class="modal-body">
            <div id="create-dir-loading-indicator" class="loading" style="display: none; z-index: 1">
              <img src="images/miscellaneous/loading.gif" />
            </div>
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <input type="hidden" name="absolutePath" id="createDirAbsolutePath" value="" />
            <input type="hidden" name="fromFile" id="fromFile" value="true" />
            <input type="hidden" name="onlyDir" id="onlyDir" value="${onlyDir}" />
            <input type="hidden" name="target" id="target" value="${target}" />
            <label for="fileName">
              <s:text name="jsp.file.folder.name" />
            </label>
            <input type="text" class="form-control" name="fileName" id="createDirfileName" placeholder="<s:text name="jsp.file.instruction.type.folder.name" />" autofocus>
            <span data-alertid="create"></span>
          </div>
          <div class="modal-footer">
            <button id="createDirButton" type="button" class="btn btn-primary" onclick="javascript:showLoading('create-dir-loading-indicator');">
              <s:text name="jsp.file.create" />
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
  <!-- Upload new File -->
  <div id="uploadModal" class="modal fade">
    <div class="modal-dialog">
      <div class="modal-content">
        <form id="uploadForm" action="uploadFile" enctype="multipart/form-data" method="POST">
          <div class="modal-body">
            <div id="upload-loading-indicator" class="loading" style="display: none; z-index: 1">
              <img src="images/miscellaneous/loading.gif" />
            </div>
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <input type="hidden" name="absolutePath" id="uploadFilePath" value="" />
            <input type="hidden" name="fromFile" id="fromFile" value="true" />
            <input type="hidden" name="onlyDir" id="onlyDir" value="${onlyDir}" />
            <input type="hidden" name="target" id="target" value="${target}" />
            <label for="file">
              <s:text name="jsp.file.file" />
            </label>
            <input type="file" class="form-control" name="file" id="file" placeholder="<s:text name="jsp.file.select.file" />">
            <label for="url">
              <s:text name="jsp.file.or.url" />
            </label>
            <input type="text" class="form-control" name="url" id="url" placeholder="<s:text name="jsp.file.paste.url" />">
            <label for="fileName">
              <s:text name="jsp.file.or.file.name" />
            </label>
            <input type="text" class="form-control" name="typedName" id="typedName" placeholder="<s:text name="jsp.file.instruction.type.file.name" />">
            <label for="fileData">
              <s:text name="jsp.file.and.file.content" />
            </label>
            <textarea class="form-control" name="typedContent" id="typedContent" placeholder="<s:text name="jsp.file.instruction.type.file.content" />"></textarea>
            <span data-alertid="upload"></span>
          </div>
          <div class="modal-footer">
            <button id="uploadButton" type="button" class="btn btn-primary" onclick="javascript:showLoading('upload-loading-indicator');">
              <s:text name="jsp.file.upload" />
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
  <script type="text/javascript">
			$("#createDirModal").on("show", function() {
				$("#createDirfileName").focus();
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
					message : '<s:text name="jsp.file.enter.directory.name" />',
					priority : '<s:text name="jsp.file.error" />'
					} ]);
				} else {
					$("#createDirForm").submit();
				}
			});
			function showCreateDirModal(absolutePath) {
				$("#createDirAbsolutePath").val(absolutePath);
				$(document).trigger("clear-alert-id.create");
				$("#createDirModal").modal({
				"backdrop" : "static",
				"keyboard" : true,
				"show" : true
				});
			}

			$("#uploadModal").on("show", function() {
				$("#uploadModal a.btn").on("click", function(e) {
					$("#uploadModal").modal('hide');
				});
			});
			
			$("#uploadModal").on("hide", function() {
				$("#uploadModal a.btn").off("click");
			});
			
			$("#uploadModal").on("hidden", function() {
				$("#uploadModal").remove();
			});
			
			$("#uploadButton").on("click", function() {
				var file = $("#file").val();
				var url = $("#url").val();
				var fileName = $("#typedName").val();
				if (file == "" && url == "" && fileName == "") {
					$(document).trigger("set-alert-id-upload", [ {
					message : '<s:text name="jsp.file.fill.fields" />',
					priority : '<s:text name="jsp.file.error" />'
					} ]);
				} else {
					$("#uploadForm").submit();
				}
			});
			function showUploadModal(absolutePath) {
				$("#uploadFilePath").val(absolutePath);
				$(document).trigger("clear-alert-id.upload");
				$("#uploadModal").modal({
				"backdrop" : "static",
				"keyboard" : true,
				"show" : true
				});
			}
		</script>
</body>
</html>