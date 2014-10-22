<%@ page import="de.fraunhofer.iao.muvi.managerweb.web.TextController"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="style/manager.css">
<script type="text/javascript" src="js/jquery-1.11.0.js"></script>
<script src="js/style.js"></script>
<title>Large Text - Muvi Manager</title>
</head>
<body onLoad="setTextStyle('style', 'bg_color', 'txt_color');">
	<h1>Create new large Text</h1>
	<p><jsp:include page="partials/message.jsp"></jsp:include></p>
	<form action="newLargeText.do" method="post" id="xmlEditor">
		<input type="hidden" name="scenarioId" value="${paramScenarioId}">
		<input type="hidden" name="sceneNumber" value="${paramSceneNumber}">
		<input type="hidden" name="editMode" id="editMode" value="${editMode}">
		<input type="hidden" name="largeTextNumber" id="largeTextNumber" value="${largeTextNumber}">
		<table>
			<tr>
				<td>
					<table><jsp:include page="partials/textWithStyle.jsp"></jsp:include></table>
				</td>
				<td><jsp:include page="partials/showXMLTextfield.jsp"></jsp:include></td>
			</tr>
		</table>
		
		<jsp:include page="partials/screenSelector.jsp">
			<jsp:param name="enableSingleSelection" value="false" />
			<jsp:param name="enableMultipleSelection" value="false" />
			<jsp:param name="enableRectangleSelection" value="true" />
		</jsp:include>
		
		<jsp:include page="partials/submit.jsp"></jsp:include>
	</form>
	<jsp:include page="partials/formFooter.jsp"></jsp:include>
</body>
</html>