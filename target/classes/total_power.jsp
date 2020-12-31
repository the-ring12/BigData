<%--
  Created by IntelliJ IDEA.
  User: Tan
  Date: 2020/12/18
  Time: 15:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Daily Power</title>
    <script src="js/echarts.js" type="text/javascript"></script>
    <script src="js/jquery-3.5.1.js" type="text/javascript"></script>
</head>
<body>
<div id="total" style="width: 1100px; height: 400px; margin-left: 20px"></div>

<script type="text/javascript">
    var myChart = echarts.init(document.getElementById('total'));

    var option ={
        title: {
            text: "Daily Power",
            x: "center",
            padding: 5,
            textStyle: {
                fontsize: 10
            }
        },
        tooltip: {},
        legend: {
            data:['Power'],
            x: 'right',
            y: 'top',
        },
        xAxis: {
            data: []
        },
        yAxis: {
            name: "Total Power",
            axisLine: {
                show: true
            }
        },
        series: [{
            name: 'Power',
            type: 'bar',
            data: []
        }]
    };

    // 设置加载动画
    myChart.showLoading();

    //存放数据的数组
    var time = [];
    var power = [];

    // ajax发起请求
    $.ajax({
        type: "post",
        timeout: 10 * 60 * 1000,
        async: true,
        url: "TotalPowerServlet",
        data: {},
        dataType: "json",

        // 请求成功
        success : function(result) {
            if (result) {
                for (var i = 0; i < result.length; i++) {
                    time.push(result[i].time);
                }
                for (var i = 0; i < result.length; i++) {
                    power.push(result[i].power);
                }

                //隐藏加载动画
                myChart.hideLoading();

                //覆盖操作
                myChart.setOption({
                    title: {
                    },
                    tooltip: {},
                    dataZoom: [{
                        //默认控制x轴
                        type: 'slider', //这个dataZoom组件是slider型dataZoom组件
                        start: 10,      //左边10%位置
                        end : 60        //右边60%的位置
                    }],
                    xAxis: {
                        data: time
                    },
                    yAxis: {
                        name: "Total Power",
                        axisLine: {
                            show: true
                        }
                    },
                    series: {
                        name: 'Power',
                        type: 'bar',
                        data: power
                    }
                });
            }
        },

        error : function (errorMsg) {
            // 请求失败
            alert("The chart data request failed!!");
            myChart.hideLoading();
        }
    })

    // myChart.setOption(option);
</script>
</body>
</html>
