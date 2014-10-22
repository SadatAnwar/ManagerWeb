<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="style/bootstrap.css">
<link rel="stylesheet" type="text/css" href="style/manager.css">
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
		var thisFrameLink = document.getElementById("linkFrame"+(index+1));
		var decodedURL = decodeURIComponent(decodeURIComponent(element));
		thisFrame.src = decodedURL;
		thisFrameLink.href = decodedURL;
		frames.push(thisFrame);
	});
}

function clickFrame(index) {
	var theLink = decodeURIComponent(decodeURIComponent(arr[index - 1]));
	document.location.href=theLink;
}



</script>

</head>

<body onload="makeIframeAwesome()">



<h1>MuVi State</h1>
<div class="container">
    <div class="container">
        <div class="row">
            <div class="col-xs-2 col-md-2 col-lg-2">
                <a id="linkFrame2" href="#"><strong>2</strong></a><br><iframe id = "iframe2" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe>
            </div>
            <div class="col-xs-2 col-md-2 col-lg-2">
                <a id="linkFrame2" href="#"><strong>2</strong></a><br><iframe id = "iframe2" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe>
            </div>
            <div class="col-xs-2 col-md-2 col-lg-2">
                <a id="linkFrame2" href="#"><strong>2</strong></a><br><iframe id = "iframe2" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe>
            </div>
            <div class="col-xs-2 col-md-2 col-lg-2">
                <a id="linkFrame2" href="#"><strong>2</strong></a><br><iframe id = "iframe2" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe>
            </div>
            <div class="col-xs-2 col-md-2 col-lg-2">
                <a id="linkFrame2" href="#"><strong>2</strong></a><br><iframe id = "iframe2" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe>
            </div>
            <div class="col-xs-2 col-md-2 col-lg-2">
                <a id="linkFrame2" href="#"><strong>2</strong></a><br><iframe id = "iframe2" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe>
            </div>
        </div>  
    </div>
        
            
<table class="table text-center table-bordered">
 <tbody>
	<tr>
		<td><a id="linkFrame1" href="#"><strong>1</strong></a><br><iframe id = "iframe1" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe><img onclick="clickFrame(1)" src="style/images/transparent.png" style="position: relative; left: -96px; top: -54px; width: 96px; height: 54px; z-index: 10;" alt="" title="Click to open"></td>
		<td><a id="linkFrame2" href="#"><strong>2</strong></a><br><iframe id = "iframe2" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame3" href="#"><strong>3</strong></a><br><iframe id = "iframe3" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame4" href="#"><strong>4</strong></a><br><iframe id = "iframe4" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame5" href="#"><strong>5</strong></a><br><iframe id = "iframe5" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame6" href="#"><strong>6</strong></a><br><iframe id = "iframe6" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
	</tr>
	<tr>
		<td><a id="linkFrame7" href="#"><strong>7</strong></a><br> <iframe id = "iframe7" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame8" href="#"><strong>8</strong></a><br> <iframe id = "iframe8" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame9" href="#"><strong>9</strong></a><br> <iframe id = "iframe9" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame10" href="#"><strong>10</strong></a><br><iframe id = "iframe10" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame11" href="#"><strong>11</strong></a><br><iframe id = "iframe11" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame12" href="#"><strong>12</strong></a><br><iframe id = "iframe12" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
	</tr>
	<tr>
		<td><a id="linkFrame13" href="#"><strong>13</strong></a><br><iframe id = "iframe13" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame14" href="#"><strong>14</strong></a><br><iframe id = "iframe14" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame15" href="#"><strong>15</strong></a><br><iframe id = "iframe15" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame16" href="#"><strong>16</strong></a><br><iframe id = "iframe16" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame17" href="#"><strong>17</strong></a><br><iframe id = "iframe17" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame18" href="#"><strong>18</strong></a><br><iframe id = "iframe18" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
	</tr>
	<tr>
		<td><a id="linkFrame19" href="#"><strong>19</strong></a><br><iframe id = "iframe19" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame20" href="#"><strong>20</strong></a><br><iframe id = "iframe20" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame21" href="#"><strong>21</strong></a><br><iframe id = "iframe21" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame22" href="#"><strong>22</strong></a><br><iframe id = "iframe22" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame23" href="#"><strong>23</strong></a><br><iframe id = "iframe23" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame24" href="#"><strong>24</strong></a><br><iframe id = "iframe24" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
	</tr>
	<tr>
		<td><a id="linkFrame25" href="#"><strong>25</strong></a><br><iframe id = "iframe25" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame26" href="#"><strong>26</strong></a><br><iframe id = "iframe26" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame27" href="#"><strong>27</strong></a><br><iframe id = "iframe27" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame28" href="#"><strong>28</strong></a><br><iframe id = "iframe28" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame29" href="#"><strong>29</strong></a><br><iframe id = "iframe29" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame30" href="#"><strong>30</strong></a><br><iframe id = "iframe30" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
	</tr>
	<tr>
		<td><a id="linkFrame31" href="#"><strong>31</strong></a><br><iframe id = "iframe31" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame32" href="#"><strong>32</strong></a><br><iframe id = "iframe32" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame33" href="#"><strong>33</strong></a><br><iframe id = "iframe33" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame34" href="#"><strong>34</strong></a><br><iframe id = "iframe34" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame35" href="#"><strong>35</strong></a><br><iframe id = "iframe35" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
		<td><a id="linkFrame36" href="#"><strong>36</strong></a><br><iframe id = "iframe36" scrolling="no" frameborder="0" width="96" height = "54" src=""></iframe> </td>
	</tr>
	</tbody>
</table>
</div>
<p>&nbsp;</p>

<p id="scenariosLink"><a href="scenarios.do">Scenarios</a></p>
<p id="homeLink"><a href="index.jsp">Home</a></p>

</body>
</html>