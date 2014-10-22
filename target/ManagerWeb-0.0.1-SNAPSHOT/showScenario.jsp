<%@page import="de.fraunhofer.iao.muvi.managerweb.web.MainController"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="style/manager.css">
<title>Scenario editor - MuVi Manager</title>
<script type="text/javascript" src="js/jquery-1.11.0.js"></script>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link rel="stylesheet" href="style/jquery-ui-1.10.4.custom.css">
<script type="text/javascript">
	function showScene(scenarioId, sceneNumber) {
		$('#message').html(
				"Asking MuVi to show scene " + sceneNumber + " from scenario "
						+ scenarioId + "...");
		$.ajax({
			type : "POST",
			url : "showScene.do",
			data : {
				scenarioId : scenarioId,
				sceneNumber : sceneNumber
			},
			success : function(data) {
				$('#message').html(data);
			},
			error : function(e) {
				$('#message').html("Error communicating!");
			}
		});
	}
	
	function sceneScreenShot(scenarioId, sceneNumber) {
		if (confirm('Please confirm if you want to take screen shot of this scene?')) {
			document.getElementById("freezer").style.display = "block";	
			$.ajax({
				type : "POST",
				url : "sceneScreenShot.do",
				data : {
					scenarioId : scenarioId,
					sceneNumber : sceneNumber
				},
				success : function(data) {
					
					if (data.match("^Error")) {
						$('#message').html("Error taking scene shot: " + data);
						
						} else {
							document.getElementById("screenshot").src = data;
							$( "#popUpImage" ).dialog( "open" );
						}
					document.getElementById("freezer").style.display = "none";

				},
				error : function(e) {
					$('#message').html("Error taking scene shot: " + e);
					document.getElementById("freezer").style.display = "none";
				}
			 });
		}
	}
	
	function deleteScene(scenarioId, sceneNumber) {
		if (confirm('Are you sure you want to delete the scene ${scene.name}?')) {
			$.ajax({
				type : "POST",
				url : "editscene.do",
				data : {
					scenarioId : scenarioId,
					sceneNumber : sceneNumber,
					action: 'deleteScene'
				},
				success : function(data) {
					location.reload();
				},
				error : function(e) {
					$('#message').html("Error deleting scene!");
				}
			});
		}
	}

	 $(function() {
		 $("#popUpImage").dialog({
			 autoOpen: false,
			 resizable: false,
			 draggable: false,
			 width: "800px",
			 height: "auto",
			 show: {
				 effect: "blind",
				 duration: 300
			 },
			 hide: {
				 effect: "blind",
				 duration: 100
			 }
		 });
	 });
	 
	 function hideScreenShot() {
		 $("#popUpImage").dialog( "close" );
	 }
	
</script>
</head>
<body>
	<div id="popUpImage" title="Image saved">
		<a href="javascript:hideScreenShot();">
			<img alt="ERROR Saving image!!" src="" width="100%" height="100%" id="screenshot">
		</a>
	</div>

	<h1>Scenario editor</h1>

	<h2>Scenario ${scenario.name} (${scenario.id})</h2>
	<p><jsp:include page="partials/message.jsp"></jsp:include></p>

	<ol id="scenesList">
		<c:forEach var="scene" items="${scenario.scenes}"
			varStatus="loopCounter">
			<li>Scene ${loopCounter.count} ${scene.name} <br> <%@include
					file="partials/sceneContents.jsp"%> <br>
						
				<a class="showSceneLink" href="javascript:showScene(${scenario.id},${loopCounter.count})">
					<c:choose>
						<c:when test="${scene.screenshot != null}"><img class="largeScreenshot" src="${scene.screenshot}" alt="Screenshot of scene ${loopCounter.count} ${scene.name}" title="Show scene ${loopCounter.count} ${scene.name}"></c:when>
						<c:otherwise>Show scene ${loopCounter.count}</c:otherwise>
					</c:choose>
				</a>
				
				<br>
				
				<a href="javascript:sceneScreenShot(${scenario.id},${loopCounter.count})">SceneShot</a>
				<a href="#"
				onclick="deleteScene(${scenario.id},${loopCounter.count}); return false;">Delete</a>
				<c:if test="${loopCounter.count > 1}">
					<a href="#">▲ Up ▲</a>
				</c:if> <c:if test="${loopCounter.count < fn:length(scenario.scenes)}">
					<a href="#">▼ Down ▼</a>
				</c:if>
				<br>
				<a href="newLargeImage.do?scenarioId=${scenario.id }&amp;sceneNumber=${loopCounter.count }">+ big img</a>
				<a href="newLargeText.do?scenarioId=${scenario.id }&amp;sceneNumber=${loopCounter.count }">+ big txt</a>	
				<a href="newLargeURL.do?scenarioId=${scenario.id }&amp;sceneNumber=${loopCounter.count }">+ big URL</a>	
				<br>
				<a href="newAnimatedText.do?scenarioId=${scenario.id }&amp;sceneNumber=${loopCounter.count }">+	anim txt</a>
				<a href="newSearchResults.do?scenarioId=${scenario.id }&amp;sceneNumber=${loopCounter.count }">+ search</a>
				<a href="newURLList.do?scenarioId=${scenario.id }&amp;sceneNumber=${loopCounter.count }">+ URLs</a>
				<br>
				<a href="newImage.do?scenarioId=${scenario.id }&amp;sceneNumber=${loopCounter.count }">+ image</a>
				<a href="newSimpleURL.do?scenarioId=${scenario.id }&amp;sceneNumber=${loopCounter.count }">+ URL</a>
				<a href="newSimpleText.do?scenarioId=${scenario.id }&amp;sceneNumber=${loopCounter.count }">+ text</a>
			</li>
			
			<p>&nbsp;</p>
			
		</c:forEach>
	</ol>
	<p class="mainLink">
		<a href="newScene.do?scenarioId=${scenario.id }">Add new scene</a>
	</p>
	<p>&nbsp;</p>
	<p class="mainLink">
		<a href="xml.do?scenarioId=${scenario.id}">XML</a>
	</p>
	<p>&nbsp;</p>
	<form action="scenarios.do" id="deleteForm" method="post">
		<input type="hidden" name="id" value="${scenario.id }"> <input
			type="submit" name="<%=MainController.ACTION_DELETE %>"
			value="Delete"
			onclick="return confirm('Are you sure you want to delete scenario ${scenario.id}?');">
	</form>
	<p>&nbsp;</p>
	<p id="scenariosLink">
		<a href="scenarios.do">Scenarios</a>
	</p>
	<p id="homeLink">
		<a href="index.jsp">Home</a>
	</p>
	
	<p>&nbsp;</p>
	
<div id="freezer" style="display: none; height:100%;width:100%; background-color:rgba(0,0,0,0.7); position: fixed; top:0px; right:0px">
<p style="text-align:center; position: relative; top:50%; color:rgb(180,220,211)">Please Wait...</p>
<img style="position: relative; display:block; margin-left: auto; margin-right: auto; top:50%; " alt="" src="./style/ajax-loader.gif">
</div>
</body>
</html>