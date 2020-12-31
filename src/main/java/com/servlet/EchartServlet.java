package com.servlet;

import com.bean.Product;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/EchartServlet")
public class EchartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String[] categories = {"鞋", "衬衫","外套","牛仔裤"};
//        Integer[] values = {80, 50, 75, 100};
//        Map<String, Object> json = new HashMap<>();
//        json.put("categories", categories);
//        json.put("values" , values);

        //定义一个List集合
        List<Product> list = new ArrayList<>();

        //数据添加到集合里面，这里可以改为获取sql数据信息，然后转为json字符串，然后存到list中
        //这里把”类别名称“作为两个属性封装在一个Product类里
        //每个Product 类的对象都可以看作一个是一个类别（x轴坐标值）与销量（y轴坐标值）的集合
        list.add(new Product("短袖", 10));
        list.add(new Product("牛仔裤", 20));
        list.add(new Product("羽绒服", 30));

        ObjectMapper mapper = new ObjectMapper();//提供Java-json相互转换的类

        String json = mapper.writeValueAsString(list);//将list中的对象转换为Json字符串

        System.out.println(json);

        //将json字符串数据返回给前端
        response.setContentType("text/html; charset=utf-8");
        response.getWriter().write(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
