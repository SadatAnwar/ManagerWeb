<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="style/manager.css">
<title>Scenarios - Muvi Manager</title>
<script type="text/javascript">
 function deleteScenario(id) {
	 if(confirm('Are you sure you want to delete scenario '+id+' ?')) {
		 document.forms["deleteScenario" + id].submit();
	 }
	 
 }
</script>
</head>

<body>

	<h1>Scenarios</h1>

	<p><jsp:include page="partials/message.jsp"></jsp:include></p>

	<c:choose>
		<c:when test="${tag.length() > 0}">Select a scenario with the tag ${tag} (<a
				href="scenarios.do">All scenarios</a>)</c:when>
		<c:otherwise>
			<h2>Select a scenario</h2>
		</c:otherwise>
	</c:choose>

	<ol id="scenariosList">
		<c:forEach var="scenario" items="${scenarios}">
			<li><span class="id">${scenario.id}</span>
				<span class="date">${scenario.date}</span> <br> 
				
				<c:choose>
					<c:when test="${sessionScope.mode == 'edit'}">
						<a class="playScenarioLink" href="showScenario.do?id=${scenario.id}">${scenario.name}</a><br> 
						<a href="playScenario.do?id=${scenario.id}">Open</a>
						<a href="xml.do?scenarioId=${scenario.id}">XML</a>
						<a href ="javascript:deleteScenario(${scenario.id})">Delete</a>
						<form id="deleteScenario${scenario.id}" action="scenarios.do" method="post">
							<input name="id" value="${scenario.id}" type="hidden">
							<input name="delete" value="delete" type="hidden">
						</form>
						<!-- <a href="scenarios.do?action=delete&amp;id=${scenario.id}"
							onclick="return confirm('Are you sure you want to delete scenario ${scenario.id}?');">Delete</a> -->
					</c:when>
					<c:otherwise>
						<a class="playScenarioLink"href="playScenario.do?id=${scenario.id}">${scenario.name}</a> 
					</c:otherwise>
				</c:choose>
				
				<c:if test="${0 < fn:length(scenario.tags)}">
				<span class="tags"><c:forEach var="tag"
						items="${scenario.tags}">
						<a class="tag" title="${tag}" href="scenarios.do?tag=${tag}">${tag}</a>
					</c:forEach>
			</span>
			</c:if>
			
			</li>
		</c:forEach>
	</ol>
	<c:choose>
		<c:when test="${sessionScope.mode == 'edit' }">
			<p class="mainLink">
				<a href="newScenario.do">New scenario</a>
			</p>
		</c:when>
	</c:choose>

	<h2>Tags</h2>
	<div id="tags">
		<c:forEach var="aTag" items="${tags}">
			<a class="tag" href="scenarios.do?tag=${aTag}">${aTag}</a>
		</c:forEach>
	</div>

	<p id="homeLink">
		<a href="index.jsp">Home</a>
	</p>
	
	<p>&nbsp;</p>

</body>
</html>