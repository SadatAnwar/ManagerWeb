<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:choose>
	<c:when test="${paramShowXML != null && paramShowXML}">
		<textarea id="xml" style="height: 135px; width: 350px;">${paramXml}</textarea>
	</c:when>
</c:choose>