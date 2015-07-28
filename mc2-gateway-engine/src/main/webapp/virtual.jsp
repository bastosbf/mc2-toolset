<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<link rel="shortcut icon" href="images/favicon.ico" />
<link href="css/prettify.css" rel="stylesheet">
<link href="css/editor.css" rel="stylesheet">
<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/jumbotron-narrow.css">
<link href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/css/bootstrap-combined.no-icons.min.css" rel="stylesheet">
<link href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/css/bootstrap-responsive.min.css" rel="stylesheet">
<link href="http://netdna.bootstrapcdn.com/font-awesome/3.0.2/css/font-awesome.css" rel="stylesheet">
<script src="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/js/bootstrap.min.js"></script>
<script src="js/prettify.js"></script>
<script src="js/bootstrap-wysiwyg.js"></script>
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<title><s:text name="jsp.virtual.title" /></title>
</head>
<body onload="window.moveTo(0, 0);window.resizeTo(screen.width, screen.height);">
  <ol class="breadcrumb">
    <li>${session.USER.projectName}/</li>
    <s:iterator var="p" value="parents">
      <li>${p.name}/</li>
    </s:iterator>
    <li class="active">${fileName}</li>
  </ol>
  <hr />
  <form id="virtualForm" target="iframe" method="post" action="http://eubrazilcc.comcidis.lncc.br:8000/">
    <input type="hidden" name="user" value="${session.USER.userName}" />
    <input type="hidden" name="project" value="${session.USER.projectName}" />
    <input type="hidden" name="filePath" value="${filePath}" />
  </form>
  <iframe name="iframe" width="100%" style="position: absolute; height: 100%"></iframe>
  <script type="text/javascript">
			document.getElementById('virtualForm').submit();
		</script>
</body>
</html>