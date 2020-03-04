<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>amCharts examples</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath }/components/newAmcharts/style.css" type="text/css">
        <script src="${pageContext.request.contextPath }/components/newAmcharts/amcharts/amcharts.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath }/components/newAmcharts/amcharts/pie.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath }/components/jquery-ui/jquery-1.2.6.js" type="text/javascript"></script>
        <script>

            var chart;

            AmCharts.ready(function () {
                $.ajax({
                    url:'statChartAction_getFactorysaleData',
                    dataType:'json',
                    type:'get',
                    success:function(value){
                        alert(JSON.stringify(value));
                        // PIE CHART
                        chart = new AmCharts.AmPieChart();

                        // title of the chart
                        chart.addTitle("生产厂家销售情况", 16);

                        chart.dataProvider = value;
                        chart.titleField = "factoryName";//===============================
                        chart.valueField = "amount";//==================================
                        chart.sequencedAnimation = true;
                        chart.startEffect = "elastic";
                        chart.innerRadius = "40%";
                        chart.startDuration = 5;
                        chart.labelRadius = 15;
                        chart.balloonText = "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>";
                        // the following two lines makes the chart 3D
                        chart.depth3D = 10;
                        chart.angle = 15;

                        chart.creditsPosition = "top-right";

                        // WRITE
                        chart.write("chartdiv");
                    }
                })

            });
        </script>
    </head>

    <body>
        <div id="chartdiv" style="width:100%; height:100%;position: absolute;left: -10px;top: 10px"></div>
    </body>

</html>