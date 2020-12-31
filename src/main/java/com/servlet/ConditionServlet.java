package com.servlet;

import com.bean.Condition;
import com.hbase.HBaseOperate;
import com.hdfs.HDFSOperate;
import com.mapreduce.gettmperature.GetTemperatureDriver;
import com.mapreduce.priodtotal.PriodTotalDriver;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ConditionServlet")
public class ConditionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        String start = (String) session.getAttribute("startTime");
        String stop = (String) session.getAttribute("stopTime");
        String plant = (String) session.getAttribute("plant");
        System.out.println(start + stop + plant);

        //在未进行选择的情况下，直接设置的数据的总范围
        if (start == null || start.equals("")) {
            start = "2020-05-15 00:00:00";
        }
        if (stop == null || stop.equals("")) {
            stop = "2020-06-17 23:45:00";
        }
        if (plant == null || plant.equals("")) {
            plant = "Plant1";
        }

        FileSystem fs = new HDFSOperate().getFileSystem();
        String newFileName = start.split(" ")[0].replaceAll("-", "") +
                stop.split(" ")[0].replaceAll("-", "") + plant +"Temperature.csv";
        System.out.println(newFileName);

        if (!fs.exists(new Path("/result/" + newFileName))) {
            // 1. 调用Driver运行
            new GetTemperatureDriver().TemperatureDriver(start, stop, plant);

            System.out.println("mapreduce run successfully");
            //2. 处理文件
            new HDFSOperate().handleFile("result2", newFileName);
            System.out.println("File handle successfully");

            if (plant.equals("Plant1")) {
                //3.将数据插入hbase
                new HBaseOperate().insertDataToHBase("/result/" + newFileName,
                        "result1",
                        "rowkey,cf:TotalPower,cf:DifferenceTemperature,cf:Irradiation");
            } else if (plant.equals("Plant2")) {
                new HBaseOperate().insertDataToHBase("/result/" + newFileName,
                        "result2",
                        "rowkey,cf:TotalPower,cf:DifferenceTemperature,cf:Irradiation");
            }
        }

        List<Condition> list = null;
        if (plant.equals("Plant1")) {
            //4. 读取hbase内的数据
            list = new HBaseOperate().getDataFromHbase("result1", start, stop);
        } else if (plant.equals("Plant2")) {
            list = new HBaseOperate().getDataFromHbase("result2", start, stop);
        }

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(list);
        System.out.println(list);

        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
