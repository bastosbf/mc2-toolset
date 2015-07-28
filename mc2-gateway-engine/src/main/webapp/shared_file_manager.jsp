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
<title><s:text name="jsp.shared_file_manager.title" />
</title>
</head>
<body>
  <div class="container">
    <%@ include file="jspf/header.jspf"%>
    <s:if test="%{error}">
      <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        <strong>
          <s:text name="jsp.shared_file_manager.error.msn" />
        </strong>
      </div>
    </s:if>
    <div align="center">
      <h3 class="text-muted">
        <s:text name="jsp.shared_file_manager.title" />
      </h3>
    </div>
    <ol class="breadcrumb">
      <s:iterator var="p" value="parents">
        <li>
          <a href="manageSharedFiles?absolutePath=${p.absolutePath}" onclick="javascript:showLoading();">${p.name}</a>
        </li>
      </s:iterator>
      <li class="active">${fileName}</li>
    </ol>
    <table class="table">
      <tr>
        <td align="left">
          <b>
            <s:text name="jsp.shared_file_manager.file" />
          </b>
        </td>
        <td align="left">
          <b>
            <s:text name="jsp.shared_file_manager.options" />
          </b>
        </td>
        <td align="center">
          <b>
            <s:text name="jsp.shared_file_manager.modification.date" />
          </b>
        </td>
        <td align="right">
          <b>
            <s:text name="jsp.shared_file_manager.size" />
          </b>
        </td>
      </tr>
      <s:iterator var="f" value="files">
        <tr>
          <td align="left">
            <s:if test="%{directory}">
              <a href="manageSharedFiles?absolutePath=${f.absolutePath}" onclick="javascript:showLoading();">
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
                <li>
                  <a href="javascript:void(0)"
                    onclick='javascript:startDownload("popupDownload?action=downloadSharedFile&directory=<s:property value="directory"/>&absolutePath=<s:property value="absolutePath"/>")'
                  >
                    <s:text name="jsp.shared_file_manager.download" />
                  </a>
                </li>
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
  <!-- Remove file -->
  <div id="removeModal" class="modal fade">
    <div class="modal-dialog">
      <div class="modal-content">
        <form id="removeForm" action="deleteSharedFile" method="POST">
          <div class="modal-body">
            <button type="button" class="close" data-dismiss="modal">&times;</button>
            <input type="hidden" name="filePath" id="removeFilePath" value="" />
            <input type="hidden" name="fileName" id="removeFileName" value="" />
            <p>
              <s:text name="jsp.shared_file_manager.question.sure.remove.file" />
            </p>
          </div>
          <div class="modal-footer">
            <button id="removeYesButton" type="button" class="btn btn-primary">
              <s:text name="jsp.shared_file_manager.yes" />
            </button>
            <button id="removeNoButton" type="button" class="btn btn-red">
              <s:text name="jsp.shared_file_manager.no" />
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
  <!-- Copy File -->
  <div id="copyModal" class="modal fade">
    <div class="modal-dialog">
      <div class="modal-content">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <div align="center">
          <h3>
            <s:text name="jsp.shared_file_manager.copy.shared.file.to" />
          </h3>
        </div>
        <iframe id="copyIFrame" style="width: 100%; height: 500px"></iframe>
      </div>
    </div>
  </div>
  <script type="text/javascript">
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
    function showRemoveModal(fileName, filePath) {
        $("#removeFileName").val(fileName);
        $("#removeFilePath").val(filePath);
        $("#removeModal").modal({                  
          "backdrop"  : "static",
          "keyboard"  : true,
          "show"      : true                   
        });
    }
    
    $("#copyModal").on("hide", function() {
        $("#copyModal a.btn").off("click");
    });
    
    $("#copyModal").on("hidden", function() {
        $("#copyModal").remove();
    });
   
    function showCopyModal(filePath) {
        $("#copyIFrame").attr("src", "listProjectRepository?shared=true&from="+filePath);
        $("#copyModal").modal({                  
          "backdrop"  : "static",
          "keyboard"  : true,
          "show"      : true                   
        });
    }
    </script>
</body>
</html>