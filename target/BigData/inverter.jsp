<%--
  Created by IntelliJ IDEA.
  User: Tan
  Date: 2020/12/18
  Time: 15:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Inverter</title>
    <script src="js/echarts.js" type="text/javascript"></script>
    <script src="js/jquery-3.5.1.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>
<!-- 选择显示的inverter-->
<table style="margin-top: 20px">
    <tr>
        <td>
            <form id="inverters" method="post">
            <div class="inverter" style="float: left; margin-top: 20px; margin-left: -20px;
    width: 100px;
    height: 600px;
    background: #00F7DE;
    border: #00F7DE 2px solid;">
                <input type="radio" name="inverter" value="0">Inverter1</input><br>
                <input type="radio" name="inverter" value="1">Inverter2</input><br>
                <input type="radio" name="inverter" value="2">Inverter3</input><br>
                <input type="radio" name="inverter" value="3">Inverter4</input><br>
                <input type="radio" name="inverter" value="4">Inverter5</input><br>
                <input type="radio" name="inverter" value="5">Inverter6</input><br>
                <input type="radio" name="inverter" value="6">Inverter7</input><br>
                <input type="radio" name="inverter" value="7">Inverter8</input><br>
                <input type="radio" name="inverter" value="8">Inverter9</input><br>
                <input type="radio" name="inverter" value="9">Inverter10</input><br>
                <input type="radio" name="inverter" value="10">Inverter11</input><br>
                <input type="submit" value="Submit" onclick="inverter_submit()"><br>
            </div>
            </form>
<!--            将选择的inverter数据提交到后端-->
            <script>
                function inverter_submit() {
                        $.ajax({
                            // 请求的类型
                            type:"post",
                            // 后端请求的地址
                            url: "InverterDataServlet",
                            //方法一：将form表单数据序列化
                            data : $('#inverters').serialize(),
                            // //方法二：传送json数据
                            // data: {start: startTime, stop: stopTime, plant: plant},
                            dataType: "json",
                            success : function(data) {
                                //接收后台返回的结果
                                if (data > 0) {
                                    alert("set successfully!!");
                                } else {
                                    alert("set failure ");
                                }
                            },
                            error : function (data) {
                                alert("operate failure!!");
                            }
                        })
                }
            </script>
        </td>
        <td>
            <div >
                <table style="margin-top: 20px">
                    <tr>
                        <td>
                            <div id="day_power" style="width: 500px; height: 300px; margin-left: 50px;"></div>
                        </td>
                        <td>
                            <div id="absent" style="width: 200px; height: 200px; margin-left: 50px"></div>
                        </td>
                    </tr>
                </table>
<!--                <div id="time_power" style="width: 600px; height: 300px; margin-left: 250px; margin-top: 20px"></div>-->
            </div>
        </td>
    </tr>
</table>
<script type="text/javascript">

        var myChart = echarts.init(document.getElementById('day_power'));

        var option = {
            title: {
                text: "Day Power",
                x: "left",
                margin: 10,
                padding: 0,
                textStyle: {
                    fontsize: 10
                }
            },
            tooltip: {},
            legend: {
                data: ["Power"],
                x: 'right',
                y: 'top',
            },
            xAxis: {
                data: ['inverter1', 'inverter2', 'inverter3']
            },
            yAxis: {
                name: "Power",
                axisLine: {
                    show: true
                }
            },
            series: [{
                name: "Power",
                type: 'bar',
                data: [1000, 2000, 1500]
            }]
        };

        myChart.showLoading();
        // myChart.setOption(option);

        var myChart2 = echarts.init(document.getElementById('absent'));

        var option = {
            title: {
                text: "Inverter",
                x: "left",
                padding: 5,
                textStyle: {
                    fontsize: 10
                }
            },
            tooltip:{
                trigger : 'item',
                formatter: "{a}<br/>{b} : {c}({d}%)"
            },
            series: [{
                name: '',
                type: 'pie',
                data: [
                    {value: 10, name: "normal"},
                    {value: 100, name: "unormal"}
                ]
            }],
            itemStyle: {
                normal: {
                    label: {
                        show: true,
                        formatter: '{b} : {c} ({d}%)'
                    },
                    labelLine:{show: true}
                }
            }
        };

        myChart2.showLoading();
        // myChart2.setOption(option);

    // {
    //     var myChart3 = echarts.init(document.getElementById('time_power'));
    //
    //     var option = {
    //         title: {
    //             text: "Temperature Difference",
    //             x: "center",
    //             margin: 10,
    //             padding: 0,
    //             textStyle: {
    //                 fontsize: 10
    //             }
    //         },
    //         tooltip: {},
    //         legend: {
    //             data: ['Temperature Difference'],
    //             x: 'right',
    //             y: 'top',
    //         },
    //         xAxis: {
    //             data: ['inverter1', 'inverter2', 'inverter3']
    //         },
    //         yAxis: {
    //             name: 'Difference',
    //             axisLine: {
    //                 show: true
    //             }
    //         },
    //         series: [{
    //             name: 'Temperature Difference',
    //             type: 'bar',
    //             data: [1000, 2000, 1500]
    //         }]
    //     };
    //     myChart3.setOption(option);
    // }

    //定义接收数据的大
        var normal = 0;
        var abnormal = 0;
    var time = [];
    var power = [];

    //ajax发起请求
    $.ajax({
        type: "post",
        timeout: 10 * 60 * 1000,
        async : true,
        url: "InverterServlet",
        data : {},
        dataType: "json",

        //请求成功
        success : function(result) {
            if (result) {
                // alert(result.normal + result.abnormal);
                normal = result.normal;
                abnormal = result.abnormal;
                for(var item of result.list){
                    time.push(item.time);
                    power.push(item.power);
                }

                myChart.hideLoading();

                myChart.setOption({
                        title: {
                            text: "Day Power",
                            x: "left",
                            margin: 10,
                            padding: 0,
                            textStyle: {
                                fontsize: 10
                            }
                        },
                        tooltip: {},
                        legend: {
                            data: ["Power"],
                            x: 'right',
                            y: 'top',
                        },
                        xAxis: {
                            data: time
                        },
                        yAxis: {
                            name: "Power",
                            axisLine: {
                                show: true
                            }
                        },
                        series: [{
                            name: "Power",
                            type: 'bar',
                            data: power
                        }]

                });

                myChart2.hideLoading();

                myChart2.setOption({
                    title: {
                        text: "Inverter",
                        x: "left",
                        padding: 5,
                        textStyle: {
                            fontsize: 10
                        }
                    },
                    tooltip:{
                        trigger : 'item',
                        formatter: "{a}<br/>{b} : {c}({d}%)"
                    },
                    series: [{
                        name: '',
                        type: 'pie',
                        data: [
                            {value: normal, name:"normal"},
                            {value: abnormal, name: "abnormal"}
                        ]
                    }],
                    itemStyle: {
                        normal: {
                            label: {
                                show: true,
                                formatter: '{b} : {c} ({d}%)'
                            },
                            labelLine:{show: true}
                        }
                    }
                })
                }
            },

            error : function (Msg) {
                // 请求失败
                alert("The chart data request failed!!");
                myChart.hideLoading();
            }
    })

</script>

</body>
</html>
