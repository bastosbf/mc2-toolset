<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="js/angular.min.js"></script>
<script type="text/javascript" src="js/app.js"></script>
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SINAPAD REST</title>
<style type="text/css">
.fixed {
	position: fixed;
	bottom: 2%;
	right: 15%;
	width: 120px;
	z-index: 2
}
</style>
</head>
<body>
  <div ng-app="" ng-init="viewExemples=false">
    <div class="container" id="top">
      <div class="jumbotron">
        <h1>SINAPAD REST</h1>
        <p class="lead">Description of the REST services offered by SINAPAD.</p>
      </div>

      <div>
        <ul class="nav nav-pills">
          <li role="presentation">
            <a href="#application" ng-click=viewExemples=false>Application</a>
          </li>
          <li role="presentation">
            <a href="#authentication" ng-click=viewExemples=false>Authentication</a>
          </li>
          <li role="presentation">
            <a href="#file" ng-click=viewExemples=false>File</a>
          </li>
          <li role="presentation">
            <a href="#jobMonitoring" ng-click=viewExemples=false>Job Monitoring</a>
          </li>
          <li role="presentation">
            <a href="#jobSubmission" ng-click=viewExemples=false>Job Submission</a>
          </li>
          <li role="presentation">
            <a href="#resourceMonitoring" ng-click=viewExemples=false>Resource Monitoring</a>
          </li>
          <li role="presentation">
            <a href="#" ng-click=viewExemples=true>Examples</a>
          </li>
        </ul>
      </div>
    </div>
    <br>

    <div ng-show="!viewExemples">
      <div class="container">
        <fieldset class="scheduler-border">
          <legend class="scheduler-border" id="application">Application REST Services</legend>
          <div class="list-group">
            <div class="list-group-item">
              <h4 class="list-group-item-heading">
                <a href="test-application-config.jsp" target="_blank">rest/application/config</a>
              </h4>
              <p class="list-group-item-text">
              <div class="block">Get the configuration of an specific application given its name and version.</div>
              </p>

              <dl>
                <dt>
                  <span class="paramLabel">Parameters:</span>
                </dt>
                <dd>
                  <code>uuid</code>
                  - Universally unique identifier of the user.
                </dd>
                <dd>
                  <code>application</code>
                  - The name of the application.
                </dd>
                <dd>
                  <code>version</code>
                  - The version of the application.
                </dd>
                <dt>
                  <span class="returnLabel">Returns:</span>
                </dt>
                <dd>
                  A streaming representing the application configuration file.
                  <br>
                  <code>OK</code>
                  - if the file download was successful;
                  <br>
                  <code>NO_CONTENT</code>
                  - if the file was not found or some error occur;
                  <br>
                </dd>
                <a href="codes.jsp">Click to check the code values</a>
              </dl>
              <h4 class="text-right">
                <a href="#top">Back to Top</a>
              </h4>
            </div>
          </div>

          <div class="list-group">
            <div class="list-group-item">
              <h4 class="list-group-item-heading">
                <a href="test-application-get.jsp" target="_blank">rest/application/get</a>
              </h4>
              <p class="list-group-item-text">
              <div class="block">Get an specific application given its name.</div>
              </p>

              <dl>
                <dt>
                  <span class="paramLabel">Parameters:</span>
                </dt>
                <dd>
                  <code>uuid</code>
                  - Universally unique identifier of the user.
                </dd>
                <dd>
                  <code>application</code>
                  - The name of the application.
                </dd>
                <dt>
                  <span class="returnLabel">Returns:</span>
                </dt>
                <dd>
                  A result containing the application and the code of the result.
                  <br>
                  <code>USER_NOT_AUTHORIZED</code>
                  - if the user is not valid;
                  <br>
                  <code>OK</code>
                  - if the application was found;
                  <br>
                  <code>USER_ERROR</code>
                  - if the application inputed is not valid or some required parameter is null;
                  <br>
                  <code>INTERNAL_SERVER_ERROR</code>
                  - if some error occurs.
                </dd>
                <a href="codes.jsp">Click to check the code values</a>
              </dl>
              <h4 class="text-right">
                <a href="#top">Back to Top</a>
              </h4>
            </div>
          </div>

          <div class="list-group">
            <div class="list-group-item">
              <h4 class="list-group-item-heading">
                <a href="test-application-list.jsp" target="_blank">rest/application/list</a>
              </h4>
              <p class="list-group-item-text">
              <div class="block">List all the applications available for the user.</div>
              </p>

              <dl>
                <dt>
                  <span class="paramLabel">Parameters:</span>
                </dt>
                <dd>
                  <code>uuid</code>
                  - Universally unique identifier of the user.
                </dd>
                <dt>
                  <span class="returnLabel">Returns:</span>
                </dt>
                <dd>
                  A result containing the list of the applications available for the user and the code of the result.
                  <br>
                  <code>USER_NOT_AUTHORIZED</code>
                  - if the user is not valid;
                  <br>
                  <code>OK</code>
                  - if success;
                  <br>
                  <code>INTERNAL_SERVER_ERROR</code>
                  - if some error occurs.
                </dd>
                <a href="codes.jsp">Click to check the code values</a>
              </dl>
              <h4 class="text-right">
                <a href="#top">Back to Top</a>
              </h4>
            </div>
          </div>
        </fieldset>
      </div>

      <div class="container">
        <fieldset class="scheduler-border">
          <legend class="scheduler-border" id="authentication">Authentication REST Services</legend>
          <div class="list-group">
            <div class="list-group-item">
              <h4 class="list-group-item-heading">
                <a href="test-authentication-login.jsp" target="_blank">rest/authentication/login</a>
              </h4>
              <p class="list-group-item-text">
              <div class="block">Authenticate an use given an userName and password.</div>
              </p>

              <dl>
                <dt>
                  <span class="paramLabel">Parameters:</span>
                </dt>
                <dd>
                  <code>username</code>
                  - The name of the user.
                </dd>
                <dd>
                  <code>password</code>
                  - The password of the user.
                </dd>
                <dt>
                  <span class="returnLabel">Returns:</span>
                </dt>
                <dd>
                  A result containing the uuid of the authenticated user and the code of the result.
                  <br>
                  <code>OK</code>
                  - if success;
                  <br>
                  <code>USER_NOT_AUTHORIZED</code>
                  - if the user is not valid;
                  <br>
                  <code>USER_ERROR</code>
                  - if username or password is null;
                  <br>
                  <code>INTERNAL_SERVER_ERROR</code>
                  - if some error occurs.
                </dd>
                <a href="codes.jsp">Click to check the code values</a>
              </dl>
              <h4 class="text-right">
                <a href="#top">Back to Top</a>
              </h4>
            </div>
          </div>

          <div class="list-group">
            <div class="list-group-item">
              <h4 class="list-group-item-heading">
                <a href="test-authentication-logout.jsp" target="_blank">rest/authentication/logout</a>
              </h4>
              <p class="list-group-item-text">
              <div class="block">Logout an user.</div>
              </p>

              <dl>
                <dt>
                  <span class="paramLabel">Parameters:</span>
                </dt>
                <dd>
                  <code>uuid</code>
                  - Universally unique identifier of the user.
                </dd>
                <dt>
                  <span class="returnLabel">Returns:</span>
                </dt>
                <dd>
                  A result containing the code of the result.
                  <br>
                  <code>USER_ERROR</code>
                  - if the uuid is null;
                  <br>
                  <code>USER_NOT_AUTHORIZED</code>
                  - if the user is not logged;
                  <br>
                  <code>OK</code>
                  - if success;
                  <br>
                  <code>INTERNAL_SERVER_ERROR</code>
                  - if some error occurs.
                </dd>
                <a href="codes.jsp">Click to check the code values</a>
              </dl>
              <h4 class="text-right">
                <a href="#top">Back to Top</a>
              </h4>
            </div>
          </div>
        </fieldset>
      </div>

      <div class="container">
        <fieldset class="scheduler-border">
          <legend class="scheduler-border" id="file">File REST Services</legend>

          <div class="list-group">
            <div class="list-group-item">
              <h4 class="list-group-item-heading">
                <a href="test-file-copy.jsp" target="_blank">rest/file/copy</a>
              </h4>
              <p class="list-group-item-text">
              <div class="block">Copies a file.</div>
              </p>

              <dl>
                <dt>
                  <span class="paramLabel">Parameters:</span>
                </dt>
                <dd>
                  <code>uuid</code>
                  - Universally unique identifier of the user.
                </dd>
                <dd>
                  <code>project</code>
                  - A project owned by the user.
                </dd>
                <dd>
                  <code>source</code>
                  - The source of the file.
                </dd>
                <dd>
                  <code>destination</code>
                  - The destination of the copy.
                </dd>
                <dt>
                  <span class="returnLabel">Returns:</span>
                </dt>
                <dd>
                  A result containing the code of the result.
                  <br>
                  <code>USER_ERROR</code>
                  - if the uuid or the project is null;
                  <br>
                  <code>USER_NOT_AUTHORIZED</code>
                  - if the user is not valid;
                  <br>
                  <code>PROJECT_NOT_FOUND</code>
                  - if the project is not found;
                  <br>
                  <code>OK</code>
                  - if success;
                  <br>
                  <code>FILE_DOES_NOT_EXIST</code>
                  - if the file does not exist;
                  <br>
                  <code>INTERNAL_SERVER_ERROR</code>
                  - if some error occurs.
                </dd>
                <a href="codes.jsp">Click to check the code values</a>
              </dl>
              <h4 class="text-right">
                <a href="#top">Back to Top</a>
              </h4>
            </div>
          </div>

          <div class="list-group">
            <div class="list-group-item">
              <h4 class="list-group-item-heading">
                <a href="test-file-create.jsp" target="_blank">rest/file/create</a>
              </h4>
              <p class="list-group-item-text">
              <div class="block">Creates a new file or directory.</div>
              </p>

              <dl>
                <dt>
                  <span class="paramLabel">Parameters:</span>
                </dt>
                <dd>
                  <code>uuid</code>
                  - Universally unique identifier of the user.
                </dd>
                <dd>
                  <code>project</code>
                  - A project owned by the user.
                </dd>
                <dd>
                  <code>parents</code>
                  - (optional) The parents directory. Null if there is no parents. Parents are separated using the '/' character.
                </dd>
                <dd>
                  <code>file</code>
                  - The file name.
                </dd>
                <dd>
                  <code>directory</code>
                  - True if it is a directory or false if it is a file.
                </dd>
                <dt>
                  <span class="returnLabel">Returns:</span>
                </dt>
                <dd>
                  A result containing the code of the result.
                  <br>
                  <code>USER_ERROR</code>
                  - if some required parameter is null;
                  <br>
                  <code>USER_NOT_AUTHORIZED</code>
                  - if the user is not valid;
                  <br>
                  <code>PROJECT_NOT_FOUND</code>
                  - if the project is not found;
                  <br>
                  <code>OK</code>
                  - if success;
                  <br>
                  <code>FILE_ALREADY_EXISTS</code>
                  - if the file already exists;
                  <br>
                  <code>INTERNAL_SERVER_ERROR</code>
                  - if some error occurs.
                </dd>
                <a href="codes.jsp">Click to check the code values</a>
              </dl>
              <h4 class="text-right">
                <a href="#top">Back to Top</a>
              </h4>
            </div>
          </div>

          <div class="list-group">
            <div class="list-group-item">
              <h4 class="list-group-item-heading">
                <a href="test-file-delete.jsp" target="_blank">rest/file/delete</a>
              </h4>
              <p class="list-group-item-text">
              <div class="block">Deletes a file or directory.</div>
              </p>

              <dl>
                <dt>
                  <span class="paramLabel">Parameters:</span>
                </dt>
                <dd>
                  <code>uuid</code>
                  - Universally unique identifier of the user.
                </dd>
                <dd>
                  <code>project</code>
                  - A project owned by the user.
                </dd>
                <dd>
                  <code>parents</code>
                  - (optional) The parents directory. Null if there is no parents. Parents are separated using the '/' character.
                </dd>
                <dd>
                  <code>file</code>
                  - The file name.
                </dd>
                <dt>
                  <span class="returnLabel">Returns:</span>
                </dt>
                <dd>
                  A result containing the code of the result.
                  <br>
                  <code>USER_ERROR</code>
                  - if some required parameter is null;
                  <br>
                  <code>USER_NOT_AUTHORIZED</code>
                  - if the user is not valid;
                  <br>
                  <code>PROJECT_NOT_FOUND</code>
                  - if the project is not found;
                  <br>
                  <code>OK</code>
                  - if success;
                  <br>
                  <code>FILE_DOES_NOT_EXIST</code>
                  - if the file does not exist;
                  <br>
                  <code>INTERNAL_SERVER_ERROR</code>
                  - if some error occurs.
                </dd>
                <a href="codes.jsp">Click to check the code values</a>
              </dl>
              <h4 class="text-right">
                <a href="#top">Back to Top</a>
              </h4>
            </div>
          </div>

          <div class="list-group">
            <div class="list-group-item">
              <h4 class="list-group-item-heading">
                <a href="test-file-download.jsp" target="_blank">rest/file/download</a>
              </h4>
              <p class="list-group-item-text">
              <div class="block">Downloads a file.</div>
              </p>

              <dl>
                <dt>
                  <span class="paramLabel">Parameters:</span>
                </dt>
                <dd>
                  <code>uuid</code>
                  - Universally unique identifier of the user.
                </dd>
                <dd>
                  <code>project</code>
                  - A project owned by the user.
                </dd>
                <dd>
                  <code>parents</code>
                  - (optional) The parents directory. Null if there is no parents. Parents are separated using the '/' character.
                </dd>
                <dd>
                  <code>file</code>
                  - The file name.
                </dd>
                <dt>
                  <span class="returnLabel">Returns:</span>
                </dt>
                <dd>
                  A streaming for download the file and a result containing the code of the result.
                  <br>
                  <code>OK</code>
                  - if success;
                  <br>
                  <code>NO_CONTENT</code>
                  - if the file was not found;
                  <br>
                </dd>
                <a href="codes.jsp">Click to check the code values</a>
              </dl>
              <h4 class="text-right">
                <a href="#top">Back to Top</a>
              </h4>
            </div>
          </div>

          <div class="list-group">
            <div class="list-group-item">
              <h4 class="list-group-item-heading">
                <a href="test-file-exists.jsp" target="_blank">rest/file/exists</a>
              </h4>
              <p class="list-group-item-text">
              <div class="block">Checks if a file exists.</div>
              </p>

              <dl>
                <dt>
                  <span class="paramLabel">Parameters:</span>
                </dt>
                <dd>
                  <code>uuid</code>
                  - Universally unique identifier of the user.
                </dd>
                <dd>
                  <code>project</code>
                  - A project owned by the user.
                </dd>
                <dd>
                  <code>parents</code>
                  - (optional) The parents directory. Null if there is no parents. Parents are separated using the '/' character.
                </dd>
                <dd>
                  <code>file</code>
                  - (optional) The file name. Null if there is not file name.
                </dd>
                <dt>
                  <span class="returnLabel">Returns:</span>
                </dt>
                <dd>
                  A result containing the code of the result.
                  <br>
                  <code>USER_ERROR</code>
                  - if the uuid or the project is null;
                  <br>
                  <code>USER_NOT_AUTHORIZED</code>
                  - if the user is not valid;
                  <br>
                  <code>PROJECT_NOT_FOUND</code>
                  - if the project is not found;
                  <br>
                  <code>OK</code>
                  - if success;
                  <br>
                  <code>FILE_DOES_NOT_EXIST</code>
                  - if the file does not exist;
                  <br>
                  <code>INTERNAL_SERVER_ERROR</code>
                  - if some error occurs.
                </dd>
                <a href="codes.jsp">Click to check the code values</a>
              </dl>
              <h4 class="text-right">
                <a href="#top">Back to Top</a>
              </h4>
            </div>
          </div>

          <div class="list-group">
            <div class="list-group-item">
              <h4 class="list-group-item-heading">
                <a href="test-file-find.jsp" target="_blank">rest/file/find</a>
              </h4>
              <p class="list-group-item-text">
              <div class="block">Finds a file representation.</div>
              </p>

              <dl>
                <dt>
                  <span class="paramLabel">Parameters:</span>
                </dt>
                <dd>
                  <code>uuid</code>
                  - Universally unique identifier of the user.
                </dd>
                <dd>
                  <code>project</code>
                  - A project owned by the user.
                </dd>
                <dd>
                  <code>parents</code>
                  - (optional) The parents directory. Null if there is no parents. Parents are separated using the '/' character.
                </dd>
                <dd>
                  <code>file</code>
                  - (optional) The file name. Null if there is not file name.
                </dd>
                <dt>
                  <span class="returnLabel">Returns:</span>
                </dt>
                <dd>
                  A result containing the file information and the code of the result.
                  <br>
                  <code>USER_ERROR</code>
                  - if the uuid or the project is null;
                  <br>
                  <code>USER_NOT_AUTHORIZED</code>
                  - if the user is not valid;
                  <br>
                  <code>PROJECT_NOT_FOUND</code>
                  - if the project is not found;
                  <br>
                  <code>OK</code>
                  - if success;
                  <br>
                  <code>FILE_DOES_NOT_EXIST</code>
                  - if the file does not exist;
                  <br>
                  <code>INTERNAL_SERVER_ERROR</code>
                  - if some error occurs.
                </dd>
                <a href="codes.jsp">Click to check the code values</a>
              </dl>
              <h4 class="text-right">
                <a href="#top">Back to Top</a>
              </h4>
            </div>
          </div>

          <div class="list-group">
            <div class="list-group-item">
              <h4 class="list-group-item-heading">
                <a href="test-file-move.jsp" target="_blank">rest/file/move</a>
              </h4>
              <p class="list-group-item-text">
              <div class="block">Moves a file.</div>
              </p>

              <dl>
                <dt>
                  <span class="paramLabel">Parameters:</span>
                </dt>
                <dd>
                  <code>uuid</code>
                  - Universally unique identifier of the user.
                </dd>
                <dd>
                  <code>project</code>
                  - A project owned by the user.
                </dd>
                <dd>
                  <code>parentsFrom</code>
                  - (optional) The parents directory where the file is. Null if there is no parents. Parents are separated using the '/' character.
                </dd>
                <dd>
                  <code>fileName</code>
                  - The name of the file.
                </dd>
                <dd>
                  <code>parentsTo</code>
                  - (optional) The parents directory where the file will be placed. Null if there is no parents. Parents are separated using the '/' character.
                </dd>
                <dt>
                  <span class="returnLabel">Returns:</span>
                </dt>
                <dd>
                  A result containing the code of the result.
                  <br>
                  <code>USER_ERROR</code>
                  - if the uuid or the project is null;
                  <br>
                  <code>USER_NOT_AUTHORIZED</code>
                  - if the user is not valid;
                  <br>
                  <code>PROJECT_NOT_FOUND</code>
                  - if the project is not found;
                  <br>
                  <code>OK</code>
                  - if success;
                  <br>
                  <code>FILE_DOES_NOT_EXIST</code>
                  - if the file does not exist;
                  <br>
                  <code>INTERNAL_SERVER_ERROR</code>
                  - if some error occurs.
                </dd>
                <a href="codes.jsp">Click to check the code values</a>
              </dl>
              <h4 class="text-right">
                <a href="#top">Back to Top</a>
              </h4>
            </div>
          </div>

          <div class="list-group">
            <div class="list-group-item">
              <h4 class="list-group-item-heading">
                <a href="test-file-rename.jsp" target="_blank">rest/file/rename</a>
              </h4>
              <p class="list-group-item-text">
              <div class="block">Renames a file.</div>
              </p>

              <dl>
                <dt>
                  <span class="paramLabel">Parameters:</span>
                </dt>
                <dd>
                  <code>uuid</code>
                  - Universally unique identifier of the user.
                </dd>
                <dd>
                  <code>project</code>
                  - A project owned by the user.
                </dd>
                <dd>
                  <code>parents</code>
                  - (optional)The parents directory. Null if there is no parents. Parents are separated using the '/' character.
                </dd>
                <dd>
                  <code>oldName</code>
                  - The old name of the file.
                </dd>
                <dd>
                  <code>newName</code>
                  - The new name of the file.
                </dd>
                <dt>
                  <span class="returnLabel">Returns:</span>
                </dt>
                <dd>
                  A result containing the code of the result.
                  <br>
                  <code>USER_ERROR</code>
                  - if some required parameter is null;
                  <br>
                  <code>USER_NOT_AUTHORIZED</code>
                  - if the user is not valid;
                  <br>
                  <code>PROJECT_NOT_FOUND</code>
                  - if the project is not found;
                  <br>
                  <code>OK</code>
                  - if success;
                  <br>
                  <code>FILE_DOES_NOT_EXIST</code>
                  - if the file does not exist;
                  <br>
                  <code>INTERNAL_SERVER_ERROR</code>
                  - if some error occurs.
                </dd>
                <a href="codes.jsp">Click to check the code values</a>
              </dl>
              <h4 class="text-right">
                <a href="#top">Back to Top</a>
              </h4>
            </div>
          </div>

          <div class="list-group">
            <div class="list-group-item">
              <h4 class="list-group-item-heading">
                <a href="test-file-upload.jsp" target="_blank">rest/file/upload</a>
              </h4>
              <p class="list-group-item-text">
              <div class="block">Uploads a file.</div>
              </p>

              <dl>
                <dt>
                  <span class="paramLabel">Parameters:</span>
                </dt>
                <dd>
                  <code>uuid</code>
                  - Universally unique identifier of the user.
                </dd>
                <dd>
                  <code>project</code>
                  - A project owned by the user.
                </dd>
                <dd>
                  <code>parents</code>
                  - (optional) The parents directory. Null if there is no parents. Parents are separated using the '/' character.
                </dd>
                <dd>
                  <code>file</code>
                  - The file.
                </dd>
                <dd>
                  <code>override</code>
                  - True if the file already exists and want to override.
                </dd>
                <dt>
                  <span class="returnLabel">Returns:</span>
                </dt>
                <dd>
                  A result containing the code of the result.
                  <br>
                  <code>USER_ERROR</code>
                  - if some required parameter is null;
                  <br>
                  <code>USER_NOT_AUTHORIZED</code>
                  - if the user is not valid;
                  <br>
                  <code>PROJECT_NOT_FOUND</code>
                  - if the project is not found;
                  <br>
                  <code>OK</code>
                  - if success;
                  <br>
                  <code>FILE_ALREADY_EXISTS</code>
                  - if the file already exists;
                  <br>
                  <code>INTERNAL_SERVER_ERROR</code>
                  - if some error occurs.
                </dd>
                <a href="codes.jsp">Click to check the code values</a>
              </dl>
              <h4 class="text-right">
                <a href="#top">Back to Top</a>
              </h4>
            </div>
          </div>
        </fieldset>
      </div>

      <div class="container">
        <fieldset class="scheduler-border">
          <legend class="scheduler-border" id="jobMonitoring">Job Monitoring REST Services</legend>
          <div class="list-group">
            <div class="list-group-item">
              <h4 class="list-group-item-heading">
                <a href="test-job-monitoring-delete.jsp" target="_blank">rest/job-monitoring/delete</a>
              </h4>
              <p class="list-group-item-text">
              <div class="block">Deletes a job result.</div>
              </p>

              <dl>
                <dt>
                  <span class="paramLabel">Parameters:</span>
                </dt>
                <dd>
                  <code>uuid</code>
                  - Universally unique identifier of the user.
                </dd>
                <dd>
                  <code>project</code>
                  - A project owned by the user.
                </dd>
                <dd>
                  <code>jobId</code>
                  - The id of the job.
                </dd>
                <dt>
                  <span class="returnLabel">Returns:</span>
                </dt>
                <dd>
                  A result containing the code of the result.
                  <br>
                  <code>USER_NOT_AUTHORIZED</code>
                  - if the user is not valid;
                  <br>
                  <code>PROJECT_NOT_FOUND</code>
                  - if the project is not found;
                  <br>
                  <code>OK</code>
                  - if success;
                  <br>
                  <code>USER_ERROR</code>
                  - if some required parameter is null;
                  <br>
                  <code>JOB_DOES_NOT_EXIST</code>
                  - if the job result does not exist;
                  <br>
                  <code>INTERNAL_SERVER_ERROR</code>
                  - if some error occurs.
                </dd>
                <a href="codes.jsp">Click to check the code values</a>
              </dl>
              <h4 class="text-right">
                <a href="#top">Back to Top</a>
              </h4>
            </div>
          </div>

          <div class="list-group">
            <div class="list-group-item">
              <h4 class="list-group-item-heading">
                <a href="test-job-monitoring-get.jsp" target="_blank">rest/job-monitoring/get</a>
              </h4>
              <p class="list-group-item-text">
              <div class="block">Gets the job result.</div>
              </p>

              <dl>
                <dt>
                  <span class="paramLabel">Parameters:</span>
                </dt>
                <dd>
                  <code>uuid</code>
                  - Universally unique identifier of the user.
                </dd>
                <dd>
                  <code>project</code>
                  - A project owned by the user.
                </dd>
                <dd>
                  <code>jobId</code>
                  - The id of the job.
                </dd>
                <dt>
                  <span class="returnLabel">Returns:</span>
                </dt>
                <dd>
                  A result containing the status of the job and the code of the result.
                  <br>
                  <code>USER_ERROR</code>
                  - if some required parameter is null;
                  <br>
                  <code>USER_NOT_AUTHORIZED</code>
                  - if the user is not valid;
                  <br>
                  <code>PROJECT_NOT_FOUND</code>
                  - if the project is not found;
                  <br>
                  <code>OK</code>
                  - if success;
                  <br>
                  <code>FILE_DOES_NOT_EXIST</code>
                  - if the file does not exist;
                  <br>
                  <code>INTERNAL_SERVER_ERROR</code>
                  - if some error occurs.
                </dd>
                <a href="codes.jsp">Click to check the code values</a>
              </dl>
              <h4 class="text-right">
                <a href="#top">Back to Top</a>
              </h4>
            </div>
          </div>

          <div class="list-group">
            <div class="list-group-item">
              <h4 class="list-group-item-heading">
                <a href="test-job-monitoring-history.jsp" target="_blank">rest/job-monitoring/history</a>
              </h4>
              <p class="list-group-item-text">
              <div class="block">Gets the immutable file history used and returned by the job.</div>
              </p>

              <dl>
                <dt>
                  <span class="paramLabel">Parameters:</span>
                </dt>
                <dd>
                  <code>uuid</code>
                  - Universally unique identifier of the user.
                </dd>
                <dd>
                  <code>project</code>
                  - A project owned by the user.
                </dd>
                <dd>
                  <code>jobId</code>
                  - The id of the job.
                </dd>
                <dd>
                  <code>parents</code>
                  - (optional) The parents directory. Null if there is no parents
                </dd>
                <dd>
                  <code>file</code>
                  - (optional) The file name. Null if there is not file name
                </dd>
                <dt>
                  <span class="returnLabel">Returns:</span>
                </dt>
                <dd>
                  A result containing the status of the job and the code of the result.
                  <br>
                  <code>USER_ERROR</code>
                  - if some required parameter is null;
                  <br>
                  <code>USER_NOT_AUTHORIZED</code>
                  - if the user is not valid;
                  <br>
                  <code>PROJECT_NOT_FOUND</code>
                  - if the project is not found;
                  <br>
                  <code>USER_ERROR</code>
                  - if the job is not valid;
                  <br>
                  <code>OK</code>
                  - if success;
                  <br>
                  <code>FILE_DOES_NOT_EXIST</code>
                  - if the file does not exist;
                  <br>
                  <code>INTERNAL_SERVER_ERROR</code>
                  - if some error occurs.
                </dd>
                <a href="codes.jsp">Click to check the code values</a>
              </dl>
              <h4 class="text-right">
                <a href="#top">Back to Top</a>
              </h4>
            </div>
          </div>

          <div class="list-group">
            <div class="list-group-item">
              <h4 class="list-group-item-heading">
                <a href="test-job-monitoring-list.jsp" target="_blank">rest/job-monitoring/list</a>
              </h4>
              <p class="list-group-item-text">
              <div class="block">Lists all results in a given period.</div>
              </p>

              <dl>
                <dt>
                  <span class="paramLabel">Parameters:</span>
                </dt>
                <dd>
                  <code>uuid</code>
                  - Universally unique identifier of the user.
                </dd>
                <dd>
                  <code>project</code>
                  - A project owned by the user.
                </dd>
                <dd>
                  <code>from</code>
                  - (optional) The minimum start date of the job, or null for dates from any period.
                </dd>
                <dd>
                  <code>to</code>
                  - (optional) The maximum start date of the job, or null for dates to any period.
                </dd>
                <dt>
                  <span class="returnLabel">Returns:</span>
                </dt>
                <dd>
                  A result containing the list of statuses and the code of the result.
                  <br>
                  <code>USER_NOT_AUTHORIZED</code>
                  - if the user is not valid;
                  <br>
                  <code>USER_ERROR</code>
                  - if the format of one the dates(from or to) is not null and is incorrect, or if some required parameter is null;
                  <br>
                  <code>OK</code>
                  - if success;
                  <br>
                  <code>INTERNAL_SERVER_ERROR</code>
                  - if some error occurs.
                </dd>
                <a href="codes.jsp">Click to check the code values</a>
              </dl>
              <h4 class="text-right">
                <a href="#top">Back to Top</a>
              </h4>
            </div>
          </div>

          <div class="list-group">
            <div class="list-group-item">
              <h4 class="list-group-item-heading">
                <a href="test-job-monitoring-log.jsp" target="_blank">rest/job-monitoring/log</a>
              </h4>
              <p class="list-group-item-text">
              <div class="block">Gets the log of the job.</div>
              </p>

              <dl>
                <dt>
                  <span class="paramLabel">Parameters:</span>
                </dt>
                <dd>
                  <code>uuid</code>
                  - Universally unique identifier of the user.
                </dd>
                <dd>
                  <code>project</code>
                  - A project owned by the user.
                </dd>
                <dd>
                  <code>jobId</code>
                  - The id of the job.
                </dd>
                <dt>
                  <span class="returnLabel">Returns:</span>
                </dt>
                <dd>
                  The log of the job.
                  <br>
                  <code>OK</code>
                  - if success;
                  <br>
                  <code>NO_CONTENT</code>
                  - if the log can not be returned;
                  <br>
                </dd>
                <a href="codes.jsp">Click to check the code values</a>
              </dl>
              <h4 class="text-right">
                <a href="#top">Back to Top</a>
              </h4>
            </div>
          </div>
        </fieldset>
      </div>

      <div class="container">
        <fieldset class="scheduler-border">
          <legend class="scheduler-border" id="jobSubmission">Job Submission REST Services</legend>
          <div class="list-group">
            <div class="list-group-item">
              <h4 class="list-group-item-heading">
                <a href="test-job-submission-cancel.jsp" target="_blank">rest/job-submission/cancel</a>
              </h4>
              <p class="list-group-item-text">
              <div class="block">Cancels a job.</div>
              </p>

              <dl>
                <dt>
                  <span class="paramLabel">Parameters:</span>
                </dt>
                <dd>
                  <code>uuid</code>
                  - Universally unique identifier of the user.
                </dd>
                <dd>
                  <code>project</code>
                  - A project owned by the user.
                </dd>
                <dd>
                  <code>jobId</code>
                  - The id of the job.
                </dd>
                <dt>
                  <span class="returnLabel">Returns:</span>
                </dt>
                <dd>
                  A result containing the code of the result.
                  <br>
                  <code>USER_NOT_AUTHORIZED</code>
                  - if the user is not valid;
                  <br>
                  <code>PROJECT_NOT_FOUND</code>
                  - if the project is not found;
                  <br>
                  <code>JOB_CANNOT_BE_CANCELLED</code>
                  - if the job can not be cancelled.;
                  <br>
                  <code>OK</code>
                  - if success;
                  <br>
                  <code>USER_ERROR</code>
                  - if some required parameter is null;
                  <br>
                  <code>INTERNAL_SERVER_ERROR</code>
                  - if some error occurs.
                </dd>
                <a href="codes.jsp">Click to check the code values</a>
              </dl>
              <h4 class="text-right">
                <a href="#top">Back to Top</a>
              </h4>
            </div>
          </div>

          <div class="list-group">
            <div class="list-group-item">
              <h4 class="list-group-item-heading">
                <a href="test-job-submission-run.jsp" target="_blank">rest/job-submission/run</a>
              </h4>
              <p class="list-group-item-text">
              <div class="block">Runs a job.</div>
              </p>

              <dl>
                <dt>
                  <span class="paramLabel">Parameters:</span>
                </dt>
                <dd>
                  <code>uuid</code>
                  - Universally unique identifier of the user.
                </dd>
                <dd>
                  <code>project</code>
                  - A project owned by the user.
                </dd>
                <dd>
                  <code>application</code>
                  - The name of the application.
                </dd>
                <dd>
                  <code>version</code>
                  - The version of the application.
                </dd>
                <dd>
                  <code>args</code>
                  - The arguments of the job. The used format is 'ARGUMENT1::VALUE1;ARGUMENT2::VALUE2;ARGUMENT3::VALUE3'.
                </dd>
                <dt>
                  <span class="returnLabel">Returns:</span>
                </dt>
                <dd>
                  A result containing the code of the result.
                  <br>
                  <code>USER_NOT_AUTHORIZED</code>
                  - if the user is not valid;
                  <br>
                  <code>PROJECT_NOT_FOUND</code>
                  - if the project is not found;
                  <br>
                  <code>OK</code>
                  - if success;
                  <br>
                  <code>USER_ERROR</code>
                  - if some required inputed value is not valid or is null;
                  <br>
                  <code>INTERNAL_SERVER_ERROR</code>
                  - if some error occurs.
                </dd>
                <a href="codes.jsp">Click to check the code values</a>
              </dl>
              <h4 class="text-right">
                <a href="#top">Back to Top</a>
              </h4>
            </div>
          </div>

        </fieldset>
      </div>

      <div class="container">
        <fieldset class="scheduler-border">
          <legend class="scheduler-border" id="resourceMonitoring">Resource Monitoring REST Services</legend>
          <div class="list-group">
            <div class="list-group-item">
              <h4 class="list-group-item-heading">
                <a href="test-resource-monitoring-get.jsp" target="_blank">rest/resource-monitoring/get</a>
              </h4>
              <p class="list-group-item-text">
              <div class="block">Gets a resource information.</div>
              </p>

              <dl>
                <dt>
                  <span class="paramLabel">Parameters:</span>
                </dt>
                <dd>
                  <code>uuid</code>
                  - Universally unique identifier of the user.
                </dd>
                <dd>
                  <code>resource</code>
                  - The name of the resource.
                </dd>
                <dt>
                  <span class="returnLabel">Returns:</span>
                </dt>
                <dd>
                  A result containing the resource information and the code of the result.
                  <br>
                  <code>USER_NOT_AUTHORIZED</code>
                  - if the user is not valid;
                  <br>
                  <code>OK</code>
                  - if success;
                  <br>
                  <code>USER_ERROR</code>
                  - if the resource or the uuid is null;
                  <br>
                  <code>FILE_DOES_NOT_EXIST</code>
                  - if the resource does not exist;
                  <br>
                  <code>INTERNAL_SERVER_ERROR</code>
                  - if some error occurs.
                </dd>
                <a href="codes.jsp">Click to check the code values</a>
              </dl>
              <h4 class="text-right">
                <a href="#top">Back to Top</a>
              </h4>
            </div>
          </div>

          <div class="list-group">
            <div class="list-group-item">
              <h4 class="list-group-item-heading">
                <a href="test-resource-monitoring-list.jsp" target="_blank">rest/resource-monitoring/list</a>
              </h4>
              <p class="list-group-item-text">
              <div class="block">List all the resources available for the user.</div>
              </p>

              <dl>
                <dt>
                  <span class="paramLabel">Parameters:</span>
                </dt>
                <dd>
                  <code>uuid</code>
                  - Universally unique identifier of the user.
                </dd>
                <dt>
                  <span class="returnLabel">Returns:</span>
                </dt>
                <dd>
                  A result containing the resource information and the code of the result.
                  <br>
                  <code>USER_ERROR</code>
                  - if the uuid is null;
                  <br>
                  <code>USER_NOT_AUTHORIZED</code>
                  - if the user is not valid;
                  <br>
                  <code>OK</code>
                  - if success;
                  <br>
                  <code>INTERNAL_SERVER_ERROR</code>
                  - if some error occurs.
                </dd>
                <a href="codes.jsp">Click to check the code values</a>
              </dl>
              <h4 class="text-right">
                <a href="#top">Back to Top</a>
              </h4>
            </div>
          </div>
        </fieldset>
      </div>
    </div>


    <div ng-show="viewExemples">
      <%@ include file="jspf/examples.jspf"%>
    </div>
  </div>
</body>
</html>