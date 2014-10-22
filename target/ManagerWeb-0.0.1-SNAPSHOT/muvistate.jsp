<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="style/bootstrap.css">
<link rel="stylesheet" type="text/css" href="style/manager.css">
<title>MuVi State</title>
</head>
<script type="text/javascript">
function goToURL(encoded) {
	var value = decodeURIComponent(encoded);
	document.location.href=value;
}
function makeIframeAwesome() {
	var arr = ${urlList};
	var n = 1
	arr.forEach(function (element, index) {
		var thisFrame = document.getElementById("iframe"+(index+1));
		var decodedURL = decodeURIComponent(decodeURIComponent(element));
		thisFrame.src = decodedURL;
		thisFrame.contentWindow.document.body.onclick = 
			function(e) {
			e.preventDefault();
			document.location.href=thisFrame.src;
			}
	});
	var url = decodeURIComponent(decodeURIComponent('${screen1URL}'));
	thisFrame.src = url;
	
}



</script>
<body onload="makeIframeAwesome()">

<h1>MuVi State</h1>
<div class="container">
<table class="table text-center table-bordered">
 <tbody>
	<tr>
		<td><strong>1</strong><br> <iframe id = "iframe1" scrolling="no" frameborder="0" width="100" height = "60" src=""></iframe> </td>
		<td><strong>2</strong><br><a href="javascript:goToURL('${screen2URL}')">${screen2State}</a></td>
		<td><strong>3</strong><br><a href="javascript:goToURL('${screen3URL}')">${screen3State}</a></td>
		<td><strong>4</strong><br><a href="javascript:goToURL('${screen4URL}')">${screen4State}</a></td>
		<td><strong>5</strong><br><a href="javascript:goToURL('${screen5URL}')">${screen5State}</a></td>
		<td><strong>6</strong><br><a href="javascript:goToURL('${screen6URL}')">${screen6State}</a></td>
	</tr>
	<tr>
		<td><strong>7</strong><br><a href="javascript:goToURL('${screen7URL}')">${screen7State}</a></td>
		<td><strong>8</strong><br><a href="javascript:goToURL('${screen8URL}')">${screen8State}</a></td>
		<td><strong>9</strong><br><a href="javascript:goToURL('${screen9URL}')">${screen9State}</a></td>
		<td><strong>10</strong><br><a href="javascript:goToURL('${screen10URL}')">${screen10State}</a></td>
		<td><strong>11</strong><br><a href="javascript:goToURL('${screen11URL}')">${screen11State}</a></td>
		<td><strong>12</strong><br><a href="javascript:goToURL('${screen12URL}')">${screen12State}</a></td>
	</tr>
	<tr>
		<td><strong>13</strong><br><a href="javascript:goToURL('${screen13URL}')">${screen13State}</a></td>
		<td><strong>14</strong><br><a href="javascript:goToURL('${screen14URL}')">${screen14State}</a></td>
		<td><strong>15</strong><br><a href="javascript:goToURL('${screen15URL}')">${screen15State}</a></td>
		<td><strong>16</strong><br><a href="javascript:goToURL('${screen16URL}')">${screen16State}</a></td>
		<td><strong>17</strong><br><a href="javascript:goToURL('${screen17URL}')">${screen17State}</a></td>
		<td><strong>18</strong><br><a href="javascript:goToURL('${screen18URL}')">${screen18State}</a></td>
	</tr>
	<tr>
		<td><strong>19</strong><br><a href="javascript:goToURL('${screen19URL}')">${screen19State}</a></td>
		<td><strong>20</strong><br><a href="javascript:goToURL('${screen20URL}')">${screen20State}</a></td>
		<td><strong>21</strong><br><a href="javascript:goToURL('${screen21URL}')">${screen21State}</a></td>
		<td><strong>22</strong><br><a href="javascript:goToURL('${screen22URL}')">${screen22State}</a></td>
		<td><strong>23</strong><br><a href="javascript:goToURL('${screen23URL}')">${screen23State}</a></td>
		<td><strong>24</strong><br><a href="javascript:goToURL('${screen24URL}')">${screen24State}</a></td>
	</tr>
	<tr>
		<td><strong>25</strong><br><a href="javascript:goToURL('${screen25URL}')">${screen25State}</a></td>
		<td><strong>26</strong><br><a href="javascript:goToURL('${screen26URL}')">${screen28State}</a></td>
		<td><strong>27</strong><br><a href="javascript:goToURL('${screen27URL}')">${screen27State}</a></td>
		<td><strong>28</strong><br><a href="javascript:goToURL('${screen28URL}')">${screen28State}</a></td>
		<td><strong>29</strong><br><a href="javascript:goToURL('${screen29URL}')">${screen29State}</a></td>
		<td><strong>30</strong><br><a href="javascript:goToURL('${screen30URL}')">${screen30State}</a></td>
	</tr>
	<tr>
		<td><strong>31</strong><br><a href="javascript:goToURL('${screen31URL}')">${screen31State}</a></td>
		<td><strong>32</strong><br><a href="javascript:goToURL('${screen32URL}')">${screen32State}</a></td>
		<td><strong>33</strong><br><a href="javascript:goToURL('${screen33URL}')">${screen33State}</a></td>
		<td><strong>34</strong><br><a href="javascript:goToURL('${screen34URL}')">${screen34State}</a></td>
		<td><strong>35</strong><br><a href="javascript:goToURL('${screen35URL}')">${screen35State}</a></td>
		<td><strong>36</strong><br><a href="javascript:goToURL('${screen36URL}')">${screen36State}</a></td>
	</tr>
	</tbody>
</table>
</div>
<p>&nbsp;</p>

<p id="scenariosLink"><a href="scenarios.do">Scenarios</a></p>
<p id="homeLink"><a href="index.jsp">Home</a></p>

</body>
</html>