<!doctype html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>Image</title>
<style>
body {
	width: 100%;
	height: 100%;
	overflow: hidden;
}

#d2 {
	width: 100%;
	height: 100%;
	position: absolute;
	top: 0;
	left: 0;
}

#d2 {
	text-align: center;
}

#container {
	width: 100%;
	height: 100%;
	display: table;
}
</style>
</head>
<body>
	<div id="container">
		<div id="d2">
			<img alt="Image" src="<%=request.getParameter("img")%>" width="100%"
				height="100%">
		</div>
	</div>
</body>
</html>