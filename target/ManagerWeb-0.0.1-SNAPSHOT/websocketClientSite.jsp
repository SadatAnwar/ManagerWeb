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
	var startURL = get('url');
	
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
		console.log("Received Event: " + data);
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
		if (startURL != undefined) {
			startURL = urldecode(startURL);
			performLoadURL(startURL);
		}
		
		//Hide AlertBox on Start-Up
		hideAlertMessageBox();
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
</script>

<style type="text/css">
html,body {
	overflow: hidden;
	margin: auto;
	height: 100%;
	width: 100%;
}

#alertBox {
    position: absolute;
    top: 0;
    left: 0;
    z-index: 999;
    border: 1px solid gray;
}

</style>
</head>

<body id="body" onLoad="init();">

	<iframe src="" width="100%" height="100%"
		frameborder="0" id="frame"></iframe>
	<div id="alertBox">
	
	</div>
</body>

</html>