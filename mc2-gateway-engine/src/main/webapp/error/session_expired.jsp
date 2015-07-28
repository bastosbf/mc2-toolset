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
<title><s:text name="jsp.session_expired.title" /></title>
</head>
<body>
	<div class="container">
        <%@ include file="../jspf/header.jspf"%>
        <div class="alert alert-danger">
            <strong><s:text name="jsp.session_expired.msn" /></strong>
        </div>
        <%@ include file="../jspf/footer.jspf"%>
    </div>
</body>
</html>