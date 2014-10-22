<%@page
	import="de.fraunhofer.iao.muvi.managerweb.web.SimpleURLController"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<title>Uploaded Image Gallery - Muvi Manager</title>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" href="style/bootstrap.css">
<link rel="stylesheet" type="text/css" href="style/manager.css">
<script type="text/javascript" src="js/jquery-1.11.0.js"></script>
</head>
<body>
<div class="navbar navbar-default navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header" >
          <a class="navbar-brand" href="index.jsp" title="Click to go Home">MuVi Home</a>
        </div>
    </div>
    </div>
<c:if test="${fn:length(imagesURLs) > 0}">

<script type="text/javascript">

var lastShownImage = 9;


function showMore() {

	var lastShownImageTmp = lastShownImage + 1;
	for (var i=0; i<10; i++) {
		var nextIdx = lastShownImageTmp + i;
		showImage(nextIdx);
	}
	
}

function showImage(j) {
	if (imgUrls.length > j) {
		imgUrl = imgUrls[j];
		$('#images').append(
				'<div class="col-md-3 col-sm-4 col-xs-6"><a href="' + imgUrl + '"><img src="' + imgUrl + '" height="100" alt="Image"></a> </div>'
				);
		lastShownImage = j;
	}
}

</script>

     	<p>&nbsp;</p>
     	<p>&nbsp;</p>
     	<div class="container">
			<div class="page-header">
				<h1>Click to see full image</h1>
			</div>
			<div class="row" id="images">
				 <c:forEach var="imageURL" items="${imagesURLs}" varStatus="loopCounter">
				 	<c:if test="${loopCounter.count <= 10}">
				 		<div class="col-md-3 col-sm-4 col-xs-6"><a href="${imageURL}"><img src="${imageURL}" height="100" alt="Image"></a> </div>
				 	</c:if>
				 </c:forEach>		 
			</div>
			<div class="row">
				<div class="col-md-12 col-sm-12 col-xs-12 text-center" style="padding-top: 15px; padding-bottom: 15px;"><button type="button" class="btn btn-primary btn-lg" onClick="javascript:showMore();">Show more...</button></div>
			</div>
	 </div>
</c:if>
<c:if test="${fn:length(imagesURLs) == 0}">
	<div class="container">
 	 	<p>&nbsp;</p>
     	<p>&nbsp;</p>
		<p>No Images on the server or image list could not be loaded.</p>
	</div>
</c:if>

<script type="text/javascript">

var imgUrls = [
               <c:forEach var="imageURL" items="${imagesURLs}" varStatus="loopCounter">
               
               <c:set var="urlEsc" value="${fn:replace(imageURL, 
                   '\\\\', '\\\\\\\\')}" />
               
              "${urlEsc}",
               </c:forEach>
               ];

</script>	
</body>
</html>