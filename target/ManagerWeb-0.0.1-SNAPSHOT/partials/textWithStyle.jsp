<%@page import="de.fraunhofer.iao.muvi.managerweb.web.TextController"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<tr>
	<td><label for="text">Text: </label></td>
	<td><jsp:include page="inputTextfield.jsp"></jsp:include></td>
</tr>
<tr>
	<td><label for="style">Style: </label></td>
	<td><input type="text" id="style" name="style"
		value="${paramStyle}"></td>
</tr>
<tr>
	<td><label for="bg_color">Background color:</label></td>
	<td><select name="bg_color" id="bg_color"
		style="background-color: rgb(${paramStartBgColor}); width: 147px; display: block;"
		onchange="this.style.backgroundColor = this.options[this.selectedIndex].style.backgroundColor;
								setTextStyle('style', 'bg_color', 'txt_color');">
			<c:forEach items="${paramColors }" var="bgcolor">
				<c:choose>
					<c:when test="${bgcolor.equals(paramStartBgColor) }">
						<option style="background-color: rgb(${bgcolor})"
							value="${bgcolor}" selected="selected">&nbsp;</option>
					</c:when>
					<c:otherwise>
						<option style="background-color: rgb(${bgcolor})"
							value="${bgcolor}">&nbsp;</option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
	</select></td>
</tr>
<tr>
	<td><label for="txt_color">Text color:</label></td>
	<td><select name="txt_color" id="txt_color"
		style="background-color: rgb(${paramStartTxtColor}); width: 147px; display: block;"
		onchange="this.style.backgroundColor = this.options[this.selectedIndex].style.backgroundColor;
								setTextStyle('style', 'bg_color', 'txt_color');">
			<c:forEach items="${paramColors }" var="txtcolor">
				<c:choose>
					<c:when test="${txtcolor.equals(paramStartTxtColor) }">
						<option style="background-color: rgb(${txtcolor})"
							value="${txtcolor}" selected="selected">&nbsp;</option>
					</c:when>
					<c:otherwise>
						<option style="background-color: rgb(${txtcolor})"
							value="${txtcolor}">&nbsp;</option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
	</select></td>
</tr>
