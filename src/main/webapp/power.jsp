<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="style/manager.css">
<script type="text/javascript" src="js/jquery-1.11.0.js"></script>
<script type="text/javascript">

function powerOn(outletNumber) {
	var conf = true;
	if (outletNumber == 7) {
		conf = confirm("Are you sure you want to power on the displays?");
		if(conf){
			$('#message').html("Please wait. Complete startup will take about 2 minutes.");
		}
	}
	powerUp(conf, outletNumber);
}

function powerUp(conf, outletNumber){
	if (conf) {
		$.ajax({
			  type: "POST",
			  url: "powerOnAjax.do",
			  data: { outletNumber: outletNumber },
			  success: function(data) {
			  if (outletNumber == 0) {
				$('#message').html("Startup complete");
			  }
			  else{	
			  $('#message').html("Powered on socket " + (outletNumber+1));
			  }
			  var socketname = '#socket'+(outletNumber);
			  $(socketname).removeClass('stateOff');
				  if (outletNumber > 0) {
					  outletNumber--;
					  powerOn(outletNumber);
				  }
				  },
		       error: function(e) {
		    	   $('#message').html("Error communicating!");
				  }
			});
	}
}


function onceClickStartUp() {
	var conf = confirm("This is an automated startup of the complete MuVi Setup, Proceed?");
	if (conf) {
		document.getElementById("freezer").style.display = "block";
		$.ajax({
			  type: "POST",
			  url: "oneClickStartUp.do",
			  data: { command: "checkDCs" },
			  success: function(data) {
				  if(data.match("OK")){
					  document.getElementById("freezerMessage").innerHTML = "All computers are up proceeding further...";
					  powerUp(true, 7);
					  document.getElementById("freezerMessage").innerHTML =  "Switching on Displays, will talke about 2 mins";
					  setTimeout(function(){
						  			sysRestart(1, true);
							  		sysRestart(2, true); 
							  		sysRestart(3, true);
							  		document.getElementById("freezerMessage").innerHTML =  "All DCs restarted";
							  		setTimeout(function(){waitForDc();}, 10000);
							  		}, 10000);
					  
				  }
				  else if(data.match("^Error")){
					  alert(data+" is not switched on, please try again after manually powering it on");
					  document.getElementById("freezer").style.display = "none";
				  }
				  },
		       error: function(e) {
		    	   document.getElementById("freezer").style.display = "none";
		    	   $('#message').html("Error communicating!");
				  }
			});
	}
}

function waitForDc(){
	document.getElementById("freezerMessage").innerHTML = "Waiting for DCs to get ready...";
	$.ajax({
		  type: "POST",
		  url: "oneClickStartUp.do",
		  data: { command: "waitForDC" },
		  success: function(data) {
			  if(data.match("OK")){
				  document.getElementById("freezerMessage").innerHTML = "All Systems are now ready... Starting Chromes";
				  doStartChrome(0,"startChromeURL.do");
				  setTimeout(function(){askConfirmation();}, 5000);
			  }
			  },
	       error: function(e) {
	    	   document.getElementById("freezer").style.display = "none";
	    	   $('#message').html("Error communicating!");
			  }
		});
}

function askConfirmation(){
	if(confirm("All chromes started OK?")){
		document.getElementById("freezer").style.display = "none";		
	}
	else{
		doRestartChrome();
		setTimeout(function(){askConfirmation();}, 8000);
	}
	
}

function powerOff() {
	var conf = confirm("Are you sure you want to power off the display?");
	if (conf) {
		$.ajax({
			  type: "POST",
			  url: "powerOffAjax.do",
			  beforeSend: function() {
		            $('#message').html("Powering down...");
		        },
			  success: function() {
				$('#message').html("Powering down complete");
				for(var outletNumber=0;outletNumber<8;outletNumber++){
				$('#socket' + outletNumber).addClass('stateOff');
				}		  
				 },
		       error: function(e) {
		    	   $('#message').html("Error communicating!");
				  }
			});
		$('#message').html("Powering down...");
	}

}

function allSysRestart() {
	var conf = confirm("Are you sure you want to restart DC1, DC2 and DC3?");
	if (conf) {
		sysRestart(1, true);
		sysRestart(2, true);
		sysRestart(3, true);
	}
}

function sysRestart(sysNumber, conf) {
	if (conf) {
		$.ajax({
			  type: "POST",
			  url: "restartComputer.do",
			  data: { sysNumber: sysNumber },
			  success: function(data) {
				  $('#message').html("DC "+sysNumber+" will now restart");
				  $('#dc'+sysNumber).addClass('stateOff');
				  if(sysNumber==1){
					  dc1State = false;
				  }
				  else if (sysNumber==2){
					  dc2State = false;
				  }
				  else{
					  dc3State = false;
				  }
				  },
		       error: function(e) {
		    	   $('#message').html("Error communicating!");
				  }
			});
	} else {
		conf = confirm("Are you sure you want to restart DC " + sysNumber + "?");
		if (conf) {
			sysRestart(sysNumber, true);
		}
	}
}
	
function sysShutdown(sysNumber, conf) {
	if (conf) {
		$.ajax({
			  type: "POST",
			  url: "shutdownComputer.do",
			  data: { sysNumber: sysNumber },
			  success: function(data) {
				  $('#message').html("DC "+sysNumber+" will now power off.");
				  },
		       error: function(e) {
		    	   $('#message').html("Error communicating!");
				  }
			});
	} else {
		conf = confirm("Are you sure you want to power off DC" + sysNumber + "?");
		if (conf) {
			sysShutdown(sysNumber, true);
		}
	}
}

function allsysShutdown() {
	var conf = confirm("Are you sure you want to power off DC1, DC2 and DC3?");
	if (conf) {
		sysShutdown(1, true);
		sysShutdown(2, true);
		sysShutdown(3, true);
	}
}

function startChrome(sysNumber) {
	var msg = sysNumber > 0 ? "Are you sure you want to start Chrome on DC" + sysNumber + "?" : "Are you sure you want to start Chrome on all DCs?";
	var conf = confirm(msg);
	if (conf) {
		doStartChrome(sysNumber, "startChrome.do");
	}
}

function doStartChrome(sysNumber, url) {
	$.ajax({
		  type: "POST",
		  url: url,
		  data: { sysNumber: sysNumber },
		  success: function(data) {
			  if (data.match("^Error")) {
					alert("Display computers not ready, please try again later");
					
					} else {
						$('#message').html("Chrome started.");
						document.getElementById("freezerMessage").innerHTML = "All chrome started";
					}
			  },
	       error: function(e) {
	    	   alert("Unable to communicate with display computers!!");
	    	   $('#message').html("Error communicating!");
			  }
		});
}


function startChromeURL(sysNumber) {
	var msg = sysNumber > 0 ? "Are you sure you want to start Chrome on DC" + sysNumber + "?" : "Are you sure you want to start Chrome on all DCs?";
	var conf = confirm(msg);
	if (conf) {
		$.ajax({
			  type: "POST",
			  url: "startChromeURL.do",
			  data: { sysNumber: sysNumber },
			  success: function(data) {
				  if (data.match("^Error")) {
						alert("Display computers not ready, please try again later");
						
						} else {
							$('#message').html("Chrome started.");
						}
				  },
		       error: function(e) {
		    	   $('#message').html("Error communicating!");
				  }
			});
	}
}


function killChrome(sysNumber) {
	var msg = sysNumber > 0 ? "Are you sure you want to kill Chrome on DC" + sysNumber + "?" : "Are you sure you want to kill Chrome on all DCs?";
	var conf = confirm(msg+"\nPlease wait for kill confirmation before you proceed further");
	document.getElementById("freezer").style.display = "block";	
	if (conf) {
		$.ajax({
			  type: "POST",
			  url: "killChrome.do",
			  data: { sysNumber: sysNumber },
			  success: function(data) {
				  $('#message').html("Chrome killed.");
				  	document.getElementById("freezer").style.display = "none";
					window.alert("Kill chrome complete");
				  },
		       error: function(e) {
		    	   document.getElementById("freezer").style.display = "none";
		    	   $('#message').html("Error communicating!");
				  }
			});
	}
}

function restartChrome() {
	var conf = confirm("Are you sure you want to restart chrome on all computers?");
	document.getElementById("freezer").style.display = "block";
	if (conf) {
		doRestartChrome();
	}
}

function doRestartChrome() {
	var sysNumber = 0;
	$.ajax({
		  type: "POST",
		  url: "restartChrome.do",
		  data: { sysNumber: sysNumber },
		  success: function(data) {
			  $('#message').html("Chrome Restarted.");
			  	document.getElementById("freezer").style.display = "none";
				window.alert("Chrome restart complete");
			  },
	       error: function(e) {
	    	   document.getElementById("freezer").style.display = "none";
	    	   $('#message').html("Error communicating!");
			  }
		});
}

function checkdc(){
	$.ajax({
		  type: "POST",
		  url: "checkDcPower.do",
		  success: function(data) {
			  if(!dc1State && data.match("1[1,0][1,0]")){
				  $('#dc1').removeClass('stateOff');
				  dc1State = true;
			  }
			  if(!dc2State && data.match("[1,0]1[1,0]")){
				  $('#dc2').removeClass('stateOff');
				  dc2State = true;
			  }
			  if(!dc3State && data.match("[1,0][1,0]1")){
				  $('#dc3').removeClass('stateOff');
				  dc3State = true;
			  }

			  },
	       error: function(e) {

			  }
		});
}

function checksocket(){
	var sw = ['${Socket1}','${Socket2}','${Socket3}','${Socket4}','${Socket5}','${Socket6}','${Socket7}','${Socket8}'];
	for(var i=0; i<8; i++){
		var socketname = '#socket'+i;
		if(sw[i]==0){
			$(socketname).addClass('stateOff');
		}
		else {
			$(socketname).removeClass('stateOff');
		}
		
	}
}

function checkChrome(){
	$.ajax({
		  type: "POST",
		  url: "checkChromeService.do",
		  success: function(data) {
			  if(dc1State && data.match("1[1,0][1,0]")){
				  $('#chrome1').removeClass('stateOff');
			  }
			  if(dc2State && data.match("[1,0]1[1,0]")){
				  $('#chrome2').removeClass('stateOff');
			  }
			  if(dc3State && data.match("[1,0][1,0]1")){
				  $('#chrome3').removeClass('stateOff');
			  }

			  },
	       error: function(e) {

			  }
		});
}
var dc1State = false;
var dc2State = false;
var dc3State = false;

function checkState(){
	checksocket();
	checkdc();
	checkChrome();
	setInterval(function(){checkdc();},10000);
	setInterval(function(){checkChrome();},10000);
}

$( document ).ready(function() {
	checkState();
	});


</script>
<title>Muvi Manager Power Control</title>
</head>
<body>

<h1>Power on and off - MuVi Manager</h1>

<p id="message"></p>		

<table id="powerStatus">
<tr>
	<td>Outlets</td>
	<td>DCs</td>
	<td>Chrome</td>
</tr>
<tr>
	<td>
	<span id="socket0">1</span>
	<span id="socket1">2</span>
	<span id="socket2">3</span>
	<span id="socket3">4</span>
	<span id="socket4">5</span>
	<span id="socket5">6</span>
	<span id="socket6">7</span>
	<span id="socket7">8</span>
</td>
	<td>
	<span id="dc1" class = 'stateOff'>1</span>
	<span id="dc2" class = 'stateOff'>2</span>
	<span id="dc3" class = 'stateOff'>3</span>
	</td>
	<td>
	<span id="chrome1" class = 'stateOff'>1</span>
	<span id="chrome2" class = 'stateOff'>2</span>
	<span id="chrome3" class = 'stateOff'>3</span>
	</td>
</tr>
</table>



<div id="powerControls">
<p>&nbsp;</p>
<p>&nbsp;</p>
<a href="javascript:onceClickStartUp()">One Click Start</a>
<a href="javascript:powerOn(7)">Power on displays</a>
<a href="javascript:allSysRestart()">Restart all computers</a>
<a href="javascript:startChromeURL(0)">Start Chrome</a>
<p>&nbsp;</p>
<a href="javascript:allsysShutdown()">Power off computers</a>
<a href="javascript:powerOff()">Power off displays</a>
<p>&nbsp;</p>
<a href="index.jsp">Home</a>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<a href="javascript:restartChrome()">Restart Chrome</a>
<a href="javascript:killChrome(0)">Kill Chrome</a>
<a href="powerStatus.do">Update status</a>
<a href="screenshot.do">Screenshot</a>
<a href="javascript:startChrome(0)">Start Chrome with Google homepage</a>
<a href="interrupt.do">Interrupt show URLs</a>
<a href="javascript:alert('This is currently not implemented.\nYou need to go to the server room and power on the three display computers manually.')">Power on computers</a>
</div>

<p style="clear: both;"></p>

<div id="dcControls">

<div id="startChrome">
<a href="javascript:startChromeURL(1)">Start Chrome 1</a>
<a href="javascript:startChromeURL(2)">Start Chrome 2</a>
<a href="javascript:startChromeURL(3)">Start Chrome 3</a>
</div>

<div id="killChrome">
<a href="javascript:killChrome(1)">Kill Chrome 1</a>
<a href="javascript:killChrome(2)">Kill Chrome 2</a>
<a href="javascript:killChrome(3)">Kill Chrome 3</a>
</div>

<div id="restart">
<a href="javascript:sysRestart(1)">Restart DC1</a>
<a href="javascript:sysRestart(2)">Restart DC2</a>
<a href="javascript:sysRestart(3)">Restart DC3</a>
</div>

<div id="shutdown">
<a href="javascript:sysShutdown(1, false)">Power off DC1</a>
<a href="javascript:sysShutdown(2, false)">Power off DC2</a>
<a href="javascript:sysShutdown(3, false)">Power off DC3</a>
</div>
</div>

<p id="homeLink"><a href="index.jsp">Home</a></p>
<div id="freezer" style="display: none; height:100%;width:100%; background-color:rgba(0,0,0,0.7); position: fixed; top:0px; right:0px">
<p id="freezerMessage" style="text-align:center; position: relative; top:50%; color:rgb(180,220,211)"></p>
<p style="text-align:center; position: relative; top:50%; color:rgb(180,220,211)">Please Wait...</p>
<img style="position: relative; display:block; margin-left: auto; margin-right: auto; top:50%; " alt="" src="./style/ajax-loader.gif">
</div>



</body>
</html>