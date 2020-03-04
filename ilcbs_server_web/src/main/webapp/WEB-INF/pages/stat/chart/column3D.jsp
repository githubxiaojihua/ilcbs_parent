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

            AmCharts.ready(function () {
                $.ajax({
                    url:"statChartAction_getProductsaleData",
                    dataType:"json",
                    type:"get",
                    success:function(value){
                        // SERIAL CHART
                        chart = new AmCharts.AmSerialChart();
                        chart.dataProvider = value;
                        chart.categoryField = "productno";//======================================
                        // the following two lines makes chart 3D
                        chart.depth3D = 20;
                        chart.angle = 30;

                        // AXES
                        // category
                        var categoryAxis = chart.categoryAxis;
                        //categoryAxis.labelRotation = 20;
                        categoryAxis.dashLength = 5;
                        categoryAxis.gridPosition = "start";

                        // value
                        var valueAxis = new AmCharts.ValueAxis();
                        valueAxis.title = "产品销售排行";
                        valueAxis.dashLength = 5;
                        chart.addValueAxis(valueAxis);

                        // GRAPH
                        var graph = new AmCharts.AmGraph();
                        graph.valueField = "amount";//================================================
                        graph.colorField = "color";
                        graph.balloonText = "<span style='font-size:18px'>[[category]]: <b>[[value]]</b></span>";
                        graph.type = "column";
                        graph.lineAlpha = 0;
                        graph.fillAlphas = 1;
                        chart.addGraph(graph);

                        // CURSOR
                        var chartCursor = new AmCharts.ChartCursor();
                        chartCursor.cursorAlpha = 0;
                        chartCursor.zoomable = false;
                        chartCursor.categoryBalloonEnabled = false;
                        chart.addChartCursor(chartCursor);

                        chart.creditsPosition = "top-right";


                        // WRITE
                        chart.write("chartdiv");

                    }
                });

            });
        </script>
    </head>

    <body>
        <div id="chartdiv" style="width: 100%; height: 400px;"></div>
    </body>

</html>