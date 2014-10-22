<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>A big CoolClock</title>

		<link rel="stylesheet" type="text/css" href="analogClock/css/styles.css">

		<!--[if IE]><script type="text/javascript" src="analogClock/js/excanvas.js"></script><![endif]-->
		<script src="analogClock/js/coolclock.js" type="text/javascript"></script>

	<style type="text/css">
	#canvas-container {
   width: 100%;
   text-align:center;
}

canvas {
   display: inline;
}
	</style>
	<script type="text/javascript">
		function get_url_param( name )
		{
			name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");

			var regexS = "[\\?&]"+name+"=([^&#]*)";
			var regex = new RegExp( regexS );
			var results = regex.exec( window.location.href );

			if ( results == null )
				return "";
			else
				return results[1];
		}
		
		function startClock()
		{
			//Extract parameters from URL
			cityName = get_url_param('city');
			gmtOffset = get_url_param('gmt');
			
			//Convert String to Integer
			gmtOffsetInt = parseInt(gmtOffset);
			
			//Initialize Clocks
			CoolClock.findAndCreateClocks();
			
			//Get Clock reference, set GMT/UTC
			var clock = globalClocks[0].setGMT(gmtOffset);			
			
			//Set Text for UTC
			var gmtTextField = document.getElementById("gmtOffsetText");
			gmtTextField.innerText = "UTC " + gmtOffset;
				
			//Set Text for City
			var cityTextField = document.getElementById("cityText");
			cityTextField.innerText = cityName;
		}
		
	</script>
	<style>[touch-action="none"]{ -ms-touch-action: none; touch-action: none; }[touch-action="pan-x"]{ -ms-touch-action: pan-x; touch-action: pan-x; }[touch-action="pan-y"]{ -ms-touch-action: pan-y; touch-action: pan-y; }[touch-action="scroll"],[touch-action="pan-x pan-y"],[touch-action="pan-y pan-x"]{ -ms-touch-action: pan-x pan-y; touch-action: pan-x pan-y; }</style></head>
	<body onload="startClock();"><div id="StayFocusd-infobar" style="display:none;">
</div>
	<div id="canvas-container">
		<div id="cityText" style="font-family:sans-serif;font-size:150px; font-weight:bold;text-transform:uppercase; margin-bottom: -30px;">Berlin</div>
		<div id="gmtOffsetText" style="font-family:sans-serif;font-size:20px; font-weight:bold;text-transform:uppercase;margin-bottom: 30px;">UTC +1</div>
		<canvas id="c1" class="CoolClock::400::+1 leftRightPad" width="850" height="850" style="width: 850px; height: 850px;"></canvas>
	</div>
</div>



	

</body></html>