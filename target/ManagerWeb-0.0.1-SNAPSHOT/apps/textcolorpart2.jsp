<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>Text</title>
<style type="text/css">

body {

	overflow:hidden;
	width: 100%;
	height: 100%;

<% if (request.getParameter("style") != null) { %>
	<%=request.getParameter("style")%>
<% } %>
}

#container {
	width: 100%;
	height: 100%;
	display: table;
	
}

#textBox {
	overflow: visible;
	position: relative;
	width: 100%;
	height: 100%;
	display: table-cell;
	white-space: pre;
	
}
#text{
	position: relative;
	font-size: 1em;
	font-family: "Frutiger", Verdana;
	top: -8px;
	left: 0px;
}

</style>
</head>
<body>
<div id="container">
<div id="textBox"><span id= "text"><%=request.getParameter("text")%></span>
</div>
</div>
<script type="text/javascript">

var cont = document.getElementById("container");
var rows = <%=request.getParameter("rows")%>;
var cols = <%=request.getParameter("cols")%>;
var row = <%=request.getParameter("row")%>;
var col = <%=request.getParameter("col")%>;
var textBox = document.getElementById("textBox");
var text = document.getElementById("text");
var scroll = text.scrollHeight;
var topPos = -8;
var left = 0;
var pixX=2*32;
var pixY=2*28;

var millisecondsToWait = 1;
var fontSize = 1;
var a = text.clientHeight;
setTimeout(function() {
    while(text.offsetHeight < ((window.innerHeight+pixY)*rows) && text.offsetWidth < ((window.innerWidth+pixX)*cols)){
        text.style.fontSize = fontSize+"em";
        fontSize=fontSize+10;
    }
    while(text.offsetHeight > ((window.innerHeight+pixY)*rows) || text.offsetWidth > ((window.innerWidth+pixX)*cols)){
    	fontSize=fontSize-1;
    	text.style.fontSize = fontSize+"em";
    }
    reposition();
    horizCentre();
    vertCentre();
    },millisecondsToWait);
    
    function reposition(){
    	topPos = topPos -((pixY+window.innerHeight)*(row-1));
    	left = left -((pixX+window.innerWidth)*(col-1));
    	text.style.top= topPos+"px";
    	text.style.left= left+"px";
    }
    
    function horizCentre(){
    	var textWidth = text.offsetWidth;
    	var containerWidth = ((window.innerWidth+pixX)*cols);
    	var extraRoom = containerWidth - textWidth;
    	var centreMovement = extraRoom/2;
    	left = left +centreMovement;
    	text.style.left= left+"px";
    }
    
    function vertCentre(){
    	var textHeight = text.offsetHeight;
    	var containerHeight = ((window.innerHeight+pixY)*rows);
    	var extraRoom = containerHeight - textHeight;
    	var centreMovement = extraRoom/2;
    	topPos = topPos +centreMovement;
    	text.style.top= topPos+"px";
    }
  

</script>
</body>
</html>