<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="jsp.chart.title" /></title>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
</head>
<body>
  <div id="loading">
    <script type="text/javascript">
		google.load("visualization", "1", {
			packages : [ "corechart" ]
		});
		google.setOnLoadCallback(drawChart);
		function drawChart() {
			var data = google.visualization
					.arrayToDataTable([
							[
									'<s:text name="class.ViewMetricsChartAction.period" />',
									'<s:text name="class.ViewMetricsChartAction.access" />',
									'<s:text name="class.ViewMetricsChartAction.guest" />',
									'<s:text name="class.ViewMetricsChartAction.user" />',
									'<s:text name="class.ViewMetricsChartAction.submission" />' ],
									<s:iterator var="k" value="%{mi.getKeys()}">
							 			['${k}',<s:property value="%{mi.getAccess(#k)}" />,<s:property value="%{mi.getGuest(#k)}" />,<s:property value="%{mi.getUser(#k)}" />,<s:property value="%{mi.getSubmission(#k)}" />],
									</s:iterator>		
							]);

			var options = {
				title : '<s:text name="%{#session.USER.getProjectName()}" /> - <s:text name="%{type}" /> <s:text name="class.ViewMetricsChartAction.metrics" />',
				titleTextStyle : {
					color : "black"
				},
				hAxis : {
					title : '<s:text name="class.ViewMetricsChartAction.period" />',
					textStyle : {
						color : "black"
					},
					format:'#'
				},
				vAxis : {
					textStyle : {
						color : "black"
					},
					format:'#'
				},
				backgroundColor : {
					fill : "transparent",
					stroke : "black",
					strokeWidth : "2"
				},
			};

			var chart = new google.visualization.ColumnChart(document
					.getElementById('chart_div'));
			chart.draw(data, options);
		}
	</script>
    <div id="chart_div" style="width: 580px; height: 480px"></div>
  </div>
</body>
</html>