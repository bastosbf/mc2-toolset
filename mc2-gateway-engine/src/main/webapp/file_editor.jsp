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
<title><s:text name="jsp.file_editor.title" /></title>
<script type="text/javascript">
	function save() {
		var form = document.createElement("FORM");
		
		var fileData = $("#data").val();
		var param = document.createElement("INPUT");
		param.type = "hidden";
		param.name = "fileData";
		param.id = "fileData";
		param.value = fileData;
		form.appendChild(param);
		
		var param = document.createElement("INPUT");
		param.type = "hidden";
		param.name = "absolutePath";
		param.id = "absolutePath";
		param.value = "${absolutePath}";
		form.appendChild(param);
		
		form.method = "POST";
		form.action = "editFile";
		document.body.appendChild(form);
		form.submit();
	}
</script>
</head>
<body>
  <div class="container">
    <ol class="breadcrumb">
      <li>${absolutePath}</li>
    </ol>
    <div class="hero-unit">
      <hr />
      <s:if test="%{edited}">
        <div class="alert alert-success alert-dismissable">
          <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
          <strong>
            <s:text name="jsp.file_editor.saved.success" />
          </strong>
        </div>
      </s:if>
      <textarea id="data" rows="20" class="form-control">${fileData}</textarea>
      <div align="right">
        <a class="btn btn-lg btn-success" href="javascript:void(0)" onclick="javascript:window.location.reload();">
          <s:text name='jsp.file_editor.reload.button' />
        </a>
        <a class="btn btn-lg btn-success" id="save" href="javascript:void(0)" onclick="javascript:save();">
          <s:text name="jsp.file_editor.save.button" />
        </a>
      </div>
    </div>
  </div>
</body>
</html>