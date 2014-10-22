<%@page
	import="de.fraunhofer.iao.muvi.managerweb.web.SimpleURLController"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<title>Upload Image - Muvi Manager</title>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="style/manager.css">
<script type="text/javascript" src="js/jquery-1.11.0.js"></script>
</head>
<body>
	<h1>Upload new images</h1>
	<p><jsp:include page="partials/message.jsp"></jsp:include></p>
	
	<form id="fileuploadForm" action="uploadImage.do" method="post" enctype="multipart/form-data" >
               <input id="file" type="file" name="img" multiple="multiple" accept="image/*">
               <p>
               <input type="submit" value="Upload">
     </form>
     
     <c:if test="${fn:length(imagesURLs) > 0}">
     	<p>&nbsp;</p>
		<p>URLs for the images have been generated:</p>
		<ul>
		     <c:forEach var="imageURL" items="${imagesURLs}">
		     	<li><img src="${imageURL}" height="100" alt="Image"> ${imageURL}</li>
		     </c:forEach>
		</ul>
     </c:if>
     
     <p>&nbsp;</p>
     
	<p id="homeLink">
	<a href="index.jsp">Home</a>
	</p>
	
</body>
</html>