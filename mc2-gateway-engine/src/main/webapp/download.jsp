<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
<title><s:text name="jsp.download.title" /> - ${label} </title>
</head>
<body>
  <div id="loading">
    <div align="center">
      <s:text name="jsp.download.msn" />
    </div>
    <div align="center">
      <img src="images/miscellaneous/downloading.gif" />
    </div>
    <div align="center">
      <a href="#" onclick="parent.closeIFrame(window.frameElement.id);">
        <s:text name="jsp.download.close.link" />
      </a>
    </div>
    <s:if test="%{downloadPath == null}">
      <script type="text/javascript">
	    post("${action}?absolutePath=${absolutePath}&files=${files}&directory=${directory}&jobId=${jobId}");
	  </script>
    </s:if>
    <s:else>
      <script type="text/javascript">
	    post("downloadFile?downloadPath=${downloadPath}");
        setTimeout(function(){parent.closeIFrame(window.frameElement.id)}, 2000);
      </script>
    </s:else>
  </div>
</body>
</html>