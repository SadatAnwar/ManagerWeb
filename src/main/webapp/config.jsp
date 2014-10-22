<%@page import="de.fraunhofer.iao.muvi.managerweb.backend.Database"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="style/manager.css">
<title>Configuration - MuVi Manager</title>
</head>
<body>

<h1>Configuration - MuVi Manager</h1>

<form id= 'configuration' method="post" action="saveConfig.do">

<input type="checkbox" name="debugMode" id="debugMode" value="debugMode" ${debugModeChecked}><label for="debugMode">Run in debug mode</label><br>
<input type="checkbox" name="websocketEnabled" id="websocketEnabled" value="websocketEnabled" ${websocketEnabled}><label for="websocketEnabled">Use Websockets</label><br>

<label for="managerIP">Manager IP: </label><input type="text" id="managerIP" name="managerIP" value="${managerIP}" ><br>
<label for="managerURL">Manager URL: </label><input type="text" id="managerURL" name="managerURL" value="${managerURL}" ><br>
<label for="customURL">Custom start URL: </label><input type="text" id="customURL" name="customURL" value="${customURL}" ><br>
<label for="powerOutletIP">Power outlet IP: </label><input type="text" id="powerOutletIP" name="powerOutletIP" value="${powerOutletIP}"><br>
<label for="DC1IP">Display Computer 1 IP: </label><input type="text" id="DC1IP" name="DC1IP" value="${DC1IP}">
<input type="checkbox" name="DC1Status" id="DC1Status" value="1" ${DC1Status}><br>

<label for="DC2IP">Display Computer 2 IP: </label><input type="text" id="DC2IP" name="DC2IP" value="${DC2IP}">
<input type="checkbox" name="DC2Status" id="DC2Status" value="1" ${DC2Status}><br>

<label for="DC3IP">Display Computer 3 IP: </label><input type="text" id="DC3IP" name="DC3IP" value="${DC3IP}">
<input type="checkbox" name="DC3Status" id="DC3Status" value="1" ${DC3Status}><br>

<label for="DC3IP">Display Computer 4 IP: </label><input type="text" id="DC4IP" name="DC4IP" value="${DC4IP}">
<input type="checkbox" name="DC4Status" id="DC4Status" value="1" ${DC4Status}><br>

<label for="dcUser">DC User: </label><input type="text" id="dcUser" name="dcUser" value="${dcUser}" ><br>
<label for="dcPassword">DC Password: </label><input type="password" id="dcPassword" name="dcPassword" value="${dcPassword}" ><br>

<label for="DC1screens">Display Computer 1 screens: </label><input type="text" name="DC1screens" id="DC1screens" value="${DC1screens}"><br>
<label for="DC2screens">Display Computer 2 screens: </label><input type="text" name="DC2screens" id="DC2screens" value="${DC2screens}"><br>
<label for="DC3screens">Display Computer 3 screens: </label><input type="text" name="DC3screens" id="DC3screens" value="${DC3screens}"><br>
<label for="DC4screens">Display Computer 4 screens: </label><input type="text" name="DC4screens" id="DC4screens" value="${DC4screens}"><br>

<h2>Global ID to local ID mapping</h2>

<table>

	<tr>
		<td>&nbsp;</td>
		<td>C 1</td>
		<td>C 2</td>
		<td>C 3</td>
		<td>C 4</td>
		<td>C 5</td>
		<td>C 6</td>
	</tr>
	
	<tr>
		<td>R 1</td>
		<td><label for="localID1">1: </label><input type="text" id="localID1" name="localID1" value="${localID1}"></td>
		<td><label for="localID2">2: </label><input type="text" id="localID2" name="localID2" value="${localID2}"></td>
		<td><label for="localID3">3: </label><input type="text" id="localID3" name="localID3" value="${localID3}"></td>
		<td><label for="localID4">4: </label><input type="text" id="localID4" name="localID4" value="${localID4}"></td>
		<td><label for="localID5">5: </label><input type="text" id="localID5" name="localID5" value="${localID5}"></td>
		<td><label for="localID6">6: </label><input type="text" id="localID6" name="localID6" value="${localID6}"></td>
	</tr>
	
	<tr>
		<td>R 2</td>
		<td><label for="localID7">7: </label><input type="text" id="localID7" name="localID7" value="${localID7}"></td>
		<td><label for="localID8">8: </label><input type="text" id="localID8" name="localID8" value="${localID8}"></td>
		<td><label for="localID9">9: </label><input type="text" id="localID9" name="localID9" value="${localID9}"></td>
		<td><label for="localID10">10: </label><input type="text" id="localID10" name="localID10" value="${localID10}"></td>
		<td><label for="localID11">11: </label><input type="text" id="localID11" name="localID11" value="${localID11}"></td>
		<td><label for="localID12">12: </label><input type="text" id="localID12" name="localID12" value="${localID12}"></td>
	</tr>
	
	<tr>
		<td>R 3</td>
		<td><label for="localID13">13: </label><input type="text" id="localID13" name="localID13" value="${localID13}"></td>
		<td><label for="localID14">14: </label><input type="text" id="localID14" name="localID14" value="${localID14}"></td>
		<td><label for="localID15">15: </label><input type="text" id="localID15" name="localID15" value="${localID15}"></td>
		<td><label for="localID16">16: </label><input type="text" id="localID16" name="localID16" value="${localID16}"></td>
		<td><label for="localID17">17: </label><input type="text" id="localID17" name="localID17" value="${localID17}"></td>
		<td><label for="localID18">18: </label><input type="text" id="localID18" name="localID18" value="${localID18}"></td>
	</tr>
	
	<tr>
		<td>R 4</td>
		<td><label for="localID19">19: </label><input type="text" id="localID19" name="localID19" value="${localID19}"></td>
		<td><label for="localID20">20: </label><input type="text" id="localID20" name="localID20" value="${localID20}"></td>
		<td><label for="localID21">21: </label><input type="text" id="localID21" name="localID21" value="${localID21}"></td>
		<td><label for="localID22">22: </label><input type="text" id="localID22" name="localID22" value="${localID22}"></td>
		<td><label for="localID23">23: </label><input type="text" id="localID23" name="localID23" value="${localID23}"></td>
		<td><label for="localID24">24: </label><input type="text" id="localID24" name="localID24" value="${localID24}"></td>
	</tr>
	
	<tr>
		<td>R 5</td>
		<td><label for="localID25">25: </label><input type="text" id="localID25" name="localID25" value="${localID25}"></td>
		<td><label for="localID26">26: </label><input type="text" id="localID26" name="localID26" value="${localID26}"></td>
		<td><label for="localID27">27: </label><input type="text" id="localID27" name="localID27" value="${localID27}"></td>
		<td><label for="localID28">28: </label><input type="text" id="localID28" name="localID28" value="${localID28}"></td>
		<td><label for="localID29">29: </label><input type="text" id="localID29" name="localID29" value="${localID29}"></td>
		<td><label for="localID30">30: </label><input type="text" id="localID30" name="localID30" value="${localID30}"></td>
	</tr>
	
	<tr>
		<td>R 6</td>
		<td><label for="localID31">31: </label><input type="text" id="localID31" name="localID31" value="${localID31}"></td>
		<td><label for="localID32">32: </label><input type="text" id="localID32" name="localID32" value="${localID32}"></td>
		<td><label for="localID33">33: </label><input type="text" id="localID33" name="localID33" value="${localID33}"></td>
		<td><label for="localID34">34: </label><input type="text" id="localID34" name="localID34" value="${localID34}"></td>
		<td><label for="localID35">35: </label><input type="text" id="localID35" name="localID35" value="${localID35}"></td>
		<td><label for="localID36">36: </label><input type="text" id="localID36" name="localID36" value="${localID36}"></td>
	</tr>

</table>

<input type="checkbox" name="showNumbers" value="showNumbers" id="showNumbers"><label for="showNumbers">Show numbers</label>

<input type="button" value="Save" onclick="validateAndSubmit();">

</form>

<p id="homeLink"><a href="index.jsp">Home</a></p>
<script type="text/javascript" src="js/jquery-1.11.0.js"></script>
<script type="text/javascript">

function validateAndSubmit() {
	var error = false;
	var dc1Ip = (document.getElementById("DC1IP").value.length!=14);
	var dc2Ip = (document.getElementById("DC2IP").value.length!=14);
	var dc3Ip = (document.getElementById("DC3IP").value.length!=14);
	var dc4Ip = (document.getElementById("DC4IP").value.length!=14);
	
	var dc1Status = document.getElementById("DC1Status").checked;
	var dc2Status = document.getElementById("DC2Status").checked;
	var dc3Status = document.getElementById("DC3Status").checked;
	var dc4Status = document.getElementById("DC4Status").checked;

	if(dc1Status && dc1Ip) {
		error = true;
	}
	if(dc2Status && dc2Ip) {
		error = true;
	}
	if(dc3Status && dc3Ip) {
		error = true;
	}
	if(dc4Status && dc4Ip) {
		error = true;
	}
	if(!error) {
		document.forms["configuration"].submit();
	}
	else {
		alert ("All Active DCs must have valid IP");
	}
	
}
</script>
</body>
</html>