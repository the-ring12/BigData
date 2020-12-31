<%--
  Created by IntelliJ IDEA.
  User: Tan
  Date: 2020/12/18
  Time: 15:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Temperature</title>
    <script src="js/echarts.js" type="text/javascript"></script>
    <script src="js/jquery-3.5.1.js" type="text/javascript"></script>
</head>
<body>
<!--<table style="margin-top: 20px"><tr>-->
<!--<td>-->
    <div id="temperature" style="width: 1000px; height: 500px; margin-left: 20px; margin-top: 20px"></div>
<!--</td>-->
<!--<td>-->
    <div id="power" style="width: 1000px; height: 500px; margin-left: 20px; margin-top: 50px"></div>
<!--</td>-->
<!--</tr></table>-->
<script type="text/javascript">
    var myChart = echarts.init(document.getElementById('temperature'));

    var option = {
        title: {
            text: "Temperature Difference",
            x: "center",
            margin: 10,
            padding: 0,
            textStyle: {
                fontsize: 10
            }
        },
        tooltip: {},
        legend: {
            data: ['Temperature Difference'],
            x: 'right',
            y: 'top',
        },
        xAxis: {
            data: ['inverter1', 'inverter2', 'inverter3']
        },
        yAxis: {
            name: 'Difference',
            axisLine: {
                show: true
            }
        },
        series: [{
            name: 'Temperature Difference',
            type: 'line',
            data: [1000, 2000, 1500]
        }]
    };

    myChart.showLoading();
    // myChart.setOption(option);

    var myChart2 = echarts.init(document.getElementById('power'));

    var option = {
        title: {
            text: "Power and Irradiation",
            x: "center",
            padding: 5,
            textStyle: {
                fontsize: 10
            }
        },
        tooltip: {},
        legend: {
            data: ['Power', 'Irradiation'],
            x: 'right',
            y: 'top',
        },
        xAxis: {
            data: ['inverter1', 'inverter2', 'inverter3']
        },
        yAxis: [{
            name: "Power",
            axisLine: {
                show: true
            }
        },
            {
                name: 'Irradiation',
                axisLine: {
                    show: true
                }
            }],
        series: [{
            name: 'Power',
            type: 'line',
            yAxisIndex: 0,
            data: [1000, 2000, 1500]
        },
            {
                name: 'Irradiation',
                type: "line",
                yAxisIndex: 1,
                data: [0.3, 1, 3]
            }]
    };
    myChart2.showLoading();

    var time = [];
    var temperature = [];
    var power = [];
    var irradiation = [];

    $.ajax({
        type: "post",
        timeout: 10 * 60 * 100,
        async: true,
        url: "ConditionServlet",
        data:{},
        dataType: "json",

        success : function (result) {
            if (result) {
            for (var i = 0; i < result.length; i++) {
                time.push(result[i].time);
                temperature.push(result[i].temperature);
                power.push(result[i].power);
                irradiation.push(result[i].irradiation);
            }

            myChart.setOption({
                title: {
                    text: "Temperature Difference",
                    x: "center",
                    margin: 10,
                    padding: 0,
                    textStyle: {
                        fontsize: 10
                    }
                },
                tooltip: {},
                legend: {
                    data: ['Temperature Difference'],
                    x: 'right',
                    y: 'top',
                },
                xAxis: {
                    data: time
                },
                yAxis: {
                    name: 'Difference',
                    axisLine: {
                        show: true
                    }
                },
                series: [{
                    name: 'Temperature Difference',
                    type: 'line',
                    data: temperature
                }]
            });

            myChart.hideLoading();

            myChart2.setOption({
                title: {
                    text: "Power and Irradiation",
                    x: "center",
                    padding: 5,
                    textStyle: {
                        fontsize: 10
                    }
                },
                tooltip: {},
                legend: {
                    data: ['Power', 'Irradiation'],
                    x: 'right',
                    y: 'top',
                },
                xAxis: {
                    data: time
                },
                yAxis: [{
                    name: "Power",
                    axisLine: {
                        show: true
                    }
                },
                    {
                        name: 'Irradiation',
                        axisLine: {
                            show: true
                        }
                    }],
                series: [{
                    name: 'Power',
                    type: 'line',
                    yAxisIndex: 0,
                    data: power
                },
                    {
                        name: 'Irradiation',
                        type: "line",
                        yAxisIndex: 1,
                        data: irradiation
                    }]
            });

            myChart2.hideLoading();
        }
            },
        error : function (errorMsg) {
            alert("The chart data request failed!!");
            myChart.hideLoading();
        }
    })

</script>
</body>
</html>
