/**
 * 
 */
function setTextStyle(id, bg_color_id, txt_color_id) {
	var textfield = document.getElementById(id);
	var bg_color = "background-color: rgb("
			+ document.getElementById(bg_color_id).value + ");";
	var txt_color = "color: rgb(" + document.getElementById(txt_color_id).value
			+ ");";
	var value = "";
	if (textfield.value.trim() === "") {
		value += bg_color + " " + txt_color;
	} else {
		var string = textfield.value.trim();
		// remove last semicolon to avoid redundancy
		string = string.substring(0, string.lastIndexOf(";") - 1);
		var split = string.split(";");
		for ( var i in split) {
			str = split[i].trim();
			if (str.indexOf("background-color") == 0) {
				value += bg_color;
			} else if (str.indexOf("color") == 0) {
				value += txt_color;
			} else {
				value += str + ";";
			}
			value += " ";
		}
	}
	textfield.value = value.trim();
	
	document.getElementById('text').style.background = "rgb("	+ document.getElementById(bg_color_id).value + ")";
	document.getElementById('text').style.color = "rgb("	+ document.getElementById(txt_color_id).value + ")";
	
}