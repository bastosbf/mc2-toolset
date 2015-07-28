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
<title><s:text name="jsp.public_file_manager.title" />
</title>
</head>
<body>
  <div class="container">
    <%@ include file="jspf/header.jspf"%>
    <div align="center">
      <h3 class="text-muted">
        <s:text name="jsp.public_file_manager.title" />
      </h3>
    </div>
    <ol class="breadcrumb">
      <s:iterator var="p" value="parents">
        <li>
          <a href="managePublicFiles?absolutePath=${p.absolutePath}" onclick="javascript:showLoading();">${p.name}</a>
        </li>
      </s:iterator>
      <li class="active">${fileName}</li>
    </ol>
    <table class="table">
      <tr>
        <td align="left">
          <b>
            <s:text name="jsp.public_file_manager.file" />
          </b>
        </td>
        <td align="left">
          <b>
            <s:text name="jsp.public_file_manager.options" />
          </b>
        </td>
        <td align="center">
          <b>
            <s:text name="jsp.public_file_manager.modification.date" />
          </b>
        </td>
        <td align="right">
          <b>
            <s:text name="jsp.public_file_manager.size" />
          </b>
        </td>
      </tr>
      <s:iterator var="f" value="files">
        <tr>
          <td align="left">
            <s:if test="%{directory}">
              <a href="managePublicFiles?absolutePath=${f.absolutePath}" onclick="javascript:showLoading();">
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
                    onclick='javascript:startDownload("popupDownload?action=downloadPublicFile&directory=<s:property value="directory"/>&absolutePath=<s:property value="absolutePath"/>")'
                  >
                    <s:text name="jsp.public_file_manager.download" />
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
    </script>
</body>
</html>