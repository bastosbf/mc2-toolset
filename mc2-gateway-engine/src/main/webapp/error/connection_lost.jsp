<%@page import="br.lncc.sinapad.portengin.action.BaseAction"%>
<%@page import="br.lncc.sinapad.portengin.utils.EmailUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<link rel="shortcut icon" href="images/favicon.ico" />
<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/jumbotron-narrow.css">
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.bsFormAlerts.min.js"></script>
<script type="text/javascript" src="js/bootstrap.js"></script>
<script type="text/javascript" src="js/angular.min.js"></script>
<script type="text/javascript" src="js/navbar.js"></script>
<script type="text/javascript" src="js/sinapad.js"></script>
<title><s:text name="jsp.connection_lost.title" /></title>
</head>
<body>
	<div class="container">
        <%@ include file="../jspf/header.jspf"%>
		<div class="alert alert-danger">
	        <strong><s:text name="jsp.connection_lost.msn" /></strong>
	    </div>						
		<s:form action="reportError">
		  <div class="modal-body">
			<s:hidden name="type" value="Connection Lost Error"/>
			<s:hidden name="exception" value="%{exceptionStack}"/>			
			<br />
			<label for="msn"><s:text name="jsp.connection_lost.description.label" /></label>
			<textarea id="msn" name="msn" class="form-control"></textarea>
		  </div>
		  <div class="modal-footer"><button id="sendButton" type="button" class="btn btn-primary"><s:text name="jsp.connection_lost.description.button" /></button></div>
		</s:form>
		<%
			//BaseAction.initServices();
		%>
		<%@ include file="../jspf/footer.jspf"%>
	</div>
</body>
</html>