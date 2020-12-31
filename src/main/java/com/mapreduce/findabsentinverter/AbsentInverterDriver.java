package com.mapreduce.findabsentinverter;

import com.mapreduce.gettmperature.GetTemperatureDriver;
import com.mapreduce.gettmperature.GetTemperatureMap;
import com.mapreduce.gettmperature.GetTemperatureReduce;
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

public class AbsentInverterDriver {

    public void absentInverterDriver(String start, String stop, String plant, String inverter) {
        try {
            Properties properties = System.getProperties();
            properties.setProperty("HADOOP_USER_NAME", "root");
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", "hdfs://192.168.157.129:9000");
            conf.setInt("dfs.replication", 1);


            Job job = Job.getInstance(conf);
            job.setJobName("absent Inverter");
            job.setJarByClass(AbsentInverterDriver.class);

            job.getConfiguration().set("start", start);
            job.getConfiguration().set("stop", stop);
            job.getConfiguration().set("plant", plant);
            job.getConfiguration().set("inverter", inverter);

            job.setMapperClass(AbsentInverterMap.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(IntWritable.class);

            job.setReducerClass(AbsentInverterReduce.class);
            job.setOutputKeyClass(IntWritable.class);
            job.setOutputValueClass(NullWritable.class);

            FileInputFormat.setInputPaths(job, new Path("/data/" + plant + "_Generation_Data.csv"));

            FileOutputFormat.setOutputPath(job, new Path("/result/result4"));

            if (job.waitForCompletion(true)) {
                System.out.println("AbsentInverter Successfully!!");
            } else {
                System.out.println("AbsentInverter failure!!");
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
