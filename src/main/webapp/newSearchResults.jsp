<%@page
	import="de.fraunhofer.iao.muvi.managerweb.web.SearchResultsController"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="style/manager.css">
<script type="text/javascript" src="js/jquery-1.11.0.js"></script>
<title>Search Results - Muvi Manager</title>
</head>
<body>
	<h1>Create new Search Results</h1>
	<p><jsp:include page="partials/message.jsp"></jsp:include></p>
	<form action="newSearchResults.do" method="post" id="xmlEditor">
		<input type="hidden" name="scenarioId" value="${paramScenarioId}">
		<input type="hidden" name="sceneNumber" value="${paramSceneNumber}">
		<table>
			<tr>
				<td>
					<table>
						<tr>
							<td><label for="query">Query: </label></td>
							<td><input type="text" id="query"
								name="query" value="${paramQuery }"></td>
						</tr>
						<tr>
							<td><label for="type">Type: </label></td>
							<td>
								<select name="type" id="type">
									<c:forEach var="type" items="${typeValues}">
										<c:choose>
											<c:when test="${type== paramType}">
												<option selected="selected">${type}</option>
											</c:when>
											<c:otherwise>
												<option>${type}</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
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