<%--
  Created by IntelliJ IDEA.
  User: Tan
  Date: 2020/12/19
  Time: 18:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>EChart 测试</title>
<!--    1.引入echarts.jsy-->
    <script type="text/javascript" src="js/echarts.js"></script>
<!--    引入jquery.js-->
    <script type="text/javascript" src="js/jquery-3.5.1.js"></script>
</head>
<body style="margin: 5% 26% 0% 29%">
<div style="text-align: center">
    <h2>欢迎访问jsp + servlet + echarts动态网页</h2>
    <h3>作者：the_ring</h3>
    <h3>时间：2020-12-19</h3>
    <a href=EchartServlet>Test</a>
</div>
<!--2.为Echarts准备一个具备大小（宽高）的Dom-->
<div id="main" style="width: 600px; height: 400px"></div>

<script type="text/javascript">
    var myChart = echarts.init(document.getElementById('main'));

    // 3.初始化，默认显示标题，图例和xy空坐标值
    myChart.setOption({
        titl: {
            text: 'ajax动态获取数据'
        },
        tooltip: {},

        legend: {
            data: ['销售量']
        },
        xAxis: {
            data:[]
        },
        yAxis:{},
        series:[{
            name:'销售量',
            type: 'bar',
            data:[]
        }]
    });
    // 4.设置加载动画（非必须）
    myChart.showLoading();//加载完之前先显示一段简单的Loading动画

    //5.定义数据存放数组(动态变）
    var names = [];//建立一个类别数组（实际用来盛放x轴坐标）
    var nums = [];//建立一个销量数组（实际用来盛放y坐标值）

    //6. ajax发起数据请求
    $.ajax({
        type: "post",
        async: true,//异步请求（同步请求将会锁住浏览器，其他操作需等请求完成才可执行）
        url : "EchartServlet",//请求发送到EchartServlet
        data: {},
        dataType: "json",//返回数据形式为json

        //7.请求成功后接收数据name+ num两组数据
        success : function (result) {
            //result为服务器返回的json对象
            if (result) {
                //8.取出数据存入数组
                for (var i = 0; i < result.length; i++) {
                    names.push(result[i].name);//迭代去除类别数据并填入类别数组
                }
                for (var i = 0; i < result.length; i++) {
                    nums.push(result[i].num);//迭代去除销量并填入销量数组
                }

                myChart.hideLoading();//隐藏加载动画

                //9.覆盖操作，根据数据加载数据图表
                myChart.setOption({
                    xAxis: {
                        data: names
                    },
                    series : [{
                        //根据名字对应到相应的数据
                        name : '销量',
                        data: nums
                    }]
                });
            }
        },
        error : function (errorMsg) {
            //请求失败时执行该函数
            alert("图表数据请求数据失败！");
            myChart.hideLoading();
        }
    })
</script>
</body>
</html>
