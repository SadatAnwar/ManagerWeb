<%@page
	import="de.fraunhofer.iao.muvi.managerweb.web.SimpleURLController"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<title>Upload new PowerPoint presentation - Muvi Manager</title>
<head>
<style>
#drop {
  margin-left: auto;
  margin-right: auto;
  min-height: 200px;
  width: 90%;
  text-align:center;
  font-size: 5em;
}
#drop p {
	vertical-align:middle;
}
.hover {
color: rgb(199, 202, 204);
border: 2px dashed #ff0000;
}
.default {
color: rgba(199, 202, 204, 0.6);
border: 1px dashed grey;
}
.hidden {
display: none;

}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="style/manager.css">
<script type="text/javascript" src="js/jquery-1.11.0.js"></script>
</head>
<body>
<body onload="">
	<h1>Upload new Dash-board VISML</h1>
	<p><jsp:include page="partials/message.jsp"></jsp:include></p>
	<form  id ="pptuploadForm" method="post" enctype="multipart/form-data" action = "dashboardUpload.do"><br>
    <input id="file" type="file" name="img" style="width: 100%;"><br>
    <input type="submit" value="Uplod presentation" style="width: 100%;"><br>
   </form>
    <div id="list" class="hidden">
    <ul id="imageList">
    </ul>
    </div>
	<p id="homeLink">
	<a href="index.jsp">Home</a>
	</p>
	<p id="homeLink">
	<a href="scenarios.do">Scenarios</a>
     <a href="help.html">Help page</a>   
	</p>
</body>
</html>