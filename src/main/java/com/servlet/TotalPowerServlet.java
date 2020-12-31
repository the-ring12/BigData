package com.servlet;

import com.bean.TotalPower;
import com.hdfs.HDFSOperate;
import com.mapreduce.priodtotal.PriodTotalDriver;
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

@WebServlet("/TotalPowerServlet")
public class TotalPowerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String start = null;
        String stop = null;
        String plant = null;
        start = (String) session.getAttribute("startTime");
        stop = (String) session.getAttribute("stopTime");
        plant = (String) session.getAttribute("plant");
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

        /**
        * date: 2020/12/23 20:38
        * log: 避免重复执行，添加判断，如果此文件不存在，才会执行mapreduce
        */
        FileSystem fs = new HDFSOperate().getFileSystem();

        String newFileName = start.split(" ")[0].replaceAll("-", "") +
                stop.split(" ")[0].replaceAll("-", "") + plant + "TotalPower.csv";
        System.out.println(newFileName);
        if (!fs.exists(new Path("/result/" + newFileName))) {
            // 1. 调用Driver运行
            new PriodTotalDriver().driver(start, stop, plant);

            System.out.println("mapreduce run successfully");
            //2. 处理文件
            new HDFSOperate().handleFile("result1", newFileName);
            System.out.println("File handle successfully");
        }

        //3. 读取文件内容
        List<TotalPower> list = new ArrayList<>();

        FSDataInputStream in = null;
        BufferedReader reader = null;
        String line = null;
        try {
            in = fs.open(new Path("/result/" + newFileName));
            reader = new BufferedReader(new InputStreamReader(in));
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                String[] data = line.split("\t");
                list.add(new TotalPower(data[0] , (int)Double.parseDouble(data[1])));
                System.out.println(data[0] + " " + data[1]);
                System.out.println((int)Double.parseDouble(data[1]));
            }
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

        // 4.将数据转化为json，提交到前端
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(list);
        System.out.println(json);

        //将json字符串数据返回给前端
        response.setContentType("text/html; charset=utf-8");
        response.getWriter().write(json);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
