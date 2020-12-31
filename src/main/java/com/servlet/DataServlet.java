package com.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
* date: 2020/12/23 10:08
* log: 将前端form表单的数据传入后端，成功
*/
@WebServlet("/DataServlet")
public class DataServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String start = request.getParameter("startTime");
        String stop = request.getParameter("stopTime");
        String plant = request.getParameter("plant");

        HttpSession session = request.getSession();
        session.setAttribute("startTime", start);
        session.setAttribute("stopTime", stop);
        session.setAttribute("plant", plant);

        System.out.println("Data submit to Servlet: " + start + " " + stop + " " + plant + "  successfully!!");
        response.getWriter().println(1);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
