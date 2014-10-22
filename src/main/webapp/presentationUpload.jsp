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
	<h1>Upload new PowerPoint presentation</h1>
	<p><jsp:include page="partials/message.jsp"></jsp:include></p>
	<form  id ="pptuploadForm" method="post" enctype="multipart/form-data" action = "presentationUpload.do"><br>
    <input id="file" type="file" name="img" style="width: 100%;"><br>
    <input type="checkbox" name="generateOverview" id="generateOverview" checked><label for="generateOverview" style="font-size: 1.5em;">Generate overview scene(s)</label><br>
	<input type="checkbox" name="generateZoom" id="generateZoom" checked><label for="generateZoom" style="font-size: 1.5em;">Generate zoom scenes</label><br>
	<input type="radio" name="aspectRatio" value="640" id="aspectRatio43" checked="checked"><label for="aspectRatio43" style="font-size: 1.5em;">Aspect ratio 4/3</label><br>
	<input type="radio" name="aspectRatio" value="1920" id="aspectRatio169"><label for="aspectRatio169" style="font-size: 1.5em;">Aspect ratio 16/9</label><br>
   <input type="submit" value="Uplod presentation" style="width: 100%;"><br>
   </form>
    <c:if test="${fn:length(imagesURLs) > 0}">
    	<p>&nbsp;</p>
	<p>These images were extracted from presentation:</p>
	<ul>
	     <c:forEach var="imageURL" items="${imagesURLs}">
	     	<li><img src="${imageURL}" height="100" alt="Image"></li>
	     </c:forEach>
	</ul>
    </c:if>

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
<script type="text/javascript">

var files;

function handleFile(evt) {
  $('#drop').removeClass('hover');
  $('#drop').addClass('default');
  evt.stopPropagation();
  evt.preventDefault();
  files = evt.dataTransfer.files; // FileList object.
  handleFileSelect(files);
}
function handleFileSelect(files){
  // files is a FileList of File objects. List some properties.
  var output = [];
  for (var i = 0, f; f = files[i]; i++) {
	  output.push('<li id=',f.name,'></li>');
   	 handleFileUpload(f);
  }  
  $("#imageList").append(output.join(''));
 // document.getElementById('list').insertBefore( '<ul>' + output.join('') + '</ul>';
}

function uploadImage(event, file) {  
	  object = {};
	  object.filename = file.name;
	  object.data = event.target.result;
	  var fd = new FormData();
	    fd.append("fileToUpload", file);
	    $.ajax({url: "presentationUpload.do",
	          type: 'POST',
	          cache: false,
	          contentType: false,
	          processData: false,	          
	          data: fd,
	          success: function(data) {
	        	  
	        	  var output = [];
	        	    output.push('<img src=', data, ' height="100" alt="Image">', data); 
	        	  document.getElementById(file.name.replace(new RegExp(" ", 'g'),"_")).innerHTML = output.join('');
	        	  $('#list').removeClass('hidden');
	        	  	  
	          },
	          error: function(data){
	        	  alert(data);
	          }
	    
	    });
	};

function handleFileUpload(file) {
	var reader = new FileReader(file);
	reader.onload = function(event) {  
		uploadImage(event, file);
	};  
	reader.readAsDataURL(file);
}

function handleDragOver(evt) {
  evt.stopPropagation();
  evt.preventDefault();
  evt.dataTransfer.dropEffect = 'copy'; // Explicitly show this is a copy.
}


function dragEnter(event){
	$('#drop').removeClass('default');
	$('#drop').addClass('hover'); 
	event.preventDefault();
}
function dragLeave(event){
	$('#drop').removeClass('hover');
	$('#drop').addClass('default'); 
	event.preventDefault();
}


// Setup the dnd listeners.
/* var dropZone = document.getElementById('drop');
dropZone.addEventListener('dragover', handleDragOver, false);
dropZone.addEventListener('drop', handleFile, false);
dropZone.addEventListener('dragenter', dragEnter  , false);
dropZone.addEventListener('dragleave', dragLeave  , false);
 */
</script>	
</body>
</html>