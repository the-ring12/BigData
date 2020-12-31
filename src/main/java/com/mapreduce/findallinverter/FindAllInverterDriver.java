package com.mapreduce.findallinverter;

import com.test.Main;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Properties;

public class FindAllInverterDriver {

    /**
    * date: 2020/12/14 10:29
    * log: 找出所有的inverter
    */
    /**
    * date: 2020/12/15 1:15
    * log: 添加两个参数，数据输入的文件和数据输出的目录
    */
    public void inverterDriver(String plant) {
        String jobName = "Find all inverter";
        try {
            Properties properties = System.getProperties();
            properties.setProperty("HADOOP_USER_NAME", "root");

            Configuration conf = new Configuration();
//            conf.set("yarn.resourcemanager.address", "192.168.157.129:8032");
//            conf.set("mapreduce.framework.name", "yarn");
            conf.set("fs.defaultFS", "hdfs://192.168.157.129:9000");
//            conf.set("mapreduce.app-submission.cross-platform", "true");
            conf.setInt("dfs.replication", 1);


            Job job = Job.getInstance(conf);
            job.setJobName(jobName);
            job.setJarByClass(FindAllInverterDriver.class);

            job.setMapperClass(FindAllInverterMap.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(IntWritable.class);

            job.setReducerClass(FindAllInverterReduce.class);
            job.setOutputKeyClass(NullWritable.class);
            job.setOutputValueClass(Text.class);

            FileInputFormat.addInputPath(job, new Path("/data/" + plant + "_Generation_Data.csv"));
            FileOutputFormat.setOutputPath(job, new Path("/result/result3"));

            if (job.waitForCompletion(true)) {
                System.out.println(jobName + " successfully");
            } else {
                System.out.println(jobName + " failure");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
