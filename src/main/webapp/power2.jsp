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
<script type="text/javascript">
var sw = ['${Socket1}','${Socket2}','${Socket3}','${Socket4}','${Socket5}','${Socket6}','${Socket7}','${Socket8}'];
</script>
<script src="js/powerScript.js" type="text/javascript"></script>
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
            <button id= "powerComputer" type="button" onclick= "checkDcPowerOnStatus()" class="btn btn-circle btn-danger"><i class="icon-switch fa-4x"></i></button>
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