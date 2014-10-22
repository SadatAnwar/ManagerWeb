<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="style/manager.css">
<title>New Scenario - Muvi Manager</title>
</head>
<body>
	<h1>New Scenario</h1>

	<p>${message}</p>
	<form action="newScenario.do" method="post" id="xmlEditor">
		<table>
			<tr>
				<td><label for="sName">Scenario name</label></td>
				<td><input type="text" name="sName"></td>
			</tr>
			<tr>
				<td><label for="sDesc">Scenario Description</label></td>
				<td><textarea id="sDesc" name="sDesc"></textarea></td>
			</tr>
		</table>
		<input type="submit" name="Save" value="Save">
	</form>

<p id="scenariosLink"><a href="scenarios.do">Scenarios</a></p>

<p id="homeLink"><a href="index.jsp">Home</a></p>

</body>
</html>