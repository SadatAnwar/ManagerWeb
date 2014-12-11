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
<title>VisML Dashboard - Muvi Manager</title>
</head>
<body>
	<h1>Create new VisML Dashboard</h1>
	<p><jsp:include page="partials/message.jsp"></jsp:include></p>
	<form action="saveVisMLDashboard.do" method="post" id="xmlEditor">
		<input type="hidden" name="scenarioId" value="${paramScenarioId}">
		<input type="hidden" name="editMode" id="editMode" value="${editMode}">
		<input type="hidden" name="sceneNumber" value="${paramSceneNumber}">
		<table>
			<tr>
				<td>
					<table>
						<tr>
							<td><label for="vismlUrl">URL for VisML: </label></td>
							<td><input type="text" id="vismlUrl" name="vismlUrl" size="100"
								value="${vismlUrl}"></td>
						</tr>
					</table>
				</td>
				<td><jsp:include page="partials/showXMLTextfield.jsp"></jsp:include></td>
			</tr>
		</table>
		
		
		<jsp:include page="partials/submit.jsp"></jsp:include>
	</form>
	<jsp:include page="partials/formFooter.jsp"></jsp:include>
</body>
</html>