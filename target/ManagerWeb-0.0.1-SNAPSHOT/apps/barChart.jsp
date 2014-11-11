<!DOCTYPE html>
<meta charset="UTF8">
<style>

body {
  font-size: 2em;
  font-family: Verdana, Geneva, Arial, Helvetica, sans-serif;
}
.bar {
  fill: #009474;
}

.axis {
  font-size:: 3em;
   font-family: Verdana, Geneva, Arial, Helvetica, sans-serif;
}

.axis path,
.axis line {
  fill: none;
  stroke: #000;
  shape-rendering: crispEdges;
}

.x.axis path {
  display: line;
}

.grid .tick {
    stroke: lightgrey;
    opacity: 0.7;
}
.grid path {
      stroke-width: 0;
}

</style>

<body>
<script src="../js/d3.min.js"></script>
<script type="text/javascript" src="../js/jquery-1.11.0.js"></script>

<script>
	var url = "<%=request.getParameter("url")%>"; 
   	var data;  
   	var title = "<%=request.getParameter("title")%>";
   	
	$.get(url, function (data2) {
      data = data2;    


      var margin = {top: 130, right: 50, bottom: 90, left: 80},
      width = 1900 - margin.left - margin.right,
      height = 1050 - margin.top - margin.bottom;

      
      //x and y Scales
      var xScale = d3.scale.ordinal()
          .rangeRoundBands([0, width], .1);

      var yScale = d3.scale.linear()
          .range([height, 0]);
      xScale.domain(data.map(function(d) { return d.name; }));
      yScale.domain([0, d3.max(data, function(d) { return d.value; })]);
      
      function make_x_axis() {        
    	    return d3.svg.axis()
    	        .scale(xScale)
    	         .orient("bottom")
    	         .ticks(1)
    	}

    	function make_y_axis() {        
    	    return d3.svg.axis()
    	        .scale(yScale)
    	        .orient("left")
    	        .ticks(5)
    	}
    	      
      
      //x and y Axes
      var xAxis = d3.svg.axis()
          .scale(xScale)
          .orient("bottom");

      var yAxis = d3.svg.axis()
          .scale(yScale)
          .orient("left")
          .ticks(10);

      //create svg container
      var svg = d3.select("body")
          .append("svg")
          .attr("width", width + margin.left + margin.right)
          .attr("height", height + margin.top + margin.bottom)
          .append("g")
          .attr("transform", "translate(" + margin.left + "," + margin.top + ")");   
     
      	svg.append("g")         
     		.attr("class", "grid")
      		.call(make_y_axis()
          	.tickSize(-width, 0, 0)
          	.tickFormat("")
      )

	      //create bars
	      svg.selectAll(".bar")
	          .data(data)
	          .enter()
	          .append("rect")
	          .attr("class", "bar")
	          .attr("x", function(d) { return xScale(d.name); })
	          .attr("width", xScale.rangeBand())
	          .attr("y", function(d) { return yScale(d.value); })
	          .attr("height", function(d) { return height - yScale(d.value); });
	
	      //drawing the x axis on svg
	      svg.append("g")
	          .attr("class", "x axis")
	          .attr("transform", "translate(0," + height + ")")
	          .call(xAxis);
	
	      //drawing the y axis on svg
	      svg.append("g")
	          .attr("class", "y axis")
	          .call(yAxis)
	          .append("text")
	          .attr("transform", "rotate(-90)")
	          .attr("y", 6)
	          .attr("dy", "5em")
	          .style("text-anchor", "end");
	      
	      svg.append("text")
	      	  .attr("x", (width / 2))             
	     	  .attr("y", 0 - (margin.top / 2))
	      	  .attr("text-anchor", "middle")  
	      	  .style("font-size", "2em")  
	      	  .text(title);	        
	      });
		

	
        
    </script>