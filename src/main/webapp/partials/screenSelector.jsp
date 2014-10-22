<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	//Constant definition
	var NUMBER_OF_COLUMNS = 6;
	var NUMBER_OF_SCREENS = 36;

	var startScreenID = -1;
	var endScreenID = -1;
	var mousePressed = false;

	var selectMultiple = false;
	var enableCTRLUsage = false;

	var enableSingleSelection = ${param.enableSingleSelection};
	var enableMultipleSelection = ${param.enableMultipleSelection};
	var enableRectangleSelection = ${param.enableRectangleSelection};	
	
	if(enableMultipleSelection && enableRectangleSelection)
		{
		//Enable usage of CTRL Keys to enable differentiation between Multiple- and rectangle-Selection 
		enableCTRLUsage = true;
		} 
	else if ((enableMultipleSelection && !enableRectangleSelection) && !enableCTRLUsage) {
		//In Case we only want to use multiple selection, set the state of selectMultiple to true
		selectMultiple = true;
	}
	

	var start1 = "${start1}";
	var start2 = "${start2}";
	var start3 = "${start3}";
	var start4 = "${start4}";
	var start5 = "${start5}";
	var start6 = "${start6}";
	
	var start7 = "${start7}";
	var start8 = "${start8}";
	var start9 = "${start9}";
	var start10 = "${start10}";
	var start11 = "${start11}";
	var start12 = "${start12}";
	
	var start13 = "${start13}";
	var start14 = "${start14}";
	var start15 = "${start15}";
	var start16 = "${start16}";
	var start17 = "${start17}";
	var start18 = "${start18}";
	
	var start19 = "${start19}";
	var start20 = "${start20}";
	var start21 = "${start21}";
	var start22 = "${start22}";
	var start23 = "${start23}";
	var start24 = "${start24}";
	
	var start25 = "${start25}";
	var start26 = "${start26}";
	var start27 = "${start27}";
	var start28 = "${start28}";
	var start29 = "${start29}";
	var start30 = "${start30}";
	
	var start31 = "${start31}";
	var start32 = "${start32}";
	var start33 = "${start33}";
	var start34 = "${start34}";
	var start35 = "${start35}";
	var start36 = "${start36}";

	function selectScreen(id) {

		if ($('#screen' + id).hasClass('screenSelected')) {

			$('#screen' + id).removeClass();
			$('#screen' + id).addClass(window['start' + id]);

		} else {

			$('#screen' + id).removeClass();
			$('#screen' + id).addClass('screenSelected');
			if (enableSingleSelection) {
				$('#paramScreenID').val(id);
			}		
		}
	}

	function selectScreenByColRow(col, row) {
		var calculatedScreenID = getIDFromColumnAndRow(col, row);
		selectScreen(calculatedScreenID);
		
		
		
	}
	
	function updateScreen(id) {
		deselectAllScreens();
		selectScreen(id);
	}

	function deselectAllScreens() {
		for ( var id = 1; id <= 36; id++) {
			$('#screen' + id).removeClass();
			$('#screen' + id).addClass(window['start' + id]);
		}
	}

	function updateScreens() {
		//Visualize the screens which are currently selected by the rectangle

		var startRow = getRowFromId(startScreenID);
		var startCol = getColumnFromId(startScreenID);

		var endRow = getRowFromId(endScreenID);
		var endCol = getColumnFromId(endScreenID);

		//Only do an update for the whole wall	
		if (!selectMultiple) {
			//Deselect all
			deselectAllScreens();

			//Select all
			if (endRow >= startRow) {
				for ( var rowCounter = startRow; rowCounter <= endRow; rowCounter++) {
					if (endCol >= startCol) {
						for ( var colCounter = startCol; colCounter <= endCol; colCounter++) {
							selectScreenByColRow(colCounter, rowCounter);
						}
					} else {
						for ( var colCounter = endCol; colCounter <= startCol; colCounter++) {
							selectScreenByColRow(colCounter, rowCounter);
						}
					}
				}
			} else {
				for ( var rowCounter = endRow; rowCounter <= startRow; rowCounter++) {
					if (endCol >= startCol) {
						for ( var colCounter = startCol; colCounter <= endCol; colCounter++) {
							selectScreenByColRow(colCounter, rowCounter);
						}
					} else {
						for ( var colCounter = endCol; colCounter <= startCol; colCounter++) {
							selectScreenByColRow(colCounter, rowCounter);
						}
					}
				}
			}
		} else {
			selectScreenByColRow(startCol, startRow);
		}
	}

	function getIDFromColumnAndRow(column, row) {
		return NUMBER_OF_COLUMNS * (row - 1) + column;
	}

	function getColumnFromId(id) {
		return Math.floor(1 + (id - 1) % NUMBER_OF_COLUMNS);
	}

	function getRowFromId(id) {
		return Math.floor(1 + ((id - 1) / NUMBER_OF_COLUMNS));
	}

	function exportSelectedScreensToList() {
		var selectedScreenStrList = "";
		var elementList = $('.screenSelected');
		$.each(elementList, function(index, item) {
			if (selectedScreenStrList.length > 0) {
				selectedScreenStrList = selectedScreenStrList + ","
						+ getScreenIDFromElement(item);
			} else {
				selectedScreenStrList = selectedScreenStrList
						+ getScreenIDFromElement(item);
			}
		});
		return selectedScreenStrList;
	}

	function getScreenIDFromElement(element) {
		return parseInt(element.id.substring(6));
	}
	
	function triggerSelectionChange()
	{
		$("#screenSelectorScreenList").val(exportSelectedScreensToList());
	}
	
	function updateRectangle() {
		var start = parseInt($('#start').val());
		var height = parseInt($('#height').val());
		var width = parseInt($('#width').val());
		startScreenID = start;
		var end = Math.min(36, start + (height - 1) * 6 + (width - 1));
		endScreenID = end;
		updateScreens();
	}
	
	function updateRectangleFromScreens() {
		$('#start').val(startScreenID);
		var width = 1 + getColumnFromId(endScreenID) - getColumnFromId(startScreenID);
		$('#width').val(width);
		var height = 1 + getRowFromId(endScreenID) - getRowFromId(startScreenID);
		$('#height').val(height);
	}

	$(window).keydown(function(e) {
		if (!enableMultipleSelection) {
			return;
		}

		if (!enableCTRLUsage) {
			return;
		}

		if (e.ctrlKey) {
			if (!selectMultiple) {
				selectMultiple = true;
			}
		}
	});

	$(window).keyup(function(e) {
		if (!enableMultipleSelection) {
			return;
		}

		if (!enableCTRLUsage) {
			return;
		}

		if (e.keyCode == 17) {
			if (selectMultiple) {
				selectMultiple = false;
			}
		}
	});
	
	function preSelection() {
		var ids = $('#screenSelectorScreenList').val().split(",");
		for (var i = 0; i < ids.length; i++) {
			selectScreen(ids[i]);
		}
	}

	$(document)
			.ready(
					function() {
						for ( var id = 1; id <= 36; id++) {
							$('#screen' + id).removeClass();
							$('#screen' + id).addClass(window['start' + id]);
							
							$('#screen' + id).on('click', function click(e) {
								return false;
							});

							$('body')
									.mouseup(
											function(e) {
												e.preventDefault();

												var currentID = -1;
												if (e.currentTarget
														&& e.currentTarget.id) {
													try {
														currentID = parseInt(e.currentTarget.id
															.substring(6));
													} catch (e) {
														/* Do nothing */
													}
												}

												if (enableRectangleSelection
														&& !selectMultiple) {
													if (currentID != -1) {
														endScreenID = currentID;

														
													}
													
													if (startScreenID >= 1) {
														updateScreens();
														triggerSelectionChange();
														updateRectangleFromScreens();
													}
													
													mousePressed = false;
												}
												
												startScreenID = -1;
												
											});

							$('#screen' + id)
									.on(
											'mousedown mouseup mouseover touchstart touchend touchmove MSPointerDown MSPointerMove MSPointerUp',
											function mouseState(e) {
											    e.preventDefault();
											    e.stopPropagation();

												var currentID = -1;
												var focusObject = undefined;
												if(e.type == "touchstart" || e.type == "touchend" || e.type == "touchmove" ) {
																				
													if(e.type == "touchend") {														
														var touch = e.originalEvent.changedTouches[0];
														var elem = document.elementFromPoint(touch.pageX, touch.pageY);
														focusObject = elem.parentNode;	
														//console.log("TouchEndID: " + focusObject.id);
													}else if(e.type == "touchstart") {
														var elem = e.target.parentElement;
														focusObject = elem;
													} else {
														var touch = e.originalEvent.changedTouches[0];
														var elem = document.elementFromPoint(touch.pageX, touch.pageY);
														if(elem.parentNode.id.indexOf("screen") >= 0) {
															focusObject = elem.parentNode;		
															//console.log("TouchID: " + focusObject.id);															
														}
													}
												} else if (e.type == "MSPointerDown" || e.type == "MSPointerMove" || e.type == "MSPointerUp" ) {
											
												} else {
													focusObject = e.currentTarget;
												}
												if (focusObject
														&& focusObject.id) {
													
												
													
													currentID = parseInt(focusObject.id
															.substring(6));
												
												}

												if (e.type == "mousedown" || e.type == "touchstart") {
													//code triggers on hold
													if (enableRectangleSelection
															&& !selectMultiple) {
														mousePressed = true;
														if (currentID != -1) {
															startScreenID = currentID;
															endScreenID = currentID;

															updateScreens();
														}
													} else if (enableSingleSelection
															|| enableMultipleSelection) {
														if(!enableMultipleSelection) {
															//Deselect all other screens if multiple selection is not allowed! 
															deselectAllScreens();
														}
														selectScreen(currentID);
														
														triggerSelectionChange();
													}

												} else if (e.type == "mouseover" || e.type == "touchmove") {
													if (enableRectangleSelection
															&& !selectMultiple
															&& mousePressed
															&& currentID != -1) {
														endScreenID = currentID;
														updateScreens();
													}
												} else if (e.type == "mouseup" || e.type == "touchend") {
													if (enableRectangleSelection
															&& !selectMultiple) {
														if (currentID != -1) {
															endScreenID = currentID;
															triggerSelectionChange();								
															updateScreens();
															updateRectangleFromScreens();
														}
														mousePressed = false;
													}
												}
											});
						}
						
						/* Set preselection (if any) */
						preSelection();
						
					});
</script>

<c:if test="${param.enableSingleSelection}">
	<label for="paramScreenID">Screen:</label>
	<select name="paramScreenID" id="paramScreenID" onchange="updateScreen(this.value)">
		<c:forEach var="screen" begin="1" end="36">
			<c:choose>
				<c:when test="${screen == paramScreenID}">
					<option selected="selected">${screen}</option>
				</c:when>
				<c:otherwise>
					<option>${screen}</option>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</select>
</c:if>

<c:if test="${param.enableMultipleSelection}">
</c:if>

<c:if test="${param.enableRectangleSelection}">
<label for="largeImageStart">Start display:</label>
	<select name="start" id="start" onchange="updateRectangle()">
		<c:forEach var="display" begin="1" end="36">
			<c:choose>
				<c:when test="${display == paramStartID}">
					<option selected="selected">${display}</option>
				</c:when>
				<c:otherwise>
					<option>${display}</option>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</select>
	<label for="largeImageWidth">Width:</label>
	<select name="width" id="width" onchange="updateRectangle()">
		<c:forEach var="width" begin="1" end="6">
			<c:choose>
				<c:when test="${width == paramWidth}">
					<option selected="selected">${width}</option>
				</c:when>
				<c:otherwise>
					<option>${width}</option>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</select>
	<label for="largeImageHeight">Height:</label>
	<select name="height" id="height" onchange="updateRectangle()">
		<c:forEach var="height" begin="1" end="6">
			<c:choose>
				<c:when test="${height == paramHeight}">
					<option selected="selected">${height}</option>
				</c:when>
				<c:otherwise>
					<option>${height}</option>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</select>
</c:if>

<table id="wallSelector">

	<tr>
		<td>&nbsp;</td>
		<td class="columnLabel">1</td>
		<td class="columnLabel">2</td>
		<td class="columnLabel">3</td>
		<td class="columnLabel">4</td>
		<td class="columnLabel">5</td>
		<td class="columnLabel">6</td>
		<td>&nbsp;</td>
	</tr>

	<tr>
		<td class="rowLabel">1</td>
		<td class="screenEmpty" id="screen1"><a href="#">1</a></td>
		<td class="screenEmpty" id="screen2"><a href="#">2</a></td>
		<td class="screenEmpty" id="screen3"><a href="#">3</a></td>
		<td class="screenEmpty" id="screen4"><a href="#">4</a></td>
		<td class="screenEmpty" id="screen5"><a href="#">5</a></td>
		<td class="screenEmpty" id="screen6"><a href="#">6</a></td>
		<td class="rowLabel">1</td>
	</tr>

	<tr>
		<td class="rowLabel">2</td>
		<td class="screenEmpty" id="screen7"><a href="#">7</a></td>
		<td class="screenEmpty" id="screen8"><a href="#">8</a></td>
		<td class="screenEmpty" id="screen9"><a href="#">9</a></td>
		<td class="screenEmpty" id="screen10"><a href="#">10</a></td>
		<td class="screenEmpty" id="screen11"><a href="#">11</a></td>
		<td class="screenEmpty" id="screen12"><a href="#">12</a></td>
		<td class="rowLabel">2</td>
	</tr>

	<tr>
		<td class="rowLabel">3</td>
		<td class="screenEmpty" id="screen13"><a href="#">13</a></td>
		<td class="screenEmpty" id="screen14"><a href="#">14</a></td>
		<td class="screenEmpty" id="screen15"><a href="#">15</a></td>
		<td class="screenEmpty" id="screen16"><a href="#">16</a></td>
		<td class="screenEmpty" id="screen17"><a href="#">17</a></td>
		<td class="screenEmpty" id="screen18"><a href="#">18</a></td>
		<td class="rowLabel">3</td>
	</tr>

	<tr>
		<td class="rowLabel">4</td>
		<td class="screenEmpty" id="screen19"><a href="#">19</a></td>
		<td class="screenEmpty" id="screen20"><a href="#">20</a></td>
		<td class="screenEmpty" id="screen21"><a href="#">21</a></td>
		<td class="screenEmpty" id="screen22"><a href="#">22</a></td>
		<td class="screenEmpty" id="screen23"><a href="#">23</a></td>
		<td class="screenEmpty" id="screen24"><a href="#">24</a></td>
		<td class="rowLabel">4</td>
	</tr>

	<tr>
		<td class="rowLabel">5</td>
		<td class="screenEmpty" id="screen25"><a href="#">25</a></td>
		<td class="screenEmpty" id="screen26"><a href="#">26</a></td>
		<td class="screenEmpty" id="screen27"><a href="#">27</a></td>
		<td class="screenEmpty" id="screen28"><a href="#">28</a></td>
		<td class="screenEmpty" id="screen29"><a href="#">29</a></td>
		<td class="screenEmpty" id="screen30"><a href="#">30</a></td>
		<td class="rowLabel">5</td>
	</tr>

	<tr>
		<td class="rowLabel">6</td>
		<td class="screenEmpty" id="screen31"><a href="#">31</a></td>
		<td class="screenEmpty" id="screen32"><a href="#">32</a></td>
		<td class="screenEmpty" id="screen33"><a href="#">33</a></td>
		<td class="screenEmpty" id="screen34"><a href="#">34</a></td>
		<td class="screenEmpty" id="screen35"><a href="#">35</a></td>
		<td class="screenEmpty" id="screen36"><a href="#">36</a></td>
		<td class="rowLabel">6</td>
	</tr>

	<tr>
		<td>&nbsp;</td>
		<td class="columnLabel">1</td>
		<td class="columnLabel">2</td>
		<td class="columnLabel">3</td>
		<td class="columnLabel">4</td>
		<td class="columnLabel">5</td>
		<td class="columnLabel">6</td>
		<td>&nbsp;</td>
	</tr>

</table>

<input type="hidden" name="screenSelectorScreenList" id="screenSelectorScreenList" value="${screenSelectorScreenList}">