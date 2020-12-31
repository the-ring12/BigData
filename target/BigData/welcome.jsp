<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.ParseException" %><%--
  Created by IntelliJ IDEA.
  User: Tan
  Date: 2020/12/15
  Time: 16:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>index</title>
    <script type="text/javascript" src="http://cdn.bootcss.com/jquery/3.1.1/jquery.js"></script>
    <script src="laydate/laydate.js"></script>
    <script src="qfdate/qfdate.js"></script>
    <script>
        laydate.render({
            elem: '#test2' //指定元素
            , type: 'datetime'
        });
        laydate.render({
            elem: '#test3' //指定元素
            , type: 'datetime'
        });
    </script>

    <link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>
<%--<!--处理设定日期以及选择Plant的种类-->--%>
<%--<%--%>
<%--    String start = request.getParameter("startTime");--%>
<%--    String stop = request.getParameter("stopTime");--%>
<%--    String plant = request.getParameter("plant");--%>
<%--    request.setAttribute("start", start);--%>
<%--    request.setAttribute("stop", stop);--%>
<%--    request.setAttribute("plant", plant);--%>
<%--%>--%>
<h1>Photovoltaic Power Generation Data Analysis System</h1>
<form id="thisform" method="post">
    <div class="time_date">
        <input type="text" id="test2" name="startTime">StartTime</input>
        <input type="text" id="test3" name="stopTime">StopTime</input>
        <input type="radio" name="plant" value="Plant1" checked>Plant1</input>
        <input type="radio" name="plant" value="Plant2">Plant2</input>
        <input type="button" value="Quiry" onclick="quiry()" style="background: #009688">
    </div>
</form>


<!--将form表单数据提交到后端-->
<script>
    function quiry() {
        $.ajax({
            // 请求的类型
            type:"post",
            // 后端请求的地址
            url: "DataServlet",
            //方法一：将form表单数据序列化
            data : $('#thisform').serialize(),
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


<div class="bar">
    <ul class="menu">
        <li data-id="total_power"><a>Daily Power</a></li>
        <li data-id="temperature"><a>Temperature Difference</a></li>
        <li data-id="inverter"><a>Inverter</a></li>
        </li>
    </ul>
</div>
<div id="content"></div>
</body>
<script>
    $(function () {
        $(".menu").on("click", "li", function () {
            var sId = $(this).data("id");   //获取data-id的值
            window.location.hash = sId;     //设置锚点
            loadInner(sId);
        });

        function loadInner(sId) {
            console.log("进入Inner");
            var sId = window.location.hash;
            var pathn, i;
            switch (sId) {
                case "#total_power":
                    pathn = "total_power.jsp";
                    // i = 0;
                    break;
                case "#temperature":
                    pathn = "temperature.jsp";
                    // i = 1;
                    break;
                case "#inverter":
                    pathn = "inverter.jsp"
                    // i = 2;
                    break;
                default:
                    pathn = "inverter.jsp";
                    // i = 1;
                    break
            }
            console.log(pathn);
            $("#content").load(pathn);//加载相对应的内容
            // $(".menu li").eq(i).addClass("current").siblings().removeClass("current");//当前列表高亮
        }

        var sId = window.location.hash;
        loadInner(sId);
    });
</script>
</html>
