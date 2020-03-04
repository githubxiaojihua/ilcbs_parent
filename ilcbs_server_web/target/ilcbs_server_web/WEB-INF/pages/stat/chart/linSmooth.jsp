<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>amCharts examples</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath }/components/newAmcharts/style.css" type="text/css">
        <script src="${pageContext.request.contextPath }/components/newAmcharts/amcharts/amcharts.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath }/components/newAmcharts/amcharts/serial.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath }/components/jquery-ui/jquery-1.2.6.js" type="text/javascript"></script>

        <script>
            var chart;
            var graph;

            AmCharts.ready(function () {
                $.ajax({
                    url:"statChartAction_getOnlineinfoData",
                    dataType:"json",
                    type:"get",
                    success:function(value){
                        // SERIAL CHART
                        chart = new AmCharts.AmSerialChart();

                        chart.dataProvider = value;
                        chart.marginLeft = 10;
                        chart.categoryField = "hour";
                        //chart.dataDateFormat = "YYYY";

                        // listen for "dataUpdated" event (fired when chart is inited) and call zoomChart method when it happens
                        //chart.addListener("dataUpdated", zoomChart);

                        // AXES
                        // category
                        var categoryAxis = chart.categoryAxis;
                        //categoryAxis.parseDates = true; // as our data is date-based, we set parseDates to true
                        //categoryAxis.minPeriod = "YYYY"; // our data is yearly, so we set minPeriod to YYYY
                        categoryAxis.dashLength = 3;
                        categoryAxis.minorGridEnabled = true;
                        categoryAxis.minorGridAlpha = 0.1;//0.1

                        // value
                        var valueAxis = new AmCharts.ValueAxis();
                        valueAxis.axisAlpha = 0;
                        valueAxis.inside = true;
                        valueAxis.dashLength = 3;
                        chart.addValueAxis(valueAxis);

                        // GRAPH
                        graph = new AmCharts.AmGraph();
                        graph.type = "smoothedLine"; // this line makes the graph smoothed line.
                        graph.lineColor = "#d1655d";
                        graph.negativeLineColor = "#637bb6"; // this line makes the graph to change color when it drops below 0
                        graph.bullet = "round";
                        graph.bulletSize = 8;
                        graph.bulletBorderColor = "#FFFFFF";
                        graph.bulletBorderAlpha = 1;
                        graph.bulletBorderThickness = 2;
                        graph.lineThickness = 2;
                        graph.valueField = "value";
                        graph.balloonText = "[[category]]<br><b><span style='font-size:14px;'>[[value]]</span></b>";
                        chart.addGraph(graph);

                        // CURSOR
                        var chartCursor = new AmCharts.ChartCursor();
                        chartCursor.cursorAlpha = 0;
                        chartCursor.cursorPosition = "mouse";
                        //chartCursor.categoryBalloonDateFormat = "YYYY";
                        chart.addChartCursor(chartCursor);

                        // SCROLLBAR
                        //var chartScrollbar = new AmCharts.ChartScrollbar();
                        //chart.addChartScrollbar(chartScrollbar);

                        chart.creditsPosition = "top-right";

                        // WRITE
                        chart.write("chartdiv");
                    }
                });

            });

            // this method is called when chart is first inited as we listen for "dataUpdated" event
            function zoomChart() {
                // different zoom methods can be used - zoomToIndexes, zoomToDates, zoomToCategoryValues
                chart.zoomToDates(new Date(1972, 0), new Date(1984, 0));
            }
        </script>
    </head>

    <body>
        <div id="chartdiv" style="width:100%; height:400px;"></div>
    </body>

</html>