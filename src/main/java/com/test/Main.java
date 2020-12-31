package com.test;

import com.bean.Condition;
import com.handleresult.local.HandleResult;
import com.hbase.HBaseOperate;
import com.hdfs.HDFSOperate;
import com.mapreduce.findabsentinverter.AbsentInverterDriver;
import com.mapreduce.findallinverter.FindAllInverterDriver;
import com.mapreduce.findallinverter.FindAllInverterMap;
import com.mapreduce.findallinverter.FindAllInverterReduce;
import com.mapreduce.gettmperature.GetTemperatureDriver;
import com.mapreduce.priodtotal.PriodTotalDriver;
import jdk.internal.org.objectweb.asm.Handle;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class Main {


    public static void main(String[] args) {

        /**
        * date: 2020/12/25 17:51
        * log: 测试对inverter进行计数
        */
        String start = "2020-05-15 00:00:00";
        String stop = "2020-06-01 00:00:00";
        String plant = "Plant1";
        String inverter = "zVJPv84UY57bAof";

        /**
        * date: 2020/12/25 18:40
        * log: map的outkey不可为空
        */
        new AbsentInverterDriver().absentInverterDriver(start, stop, plant, inverter);

//        /**
//        * date: 2020/12/25 8:36
//        * log: Temperature 数据处理异常，测试
//        */
//
//        String start = "2020-05-21 00:00:00";
//        String stop = "2020-06-01 00:00:00";
//        String plant = "Plant1";

//        String newFileName = start.split(" ")[0].replaceAll("-", "") +
//                stop.split(" ")[0].replaceAll("-", "") +"Temperature.csv";
//        System.out.println(newFileName);
//
//        new GetTemperatureDriver().TemperatureDriver(start, stop, plant);
//
//        System.out.println("mapreduce run successfully");
//        //2. 处理文件
//        new HDFSOperate().handleFile("result2", newFileName);
//        System.out.println("File handle successfully");
//
//        //3.将数据插入hbase
//        new HBaseOperate().insertDataToHBase("/result/" + newFileName,
//                "result",
//                "rowkey,cf:TotalPower,cf:DifferenceTemperature,cf:Irradiation");

//        try {
//            List<Condition> list = new HBaseOperate().getDataFromHbase("result", start, stop);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        /**
//        * date: 2020/12/24 17:52
//        * log: 本地操作hbase进行mapreuce
//        */
//        new GetTemperatureDriver().TemperatureDriver("2020-05-23 00:00:00", "2020-06-03 00:00:00", "Plant1");


//        /**
//        * date: 2020/12/23 19:08
//        * log: 测试多个空格转化为单个空格
//        */
//        String s = "01-06-2020 00:00    5407.25";
//        System.out.println(s.replaceAll(" + ", " "));

//        /**
//        * date: 2020/12/23 18:22
//        * log: 测试PriodTotalDriver
//        */
//        new PriodTotalDriver().driver("2020-05-23 00:00:00", "2020-06-03 00:00:00", "Plant1");

//        /**
//         * date: 2020/12/23 8:46
//         * log: 测试本第运行maoreduce，数据来源于hadoop
//         */
//        new FindAllInverterDriver().driver(null, null);

//        HDFSOperate dfs = new HDFSOperate();

//        /**
//         * @author: The_ring
//         * @date: 2020/12/11 10:55
//         * @description: 读取hdfs上的文件，测试成功
//         */
//        dfs.readFile();

//        /**
//         * date: 2020/12/11 11:23
//         * log: 获取桌面绝对路径之后测试成功，但产生了额外的文件，但是会生成一个crc校验文件
//         */
//        dfs.loadFile("/hbase/data/emp.csv");


//        /**
//         * date: 2020/12/11 15:55
//         * log: 测试将csv的数据导入hbase中，调用HDFS中的读取文件的内容方法是否可用
//         */
//        /**
//         * date: 2020/12/12 19:05
//         * log: 测试成功，能读取数据
//         */
//        HBaseOperate ho = new HBaseOperate();
//        ho.insertDataToHBase("/hbase/data/emp.csv", null, null);

//        /**
//         * date: 2020/12/12 19:28
//         * log: 测试将表中的数据插入hbase中
//         */
//        /**
//         * date: 2020/12/12 19:42
//         * log: 测试成功，将数据插入到表中成功
//         */
//        ho.insertDataToHBase("/hbase/data/emp.csv", "EmpData", "rowkey,cf:FirstName,cf:LastName,cf:salary,cf:date,cf:a,cf:b,cf:department");


//        /**
//         * date: 2020/12/13 15:29
//         * log: 测试mapreduce处理某一时间段产生的总能量
//         */
////        try {
//        /**
//         * date: 2020/12/13 15:33
//         * log: 设定时间段
//         */
//            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//            Date start = format.parse("15-05-2020 00:00");
//            Date stop = format.parse("17-06-2020 23:45");
//        /**
//         * date: 2020/12/13 15:39
//         * log: 运行失败，未知原因
//         */
//            new PriodTotalDriver().driver(start, stop);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

//        /**
//        * date: 2020/12/13 15:52
//        * log: 使用其他类调用mapreduce
//        */
//        new PriodTotalDriver().driver(null, null);
//        /**
//        * date: 2020/12/13 15:53
//        * log: 测试成功
//        */

//        try {
//            /**
//             * date: 2020/12/13 16:10
//             * log: 将Date设为static之后，重新测试
//             */
//            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//            Date start = format.parse("15-05-2020 00:00");
//            Date stop = format.parse("17-06-2020 23:45");
//            new PriodTotalDriver().driver(start, stop);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        /**
//        * date: 2020/12/13 16:13
//        * log: 将Date设为static之后运行成功
//        */


//        /**
//        * date: 2020/12/13 16:42
//        * log: 测试去掉选择日期时选择的秒
//        */
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        Date date = new Date(); // date 包括时分秒
//        System.out.println(date.toString());
//        String s = sdf.format(date); // 把带时分秒的 date 转为 yyyy-MM-dd 格式的字符串
//        try {
//            Date date2 = sdf.parse(s); // 把上面的字符串解析为日期类型
//            System.out.println(date2.getMinutes());
//            System.out.println(sdf.format(date2));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        /**
//        * date: 2020/12/13 16:44
//        * log: 测试成功，可以去掉秒
//        */


//        /**
//        * date: 2020/12/14 10:29
//        * log: 测试找出所有inverter的mapreduce能否成功运行
//        */
//        new FindAllInverterDriver().driver();
//        /**
//        * date: 2020/12/14 10:36
//        * log: 测试成功，能识别出所有的inverter，成功运行
//        */

//        /**
//        * date: 2020/12/15 0:15
//        * log: 测试文件的重命名与移动（本地）
//        */
//        File f1 = new File("C:\\Users\\Tan\\Desktop\\a.txt");
//        File f2 = new File("C:\\Users\\Tan\\Desktop\\b.txt");
//
//        if (f1.renameTo(f2)) {
//            System.out.println("Successfully");
//        } else {
//            System.out.println("Failure");
//        }
//        /**
//        * date: 2020/12/15 0:18
//        * log: 此方法会直接将文件重命名
//        */

//        /**
//        * date: 2020/12/15 0:19
//        * log: 对mapreduce的处理结果文件进行重命名，并删除原来的目录
//        */
//        String parentPath = "C:\\Users\\Tan\\Desktop\\archive\\inverters";
//        File f1 = new File(parentPath, "part-r-00000");
//        File f2 = new File("C:\\Users\\Tan\\Desktop\\archive\\inverters.csv");
//
//        if (f1.renameTo(f2)) {
//            System.out.println("Successfully");
//
//            /**
//            * date: 2020/12/15 0:30
//            * log: 删除目录时，先要删除器子文件
//            */
//            File parent = new File(parentPath);
//            if (parent.isDirectory()) {
//                String[] child = parent.list();
//                for (String s : child) {
//                    if (new File(parentPath, s).delete()) {
//                        System.out.println("子文件 " + s + "删除成功");
//                    }
//                }
//                if (parent.delete()) {
//                    System.out.println("目录删除成功");
//                }
//            }
//        } else {
//            System.out.println("Failure");
//        }
//        /**
//        * date: 2020/12/15 0:38
//        * log: 处理mapreduce处理结果，只保留我需要文件，删除其他文件，测试成功
//        */


//        /**
//        * date: 2020/12/15 1:19
//        * log: 测试找出所有的inverter，并只保留我想要的数据
//        */
//        String source = "Plant_1_Generation_Data.csv";
//        String result = "inverter";
//
//        new FindAllInverterDriver().driver(source, result);
//
//        HandleResult handle = new HandleResult(result, result + ".csv");
//        handle.handle();
//        /**
//         * date: 2020/12/15 1:27
//         * log: 测试成功，最终得到的文件只有想得到的单个文件
//         */


//        new HBaseOperate().getData("student");




    }

}
