<%--
  Created by IntelliJ IDEA.
  User: Tan
  Date: 2020/12/17
  Time: 18:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <meta charset="UTF-8">
    <title>Echart</title>
    <script src="js/echarts.js" type="text/javascript"></script>
  </head>
  <body>
  <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
  <div id="main" style="width: 600px;height:400px;"></div>

  <script type="text/javascript">

    var myChart;     //定义一个全局的图表变量，供后面动态加载时使用

    require ([  //按需加载所需图表，如需动态类型切换功能，别忘了同时加载相应图表
        'echarts',
            'echarts/chart/line',
            'echarts/chart/bar'
    ],
    function (ec) {
    // 基于准备好的dom，初始化echarts实例
    myChart = echarts.init(document.getElementById('main'));

    // 指定图表的配置项和数据
    var option = {
      title: {
        text: 'ECharts 入门示例'
      },
      tooltip: {},
      legend: {
        data:['蒸发量','降水量']
      },
      xAxis: {
        data: []
      },
      yAxis: {},
      series: [{
        name: '蒸发量',
        type: 'bar',
        data: []
      },{
        name: '降水量',
        type: 'bar',
        data: []
      }]
    };
      // 使用刚指定的配置项和数据显示图表。
      myChart.setOption(option);
    }
    );

    function getJson() {
        //获取数据时显示加载状态图
      myChart.showLoading();
      var moths=[]; //用来盛放x轴坐标值：月份
      var evapCapacitys=[];//用来盛放y轴坐标值，蒸发量
      var precipitations=[];//用来盛放y轴坐标值，降雨量



      $.ajax({
        type:"post",
        async:true, //异步请求
        url:"TestServlet",
        data:{},
        dataType : "json",
        success : function (result) {
            if (result.status) {
                var list = result.list;
                for (var i = 0; i < list.length; i++) {
                    moths.push(list[i].name);//遍历月份并填入数组
                }
                for (var i = 0; i < list.length; i++) {
                    evapCapacitys.push(list[i].evapCapacity);//遍历蒸发量
                }
                for (var i = 0; i < list.length; i++) {
                    precipitations.push(list[i].precipitation);
                }
                myChart.hideLoading();//隐藏加载动画
              myChart.setOption({   //加载数据图表
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:['蒸发量','降水量']
                },
                toolbox: {
                    show:true,
                  feature: {
                        mark: {show:true},
                    dataView:{show:true, type:['line','bar']},
                    restore:{show:true},
                    saveAsImage: {show:true}
                  }
                },
                calculable: true,
                xAxis: {
                    data: months
                },
                yAxis: {},//注意一定不能丢了这个，不然y轴不显示
                series: [{
                    name:'蒸发量',
                  type:'bar',
                  data: evapCapacitys
                }, {
                    name: '降水量',
              type:'bar',
                      data:precipitations
                }]
              });
            }
        },
        error: function(errorMsg) {
            //请求失败时执行此函数
          alert("图表请求数据失败！");
          myChart.hideLoading();
        }
      });
    }

    //设置加载动画
    // myChart.showLoading();      //数据加载完之前先显示一段间的loading动画
    //
    // //定义数据存放请求（动态变）
    // var names = [];         //建立一个类别数组（实际用来存放x轴的坐标值
    // var nums = [];          //建立一个销量数组（实际用来盛放y坐标值）
    //
    // //ajax发起动态数据请求
    // $.ajax({
    //   type: "post",
    //   async : true, //异步请求（同步请求将会锁定浏览器，其他操作需等请求完成才可执行）
    //   url : "TestServlet",  //请求发送到TestServlet
    //   data : {},
    //   dataType : "json",    //返回数据形式为json
    //
    //   //请求成功后接收数据name + num 两组数据
    //   success : function (result) {
    //         //result为服务器返回的json对象
    //     if (result) {
    //         //取出数据存入数组
    //       for (var i = 0; i < result.length; i++) {
    //           names.push(result[i].name);//迭代取出数据并填入类别数据
    //       }
    //       for (var i = 0; i < result.length; i++) {
    //           nums.push(result[i].num);
    //       }
    //
    //       myChart.hideLoading();//隐藏加载动画
    //
    //       //覆盖操作，根据数据加载数据图表
    //       myChart.setOption({
    //         xAxis: {
    //             data : names
    //         },
    //        series: [{
    //             //根据名字对应到相应的数据
    //          name: "销量",
    //          data: nums
    //        }]
    //       });
    //     }
    //   },
    //   error : function (errorMsg) {
    //         //请求失败时执行该函数
    //     alert("图表请求数据失败！")；
    //     myChart.hideLoading();
    //   }
    // })


  </script>

  </body>


</html>
