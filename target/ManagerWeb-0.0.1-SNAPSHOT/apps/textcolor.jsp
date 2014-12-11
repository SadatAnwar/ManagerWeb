<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>Text</title>
<style type="text/css">

body {
	<%=request.getParameter("style")%>
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
/*	font-size: 20em;*/
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
<script type="text/javascript">
    
var style = "<%=request.getParameter("style")%>";

    if(!(style.indexOf("font-size")>-1)){
        var cont = document.getElementById("container");
        var text = document.getElementById("text");

        text.style.fontSize = "20em";
        
        
        var millisecondsToWait = 1; 

        setTimeout(function() {
            if (text.clientHeight > window.innerHeight || text.clientWidth > window.innerWidth) {
                text.style.fontSize = "15em";
            }
            setTimeout(function() {
                if (text.clientHeight > window.innerHeight || text.clientWidth > window.innerWidth) {
                    text.style.fontSize = "10em";
                }
                setTimeout(function() {
                    if (text.clientHeight > window.innerHeight || text.clientWidth > window.innerWidth) {
                        text.style.fontSize = "5em";
                    }
                    setTimeout(function() {
                        if (text.clientHeight > window.innerHeight || text.clientWidth > window.innerWidth) {
                            text.style.fontSize = "1em";
                        }
                    }, millisecondsToWait);
                }, millisecondsToWait);
            }, millisecondsToWait);
        }, millisecondsToWait);
    } else {
        var text = document.getElementById("text");
        text.style.fontSize = "1em";
    }


</script>
</body>
</html>