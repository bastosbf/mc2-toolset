<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="js/angular.min.js"></script>
<script type="text/javascript" src="js/app.js"></script>
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Test Job Submission Cancel</title>
</head>
<body ng-app >
	<div ng-controller="login">
		<%@ include file="jspf/login.jspf"%>
		<form action="rest/job-submission/cancel" method="post" ng-show="result.uuid != null">	
			<input type="hidden" id="uuid" name="uuid" value="{{ result.uuid }}">
			<label for="project">Project:</label> 
			<input type="text" name="project">
			<br> 
			<label for="jobId">JobId:</label> 
			<input type="text" name="jobId"> 
			<br> 
			<input type="submit" value="Send">
		</form>
	</div>		

</body>
</html>