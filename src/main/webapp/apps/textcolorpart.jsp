<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>Text</title>
<%

double width = 1920;
double height = 1080;

double cols = 6;
double rows = 6;
if (request.getParameter("cols") != null) {
	cols = Integer.parseInt(request.getParameter("cols"));
}
if (request.getParameter("rows") != null) {
	rows = Integer.parseInt(request.getParameter("rows"));
}


double pixX=32;
double pixY=28;

if (request.getParameter("bezels") != null) {
	pixX=0;
	pixY=0;
}

double scaleX = (((1920*cols)+(pixX*2*(cols-1)))/1920);
double scaleY = (((1080*rows)+(pixY*2*(rows-1)))/1080);

double col = Integer.parseInt(request.getParameter("col"));
double row = Integer.parseInt(request.getParameter("row"));


/*
What this has done is effectively just inlarged the screen size
taking into account the bazels, and then each screen has an origen defined
this origen is just after the end of the previous screen, the bayel is a part of the previous screen
make any sense?
*/

double originX =0;
if (col > 1) {
	originX = 100/(cols-1)*(col-1);
}

double originY = 0;
if (row > 1) {
	originY = 100/(rows-1)*(row-1);	
}

double widthP = 100;
double heightP = 100;

double imageRatio = width / height;
double screenRatio = 1920.0 / 1080.0;

if (imageRatio > screenRatio) {
	widthP = 100;
	heightP = 100 * (1920 * height) / (1080 * width);
} else {
	heightP = 100;
	widthP = 100 * (1080 * width) / (1920 * height);
}



originX = Math.round(originX*100)/100.0d;
originY = Math.round(originY*100)/100.0d;
scaleX = Math.round(scaleX*100000)/100000.0d;
scaleY = Math.round(scaleY*100000)/100000.0d;
widthP = Math.round(widthP*100000)/100000.0d;
heightP = Math.round(heightP*100000)/100000.0d;

double topPosition = (100.0 - heightP) / 2.0;


%>
<style type="text/css">

body {

	overflow: hidden;
	width: 100%;
	height: 100%;

<% if (request.getParameter("style") != null) { %>
	<%=request.getParameter("style")%>
<% } %>
}

#container {
	width: 100%;
	height: 100%;
	overflow: hidden;
	display: table;
	-webkit-transform-origin: <%=originX%>%  <%=originY%>%;
	-webkit-transform: scale(<%=scaleX%>,<%=scaleY%>) ;
	-moz-transform-origin: <%=originX%>%  <%=originY%>%;
	-moz-transform: scale(<%=scaleX%>,<%=scaleY%>) ;
}

#text {
	overflow: hidden;
	position: relative;
	top: <%=topPosition%>%;
	width: <%=widthP%>%;
	height: <%=heightP%>%;
	display: table-cell;
	text-align:center;
	vertical-align: middle;
	font-size: 15em;
	font-family: "Frutiger", Verdana;
}
</style>
</head>
<body>
<div id="container">
<div id="text">
<%=request.getParameter("text")%>
</div>
</div>
</body>
</html>