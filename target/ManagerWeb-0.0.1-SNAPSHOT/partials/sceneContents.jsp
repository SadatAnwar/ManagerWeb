<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="sceneContents">
<span>The scene contains:</span>
<c:forEach items="${scene.largeimages}" var="largeimage" varStatus="contentCounter">
	Large image (<a onclick="return confirm('Are you sure?')" href="editscene.do?scenarioId=${scenario.id}&sceneNumber=${loopCounter.count}&action=delete&largeImageNumber=${contentCounter.count}" title="Delete">D</a>, <a href="editscene.do?scenarioId=${scenario.id}&sceneNumber=${loopCounter.count}&action=editScene&largeImageNumber=${contentCounter.count}" title="Edit">E</a>)
</c:forEach>
<c:forEach items="${scene.searchresults}" var="searchresult" varStatus="contentCounter">
	${searchresult.description} (<a onclick="return confirm('Are you sure?')" href="editscene.do?scenarioId=${scenario.id}&sceneNumber=${loopCounter.count}&action=delete&searchresultsNumber=${contentCounter.count}" title="Delete">D</a>, <a href="editscene.do?scenarioId=${scenario.id}&sceneNumber=${loopCounter.count}&action=editScene&searchresultsNumber=${contentCounter.count}" title="Edit">E</a>)
</c:forEach>
<c:forEach items="${scene.largevideos}" var="largevideo" varStatus="contentCounter">
	Large video (<a onclick="return confirm('Are you sure?')" href="editscene.do?scenarioId=${scenario.id}&sceneNumber=${loopCounter.count}&action=delete&largeVideoNumber=${contentCounter.count}" title="Delete">D</a>, <a href="editscene.do?scenarioId=${scenario.id}&sceneNumber=${loopCounter.count}&action=editScene&largeVideoNumber=${contentCounter.count}" title="Edit">E</a>)
</c:forEach>
<c:forEach items="${scene.largetexts}" var="largetext" varStatus="contentCounter">
	Large text (<a onclick="return confirm('Are you sure?')" href="editscene.do?scenarioId=${scenario.id}&sceneNumber=${loopCounter.count}&action=delete&largeTextNumber=${contentCounter.count}" title="Delete">D</a>, <a href="editscene.do?scenarioId=${scenario.id}&sceneNumber=${loopCounter.count}&action=editScene&largeTextNumber=${contentCounter.count}" title="Edit">E</a>)
</c:forEach>
<c:forEach items="${scene.largeURLs}" var="largeURL" varStatus="contentCounter">
	Large URL (<a onclick="return confirm('Are you sure?')" href="editscene.do?scenarioId=${scenario.id}&sceneNumber=${loopCounter.count}&action=delete&largeURLNumber=${contentCounter.count}" title="Delete">D</a>, <a href="editscene.do?scenarioId=${scenario.id}&sceneNumber=${loopCounter.count}&action=editScene&largeURLNumber=${contentCounter.count}" title="Edit">E</a>)
</c:forEach>
<c:forEach items="${scene.screens}" var="screen" varStatus="contentCounter">
	<c:if test="${screen.text != null}">
		Text
	</c:if>
	<c:if test="${screen.url != null}">
		URL
	</c:if>
	<c:if test="${screen.image != null}">
		Image
	</c:if>
	<c:if test="${screen.animatedText != null}">
		Animated text
	</c:if>
(<a onclick="return confirm('Are you sure?')" href="editscene.do?scenarioId=${scenario.id}&sceneNumber=${loopCounter.count}&action=delete&screenNumber=${contentCounter.count}" title="Delete">D</a>, <a href="editscene.do?scenarioId=${scenario.id}&sceneNumber=${loopCounter.count}&action=editScene&screenNumber=${contentCounter.count}" title="Edit">E</a>)
</c:forEach>
</div>