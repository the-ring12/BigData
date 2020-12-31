package com.servlet;

import com.bean.InverterBean;
import com.bean.TotalPower;
import com.hdfs.HDFSOperate;
import com.mapreduce.findabsentinverter.AbsentInverterDriver;
import com.mapreduce.findallinverter.FindAllInverterDriver;
import com.mapreduce.inverterpower.priodtotal.InverterPowerDriver;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/InverterServlet")
public class InverterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String start = (String) session.getAttribute("startTime");
        String stop = (String) session.getAttribute("stopTime");
        String plant = (String) session.getAttribute("plant");
        String invertertemp = (String) session.getAttribute("inverter");
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
        if (invertertemp == null || invertertemp.equals("")) {
            invertertemp = "0";
        }

        String fileInverter = plant + "Inverter.csv";

        FSDataInputStream in = null;
        BufferedReader reader = null;
        FileSystem fs = null;
        try {
            fs = new HDFSOperate().getFileSystem();
//        String newFileName = start.split(" ")[0].replaceAll("-", "") +
//                stop.split(" ")[0].replaceAll("-", "") + plant +"Temperature.csv";
//        System.out.println(newFileName);

            /**
             * date: 2020/12/25 15:36
             * log: 找出所有的inverter
             */
            if (!fs.exists(new Path("/result/" + fileInverter))) {
                new FindAllInverterDriver().inverterDriver(plant);
                System.out.println("mapReduce run successfully!!");

                new HDFSOperate().handleFile("result3", fileInverter);
                System.out.println("file handle successfully!!");
            }

            /**
             * date: 2020/12/25 16:03
             * log: 将所有的inverter读取出来放入数组
             */

            in = fs.open(new Path("/result/" + fileInverter));
            reader = new BufferedReader(new InputStreamReader(in));
            String[] inverters = reader.readLine().split(",");
            String inverter = inverters[Integer.parseInt(invertertemp)];

            if (fs.exists(new Path("/result/result4"))) {
                fs.delete(new Path("/result/result4"), true);
            }
            //获取到inverter的记录数
            new AbsentInverterDriver().absentInverterDriver(start, stop, plant, inverter);
            in = fs.open(new Path("/result/result4/part-r-00000"));
            reader = new BufferedReader(new InputStreamReader(in));
            long num = Integer.parseInt(reader.readLine());
            //将文件删除
            fs.delete(new Path("/result/result4//part-r-00000"), true);

            //获取应有的记录条数
            long nums = new HDFSOperate().getLines(start, stop, "/data/" + plant + "_Weather_Sensor_Data.csv");


            /**
            * date: 2020/12/25 16:57
            * log: 获取inverter的数据
            */
            String newFileName = start.split(" ")[0].replaceAll("-", "") +
                    stop.split(" ")[0].replaceAll("-", "") + plant + inverter + "Power.csv";
            System.out.println(newFileName);

            if (!fs.exists(new Path("/result/" + newFileName))) {
                new InverterPowerDriver().driver(start, stop, plant, inverter);
                System.out.println("mapreduce run successfully");

                new HDFSOperate().handleFile("result6", newFileName);
                System.out.println("File handle successfully");
            }

            List<TotalPower> datas = new ArrayList<>();

            String line = null;
            in = fs.open(new Path("/result/" + newFileName));
            reader = new BufferedReader(new InputStreamReader(in));
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                String[] data = line.split("\t");
                datas.add(new TotalPower(data[0] , (int)Double.parseDouble(data[1])));
                System.out.println(data[0] + " " + data[1]);
                System.out.println((int)Double.parseDouble(data[1]));
            }


            // 4.将数据转化为json，提交到前端
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(new InverterBean(datas, String.valueOf(num), String.valueOf(nums - num)));
            System.out.println(json);

            //将json字符串数据返回给前端
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(json);



        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
