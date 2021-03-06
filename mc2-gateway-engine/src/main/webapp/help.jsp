<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
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
<title><s:text name="jsp.help.title" /></title>
</head>
<body>
  <div class="container">
    <%@ include file="jspf/header.jspf"%>
    <jsp:include page="static/help.html" />
    <%@ include file="jspf/footer.jspf"%>
  </div>
</body>
<script src="js/ekko-lightbox.js"></script>
<script type="text/javascript">
	$(document).ready(function($) {
		// delegate calls to data-toggle="lightbox"
		$(document).delegate('*[data-toggle="lightbox"]', 'click', function(event) {
			event.preventDefault();
			return $(this).ekkoLightbox({
				onShown : function() {
					if (window.console) {
						return console.log('<s:text name="jsp.help.checking.events" />');
					}
				}
			});
		});
		//Programatically call
		$('#open-image').click(function(e) {
			e.preventDefault();
			$(this).ekkoLightbox();
		});
		$('#open-youtube').click(function(e) {
			e.preventDefault();
			$(this).ekkoLightbox();
		});
	});
</script>
</html>