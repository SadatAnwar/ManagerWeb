<!DOCTYPE html>
<html>
<head>
<title>Websocket MuVi Client</title>
<script src="js/jquery-1.11.0.js"></script>
<script src="js/html2canvas.js"></script>
<script>
	function get(name) {
		if (name = (new RegExp('[?&]' + encodeURIComponent(name) + '=([^&]*)'))
				.exec(location.search))
			return decodeURIComponent(name[1]);
	}
	var deviceData = new Object();
	var urlScreenID = get('id');
	var posX = get('col');
	var posY = get('row');
	var rows = get('rows');
	var cols = get('cols');
	var videoURL = get('video');
	var showVideoTag = get('show');	
	
	if (urlScreenID == undefined || urlScreenID == null) {
		deviceData.id = 1;
	} else {
		deviceData.id = urlScreenID;
	}

	var socket = {
		connect : function(screenID) {
			this._screenID = screenID;
			var location = "ws://${wsServerURL}";
			this._ws = new WebSocket(location);
			this._ws.onopen = this._onopen;
			this._ws.onmessage = this._onmessage;
			this._ws.onclose = this._onclose;
			this._ws.onerror = this._onerror;

			this.functions = [];

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
				//console.log("DEBUG Received Message: " + m.data);
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
			alertMessage(convertToText(e));
		},

		disconnect : function() {
			socket._sendEvent("disconnect", null);
		},

		on : function(functionName, functionHandler) {
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

	socket.connect(deviceData.id);

	socket.on('welcome', function(data) {
		console.log('"welcome"-Event vom Server empfangen:\n' + data);

		// Eigenen Event vom Client an den Server schicken
		socket.emit('register_screen_client', deviceData);
	});

	socket.on('remote_event', function(data) {
		//console.log("Received Event: " + data);
		processEvent(data);

	});

	socket.send = function(data) {
		socket.emit('message', data);
	}

	function test() {
		socket.send("Hallo");
	}

	function processEvent(data) {
		var eventName = data.name;
		switch (eventName) {
		case "showEvent":
			showDiv();
			break;

		case "hideEvent":
			hideDiv();
			break;

		case "textEvent":
			showText(data.text);
			break;

		case "clickEvent":
			performClick(data);
			break;

		case "LoadURLEvent":
			performLoadURL(data);
			break;
			
		case "TakeScreenshot":
			performScreenshot(data);
			break;	
			
		case "SnycEvent":
			performSnycEvent(data);
		break;
		
		case "MediaControlEvent":
			performMediaControlEvent(data);
		break;				
		}
	}

	function performClick(data) {
		var object = document.getElementById("text");
		if (object)
			object.innerHTML = "We got a click!";
	}

	function showText(text) {
		var object = document.getElementById("text");
		if (object)
			object.innerHTML = text;
	}

	function showDiv() {
		alertMessage("hallo");

	}

	function hodeDiv() {
		alertMessage("hallo");
	}
	var toggleState = false;
	function performTestScreenEvent(data) {
		var body = document.getElementById("body");
		if (body) {
			if (toggleState)
				body.style["background-color"] = "rgb(255,255,255)";
			else
				body.style["background-color"] = "rgb(226,0,26)";
			toggleState = !toggleState;

		}
	}

	function performColorGradientEvent(data) {
		var body = document.getElementById("body");
		if (body) {
			var color = data.color;
			body.style["background-color"] = "hsl(100,100%," + color + "%)";
			//console.log("Received ColorID: " + color);			
		}
	}

	function performLoadURL(data) {

		var frame = document.getElementById("frame");
		if (frame) {
			var url = urldecode(data.url);
			frame.src = url;
		}
	}
	
	function performScreenshot(data) {
		var frameBody = getFrameBody();
		takeScreenshot(frameBody);
	}
	
	function takeScreenshot(body) {
		var startTimestamp = window.performance.now();
		html2canvas(body, {
		  onrendered: function(canvas) {
			 console.log(canvas.toDataURL("image/jpg"));
			 var endTimestamp = window.performance.now();
			 var durationInMs = (endTimestamp - startTimestamp)/1000;
			 console.log("Duration: " + durationInMs + "ms");
			 sendScreenshotToRemote(canvas.toDataURL("image/jpg"), urlScreenID);
			//setCanvasAsImage(canvas);
			//setCanvasAsBackground(canvas);
		  },
			height: screen.height,
			width: screen.width,
			allowTaint: true
		});
	}
	
	function sendScreenshotToRemote(data, sourceID) {
		var frameSize = 65226;
		var frameCount = Math.ceil(data.length / frameSize);
		var transmittableBytes = data.length;
		var counter = 0;
		while(counter < frameCount) {
			byteCount = 65226;
			var event = new Object();
			event.name = "screenshot_response";
			event.data = data.substring(counter*frameSize, Math.min((counter+1)*frameSize, data.length));
			event.frames = frameCount;
			event.frameIndex = counter;
			event.source = sourceID;
			socket.emit('screenshot_response', event);
			console.log("Transmitted frame " + (counter+1) + " of " + frameCount);
			counter++;
		}
	}
	
	
	function setCanvasAsImage(canvas) {		
		var img = document.getElementById("testImg");
		img.src = canvas.toDataURL("image/jpg");
	}
	
	function setCanvasAsBackground(canvas) {		
		var img = new Image();
		img.src = canvas.toDataURL("image/jpg");			
		var divElement = document.getElementById("hole");
		
		divElement.style.backgroundImage="url('"+img.src+"')";
		divElement.style.backgroundSize = "200px 125px";
	}	
	
	function getFrameBody() { 
		var iframe = document.getElementById("frame");
		var body = $(iframe).contents().find('body');
		return body;
	}
	
	function urldecode(url) {
		  return decodeURIComponent(url.replace(/\+/g, ' '));
		}	
	
	function init() {	
		//Hide AlertBox on Start-Up
		hideAlertMessageBox();
		
		//Initalize the functions for the video application
		initVideoApplication();
	}
	
	
	function alertMessage(text) {
		console.log(text);
		setAlertTime(15);
		var box = $("#alertBox");
		if(box) {
			box.show();
			box.text(text);
		}
		alertBoxCountdown();		
	}
	
	function hideAlertMessageBox() {
		var box = $("#alertBox");
		if(box) {
			box.hide();
		}
	}
	var time = 15;
	
	function setAlertTime(seconds) {
		time = seconds;
	}
	function alertBoxCountdown() {
		if(time > 0) {
			time--;
			setTimeout(alertBoxCountdown, 1000);
		} else {
			hideAlertMessageBox();
		}
	}
	
	//Make an object a string that evaluates to an equivalent object
//  Note that eval() seems tricky and sometimes you have to do
//  something like eval("a = " + yourString), then use the value
//  of a.
//
//  Also this leaves extra commas after everything, but JavaScript
//  ignores them.
function convertToText(obj) {
    //create an array that will later be joined into a string.
    var string = [];

    //is object
    //    Both arrays and objects seem to return "object"
    //    when typeof(obj) is applied to them. So instead
    //    I am checking to see if they have the property
    //    join, which normal objects don't have but
    //    arrays do.
    if (typeof(obj) == "object" && (obj.join == undefined)) {
        string.push("{");
        for (prop in obj) {
            string.push(prop, ": ", convertToText(obj[prop]), ",");
        };
        string.push("}");

    //is array
    } else if (typeof(obj) == "object" && !(obj.join == undefined)) {
        string.push("[")
        for(prop in obj) {
            string.push(convertToText(obj[prop]), ",");
        }
        string.push("]")

    //is function
    } else if (typeof(obj) == "function") {
        string.push(obj.toString())

    //all other values can be done with JSON.stringify
    } else {
        string.push(JSON.stringify(obj))
    }

    return string.join("")
}	


/*MULTI SCREEN VIDEO APPLICATION LOGIC
 * 
 */
 
	function performSnycEvent(data) {
		
		var videoTag = document.getElementById('v');
		if(videoTag) {
			var timestamp = data.timestamp;
			videoTag.currentTime=data.timestamp;
		}
	}	
	
	function performMediaControlEvent(data) {
	
		var videoTag = document.getElementById('v');
		if(videoTag) {
			if(data.control == "play")
				videoTag.play();
			if(data.control == "pause")
				videoTag.pause();							
		}
	}		
 
 function initVideoApplication() {
		var v = document.getElementById('v');	
		var canvas = document.getElementById('c');
		var context = canvas.getContext('2d');
		
		if(showVideoTag == "true")
			v.style["display"] = "block";
		else v.style["display"] = "none";
		
		onLoadVideoRandomLink(v, videoURL);
	
		var cw = Math.floor(canvas.clientWidth);
		var ch = Math.floor(canvas.clientHeight);
		
		var sx = 0;
		var sy = 0;
		var sw = 0;
		var sh = 0;	
		
		canvas.width = cw;
		canvas.height = ch;
		
		canvas.addEventListener('click', function() {
			if(v.paused)
				v.play();
			else 
				v.pause();
		});
		
		//add an listener on loaded metadata
		v.addEventListener('loadeddata', function() {
			console.log("Loaded the video's data!");
			//Position has to be decreased by one due to indexing of cloudwall backend software!
			sx = Math.floor(v.videoWidth/cols)*(posX-1);
			sy = Math.floor(v.videoHeight/rows)*(posY-1);
			
			sw = Math.floor(v.videoWidth/cols);
			sh = Math.floor(v.videoHeight/rows);	
			
			//Start Video automatically
			//v.play();
			
			
		}, false);	
	

		v.addEventListener('play', function(){
		
			draw(this,context,sx, sy, sw, sh, cw,ch);
		},false);
	}
 

	function draw(v, c, sx, sy, sw, sh, w, h) {
		if (v.paused || v.ended)
			return false;
		c.drawImage(v, sx, sy, sw, sh, 0, 0, w, h);
		setTimeout(draw, 20, v, c, sx, sy, sw, sh, w, h);
	}

	function onLoadVideoRandomLink(videoElement, sourceURL) {
		var src = videoElement.getAttribute("src");
		var random = Math.floor(Math.random() * 10000);
		
		if(sourceURL != null)
			src = sourceURL;
		
		if(src.indexOf("?") > 0)
			src = src + "&i=" + random;
		else src = src + "?i=" + random;
		videoElement.setAttribute("src", src);
		videoElement.load();
	}
</script>

<style type="text/css">
html,body {
	overflow: hidden;
	margin: auto;
	height: 100%;
	width: 100%;
	background: black;
}

#alertBox {
    position: absolute;
    top: 0;
    left: 0;
    z-index: 999;
    border: 1px solid gray;
}

body {
	background: black;
}
 
#c {
	position: absolute;
	top: 0;
	bottom: 0;
	left: 0;
	right: 0;
	width: 100%;
	height: 100%;
}
 
#v {
	position: absolute;
	top: 50%;
	left: 50%;
	margin: -180px 0 0 -240px;
}
 
p {
	position: relative;
	z-index: 1;
}
</style>
</head>

<body id="body" onLoad="init();">

	<canvas id=c></canvas> 
	<!-- src="http://annodex.net/~silvia/itext/elephants_dream/elephant.ogv" -->
    <video id="v" style="display:none;" src="http://cdnbakmi.kaltura.com/p/346/sp/34600/serveFlavor/entryId/1_lncj5nra/v/1/flavorId/1_9m26lw7z/name/a.mp4" 
             autobuffer controls preload="auto"></video>
	<div id="alertBox">
	
	</div>
</body>

</html>