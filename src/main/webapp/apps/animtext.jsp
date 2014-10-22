<!doctype html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>Animation</title>
<script src="../js/jquery-1.11.0.js"></script>
<script type="text/javascript">

$( document ).ready(function() {
animate();
});

var text = true;

function animate() {

setTimeout(
  function()
  {


	if (text) {
	$('#d1').fadeIn(2000, 'linear');
	$('#d2').fadeOut(2000, 'linear');
	text = false;

} else {

	text = true;
	$('#d2').fadeIn(2000, 'linear');
	$('#d1').fadeOut(2000, 'linear');

}

    animate();

  }, <%=request.getParameter("speed")%>);

}


</script>
<style>
body {
	width: 100%;
	height: 100%;
	overflow: hidden;
}
#d1, #d2, #innerd1, #innerd2 {
	width: 100%;
	height: 100%;
	position: absolute;
	top: 0;
	left: 0;
}

#d1, #d2 {
	text-align: center;
	<%=request.getParameter("style")%>
}

#innerd1, #innerd2 {
	display: inline-block;
	vertical-align: middle;
}

#container {
	width: 100%;
	height: 100%;
	display: table;
}

#text {
	width:100%;
	height: 100%;
	display: table-cell;
	text-align:center;
	vertical-align: middle;
	font-size: 20em;
	font-family: sans-serif;
	<%=request.getParameter("style")%>
}

</style>
</head>
<body>

<div id="container">
<div id="d1"><div id="container">
<div id="text"><%=request.getParameter("text") %></div>
</div></div>
<div id="d2"><img src="<%=request.getParameter("img") %>" width="100%" height="100%"></div>
</div>

</body>
</html>