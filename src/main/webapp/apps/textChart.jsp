<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<style>

	html, body {
		height: 100%;
		width: 100%;
		border: none;
		margin: 0;
		padding: 0;	
		font-family: Verdana, Geneva, Arial, Helvetica, sans-serif;
		position: relative;
	}
	
	table {
		width: 100%;
		height: 100%;
		padding: 0;
		margin: 0;
		border-spacing: 0;
    	border-collapse: collapse;
	}
	
	#label, #value {
		font-size: 8em;
		text-align: center;
		vertical-align: middle;
		height: 50%;
	}
	
	#label {
		background-color: rgb(23,156,125);
		color: white;
	}
	
	#value {
		background-color: #efefef;
	}
</style>
<title>Text chart</title>
</head>
<body>
<script src="../js/d3.min.js"></script>
<script type="text/javascript" src="../js/jquery-1.11.0.js"></script>
<script type="text/javascript">
(function() {
function dateTime(timestamp) {
	var date = new Date(timestamp);
	var formatted = "";

	if (date.getDate() < 10)
		formatted += "0";
	formatted += date.getDate() + ".";

	if ((1 + date.getMonth()) < 10)
		formatted += "0";
	formatted += (1 + date.getMonth()) + ".";

	formatted += date.getFullYear() + " ";

	if (date.getHours() < 10)
		formatted += "0";
	formatted += date.getHours() + ":";

	if (date.getMinutes() < 10)
		formatted += "0";

	formatted += date.getMinutes() + ":";

	if (date.getSeconds() < 10)
		formatted += "0";

	formatted += date.getSeconds();

	return formatted;
}

window['DashboardUtils'] = {};
window['DashboardUtils']['dateTime'] = dateTime;
})();

	var url = "<%=request.getParameter("url")%>";
	var title = "<%=request.getParameter("title")%>";
	
	$( document ).ready(function() {
		$( "#label" ).append( decodeURIComponent(title));
	});	 

	
$.get(url, function (data2) {
  data = data2;
  var keys = Object.keys(data);
  var last = keys[keys.length-1];
  console.log(data[last].value);
  $( "#value" ).append( formatResult(data[last].value));
});
</script>
<table>
	<tr>
		<td id="label"></td>
	</tr>
	<tr>
		<td id="value"></td>
	</tr>
</table>
<script type="text/javascript">
function formatResult(input) {
	var output = input;
	<%=request.getParameter("formatFunction")%>
	return output;
}
</script>
</body>
</html>