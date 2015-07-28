<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="js/angular.min.js"></script>
<script type="text/javascript" src="js/app.js"></script>
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css" />
<title>Test File Upload</title>
</head>
<body ng-app >
  <div ng-controller="login">
  <%@ include file="jspf/login.jspf"%>
	  <form action="rest/file/upload" method="post" enctype="multipart/form-data" ng-show="result.uuid != null">
	  	<input type="hidden" id="uuid" name="uuid" value="{{ result.uuid }}">
	  	<label for="project">Project:</label>  
	    <input type="text" name="project" />
	    <br />
	    <label for="parents">Parents:</label>
	    <input type="text" name="parents" />
	    <br />
	    <label for="file">File:</label>
	    <input type="file" name="file" />
	    <br />
	    <label for="override">Override:</label>    
	    <input type="checkbox" name="override" value="true">
	    <br />
	    <input type="submit" value="Enviar" />		
	  </form>
  </div>
</body>
</html>