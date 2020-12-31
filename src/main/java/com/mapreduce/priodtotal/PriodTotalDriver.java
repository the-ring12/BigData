package com.mapreduce.priodtotal;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;

public class PriodTotalDriver {

    public void driver(String start, String stop, String plant) {
        try {
//
//        /**
//        * date: 2020/12/13 15:18
//        * log: 首先设置其实和最终的时间
//        */
//        PriodTotalReduce reduce = new PriodTotalReduce();
//        reduce.setStart(start);
//        reduce.setStop(stop);

            /**
            * date: 2020/12/23 18:00
            * log: 远程调式配置
            */
            Properties properties = System.getProperties();
            properties.setProperty("HADOOP_USER_NAME", "root");
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", "hdfs://192.168.157.129:9000");
            conf.setInt("dfs.replication", 1);

            /**
            * date: 2020/12/13 15:20
            * log: 设定任务
            */
            Job job = Job.getInstance(conf);
            job.setJobName("Priod total solar power");
            job.setJarByClass(PriodTotalDriver.class);

            job.getConfiguration().set("start", start);
            job.getConfiguration().set("stop", stop);
            job.getConfiguration().set("plant", plant);

            /**
            * date: 2020/12/23 14:42
            * log: 设置日期限制
            */


            job.setMapperClass(PriodTotalMap.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(DoubleWritable.class);

            job.setReducerClass(PriodTotalReduce.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(DoubleWritable.class);

            FileInputFormat.addInputPath(job, new Path("/data/"+ plant+"_Generation_Data.csv"));
            FileOutputFormat.setOutputPath(job, new Path("/result/result1"));

            boolean result = job.waitForCompletion(true);
            if (result) {
                System.out.println("Successfully!!");
            } else {
                System.out.println("Failure!!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        try {
//            /**
//             * date: 2020/12/13 15:33
//             * log: 设定时间段
//             */
//            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//            Date start = format.parse("15-05-2020 00:00");
//            Date stop = format.parse("17-06-2020 23:45");
//        /**
//        * date: 2020/12/13 15:47
//        * log: 将时间写死在reduce中，运行成功
//        */
//            new PriodTotalDriver().driver(null, null);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }
}
