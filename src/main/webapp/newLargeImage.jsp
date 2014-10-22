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
<title>Large Image - Muvi Manager</title>
</head>
<body>
	<h1>Create new large Image</h1>
	<p><jsp:include page="partials/message.jsp"></jsp:include></p>
	<form action="newLargeImage.do" method="post" id="xmlEditor">
		<input type="hidden" name="scenarioId" value="${paramScenarioId}">
		<input type="hidden" name="sceneNumber" value="${paramSceneNumber}">
		<input type="hidden" name="editMode" id="editMode" value="${editMode}">
		<input type="hidden" name="largeImageNumber" id="largeImageNumber" value="${largeImageNumber}">
		<table>
			<tr>
				<td>
					<table>
						<tr>
							<td><label for="imageUrl">Image URL: </label></td>
							<td><input type="text" id="imageUrl" name="imageUrl"
								value="${paramUrl }"></td>
						</tr>
						<tr>
							<td><label for="paramImageHeight">Height: </label></td>
							<td><input type="number" id="paramImageHeight"
								name="paramImageHeight" value="${paramImageHeight}"></td>
						</tr>
						<tr>
							<td><label for="paramImageWidth">Width: </label></td>
							<td><input type="number" id="paramImageWidth"
								name="paramImageWidth" value="${paramImageWidth}"></td>
						</tr>
						<tr>
							<td><label for="paramImageBackground">Background
									color: </label></td>
							<td><select name="paramImageBackground" id="paramImageBackground"
								style="background-color: rgb(${paramImageBackground}); width: 147px; display: block;"
								onchange="this.style.backgroundColor = this.options[this.selectedIndex].style.backgroundColor;
								setTextStyle('style', 'bg_color', 'txt_color');">
									<c:forEach items="${paramColors }" var="bgcolor">
										<c:choose>
											<c:when test="${bgcolor.equals(paramImageBackground) }">
												<option style="background-color: rgb(${bgcolor})"
													value="${bgcolor}" selected="selected">&nbsp;</option>
											</c:when>
											<c:otherwise>
												<option style="background-color: rgb(${bgcolor})"
													value="${bgcolor}">&nbsp;</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
							</select></td>
						</tr>
					</table>
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