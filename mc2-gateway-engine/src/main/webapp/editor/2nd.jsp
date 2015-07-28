<!DOCTYPE html>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<link rel="shortcut icon" href="../images/favicon.ico" />
<link rel="stylesheet" href="../css/bootstrap.css">
<link rel="stylesheet" href="../css/jumbotron-narrow.css">
<script type="text/javascript" src="../js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="../js/jquery.bsFormAlerts.min.js"></script>
<script type="text/javascript" src="../js/bootstrap.js"></script>
<script type="text/javascript" src="../js/angular.min.js"></script>
<script type="text/javascript" src="../js/navbar.js"></script>
<script type="text/javascript" src="../js/sinapad.js"></script>
<title>PortEditor</title>
</head>
<body>
  <div class="container">
    <div class="header" align="center">
      <br>
      <br>
      <h1>Welcome to PortEditor</h1>
      <h4 class="text-muted">PortEditor will guide you to configure the gateway</h4>
      <br />
      <br />
    </div>
    <div>
      <fieldset class="scheduler-border">
        <legend class="scheduler-border">3rd step: Configuring the gateway parameters</legend>
        <h4 class="text-muted">1 - In this step you will have to configure the gateways paramaters, including which executable it will run and which of the modules will be enabled.</h4>
        <s:form id="editor" action="editor3rdStep" enctype="multipart/form-data" method="POST" theme="simple">
          <fieldset class="scheduler-border">
            <legend class="scheduler-border">Gateway Configuration</legend>
            <label for="host">Acronym Name</label>
            <input type="text" id="acronymName" name="acronymName" class="form-control" />
            <label for="host">Full Name</label>
            <input type="text" id="fullName" name="fullName" class="form-control" />
            <label for="host">About (HTML accepted)</label>
            <textarea rows="10" id="about" name="about" class="form-control"></textarea>
            <br />
            <div class="input-group">
              <span class="input-group-addon">
                <s:checkbox name="allowMultipleVersions" />
              </span>
              <input type="text" class="form-control" disabled="disabled" value="Allow multiple versions">
            </div>
            <div class="input-group">
              <span class="input-group-addon">
                <s:checkbox name="allowResourceChoice" />
              </span>
              <input type="text" class="form-control" disabled="disabled" value="Allow resource choice">
            </div>
          </fieldset>
          <fieldset class="scheduler-border">
            <legend class="scheduler-border">Application Configuration</legend>
            <s:doubleselect list="applications" name="applicationName" listKey="name" listValue="name" cssClass="form-control" doubleList="versions" doubleName="applicationDefaultVersion"
              doubleListKey="version" doubleListValue="version" doubleCssClass="form-control"
            />
          </fieldset>
          <fieldset class="scheduler-border">
            <legend class="scheduler-border">Modules Configuration</legend>
            <div class="input-group">
              <span class="input-group-addon">
                <s:checkbox name="sharedFilesModule" />
              </span>
              <input type="text" class="form-control" disabled="disabled" value="Shared files">
            </div>
            <div class="input-group">
              <span class="input-group-addon">
                <s:checkbox name="publicFilesModule" />
              </span>
              <input type="text" class="form-control" disabled="disabled" value="Public files">
            </div>
            <div class="input-group">
              <span class="input-group-addon">
                <s:checkbox name="ldapUserModule" />
              </span>
              <input type="text" class="form-control" disabled="disabled" value="LDAP login">
            </div>
            <div class="input-group">
              <span class="input-group-addon">
                <s:checkbox name="vomsUserModule" />
              </span>
              <input type="text" class="form-control" disabled="disabled" value="VOMS login">
            </div>
            <div class="input-group">
              <span class="input-group-addon">
                <s:checkbox name="guestUserModule" />
              </span>
              <input type="text" class="form-control" disabled="disabled" value="Restricted anonymous access">
            </div>
          </fieldset>
          <fieldset class="scheduler-border">
            <legend class="scheduler-border">E-mail Configuration</legend>
            <label for="subject">Subject</label>
            <input type="text" id="subject" name="subject" class="form-control" value="Notification from PortEngin gateway" />
            <label for="body">Body</label>
            <textarea rows="10" id="body" name="body" class="form-control">You are receiving this message because one of your jobs has finished.</textarea>
          </fieldset>
          <br />
          <input class="btn btn-lg btn-primary btn-block" type="submit" value="Next" />
        </s:form>
      </fieldset>
    </div>
  </div>
</body>
</html>