<!DOCTYPE html>
<meta charset="UTF8">
<style>

body {
  font: 10px sans-serif;
}
.bar {
  fill: steelblue;
}

.bar:hover {
  fill: brown;
}

.axis {
  font: 10px sans-serif;
}

.axis path,
.axis line {
  fill: none;
  stroke: #000;
  shape-rendering: crispEdges;
}

.x.axis path {
  display: none;
}

</style>

<body>
<script src="js/d3.min.js"></script>
<script type="text/javascript" src="js/jquery-1.11.0.js"></script>

<script>
	var url = 'https://dashboard.iao.fraunhofer.de/arpos/dashboard-values?&value=plz_anteile&bucket=0&bucket=1&bucket=2&bucket=3&bucket=4&bucket=5&bucket=6&bucket=7&bucket=8&bucket=9&bucket=%3F'; 
   var data;  
	$.get(url, function (data2) {
      data = data2;    
      var width = 1850, height = 1000;
      var margin = {top: 50, right: 40, bottom: 30, left: 40};

      //data
      
      //x and y Scales
      var xScale = d3.scale.ordinal()
          .rangeRoundBands([0, width], .1);

      var yScale = d3.scale.linear()
          .range([height, 0]);
      xScale.domain(data.map(function(d) { return d.name; }));
      yScale.domain([0, d3.max(data, function(d) { return d.value; })]);
      //x and y Axes
      var xAxis = d3.svg.axis()
          .scale(xScale)
          .orient("bottom");

      var yAxis = d3.svg.axis()
          .scale(yScale)
          .orient("left")
          .ticks(20);

      //create svg container
      var svg = d3.select("body")
          .append("svg")
          .attr("width", width + margin.left + margin.right)
          .attr("height", height + margin.top + margin.bottom)
          .append("g")
          .attr("transform", "translate(" + margin.left + "," + margin.top + ")");        

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
          .attr("dy", "2em")
          .style("text-anchor", "end")
          .text("Frequency");
      });

	
        
    </script>