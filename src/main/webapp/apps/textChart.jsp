<!DOCTYPE html>

<html>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<head>
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
var url = 'https://dashboard.iao.fraunhofer.de/arpos/dashboard-values?&value=anzahl_aktiver_werkstaetten_kpi'; 
	var data;  
	var title = "PLZ Bereiche";
	
$.get(url, function (data2) {
  data = data2;
  var keys = Object.keys(data);
  var last = keys[keys.length-1];
  console.log(data[last].value);
  $( "#value" ).append( data[last].value);
  $( "#label" ).append( decodeURIComponent(escape("Anzahl aktiver Werkstätten")));
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
</body>
</html>