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
<title><s:text name="jsp.shared_file.title" /></title>
</head>
<body>
  <div class="container">
    <%@ include file="jspf/header.jspf"%>
    <div align="center">
      <s:if test="%{exists}">
        <div class="alert alert-danger">
          <strong>
            <s:text name="jsp.shared_file.shared.file.exists" />
          </strong>
        </div>
      </s:if>
      <s:if test="%{shared}">
        <div class="alert alert-success">
          <strong>
            <s:text name="jsp.shared_file.file.shared" />
          </strong>
        </div>
      </s:if>
    </div>
  </div>
</body>
</html>