<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
	import= "java.nio.file.Files"
	import= "java.net.URL"
	import = "java.io.File"
	import= "java.nio.file.Paths"
	import= "java.nio.file.Path"
	import = "java.net.URLConnection" 
	import = "javax.servlet.ServletOutputStream"%><%

String filename = request.getParameter("img");
	URL url = new URL(filename);
File f = new File(url.toURI());
Path path = Paths.get(f.getAbsolutePath());
byte[] data = Files.readAllBytes(path);

String contentType = URLConnection.getFileNameMap().getContentTypeFor(filename);
response.setContentType(contentType);
response.setHeader("X-Content-Duration","70.0");

 
ServletOutputStream outPut = response.getOutputStream(); 
outPut.write(data);
%>