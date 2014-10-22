<%@ page import="de.fraunhofer.iao.muvi.managerweb.web.XMLController"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="style/manager.css">
<title>XML editor - Muvi Manager</title>
<script type="text/javascript" src="js/jquery-1.11.0.js"></script>
</head>
<body>

<h1>XML editor</h1>

<form method="post" action="xml.do" id="xmlEditor">

<div id="editor">${xml}</div>

<script src="js/ace/ace.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
    var editor = ace.edit("editor");
    editor.setTheme("ace/theme/monokai");
    editor.getSession().setMode("ace/mode/xml");
    
    function updateXml() {
    	$('#xml').val(editor.getValue());
    }
    
</script>

<p id="message">${message}</p>

<textarea id="xml" name="xml" placeholder="Enter XML code here">${xml}</textarea>

<input type="submit" value="Save" name="<%=XMLController.ACTION_SAVE%>" onclick="updateXml(); return true;">
<input type="submit" value="Show" name="<%=XMLController.ACTION_SHOW%>" onclick="updateXml(); return true;">

</form>

<p>&nbsp;</p>

<p id="scenariosLink"><a href="scenarios.do">Scenarios</a></p>

<p id="homeLink"><a href="index.jsp">Home</a></p>

</body>
</html>