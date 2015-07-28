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
<style>
tr.highlight {
	background-color: #428bca;
	color: #ffffff;
}

tr.highlight a.dir {
	color: #ffffff;
}
</style>
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.bsFormAlerts.min.js"></script>
<script type="text/javascript" src="js/bootstrap.js"></script>
<script type="text/javascript" src="js/angular.min.js"></script>
<script type="text/javascript" src="js/navbar.js"></script>
<script type="text/javascript" src="js/sinapad.js"></script>
<script type="text/javascript">
function publishFile(absolutePath, fileName, directory) {
    showLoading();
    window.location = "uploadPublicFile?absolutePath="+absolutePath+"&fileName="+fileName+"&directory="+directory;
}
function selectRow(id) {
    id = id.replace(/\./g, '\\.');
    var attr = $("#cb_"+id).attr('checked');
    if(attr) {
        $("#cb_"+id).removeAttr('checked');
        $("#tb_"+id).removeClass('highlight');
    } else {
        $("#cb_"+id).attr('checked','checked');
        $("#tb_"+id).addClass('highlight');
    }
}
function downloadMultiple() {    
    var cbs = $( "input[id^='cb_']" );
    var notChecked = true;
    for(var i = 0; i < cbs.length; i++) {
        var id = cbs[i].id;
        id = id.replace(/\./g, '\\.');
        if($("#"+id).attr("checked") == "checked") {
            notChecked = false;
            break;
        }
    }
    if(notChecked) {
        $('#notChecked').show();
        return;
    }    
    var files = "";
    for(var i = 0; i < cbs.length; i++) {
        var id = cbs[i].id;
        id = id.replace(/\./g, '\\.');
        if($("#"+id).attr("checked") == "checked") {            
            files += cbs[i].value + ";";
        }
    }
    files = files.substring(0, files.length - 1);
    startDownload("popupDownload?action=downloadMultipleFiles&files="+files);
}
</script>
<title><s:text name="jsp.file_manager.title" /></title>
</head>
<body>
  <div class="container">
    <%@ include file="jspf/header.jspf"%>
    <div id="notChecked" class="alert alert-danger alert-dismissable" style="display: none;">
      <button type="button" id="notChecked_btn" class="close">&times;</button>
      <strong>
        <s:text name="jsp.file_manager.select.file.to.download" />
      </strong>
    </div>
    <script type="text/javascript">$('#notChecked_btn').on('click', function () {$('#notChecked').hide();});</script>
    <s:if test="%{exist}">
      <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        <strong>
          <s:text name="jsp.file_manager.file.to.upload.already.exists" />
        </strong>
      </div>
    </s:if>
    <s:if test="%{invalid}">
      <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        <strong>
          <s:text name="jsp.file_manager.url.invalid" />
        </strong>
      </div>
    </s:if>
    <s:if test="%{replaced}">
      <div class="alert alert-warning alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        <strong>
          <s:text name="jsp.file_manager.already.exists.replaced" />
        </strong>
      </div>
    </s:if>
    <s:if test="%{truncated}">
      <div class="alert alert-warning alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        <strong>
          <s:text name="jsp.file_manager.file.name.truncated" />
        </strong>
      </div>
    </s:if>
    <s:if test="%{uploaded}">
      <div class="alert alert-success alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        <strong>
          <s:text name="jsp.file_manager.upload.success" />
        </strong>
      </div>
    </s:if>
    <div align="center">
      <h3 class="text-muted">
        <s:text name="jsp.file_manager.title" />
      </h3>
    </div>
    <ol class="breadcrumb">
      <li>
        <a href="manageFiles" onclick="javascript:showLoading();">${application.project}</a>
      </li>
      <s:iterator var="p" value="parents">
        <li>
          <a href="manageFiles?absolutePath=${p.absolutePath}" onclick="javascript:showLoading();">${p.name}</a>
        </li>
      </s:iterator>
      <li class="active">${fileName}</li>
    </ol>
    <div class="row">
      <div align="left">
        <b>
          <h7>&nbsp;&nbsp;&nbsp;&nbsp;<s:text name="jsp.file_manager.warning.to.select.multiple.files" /></h7>
        </b>
      </div>
      <div align="right" class="col-md-12">
        <button type="button" class="btn btn-default" onclick="javascript:downloadMultiple()">
          <s:text name="jsp.file_manager.download.selected.files" />
        </button>
        <button type="button" class="btn btn-default" onclick="javascript:showCreateDirModal('${absolutePath}')">
          <s:text name="jsp.file_manager.menu.create" />
        </button>
        <button type="button" class="btn btn-default" onclick="javascript:showUploadModal('${absolutePath}')">
          <s:text name="jsp.file_manager.menu.upload" />
        </button>
      </div>
    </div>
    <table class="table">
      <tr>
        <td align="left">
          <b>
            <s:text name="jsp.file_manager.file" />
          </b>
        </td>
        <td align="left">
          <b>
            <s:text name="jsp.file_manager.options" />
          </b>
        </td>
        <td align="center">
          <b>
            <s:text name="jsp.file_manager.modification.date" />
          </b>
        </td>
        <td align="right">
          <b>
            <s:text name="jsp.file_manager.size" />
          </b>
        </td>
      </tr>
      <s:iterator var="f" value="files">
        <input id="cb_${f.name}" type="checkbox" value="${f.absolutePath}" style="display: none" />
        <tr id="tb_${f.name}">
          <s:if test="%{directory}">
            <td align="left">
              <a class="dir" href="manageFiles?absolutePath=${f.absolutePath}" onclick="javascript:showLoading();">
                <img src="images/files/folder.png" width="30px" />
                <s:property value="name" />
              </a>
            </td>
          </s:if>
          <s:else>
            <td align="left" onclick="javascript:selectRow('${f.name}');">
              <img src="images/files/file.png" width="30px" />
              <s:property value="name" />
            </td>
          </s:else>
          <td align="left">
            <div class="dropdown">
              <button id="menu" type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                <span class="caret"></span>
              </button>
              <ul class="dropdown-menu" role="menu" aria-labelledby="menu">
                <s:if test="%{directory}">
                  <li>
                    <a href="javascript:void(0)" onclick='javascript:showRemoveModal("<s:property value="name"/>", "<s:property value="absolutePath"/>")'>
                      <s:text name="jsp.file_manager.menu.remove" />
                    </a>
                  </li>
                  <s:if test='%{#application.sharedFilesEnabled}'>
                    <li>
                      <a href="javascript:void(0)" onclick='javascript:showShareModal("<s:property value="absolutePath"/>","<s:property value="name"/>",<s:property value="directory"/>)'>
                        <s:text name="jsp.file_manager.menu.share.dir" />
                      </a>
                    </li>
                  </s:if>
                  <s:if test='%{#application.publicFilesEnabled}'>
                    <li>
                      <a href='javascript:publishFile("<s:property value="absolutePath"/>", "<s:property value="name"/>", true);'>
                        <s:text name="jsp.file_manager.menu.publish.dir" />
                      </a>
                    </li>
                  </s:if>
                  <li>
                    <a href="javascript:void(0)" onclick='javascript:startDownload("popupDownload?action=downloadProjectFile&directory=true&absolutePath=<s:property value="absolutePath"/>");'>
                      <s:text name="jsp.file_manager.menu.download" />
                    </a>
                  </li>
                </s:if>
                <s:else>
                  <li>
                    <a href="javascript:void(0)" onclick='javascript:popup("openFile?absolutePath=<s:property value="absolutePath"/>", 800, 700);'>
                      <s:text name="jsp.file_manager.menu.edit" />
                    </a>
                  </li>
                  <li>
                    <a href="javascript:void(0)" onclick='javascript:showMoveModal("<s:property value="absolutePath"/>")'>
                      <s:text name="jsp.file_manager.menu.move" />
                    </a>
                  </li>
                  <li>
                    <a href="javascript:void(0)" onclick='javascript:showRemoveModal("<s:property value="name"/>", "<s:property value="absolutePath"/>")'>
                      <s:text name="jsp.file_manager.menu.remove" />
                    </a>
                  </li>
                  <s:if test='%{#application.sharedFilesEnabled}'>
                    <li>
                      <a href="javascript:void(0)" onclick='javascript:showShareModal("<s:property value="absolutePath"/>","<s:property value="name"/>",<s:property value="directory"/>)'>
                        <s:text name="jsp.file_manager.menu.share" />
                      </a>
                    </li>
                  </s:if>
                  <s:if test='%{#application.publicFilesEnabled}'>
                    <li>
                      <a href="javascript:void(0)" onclick='javascript:publishFile("<s:property value="absolutePath"/>", "<s:property value="name"/>");'>
                        <s:text name="jsp.file_manager.menu.publish" />
                      </a>
                    </li>
                  </s:if>
                  <li>
                    <a href="javascript:void(0)" onclick='javascript:startDownload("popupDownload?action=downloadProjectFile&absolutePath=<s:property value="absolutePath"/>")'>
                      <s:text name="jsp.file_manager.menu.download" />
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
    <%@ include file="jspf/footer.jspf"%>
  </div>
  <!-- ALERTS -->
  <!-- Create new Directory -->
  <div id="createDirModal" class="modal fade">
    <div class="modal-dialog">
      <div class="modal-content">
        <form id="createDirForm" action="createNewFolder" method="POST">
          <div class="modal-body">
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <input type="hidden" name="directory" id="directory" value="true" />
            <input type="hidden" name="absolutePath" id="createDirFilePath" value="" />
            <label for="fileName">
              <s:text name="jsp.file_manager.folder.name" />
            </label>
            <input type="text" class="form-control" name="fileName" id="createDirfileName" placeholder="<s:text name="jsp.file_manager.instruction.type.folder.name" />" autofocus>
            <span data-alertid="create"></span>
          </div>
          <div class="modal-footer">
            <button id="createDirButton" type="button" class="btn btn-primary">
              <s:text name="jsp.file_manager.button.create" />
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
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <input type="hidden" name="absolutePath" id="uploadFilePath" value="" />
            <label for="file">
              <s:text name="jsp.file_manager.file" />
            </label>
            <input type="file" class="form-control" name="file" id="file" placeholder="<s:text name="jsp.file_manager.select.file" />">
            <label for="url">
              <s:text name="jsp.file_manager.or.url" />
            </label>
            <input type="text" class="form-control" name="url" id="url" placeholder="<s:text name="jsp.file_manager.paste.url" />">
            <label for="fileName">
              <s:text name="jsp.file_manager.or.file.name" />
            </label>
            <input type="text" class="form-control" name="typedName" id="typedName" placeholder="<s:text name="jsp.file_manager.instruction.type.file.name" />">
            <label for="fileData">
              <s:text name="jsp.file_manager.and.file.content" />
            </label>
            <textarea class="form-control" name="typedContent" id="typedContent" placeholder="<s:text name="jsp.file_manager.instruction.type.file.content" />"></textarea>
            <span data-alertid="upload"></span>
          </div>
          <div class="modal-footer">
            <button id="uploadButton" type="button" class="btn btn-primary">
              <s:text name="jsp.file_manager.button.upload" />
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
  <!-- Remove file -->
  <div id="removeModal" class="modal fade">
    <div class="modal-dialog">
      <div class="modal-content">
        <form id="removeForm" action="deleteFile" method="POST">
          <div class="modal-body">
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <input type="hidden" name="absolutePath" id="removeFilePath" value="" />
            <input type="hidden" name="fileName" id="removeFileName" value="" />
            <p>
              <s:text name="jsp.file_manager.question.sure.remove.file" />
            </p>
          </div>
          <div class="modal-footer">
            <button id="removeYesButton" type="button" class="btn btn-primary">
              <s:text name="jsp.file_manager.button.yes" />
            </button>
            <button id="removeNoButton" type="button" class="btn btn-red">
              <s:text name="jsp.file_manager.button.no" />
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
  <!-- Edit file -->
  <div id="editModal" class="modal fade">
    <div class="modal-dialog">
      <div class="modal-content">
        <form id="removeForm" action="deleteFile" method="POST">
          <div class="modal-body">
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <input type="hidden" name="absolutePath" id="removeFilePath" value="" />
            <input type="hidden" name="fileName" id="removeFileName" value="" />
            <p>
              <s:text name="jsp.file_manager.question.sure.remove.file" />
            </p>
          </div>
          <div class="modal-footer">
            <button id="removeYesButton" type="button" class="btn btn-primary">
              <s:text name="jsp.file_manager.button.yes" />
            </button>
            <button id="removeNoButton" type="button" class="btn btn-red">
              <s:text name="jsp.file_manager.button.no" />
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
  <!-- Move File -->
  <div id="moveModal" class="modal fade">
    <div class="modal-dialog">
      <div class="modal-content">
        <button type="button" class="close" data-dismiss="modal" onclick="window.location.reload();">&times;</button>
        <div align="center">
          <h3>
            <s:text name="jsp.file_manager.move.file.to" />
          </h3>
        </div>
        <iframe id="moveIFrame" style="width: 100%; height: 500px"></iframe>
      </div>
    </div>
  </div>
  <!-- Share File -->
  <div id="shareModal" class="modal fade">
    <div class="modal-dialog">
      <div class="modal-content">
        <form id="shareFileForm" action="uploadSharedFile" method="POST">
          <div class="modal-body">
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <input type="hidden" name="directory" id="sharedDirectory" value="false" />
            <input type="hidden" name="absolutePath" id="sharedFilePath" value="" />
            <label for="fileName">
              <s:text name="jsp.file_manager.shared.file.name" />
            </label>
            <input type="text" class="form-control" name="fileName" id="sharedFileName" value="" />
            <span data-alertid="create"></span>
          </div>
          <div class="modal-footer">
            <button id="shareFileButton" type="button" class="btn btn-primary">
              <s:text name="jsp.file_manager.button.share" />
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
  <!-- SCRIPTS -->
  <script>
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
                 $(document).trigger("set-alert-id-create", [{
                        message: '<s:text name="jsp.file_manager.enter.directory.name" />',
                        priority: '<s:text name="jsp.file_manager.error" />'
                 }]);
            } else {
                $("#createDirForm").submit();
            }
        });
        function showCreateDirModal(absolutePath) {
            $("#createDirFilePath").val(absolutePath);
           $(document).trigger("clear-alert-id.create");
           $("#createDirModal").modal({                  
             "backdrop"  : "static",
             "keyboard"  : true,
             "show"      : true                   
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
                 $(document).trigger("set-alert-id-upload", [{
                        message: '<s:text name="jsp.file_manager.fill.fields" />',
                        priority: '<s:text name="jsp.file_manager.error" />'
                 }]);
            } else {
                $("#uploadForm").submit();
            }
        });
        function showUploadModal(absolutePath) {
            $("#uploadFilePath").val(absolutePath);
           $(document).trigger("clear-alert-id.upload");
           $("#uploadModal").modal({                  
             "backdrop"  : "static",
             "keyboard"  : true,
             "show"      : true                   
           });
        }
        
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
        function showRemoveModal(fileName, absolutePath) {
            $("#removeFileName").val(fileName);
            $("#removeFilePath").val(absolutePath);
            $("#removeModal").modal({                  
              "backdrop"  : "static",
              "keyboard"  : true,
              "show"      : true                   
            });
        }
        
        $("#moveModal").on("show", function() {
            $("#moveModal a.btn").on("click", function(e) {
                $("#moveModal").modal('hide');
            });
        });
     
        $("#moveModal").on("hide", function() {
            $("#moveModal a.btn").off("click");
        });
        
        $("#moveModal").on("hidden", function() {
            $("#moveModal").remove();
        });
       
        function showMoveModal(absolutePath) {
            $("#moveIFrame").attr("src", "listProjectRepository?move=true&from="+absolutePath);
            $("#moveModal").modal({                  
              "backdrop"  : "static",
              "keyboard"  : true,
              "show"      : true                   
            });
        }
        
        $("#shareModal").on("show", function() {
            $("#shareModal a.btn").on("click", function(e) {
                $("#shareModal").modal('hide');
            });
        });
     
        $("#shareModal").on("hide", function() {
            $("#shareModal a.btn").off("click");
        });
        
        $("#shareModal").on("hidden", function() {
            $("#shareModal").remove();
        });
        
        $("#shareFileButton").on("click", function() {
            var fileName = $("#sharedFileName").val();
            if (fileName == "") {
                 $(document).trigger("set-alert-id-create", [{
                        message: '<s:text name="jsp.file_manager.enter.shared.file.name" />',
                        priority: '<s:text name="jsp.file_manager.error" />'
                 }]);
            } else {
                $("#shareFileForm").submit();
            }
        });
       
        function showShareModal(absolutePath, fileName, directory) {
        	 $("#sharedFilePath").val(absolutePath);
        	 $("#sharedFileName").val(fileName);        	 
        	 $("#sharedDirectory").val(directory);
             $(document).trigger("clear-alert-id.create");
             $("#shareModal").modal({                  
               "backdrop"  : "static",
               "keyboard"  : true,
               "show"      : true                   
             });            
        }
    </script>
</body>
</html>