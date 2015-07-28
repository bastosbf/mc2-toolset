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
        <legend class="scheduler-border">1st step: Configuring gateways certificates</legend>
        <h4 class="text-muted">
          1 - In this step you will have to created valid credentials using the RSA mechanism and register into the authentication service that will be used by the gateway. The SINAPAD team provides a
          KSH script for creating the credentials that can be downloaded
          <a href="scripts/openssl-generate.ksh" download> here.</a>
        </h4>
        <form id="login" action="editor1stStep" enctype="multipart/form-data" method="POST">
          <br />
          <label for="privateKey">Private key (.key file)</label>
          <input type="file" class="form-control" id="privateKey" name="privateKey" />
          <br />
          <input class="btn btn-lg btn-primary btn-block" type="submit" value="Next" />
        </form>
      </fieldset>
    </div>
  </div>
</body>
</html>