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
<title><s:text name="jsp.file_view.title" /></title>
</head>
<body>
  <div class="container" align="center">
    <ol class="breadcrumb">
      <li>${absolutePath}</li>
    </ol>
    ${view}
    <div align="center">
      <a href="downloadFile?downloadPath=${relativePath}">
        <s:text name="jsp.file_view.download" />
      </a>
    </div>
  </div>
</body>
</html>