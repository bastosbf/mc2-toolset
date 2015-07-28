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
<title><s:text name="jsp.contact.title" /></title>
</head>
<body>
  <div class="container">
    <%@ include file="jspf/header.jspf"%>
    <s:if test="%{sent}">
      <div class="alert alert-success alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        <strong>
          <s:text name="jsp.contact.msn.sent" />
        </strong>
      </div>
    </s:if>
    <s:if test="%{invalid}">
      <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        <strong>
          <s:text name="jsp.contact.code.invalid" />
        </strong>
      </div>
    </s:if>
    <form id="sendForm" action="sendMessage" method="POST">
      <div class="modal-body">
        <label for="name">
          <s:text name="jsp.contact.form.name" />
        </label>
        <input type="text" class="form-control" name="name" id="name" placeholder="<s:text name="jsp.contact.form.name.tip" />">
        <label for="email">
          <s:text name="jsp.contact.form.email" />
        </label>
        <input type="text" class="form-control" name="email" id="email" placeholder="<s:text name="jsp.contact.form.email.tip" />">
        <label for="subject">
          <s:text name="jsp.contact.form.subject" />
        </label>
        <input type="text" class="form-control" name="subject" id="subject" placeholder="<s:text name="jsp.contact.form.subject.tip" />">
        <label for="msn">
          <s:text name="jsp.contact.form.msn" />
        </label>
        <textarea class="form-control" name="msn" id="msn" placeholder="<s:text name="jsp.contact.form.msn.tip" />"></textarea>
        <span data-alertid="send"></span>
        <s:if test='%{#session.isGuest}'>
          <div align="center">
            <%
            	net.tanesha.recaptcha.ReCaptcha c = net.tanesha.recaptcha.ReCaptchaFactory
            				.newSecureReCaptcha(
            						"6Lfyf8cSAAAAAAMb_Wm8dQa_p5-Eh11K_gpNg59j",
            						"6Lfyf8cSAAAAAJ9qXZpQAvs05FxsTjIYVHFh4mcS",
            						false);
            		((net.tanesha.recaptcha.ReCaptchaImpl) c)
            				.setRecaptchaServer("https://www.google.com/recaptcha/api");
            		out.print(c.createRecaptchaHtml(null, null));
            %>
          </div>
        </s:if>
      </div>
      <div class="modal-footer">
        <button id="sendButton" type="button" class="btn btn-primary">
          <s:text name="jsp.contact.form.submit" />
        </button>
      </div>
    </form>
    <%@ include file="jspf/footer.jspf"%>
  </div>
  <script>
			$("#sendButton").on("click", function() {
				var name = $("#name").val();
				var email = $("#email").val();
				var subject = $("#subject").val();
				var msn = $("#msn").val();
				if (name == "") {
					$(document).trigger("set-alert-id-send", [ {
					message : "<s:text name='jsp.contact.form.warning' />",
					priority : "<s:text name='jsp.contact.error' />"
					} ]);
				} else {
					$("#sendForm").submit();
				}
			});
		</script>
</body>
</html>
