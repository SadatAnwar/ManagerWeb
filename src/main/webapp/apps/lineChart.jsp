<!DOCTYPE html>
<meta charset="UTF8">
<style>
			/* tell the SVG path to be a thin blue line without any area fill */
			body {
			  font-size: 1em;
			  font-family: Verdana, Geneva, Arial, Helvetica, sans-serif;
			}
			path {
				stroke:  #009474;
				stroke-width: 10;
				fill: none;
			}
			
			.axis {
			  shape-rendering: crispEdges;
			}

			.x.axis line {
			  stroke: lightgrey;
			}

			.x.axis .minor {
			  stroke-opacity: .5;
			}

			.x.axis path {
			  display: line;
			  stroke : #000;
			  stroke-width: 5;
			}

			.y.axis line, .y.axis path {
			  fill: none;
			  stroke: #000;
			  stroke-width: 5;
			}
		</style>

<body>
<div id="graph" class="aGraph" style="position:absolute;top:0px;left:0; float:left;"></div>

<script src="../js/d3.min.js"></script>
<script type="text/javascript" src="../js/jquery-1.11.0.js"></script>

<script>
	var url = 'https://dashboard.iao.fraunhofer.de/arpos/dashboard-values?&value=Anzahl_Faelle_manuell_letzte_Stunde'; 
   	var jsondata;  
   	var title = "PLZ Bereiche";
   	
	$.get(url, function (data2) {
		jsondata = data2;    

		/* implementation heavily influenced by http://bl.ocks.org/1166403 */
		
		// define dimensions of graph
		var m = [80, 80, 80, 80]; // margins
		var w = 1700 - m[1] - m[3]; // width
		var h = 950 - m[0] - m[2]; // height
		
		// create a simple data array that we'll plot with a line (this array represents only the Y values, X will just be the index location)
		var data = jsondata;
		var parseDate = d3.time.format("%d/%m/%Y:%H:%M:%S").parse;
		var max=0.0;
		var value=[];
		var date=[]
		data.forEach(function(d, i) { 
		  value.push(parseFloat(d.value));
		  date.push(parseDate(dateTime(d.name)));
		  if(d.vaule>max){
			  max=d.value;
		  }
		  });
		max = d3.max(value);
		
		

		// X scale will fit all values from data[] within pixels 0-w
		var x = d3.time.scale()
	    .range([0, w]).domain([d3.min(date), d3.max(date)]);
		// Y scale will fit values from 0-10 within pixels h-0 (Note the inverted domain for the y-scale: bigger is up!)
		
		var y = d3.scale.linear().domain([0, max]).range([h, 0]);
			// automatically determining max range can work something like this
			// var y = d3.scale.linear().domain([0, d3.max(data)]).range([h, 0]);

		// create a line function that can convert data[] into x and y points
		var line = d3.svg.line()
			// assign the X function to plot our line as we wish
			.x(function(d) { 
				// verbose logging to show what's actually being done
				var date = dateTime(d.name);
				
				// return the X coordinate where we want to plot this datapoint
				return x(parseDate(date));
			})
			.y(function(d) { 
				// verbose logging to show what's actually being done
				console.log('Plotting Y value for data point: ' + d + ' to be at: ' + y(d.value) + " using our yScale.");
				// return the Y coordinate where we want to plot this datapoint
				return y(d.value); 
			})

			// Add an SVG element with the desired dimensions and margin.
			var graph = d3.select("#graph").append("svg:svg")
			      .attr("width", w + m[1] + m[3])
			      .attr("height", h + m[0] + m[2])
			    .append("svg:g")
			      .attr("transform", "translate(" + m[3] + "," + m[0] + ")");

			// create yAxis
			var xAxis = d3.svg.axis().scale(x)
    						.orient("bottom").ticks(10)
    						.tickFormat(d3.time.format("%m/%d/%Y:%H:%M:%S"));
			//var xAxis = d3.svg.axis().scale(x).tickSize(-h).tickSubdivide(true);
			// Add the x-axis.
			graph.append("svg:g")
			      .attr("class", "x axis")
			      .attr("transform", "translate(0," + h + ")")
			      .call(xAxis)
			      .selectAll("text")  
		            .style("text-anchor", "end")
		            .attr("dx", "-.8em")
		            .attr("dy", ".15em")
		            .attr("transform", function(d) {
		                return "rotate(-65)" 
                });


			// create left yAxis
			var yAxisLeft = d3.svg.axis().scale(y).ticks(4).orient("left");
			// Add the y-axis to the left
			graph.append("svg:g")
			      .attr("class", "y axis")
			      .attr("transform", "translate(-25,0)")
			      .call(yAxisLeft);
			
  			// Add the line by appending an svg:path element with the data line we created above
			// do this AFTER the axes above so that the line is above the tick-lines
  			graph.append("svg:path").attr("d", line(data));
			

  			graph.append("text")
	      	  .attr("x", (w / 2))             
	     	  .attr("y", 0 - (m[0] / 2))
	      	  .attr("text-anchor", "middle")  
	      	  .style("font-size", "2em")  
	      	  .text(title);

	      });
	function dateTime(timestamp) {
		  var date = new Date(timestamp);
		  var formatted = "";

		  if (date.getDate() < 10)
		   formatted += "0";
		  formatted += date.getDate() + "/";

		  if ((1 + date.getMonth()) < 10)
		   formatted += "0";
		  formatted += (1 + date.getMonth()) + "/";

		  formatted += date.getFullYear() + ":";

		  if (date.getHours() < 10)
		   formatted += "0";
		  formatted += date.getHours() + ":";

		  if (date.getMinutes() < 10)
		   formatted += "0";

		  formatted += date.getMinutes() + ":";

		  if (date.getSeconds() < 10)
		   formatted += "0";

		  formatted += date.getSeconds();

		  return formatted;
		 }
		

	
        
    </script>