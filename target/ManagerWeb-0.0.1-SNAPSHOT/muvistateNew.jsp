<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="style/bootstrap.css">
<link rel="stylesheet" type="text/css" href="style/manager.css">
<script type="text/javascript" src="js/jquery-1.11.0.js"></script>
<title>MuVi State</title>

<script type="text/javascript">

function goToURL(encoded) {
	var value = decodeURIComponent(encoded);
	document.location.href=value;
}

var arr = ${urlList};

function makeIframeAwesome() {

	var frames= [];
	arr.forEach(function (element, index) {
		var thisFrame = document.getElementById("iframe"+(index+1));
		var decodedURL = decodeURIComponent(decodeURIComponent(element));
		thisFrame.src = decodedURL;
		frames.push(thisFrame);
	});
}

function clickFrame(index) {
	var theLink = decodeURIComponent(decodeURIComponent(arr[index - 1]));
	document.location.href=theLink;
}
   
function generatePage() {
    var n = 1;
    for (var rows = 1; rows <=6; rows++) {
        var rowDom=[];
        rowDom.push('<div class="row" id="row'+rows+'"></div>');
        $("#outer").append(rowDom);
        for (var cols = 1; cols <=6; cols++) {
            var iframeTag = '<iframe id = "iframe'+n+'" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe>';
            var transparentImage = ' <img onclick="clickFrame('+n+')" src="style/images/transparent.png"'+
                'style="position: relative;  top: -59px; width: 96px; height: 54px; z-index: 10;" alt="" title="Click to open">';
            var cell=[];
            cell.push('<div class="col-xs-2 col-md-2 col-lg-2">');
            cell.push(iframeTag,transparentImage);
            cell.push('</div>');
            $("#row"+rows).append(cell.join(''));
            n++;
        }
    }   
    makeIframeAwesome();
}
    
</script>
</head>

<body onload="generatePage()">
    <h1>MuVi Overview</h1>
    <div id= "outer" class="container">
    </div>  
    <p>&nbsp;</p>
    <p id="scenariosLink"><a href="scenarios.do">Scenarios</a></p>
    <p id="homeLink"><a href="index.jsp">Home</a></p>

</body>
</html>