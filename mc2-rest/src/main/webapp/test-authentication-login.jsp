<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="js/angular.min.js"></script>
<script type="text/javascript" src="js/app.js"></script>
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Test Authentication Login</title>
</head>
<body ng-app >
	<div ng-controller="login">
		<div>
			<fieldset>
				<legend>Login</legend>
				<label for="username">Username:</label> 
				<input type="text" id="username" name="username" value=""> 
				<br> 
				<label for="password">Password:</label> 
				<input type="password" id="password" name="password" value="">
				<br>
				<button ng-click="doLogin()">Login</button>
			</fieldset>
		</div>
		<div ng-show="result.code==591" class="alert alert-danger" role="alert">User Not Authorized.</div>
		<div ng-show="result.code==200" class="alert alert-success" role="alert">Success!</div>
	</div>		
</body>
</html>