<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="style/manager.css">
<title>New Scene - Muvi Manager</title>
</head>
<body>
	<h1>New Scene</h1>
	<p><jsp:include page="partials/message.jsp"></jsp:include></p>
	<form action="newScene.do" method="post" id="xmlEditor">
		<input type="hidden" name="scenarioId" value="${scenarioId }">
		<table>
			<tr>
				<td><label for="sName">Name</label></td>
				<td><input type="text" id="sName" name="sName"></td>
			</tr>
			<tr>
				<td><label for="sDesc">Description</label></td>
				<td><textarea id="sDesc" name="sDesc"></textarea></td>
			</tr>
			<tr>
				<td><label for="sDuration">Duration</label></td>
				<td><input type="number" id="sDuration" name="sDuration"></td>
			</tr>
			<tr>
				<td><label for="sResetcolor">Reset color</label></td>
				<td><select name="sResetcolor" id="sResetcolor"
					style="width: 147px; display: block; background-color: rgb(${paramResetColor})"
					onchange="this.style.backgroundColor = this.options[this.selectedIndex].style.backgroundColor;">
						<option value="" style="background-color: white"
							<c:choose>
								<c:when test="${paramResetColor == ''}">
									selected="selected" 
								</c:when>
							</c:choose>>NONE</option>
						<c:forEach items="${paramColors }" var="reset_color">
							<c:choose>
								<c:when test="${reset_color == paramResetColor }">
									<option style="background-color: rgb(${reset_color})"
										value="${reset_color}" selected="selected">&nbsp;</option>
								</c:when>
								<c:otherwise>
									<option style="background-color: rgb(${reset_color})"
										value="${reset_color}">&nbsp;</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
				</select></td>
			</tr>
		</table>
		<input type="submit" name="Save" value="Save">
	</form>
	<p id="scenariosLink">
		<a href="scenarios.do">Scenarios</a>
	</p>
	<p id="homeLink">
		<a href="index.jsp">Home</a>
	</p>

</body>
</html>