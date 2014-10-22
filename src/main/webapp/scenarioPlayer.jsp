<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="style/manager.css">
<title>Scenario player - MuVi Manager</title>
<script type="text/javascript" src="js/jquery-1.11.0.js"></script>
<script type="text/javascript">

function showScene(scenarioId, sceneNumber) {
	$('#message').html("Asking MuVi to show scene " + sceneNumber + " fron scenario " + scenarioId + "...");
	$.ajax({
		  type: "POST",
		  url: "showScene.do",
		  data: { scenarioId: scenarioId, sceneNumber: sceneNumber },
		  success: function(data) {
		 	  $('#message').html(data);
			  },
	     error: function(e) {
	  	   $('#message').html("Error communicating!");
			  }
		});
}

//Code involved in playing the entire scenario
var Sceneshot;
var SceneName;
var delayTimeOut, secondsLeft;
var nextScene, timer;
var currentScene;

function checkShowScenario(scenarioId, maxScene, sceneId) {
	if(confirm('Proceed with showing complete scenario?')){
		showScenario(scenarioId, maxScene, sceneId);
	}
}


function showScenario(scenarioId, maxScene, sceneId) {
	if (sceneId<maxScene){
		currentScene = sceneId;
		document.getElementById("senarioPlayer").style.display = "block";	
		$.ajax({
			  type: "POST",
			  url: "playEntireScenario.do",
			  data: { scenarioId: scenarioId, sceneId: sceneId },
			  dataType: "xml",
			  error: function (xhr, ajaxOptions, thrownError) {
				  document.getElementById("senarioPlayer").style.display = "none";
			      alert(thrownError);
			  },
			  success: function(xml) {
				  
					  $(xml).find('Scenario').each(function(){
							  //SceneName =  $(this).find("Scene").text();							 
							  Sceneshot =  $(xml).find("Sceneshot").text();		 
							  delayTimeOut = $(xml).find("Duration").text();
					  });
					  secondsLeft = (delayTimeOut/1000)-1;
					  document.getElementById("textNowShowing").innerHTML = "Now showing scene " +(sceneId+1)+" of "+maxScene ;
					  if(Sceneshot!="null"){
						  document.getElementById("sceneShowing").style.height = "300px";
						  document.getElementById("sceneShowing").src = Sceneshot;
					  }
					  else{
						  document.getElementById("sceneShowing").style.height = "auto";
						  document.getElementById("sceneShowing").src = "";
					  }
					  nextScene = setTimeout(function(){next(scenarioId, maxScene);}, delayTimeOut);
					  timer = setInterval(function(){updateTimer();}, 1000);
					  }
			});
		//after the call
	}
	
	else{
		document.getElementById("senarioPlayer").style.display = "none";
		alert("Show scenario complete!");
	}
}

function updateTimer(){
	document.getElementById("delayScenonds").innerHTML = "Next scene in "+secondsLeft+" sec";
	if(secondsLeft>0){
		secondsLeft=secondsLeft-1;
	}
	else{
		document.getElementById("delayScenonds").innerHTML = "Next scene now..";
	}
}

function cencel(){
	clearTimeout(nextScene);
	clearTimeout(timer);
	document.getElementById("senarioPlayer").style.display = "none";
}

function next(scenarioId, maxScene){
	clearTimeout(nextScene);
	clearTimeout(timer);
	showScenario(scenarioId, maxScene, currentScene+1);
}

function previous(scenarioId, maxScene){	
	if(currentScene==0){
		alert("This is the first scene of the current scenario");
	}
	else{
		clearTimeout(nextScene);
		clearTimeout(timer);
		showScenario(scenarioId, maxScene, currentScene-1);
	}
}

</script>
</head>
<body>


	<h1>Play scenario ${scenario.name} (${scenario.id})</h1>
	
	<p id="message"></p>

	<ol id="scenesList">
		<c:forEach var="scene" items="${scenario.scenes}"
			varStatus="loopCounter">
			<li>
				${loopCounter.count}. ${scene.name} 
				<br>
				<a class="showSceneLink" href="javascript:showScene(${scenario.id},${loopCounter.count})">
					<c:choose>
						<c:when test="${scene.screenshot != null}"><img class="largeScreenshot" src="${scene.screenshot}" alt="Screenshot of scene ${loopCounter.count} ${scene.name}" title="Show scene ${loopCounter.count} ${scene.name}"></c:when>
						<c:otherwise>Show scene ${loopCounter.count}</c:otherwise>
					</c:choose>
				</a>
			</li>
		</c:forEach>
	</ol>

<p class="mainLink"><a href="javascript:checkShowScenario(${scenario.id}, ${scenario.scenesCount}, 0)">Play scenario</a></p>

<p id="scenariosLink"><a href="scenarios.do">Scenarios</a></p>

<p id="homeLink"><a href="index.jsp">Home</a></p>

<p>&nbsp;</p>

<div id="senarioPlayer" style="display: none; height:100%;width:100%; background-color:rgba(0,0,0,0.7); position: fixed; top:0px; left:0px;">
	<div style="padding: 10px; position: relative; height:300px; margin: auto ;">
	<p id="textNowShowing" style="font-size: 3em;text-align:center; position: relative;  color:rgb(180,220,211)">Now showing...</p>
	<img id="sceneShowing" style="font-size:2em; position: relative; display:block; height: auto; width:auto; margin: auto;"  alt="Scene shot not available" src="">
	<p id="delayScenonds" style="font-size:3em; text-align:center; position: relative;  color:rgb(180,220,211)"></p>
	<p style="text-align:center;font-size: 1.5em; padding:5px; position: relative; top:5px;color:rgb(180,220,211)">
		<a style="padding:10px; margin:10px;" href="javascript:previous(${scenario.id}, ${scenario.scenesCount})">￩ Prev</a>
		<a style="padding:10px; margin:10px;" href="javascript:cencel()">Stop</a>
		<a style="padding:10px; margin:10px;" href="javascript:next(${scenario.id}, ${scenario.scenesCount})">Next ￫</a>
	</p>
	</div>
</div>
</body>
</html>