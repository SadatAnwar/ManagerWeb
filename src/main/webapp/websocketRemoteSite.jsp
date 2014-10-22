<!DOCTYPE html>
<html>
<head>
  <title>Websocket MuVi Remote</title>
 <script src="js/jquery-1.11.0.js"></script>
  <script>
	var remoteData = new Object();
	remoteData.id = "1";
	
	var socket = {
			connect : function(screenID) {
				this._screenID = screenID;
				var location = "ws://${wsServerURL}";
				this._ws = new WebSocket(location);
				this._ws.onopen = this._onopen;
				this._ws.onmessage = this._onmessage;
				this._ws.onclose = this._onclose;
				this._ws.onerror = this._onerror;

				//this.functions = [];

				return this;
			},

			_onopen : function() {
				//data = new Object();
				//data.id = "10";
				//data.message = "Thanks for welcoming me!";
				//room._sendEvent("welcome", data);
			},

			_onmessage : function(m) {
				if (m.data) {
					console.log("DEBUG Received Message: " + m.data);
					var jsonObject = JSON.parse(m.data);
					if (jsonObject.event) {
						socket._call(jsonObject.event, jsonObject.data);
					}
				}
			},

			_onclose : function(m) {
				this._ws = null;
			},

			_onerror : function(e) {
				alert(e);
			},

			disconnect : function() {
				socket._sendEvent("disconnect", null);
			},

			on : function(functionName, functionHandler) {				
				if(!this.functions || this.functions == undefined)
					this.functions = [];
				var newFunction = new Object();
				newFunction.name = functionName;
				newFunction.handler = functionHandler;
				socket.functions.push(newFunction);
			},

			_call : function(functionName, args) {
				socket.functions.forEach(function(entry) {
					if (entry.name == functionName) {
						entry.handler(args);
					}
				});
			},

			_sendEvent : function(eventName, data) {
				var socketMessage = new Object();
				socketMessage.event = eventName;
				socketMessage.data = data;
				if (socket._ws)
					socket._ws.send(JSON.stringify(socketMessage));
			},

			emit : function(eventName, data) {
				socket._sendEvent(eventName, data);
			}
		};  
	
	socket.connect(remoteData.id);	

    // Warten auf Nachrichten
    socket.on('welcome', function (data) {
      console.log('[socket.io] "welcome"-Event vom Server empfangen:\n' + data);
 
      // Eigenen Event vom Client an den Server schicken
      socket.emit('register_remote_client', remoteData);
    });
	
    socket.on('message', function (data) {
	  console.log("Received Message: " + data);
	  
    });
	
    socket.on('remote_feedback_screen_registered', function (data) {
	  console.log("Received Feedback Message: " + data);
	  if(data && data.id)
	  {
		var id=data.id;
		visualizeScreenRegistration(id, true);
	  }	  
    });	
	
    socket.on('remote_feedback_screen_unregistered', function (data) {
	  console.log("Received Feedback Message: " + data);
	  if(data && data.id)
	  {
		var id=data.id;
		visualizeScreenRegistration(id, false);
	  }	  
    });		
    
    socket.on('screenshot_response', function (data) {
  	  console.log("Received Screenshot Response:");
  	 	 handleScreenshotData(data, data.id);
      });		    
	
	socket.send = function (data) {
		socket.emit('message', data);
	};
	
	function init()
	{
		setupScreenUI();
		//Retrieve Information about which screen is already connected
		sendUpdateDiscoveryRequest();
		
		reloadVideo();
		
		initVideoSynch();
		
		updateUI();
	}
	
	function sendUpdateDiscoveryRequest()
	{
		var requestData = new Object();
		socket.emit('remote_discoveryUpdate', requestData);
	}	
	
	function sendEvent(eventName)
	{
		var event = new Object();
		event.name = eventName;
		socket.emit('remote_event', event);
	}
	
	function sendEventData(data)
	{
		socket.emit('remote_event', data);
	}	
	
	function sendText(text) {
		var event = new Object();
		event.name = "textEvent";
		event.text = text;
		sendEventData(event);
	}
	
	var screenArray = new Object();
	
	function getScreenStack(screenID) {
		return screenArray[screenID];
	}
	function handleScreenshotData(data, screenID) {
		var screeenShotArray = getScreenStack(screenID);
		if(screeenShotArray===undefined || screeenShotArray.complete) {
			screeenShotArray = new Object();
			screeenShotArray.complete = false;
			screeenShotArray.size = 0;
			
			screenArray[screenID] = screeenShotArray;
		}
		var imgData = data.data.data;
		var frameCount = data.data.frames;
		var frameIndex = data.data.frameIndex;
		screeenShotArray.expectedSize = frameCount;
		screeenShotArray.size = screeenShotArray.size+1;		
		screeenShotArray[frameIndex] = imgData;
		console.log("Received " + frameIndex + " of " + frameCount);
		if(screeenShotArray.expectedSize == screeenShotArray.size) {
			screeenShotArray.complete = true;
			console.log("Transmission finished. Creating Image");
			dataStr = screenshotArrayToDataURL(screeenShotArray);
			console.log(dataStr);
			setCanvasAsBackground(dataStr, screenID);
		}		
	}
	
	function screenshotArrayToDataURL(screeenShotArray) {
		var dataStr = "";
		for(var i=0; i<screeenShotArray.size; i++) {
			dataStr = dataStr+screeenShotArray[i];
		}
		return dataStr;
	}
	
	function setCanvasAsBackground(imageDataURL, screenID) {		
		var img = new Image();
		img.src = imageDataURL;			
		var divElement = document.getElementById("box"+screenID);
		if(divElement != null) {
			divElement.style.backgroundImage="url('"+img.src+"')";
			divElement.style.backgroundSize = "100% 100%";
		}
	}	
	
	function visualizeScreenRegistration(id, enabled)
	{
		var domObject = document.getElementById("box" + id);
		if(domObject)
		{
			if(enabled)
				domObject.style["color"] = "#E1E2E4";
			else domObject.style["color"] = "#9AD39F";
		}
	}
	
	
	
	var test;
	function performClick(object)
	{
	test=object;
		console.log(object);
		if(object)
		{
			var clickedScreen = test.getAttribute("id").substring(3);
			if(clickedScreen)
			{
				var screenID = parseInt(clickedScreen);
				var event = new Object();
				event.name = "clickEvent";
				event.destination = screenID;				
				sendEventData(event);
			}
		}
	}
	
	function toggleSelection(domObject)
	{
		if(domObject)
		{
			if(domObject.selected) {
				domObject.selected = !domObject.selected;
			}
			else {
				domObject.selected = true;
			}
			//Update Style
			performSelection(domObject,domObject.selected);			
		}
	}
	
	function performSelection(domObject, selected)
	{

		if(domObject)
		{
			if(selected) {			
				domObject.style["border-style"] = "dashed";
				domObject.style["border-width"] = "1px";
				domObject.style["border-color"] = "#5C5C5C";
				domObject.style["background-color"] = "#BAE2BE";
			}
			else {
				domObject.style["border-style"] = "solid";
				domObject.style["border-width"] = "1px";
				domObject.style["border-color"] = "#E6E6E6";
				domObject.style["background-color"] = "#71C37A";
				//domObject.style["color"] = "#9AD39F";
				
			}
		}		
	}
	
	function sendTestScreen(url)
	{
		var event = new Object();
		event.name = "testScreenEvent";
		event.url = url;
		sendEventData(event);	
	}
	
	function startColorGradientLoop()
	{
		for(var i = 0; i<= 100; i++)
		{
			var event = new Object();
			event.name = "colorGradientEvent";
			event.color = i;
			window.setTimeout(sendEventData(event), 1000);
					
			
		}
	}
	
	function startLoopedGradient(index)
	{
			var event = new Object();
			event.name = "colorGradientEvent";
			event.color = index;
			sendEventData(event);
			if(index <= 100)
				window.setTimeout("startLoopedGradient(" + (index+1) + ")", 150);	
	}
	
	
	function sendTestScreen(url)
	{
		var event = new Object();
		event.name = "LoadURLEvent";
		event.url = url;
		sendEventData(event);	
	}

	
	function openURLonScreen(url, screenID, enablePreview)
	{
		var event = new Object();
		event.name = "LoadURLEvent";
		event.url = url;
		event.destination = screenID;		
		sendEventData(event);	
		
		if(enablePreview) {
			window.setTimeout(takeScreenshotOnRemote(screenID), 8000);	
		}
	}
	

	
	function takeScreenshotOnRemote(screenID)
	{
		var event = new Object();
		event.name = "TakeScreenshot";
		event.destination = screenID;		
		sendEventData(event);	
	}	
		
	function startSendingURLs()
	{
		var newUrlInput = document.getElementById("url");
		console.log("URL: " + newUrlInput.value);
		sendTestScreen(newUrlInput.value);
	}
	
	function setupScreenUI() {
	//<div id=box1 class="box" onClick="javascript:toggleSelection(this);"
		var containerObject = document.getElementById("container");
		for(var i = 0; i<36; i++) {
			var divScreenBox = document.createElement("div");
			divScreenBox.id = "box" + (i+1);
			divScreenBox.className = "box";
			divScreenBox.onClick = toggleSelection;
			divScreenBox.onclick = function () {
				toggleSelection(this);
			};
			divScreenBox.innerHTML = i+1;
			containerObject.appendChild(divScreenBox);	
		}
	}
	
	function sendSnycMessage(videoTimestamp)
	{
		var event = new Object();
		event.name = "SnycEvent";
		event.timestamp = videoTimestamp;
		sendEventData(event);	
	}		
	
// First, let's shim the requestAnimationFrame API, with a setTimeout fallback
window.requestAnimFrame = (function(){
    return  window.requestAnimationFrame ||
    window.webkitRequestAnimationFrame ||
    window.mozRequestAnimationFrame ||
    window.oRequestAnimationFrame ||
    window.msRequestAnimationFrame ||
    function( callback ){
        window.setTimeout(callback, 1000 / 60);
    };
})();	



function draw() {
    var currentTime = videoTag.currentTime;
	var diff = currentTime-lastTimestamp;
	
	if(diff > 5)
	{
		if(timestampAvailable)
		{
			sendSnycMessage(currentTime);
			lastTimestamp = videoTag.currentTime;
		}
	}

	if(!videoTag.paused) {
		// set up to draw again
		requestAnimFrame(draw);
	}
}

var videoTag;
var lastTimestamp = 0;
var timestampAvailable = false;
function initVideoSynch(){
	videoTag = document.getElementById('remoteVideo');
	
	if(videoTag == undefined)
		return;
	
	//add an listener on loaded metadata
	videoTag.addEventListener('loadeddata', function() {
		console.log("Loaded the video's data!");		
		//Start Video automatically
		this.play(); 		
		
	}, false);	

videoTag.addEventListener("loadedmetadata", function()
  {
	timestampAvailable = true;
  }
);	
	
	videoTag.addEventListener('play', function(){
		lastTimestamp = this.currentTime;
		requestAnimFrame(draw);    // start the drawing loop.		
	},false);	
}

function toggleVideoPlayback()
{
	if(videoTag)
	{
		if(videoTag.paused) {		
			videoTag.play();
			sendPlayMessage();
		}
		else {
			sendPauseMessage();
			videoTag.pause();
		}
	}
}

function sendVideoSyncMessage()
{
	if(videoTag)
	{
		sendSnycMessage(videoTag.currentTime);
	}
	
}

function sendPlayMessage()
{
	sendMediaControlMessage("play");
}

function sendPauseMessage()
{
	sendMediaControlMessage("pause");
}
var files, dragHoles;
	function updateUI() {
		files = document.querySelectorAll('#file');
		[].forEach.call(files, function(fileElement) {
			fileElement.addEventListener('dragstart', handleDragStart, false);
		});
	
	
		dragHoles = document.querySelectorAll('.box');
		[].forEach.call(dragHoles, function(holeElement) {
			holeElement.addEventListener('dragenter', handleDragEnter, false);
			holeElement.addEventListener('dragover', handleDragOver, false);
			holeElement.addEventListener('dragleave', handleDragLeave, false);
			holeElement.addEventListener('drop', handleDrop, false);
			holeElement.addEventListener('dragend', handleDragEnd, false);
		});
	}

	function sendMediaControlMessage(control)
	{
		var event = new Object();
		event.name = "MediaControlEvent";
		event.control = control;
		sendEventData(event);	
	}	
	
	function handleDragStart(e) {
		this.style.opacity = '0.4';  // this / e.target is the source node.
		
		e.dataTransfer.effectAllowed = 'move';
		var newDataObject = new Object();
		newDataObject.url = $( this ).attr( "data-url" );
		e.dataTransfer.setData('text/html', JSON.stringify(newDataObject));
	}

	function handleDragOver(e) {
	  if (e.preventDefault) {
		e.preventDefault(); // Necessary. Allows us to drop.
	  }

	  e.dataTransfer.dropEffect = 'move';  // See the section on the DataTransfer object.

	  return false;
	}			
	
	function handleDragEnter(e) {
	  // this / e.target is the current hover target.
	  this.classList.add('over');
	}

	function handleDragLeave(e) {
	  this.classList.remove('over');  // this / e.target is previous target element.
	}

	function handleDrop(e) {
	  // this / e.target is current target element.

	  if (e.stopPropagation) {
		e.stopPropagation(); // stops the browser from redirecting.
	  }
	  
	  removeStyles();

	  //unbundle Data
	  var id = e.currentTarget.innerHTML;
	  var data = JSON.parse(e.dataTransfer.getData('text/html'));
	  console.log(data);
	  
	  // See the section on the DataTransfer object.
	  openURL(data.url, id, true);

	  return false;
	}

	function handleDragEnd(e) {
		removeStyles();
	}	
	
	function removeStyles() {
		//Alles wieder schön machen
		[].forEach.call(dragHoles, function (hole) {
			hole.classList.remove('over');
		});
	  
		[].forEach.call(files, function(fileElement) {
			fileElement.style.opacity = '1.0';
		});			  
	}	
	
	function openURL(url, id, enablePreview) {
		if(url != "") {
			openURLonScreen(url, id, enablePreview); 				
		}
	}
	
	function reloadVideo() {
		var v = document.getElementById('remoteVideo');
		
		onLoadVideoRandomLink(v);
	}
	
	function onLoadVideoRandomLink(videoElement) {
		var src = videoElement.getAttribute("src");
		var random = Math.floor(Math.random() * 10000);
		if(src.indexOf("?") > 0)
			src = src + "&i=" + random;
		else src = src + "?i=" + random;
		videoElement.setAttribute("src", src);
		videoElement.load();
	}
	
	
  </script>
  
<style type="text/css">

body {
	background-color: #2A323B;
}

#maincontainer {
	text-align: center;
	vertical-align: middle;
}

#container {
	width: 1040px;
	height: 650px;
	display: inline-block;
}

.box {
	width: 150px; 
	height: 84px; 
	border: 1px solid #E6E6E6;
	line-height: 84px;
	text-align:center;
	vertical-align: middle;
	font-family: sans-serif;
	font-weight: bold;
	font-size: 2em;
	margin-right: 20px;
	margin-bottom: 20px;
	float:left;
	color: #9AD39F;
	cursor: pointer; cursor: hand;
	background-color: #71C37A;
}

#heading {
	color: #E1E2E4;
	font-family: sans-serif, Arial;
}

div.title {
	font-size: 25pt;
	font-weight: bold;
}
</style>
  
</head>
 
<body onLoad="init()">
<div id="heading"><div class="title">MUVI MANAGER</div><div class="subtitle">Monitor overview</div></div>
<div id="maincontainer">
<div id=container>
</div>
	<div>
		<button type="button" id="testButton" onClick="takeScreenshotOnRemote(-1)">Screenshot!</button>	
	
		<button type="button" id="testButton" onClick="sendTestScreen()">Toggle
			all screens!</button>
		<button type="button" id="testButton2"
			onClick="startColorGradientLoop()">Start Color gradient</button>
		<button type="button" id="testButton2"
			onClick="startLoopedGradient(0)">Start Timed Color gradient</button>
		<button type="button" id="testButton2" onClick="startSendingURLs()">Load
			various URLs</button>
		<input type=text id="url"> <br>
		<button type="button" id="playbackToggle"
			onClick="toggleVideoPlayback()">Toggle Playback</button>
		<button type="button" id="sendSyncButton"
			onClick="sendVideoSyncMessage()">Send Snyc</button>

		<button type="button" id="playButton" onClick="sendPlayMessage()">Play</button>
		<button type="button" id="pauseButton" onClick="sendPauseMessage()">Pause</button>
		<br>
 		<video id="remoteVideo" src="http://cdnbakmi.kaltura.com/p/346/sp/34600/serveFlavor/entryId/1_lncj5nra/v/1/flavorId/1_9m26lw7z/name/a.mp4" autobuffer controls preload="auto">
			<!-- //<source src="http://localhost:8001/VfE.webm?i=100"> -->
		<!-- 	<source
				src="http://annodex.net/~silvia/itext/elephants_dream/elephant.ogv?i=100"> -->
		</video> 
		
		<div draggable="true" id="file" style="height:140px; width: 200px; text-align: center; border: 1px solid gray; float:left; margin-right: 15px;" data-url="https://arpos.iao.fraunhofer.de">ARPOS</div>
		<div draggable="true" id="file" style="height:140px; width: 200px; text-align: center; border: 1px solid gray; float:left; margin-right: 15px;" data-url="http://www.iao.fraunhofer.de">IAO</div>
		<div draggable="true" id="file" style="height:140px; width: 200px; text-align: center; border: 1px solid gray; float:left; margin-right: 15px;" data-url="http://www.ipa.fraunhofer.de">IPA</div>
	</div>
</div>

</body>
 
</html>