<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width; initial-scale=1.0;" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Bootstrap core CSS -->
<link rel="stylesheet" href="style/bootstrap.css">
<link rel="stylesheet" type="text/css" href="style/manager.css">
<link href="style/carousel.css" rel="stylesheet">
<link href="style/font-awesome/css/font-awesome.css" rel="stylesheet">   
<script type="text/javascript" src="js/jquery-1.11.0.js"></script>
<style type="text/css">
p {
   font-size: 20px !important;
}
    .row {
 margin-right: 0px; 
 margin-left: 0px; 
}
    a:HOVER {
	color: rgb(199, 202, 204);
	text-decoration: underline;
	background: rgb(0, 90, 148);
}
</style>
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
var sw = ['${Socket1}','${Socket2}','${Socket3}','${Socket4}','${Socket5}','${Socket6}','${Socket7}','${Socket8}'];

function onOrOff(){
	var count = 0;
	for(var i=0; i<8; i++){
		if(sw[i]==1){
			count++;
		}
	}
		if(count == 8){
			powerOff();
			}
		else{
			powerOn(7);
		}
	
}

function powerUp(conf, outletNumber){
	if (conf) {
		$.ajax({
			  type: "POST",
			  url: "powerOnAjax.do",
			  data: { outletNumber: outletNumber },
			  beforeSend: function() {
				  document.getElementById("powerScreen").disabled=true;
				  $('#powerScreen').removeClass('btn-danger');
				  $('#powerScreen').removeClass('btn-success');
				  $('#powerScreen').addClass('btn-warning');
		        },
			  success: function(data) {
			  if (outletNumber == 0) {
				$('#message').html("Startup complete");
				sw[outletNumber]=1;
				document.getElementById("powerScreen").disabled=false;
				$('#powerScreen').removeClass('btn-warning');
				$('#powerScreen').removeClass('btn-danger');
				$('#powerScreen').addClass('btn-success');
			  }
			  else{	
			  $('#message').html("Powered on socket " + (outletNumber+1));
			  sw[outletNumber]=1;
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
		
		 $( "#screenIcon" ).animate({
			 opacity: "toggle"
			 }, 3000, "linear", function() {
			 // Animation complete
			 });
		
	}
	
}


function oneClickStartUp() {
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
							  		sysRestart(4, true);
							  		document.getElementById("freezerMessage").innerHTML =  "All DCs restarted";
							  		$('#chromeService').removeClass('btn-danger btn-success').addClass('btn-warning'); 
							  		$("i.icon-spinner").addClass('fa-spin');
							  		setTimeout(function(){waitForDc();}, 20000);
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
				  $('#chromeService').removeClass('btn-danger btn-warning').addClass('btn-success'); 
			  	  $("i.icon-spinner").removeClass('fa-spin');
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
		            document.getElementById("powerScreen").disabled=true;
					$('#powerScreen').removeClass('btn-danger');
					$('#powerScreen').removeClass('btn-success');
					$('#powerScreen').addClass('btn-warning');
		        },
			  success: function() {
				$('#message').html("Powering down complete");
				document.getElementById("powerScreen").disabled=false;
				$('#powerScreen').addClass('btn-danger');
				$('#powerScreen').removeClass('btn-warning');				
				$('#powerScreen').removeClass('btn-success');
				for(var outletNumber=0;outletNumber<8;outletNumber++){
					$('#socket' + outletNumber).addClass('stateOff');
					sw[outletNumber]=0;
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
	var conf = confirm("Are you sure you want to restart DC1, DC2, DC3 and DC4?");
	if (conf) {
		sysRestart(1, true);
		sysRestart(2, true);
		sysRestart(3, true);
		sysRestart(4, true);
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
					  dCState.DC1 = false;
				  }
				  else if (sysNumber==2){
					  dCState.DC2 = false;
				  }
				  else if (sysNumber==3){
					  dCState.DC3 = false;
				  }
				  else if (sysNumber==4){
					  dCState.DC4 = false;
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
                  $('#dc'+sysNumber).addClass('stateOff');
                  $('#powerComputer').addClass('btn-warning');
                  $('#powerComputer').removeClass('btn-danger');
				  $('#powerComputer').removeClass('btn-success');
                  dCState["DC"+sysNumber] = false;
                  
                  if(!dCState.DC1 && !dCState.DC2 && !dCState.DC3 && !dCState.DC4) {
                        $('#powerComputer').addClass('btn-danger');
                        $('#powerComputer').removeClass('btn-warning');
                      
                  }
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

function allSysPowerOn() {
	var conf = confirm("Are you sure you want to power on DC1, DC2, DC3 and DC4?");
	if (conf) {
		$.ajax({
			  type: "POST",
			  url: "powerOnComputer.do",
			  dataType: "text",
			  success: function(data) {
				  var json = $.parseJSON(data);
				  if(json.DC1){
                      $('#dc1').removeClass('stateOff');
                      $('#powerComputer').addClass('btn-warning');
                      dCState.DC1 = true;
			     }
                  if(json.DC2){
                      $('#dc2').removeClass('stateOff');         
                      $('#powerComputer').addClass('btn-warning');
                      dCState.DC2 = true;
                  }
                  if(json.DC3) {
                      $('#dc3').removeClass('stateOff');
                      $('#powerComputer').addClass('btn-warning');
                      dCState.DC3 = true;
                  }
                  if(json.DC4) {
                      $('#dc4').removeClass('stateOff');
                      $('#powerComputer').addClass('btn-warning');
                      dCState.DC4 = true;
                  }
                  if( dCState.DC1 && dCState.DC2 && dCState.DC3 && dCState.DC4) {
                	  $('#powerComputer').addClass('btn-success');
    				  $('#powerComputer').removeClass('btn-danger');
    				  $('#powerComputer').removeClass('btn-warning');
                  }
               },
		       error: function(e) {
		    	   $('#message').html("Error communicating!");
				  }
			});
	}
}

function allsysShutdown() {
	var conf = confirm("Are you sure you want to power off DC1, DC2, DC3 and DC4?");
	if (conf) {
		sysShutdown(1, true);
		sysShutdown(2, true);
		sysShutdown(3, true);
		sysShutdown(4, true);
	}
}

function startRestartChrome(){
	$.ajax({
		  type: "POST",
		  url: "checkChromeWindow.do",
		  success: function(data) {
			  if(data.match("000")){
				  startChromeURL(0);
			  }
			  else{
				  killChrome(0);
				  $('#chromeStatus').removeClass('btn-warning');
				  $('#chromeStatus').removeClass('btn-success');
				  $('#chromeStatus').addClass('btn-danger');
			  }
		  }
	});
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
		  beforeSend: function() {
			  $('#chromeStatus').removeClass('btn-danger btn-success').addClass('btn-warning');
	        },
		  success: function(data) {
			  if (data.match("^Error")) {
					alert("Display computers not ready, please try again later");
					
					} else {
						$('#chromeStatus').removeClass('btn-danger btn-warning').addClass('btn-success');
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
			  beforeSend: function() {
				  $('#chromeStatus').removeClass('btn-danger btn-success').addClass('btn-warning');
			  },
			  success: function(data) {
				  $('#message').html("Chrome killed.");
				  	document.getElementById("freezer").style.display = "none";
				  	$('#chromeStatus').removeClass('btn-success btn-warning').addClass('btn-danger');
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
		  data: { sysNumber: sysNumber},
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
        dataType: "text",
        success: function(data) {
            var json = $.parseJSON(data);
             if(json.DC1.match("On")){
				  $('#dc1').removeClass('stateOff');  
				  $('#powerComputer').removeClass('btn-danger');
				  $('#powerComputer').addClass('btn-warning');
				  dCState.DC1 = true;
			  }
			  if(json.DC2.match("On")){
				  $('#dc2').removeClass('stateOff');
				  $('#powerComputer').removeClass('btn-danger');
				  $('#powerComputer').addClass('btn-warning');
				  dCState.DC2 = true;
			  }
			  if(json.DC3.match("On")){
				  $('#dc3').removeClass('stateOff');
				  $('#powerComputer').removeClass('btn-danger');
				  $('#powerComputer').addClass('btn-warning');
				  dCState.DC3 = true;
			  }
			  if(json.DC4.match("On")){
				  $('#dc4').removeClass('stateOff');
				  $('#powerComputer').removeClass('btn-danger');
				  $('#powerComputer').addClass('btn-warning');
				  dCState.DC4 = true;
			  }
			  if(dCState.DC1 && dCState.DC2 && dCState.DC3 && dCState.DC4){
                  $('#powerComputer').addClass('btn-success');
				  $('#powerComputer').removeClass('btn-danger');
				  $('#powerComputer').removeClass('btn-warning');
			  }

			  },
	       error: function(e) {

			  }
		});
}

function checksocket(){
	var count=0;
	for(var i=0; i<8; i++){
		var socketname = '#socket'+i;
		if(sw[i]==0){
			$(socketname).addClass('stateOff');
		}
		else {
			$(socketname).removeClass('stateOff');
			count++;
		}
	}
	if(count==0){
		$('#powerScreen').removeClass('btn-success');
		$('#powerScreen').removeClass('btn-warning');
		$('#powerScreen').addClass('btn-danger');
	}
	else if(count==8){
		$('#powerScreen').removeClass('btn-warning');
		$('#powerScreen').removeClass('btn-danger');
		$('#powerScreen').addClass('btn-success');
	}
	else{
		$('#powerScreen').removeClass('btn-danger');
		$('#powerScreen').removeClass('btn-success');
		$('#powerScreen').addClass('btn-warning');
	}
}

var chrome1 = false;
var chrome2 = false;
var chrome3 = false;
var chrome4 = false;

function checkChrome(){
	$.ajax({
		  type: "POST",
		  url: "checkChromeService.do",
		  success: function(data) {
		  
			  if(dCState.DC1 && data.match("1[1,0][1,0][1,0]")){
				  chrome1 = true;
				  $('#chrome1').removeClass('stateOff');
				  $('#chromeService').removeClass('btn-danger');
				  $('#chromeService').removeClass('btn-success');
				  $('#chromeService').addClass('btn-warning');
			  }
			  if(dCState.DC2 && data.match("[1,0]1[1,0][1,0]")){
				  chrome2 = true;
				  $('#chrome2').removeClass('stateOff');
				  $('#chromeService').removeClass('btn-danger');
				  $('#chromeService').removeClass('btn-success');
				  $('#chromeService').addClass('btn-warning');
			  }
			  if(dCState.DC3 && data.match("[1,0][1,0]1[1,0]")){
				  chrome3 = true;
				  $('#chrome3').removeClass('stateOff');
				  $('#chromeService').removeClass('btn-danger');
				  $('#chromeService').removeClass('btn-success');
				  $('#chromeService').addClass('btn-warning');
			  }
			  if(dCState.DC4 && data.match("[1,0][1,0][1,0]1")){
				  chrome4 = true;
				  $('#chrome4').removeClass('stateOff');
				  $('#chromeService').removeClass('btn-danger');
				  $('#chromeService').removeClass('btn-success');
				  $('#chromeService').addClass('btn-warning');
			  }
			  if(chrome1 && chrome2 && chrome3 && chrome4){
				  $('#chromeService').removeClass('btn-danger');
				  $('#chromeService').removeClass('btn-warning');
				  $('#chromeService').addClass('btn-success');  
			  }
			  },
	       error: function(e) {
	    	   alert("Error in check chrome"+e);

			  }
		});
}
var browser1 = false;
var browser2 = false;
var browser3 = false;
var browser4 = false;

function checkChromeBrowser(){
	$.ajax({
		  type: "POST",
		  url: "checkChromeWindow.do",
		  success: function(data) {
			  if(chrome1 && data.match("1[1,0][1,0][1,0]")){
				  browser1 = true;
				  $('#chromebrowser1').removeClass('stateOff');
				  $('#chromeStatus').removeClass('btn-danger');
				  $('#chromeStatus').removeClass('btn-success');
				  $('#chromeStatus').addClass('btn-warning');
			  }
			  if(chrome2 && data.match("[1,0]1[1,0][1,0]")){
				  browser2 = true;
				  $('#chromebrowser2').removeClass('stateOff');
				  $('#chromeStatus').removeClass('btn-danger');
				  $('#chromeStatus').removeClass('btn-success');
				  $('#chromeStatus').addClass('btn-warning');
			  }
			  if(chrome3 && data.match("[1,0][1,0]1[1,0]")){
				  browser3 = true;
				  $('#chromebrowser3').removeClass('stateOff');
				  $('#chromeStatus').removeClass('btn-danger');
				  $('#chromeStatus').removeClass('btn-success');
				  $('#chromeStatus').addClass('btn-warning');
			  }
			  if(chrome4 && data.match("[1,0][1,0][1,0]1")){
				  browser4 = true;
				  $('#chromebrowser3').removeClass('stateOff');
				  $('#chromeStatus').removeClass('btn-danger');
				  $('#chromeStatus').removeClass('btn-success');
				  $('#chromeStatus').addClass('btn-warning');
			  }
			  if(browser1 && browser2 && browser3 && browser4){
				  $('#chromeStatus').removeClass('btn-danger');
				  $('#chromeStatus').removeClass('btn-warning');
				  $('#chromeStatus').addClass('btn-success');  
			  }
			  },
	       error: function(e) {
				confirm(e);
			  }
		});
}

    var dCState ={
        DC1: false,
        DC2: false,
        DC3: false,
        DC4: false
    };


function checkState(){
	checksocket();
	checkdc();
	setTimeout(function(){if(dCState.DC1 && dCState.DC2 && dCState.DC3 && dCState.DC4){
		checkChrome();
	}
	else {
		 $('#chrome1').addClass('stateOff');
		 $('#chrome2').addClass('stateOff');
		 $('#chrome3').addClass('stateOff');
		 $('#chrome4').addClass('stateOff');
		 $('#chromeService').addClass('btn-danger');
		 $('#chromeService').removeClass('btn-success');
		 $('#chromeService').removeClass('btn-warning');
	};},2000);
//	setInterval(function(){checkdc();},10000);
 	setInterval(function(){
		if(dCState.DC1 && dCState.DC2 && dCState.DC3 && dCState.DC4){
		checkChrome();
		}
		else {
			 $('#chrome1').addClass('stateOff');
			 $('#chrome2').addClass('stateOff');
			 $('#chrome3').addClass('stateOff');
			 $('#chrome4').addClass('stateOff');
			 $('#chromeService').addClass('btn-danger');
			 $('#chromeService').removeClass('btn-success');
			 $('#chromeService').removeClass('btn-warning');
		};},20000); 
	
}

$( document ).ready(function() {
	checkState();
	//$('.btn').tooltip();
	});

</script>
<title>Muvi Manager Power Control</title>
</head>
<body>



<div class="container">

<div class="navbar navbar-default navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header" >
          <a class="navbar-brand" href="index.jsp" title="Click to go Home">MuVi Home</a>
        </div>
    </div>
    </div>
<p>&nbsp;</p>
<p>&nbsp;</p>		
<div class="container">
<div class="process">
    <div class="row">  
         <div class="process-step col-md-4 col-lg-4">
            <button id= "powerScreen" type="button" class="btn btn-circle " onclick="onOrOff();"><i id="screenIcon" class="icon-screen fa-4x"></i></button>
            <p>Power screens</p>
            <span id="socket0">1</span>
			<span id="socket1">2</span>
			<span id="socket2">3</span>
			<span id="socket3">4</span>
			<span id="socket4">5</span>
			<span id="socket5">6</span>
			<span id="socket6">7</span>
			<span id="socket7">8</span>
        </div>
        <div class="process-step col-md-4 col-lg-4">
            <button id= "powerComputer" type="button" onclick= "allSysPowerOn()" class="btn btn-circle btn-danger"><i class="icon-switch fa-4x"></i></button>
            <p>Power DC</p>
            <span id="dc1" class = 'stateOff'>1</span>
			<span id="dc2" class = 'stateOff'>2</span>
			<span id="dc3" class = 'stateOff'>3</span>
			<span id="dc4" class = 'stateOff'>4</span>
        </div>
          <div class="process-step col-md-4 col-lg-4">
            <button id= "chromeService" type="button" class="btn  btn-circle" onclick="javascript:allSysRestart()"><i class=" icon-spinner fa-4x"></i></button>
            <p>Chrome Service</p>
            <span id="chrome1" class = 'stateOff'>1</span>
			<span id="chrome2" class = 'stateOff'>2</span>
			<span id="chrome3" class = 'stateOff'>3</span>
			<span id="chrome4" class = 'stateOff'>4</span>
        </div>
       
    </div>
</div>
    <p>&nbsp;</p>
  <div class="row">
    <div class="collapse-group ">
        <a class="col-md-12 col-lg-12 col-sm-12 col-xs-12 btn" style="padding: 20px; font-size:2em" href="#">Need help starting? &raquo;</a>
        <div class="collapse " >
            <p>
            1. If <i class="icon-screen "></i> button is <span class="text-danger">red</span> click on it and wait for the screens to turn on. When the button is <span class="text-success">green</span> all screens are on.
            </p><br>
            <p>
                2. If <i class="icon-switch"></i> button is <span class="text-danger">red</span> click to power up computers. When the button is <span class="text-success">green</span> all computers are on.
            </p><br>
            <p>
               More detailed help can be found <a href="../manager/help.html"> here</a> 
            </p>
            </div>
    </div>
    </div>
    <br>
</div>
<p>&nbsp;</p>
<div id="powerControls">
<p>&nbsp;</p>
<p>&nbsp;</p>
<a href="javascript:oneClickStartUp()">One Click Start</a>
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
</div>

<p style="clear: both;"></p>
<div class="row">
<div class="collapse-group ">
<a class="col-md-12 col-lg-12 col-sm-12 col-xs-12 btn dcControls" style="padding: 20px; font-size:2em" href="#">DC Controls &raquo;</a>
 <div class="collapse" >
<div id="dcControls">

<div id="startChrome">
<a href="javascript:startChromeURL(1)">Start Chrome 1</a>
<a href="javascript:startChromeURL(2)">Start Chrome 2</a>
<a href="javascript:startChromeURL(3)">Start Chrome 3</a>
<a href="javascript:startChromeURL(4)">Start Chrome 4</a>
</div>

<div id="killChrome">
<a href="javascript:killChrome(1)">Kill Chrome 1</a>
<a href="javascript:killChrome(2)">Kill Chrome 2</a>
<a href="javascript:killChrome(3)">Kill Chrome 3</a>
<a href="javascript:killChrome(4)">Kill Chrome 4</a>
</div>

<div id="restart">
<a href="javascript:sysRestart(1)">Restart DC1</a>
<a href="javascript:sysRestart(2)">Restart DC2</a>
<a href="javascript:sysRestart(3)">Restart DC3</a>
<a href="javascript:sysRestart(4)">Restart DC4</a>
</div>

<div id="shutdown">
<a href="javascript:sysShutdown(1, false)">Power off DC1</a>
<a href="javascript:sysShutdown(2, false)">Power off DC2</a>
<a href="javascript:sysShutdown(3, false)">Power off DC3</a>
<a href="javascript:sysShutdown(4, false)">Power off DC4</a>
</div>
</div>
</div>
</div>
    </div>
    <p></p>
<p id="homeLink"><a href="index.jsp">Home</a></p>
</div>
<div id="freezer" style="display: none; height:100%;width:100%; background-color:rgba(0,0,0,0.7); position: fixed; top:0px; right:0px">
<p id="freezerMessage" style="text-align:center; position: relative; top:50%; color:rgb(180,220,211)"></p>
<p style="text-align:center; position: relative; top:50%; color:rgb(180,220,211)">Please Wait...</p>
<img style="position: relative; display:block; margin-left: auto; margin-right: auto; top:50%; " alt="" src="./style/ajax-loader.gif">
</div>
<script src="js/bootstrap.js"></script>
    <script type="text/javascript">
    $('.row .btn').on('click', function(e) {
    e.preventDefault();
    var $this = $(this);
    var $collapse = $this.closest('.collapse-group').find('.collapse');
    $collapse.collapse('toggle');
});
    </script>
</body>
</html>