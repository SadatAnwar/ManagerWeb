<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="style/manager.css">
<title>Scene editor - MuVi Manager</title>
</head>
<body>

<h1>Scene Editor</h1>

<form action="" method="post">

<label for="name">Name: </label><input type="text" id="name"><br>
<label for="description">Description: </label><input type="text" id="description"><br>

<div>
<h2>Show large image</h2>

<label for="largeImageUrl">Image URL: </label><input type="text" id="largeImageUrl"><br>
<label for="largeImageScale">Scale: </label>
<select id="largeImageScale">
<option value="fullscreen">Fullscreen</option>
<option value="original">Original</option>
<option value="repeat">Repeat</option>
</select>
<br>

<h3>Display area</h3>
<label for="largeImageStart">Start (1 to 36): </label><input type="text" id="largeImageStart">
<label for="largeImageWidth">Width (1 to 6): </label><input type="text" id="largeImageWidth">
<label for="largeImageHeight">Height (1 to 6): </label><input type="text" id="largeImageHeight"><br>

</div>

<div>
<h2>Show large video</h2>

<label for="largeVideoUrl">URL: </label><input type="text" id="largeVideoUrl"><br>
<label for="largeVideoStarttime">Start time: </label><input type="text" id="largeVideoStarttime"><br>
<label for="largeVideoStoptime">Stop time: </label><input type="text" id="largeVideoStoptime"><br>
<input type="checkbox" id="largeVideoAutostart"><label for="largeVideoAutostart">Autostart</label>
<input type="checkbox" id="largeVideoLoop"><label for="largeVideoLoop">Loop</label><br>
	
<label for="largeVideoScale">Scale: </label>
<select id="largeVideoScale">
<option value="fullscreen">Fullscreen</option>
<option value="original">Original</option>
<option value="repeat">Repeat</option>
</select>
<br>

<h3>Display area</h3>
<label for="largeVideoStart">Start (1 to 36): </label><input type="text" id="largeVideoStart">
<label for="largeVideoWidth">Width (1 to 6): </label><input type="text" id="largeVideoWidth">
<label for="largeVideoHeight">Height (1 to 6): </label><input type="text" id="largeVideoHeight"><br>

</div>

<div>
<h2>Show search results</h2>

<label for="">Search type: </label>
<select id="">
<option value="Google">Google</option>
<option value="GoogleImages">Google Images</option>
<option value="Amazon">Amazon</option>
<option value="WebMiningCockpit">Web Mining Cockpit</option>
<option value="PRSearch">PRSearch</option>
</select><br>
<label for="searchResultsQuery">Query: </label><input type="text" id="searchResultsQuery"><br>
<label for="searchResultsSearchscreen">Show search engine page on screen: </label><input type="text" id="searchResultsSearchscreen"><br>

<h3>Display area</h3>
<label for="largeVideoStart">Start (1 to 36): </label><input type="text" id="largeVideoStart">
<label for="largeVideoWidth">Width (1 to 6): </label><input type="text" id="largeVideoWidth">
<label for="largeVideoHeight">Height (1 to 6): </label><input type="text" id="largeVideoHeight"><br>


SearchResultType type;


</div>

<input type="submit" value="Save">
<input type="submit" value="Show">

</form>

<p><a href="index.jsp">Home</a></p>

</body>
</html>