<%@page import="de.fraunhofer.iao.muvi.managerweb.web.ImageController"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="style/manager.css">
<script type="text/javascript" src="js/jquery-1.11.0.js"></script>
<title>Image - Muvi Manager</title>
</head>
<body>
	<h1>Create new Image Screen</h1>
	<p><jsp:include page="partials/message.jsp"></jsp:include></p>
	<form action="newImage.do" method="post" id="xmlEditor">
		<input type="hidden" name="scenarioId" value="${paramScenarioId}">
		<input type="hidden" name="sceneNumber" value="${paramSceneNumber}">
		<input type="hidden" name="editMode" id="editMode" value="${editMode}">
		<input type="hidden" name="imageNumber" id="imageNumber" value="${imageNumber}">
		<table>
			<tr>
				<td>
					<table>
						<tr>
							<td><label for="imageUrl">Image URL: </label></td>
							<td><input type="text" id="imageUrl" name="imageUrl"
								value="${paramUrl}"></td>
						</tr>
						<tr>
							<td><label for="imageScale">Scale: </label></td>
							<td><select name="imageScale" id="imageScale">
									<c:forEach var="scale" items="${scaleValues}">
										<c:choose>
											<c:when test="${scale == paramScale}">
												<option selected="selected">${scale}</option>
											</c:when>
											<c:otherwise>
												<option>${scale}</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
							</select></td>
						</tr>
						<tr>
					</table>
				</td>
				<td><jsp:include page="partials/showXMLTextfield.jsp"></jsp:include></td>
			</tr>
		</table>
		
		<jsp:include page="partials/screenSelector.jsp">
			<jsp:param name="enableSingleSelection" value="true" />
			<jsp:param name="enableMultipleSelection" value="false" />
			<jsp:param name="enableRectangleSelection" value="false" />
		</jsp:include>
		
		<jsp:include page="partials/submit.jsp"></jsp:include>
	</form>
	<jsp:include page="partials/formFooter.jsp"></jsp:include>
</body>
</html>