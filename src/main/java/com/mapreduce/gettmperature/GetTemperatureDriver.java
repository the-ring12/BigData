package com.mapreduce.gettmperature;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Properties;


public class GetTemperatureDriver {

    public void TemperatureDriver(String start, String stop, String plant) {

        try {
            Properties properties = System.getProperties();
            properties.setProperty("HADOOP_USER_NAME", "root");
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", "hdfs://192.168.157.129:9000");
            conf.setInt("dfs.replication", 1);


            Job job = Job.getInstance(conf);
            job.setJobName("GetTemperature");
            job.setJarByClass(GetTemperatureDriver.class);

            job.getConfiguration().set("start", start);
            job.getConfiguration().set("stop", stop);
            job.getConfiguration().set("plant", plant);

            job.setMapperClass(GetTemperatureMap.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);

            job.setReducerClass(GetTemperatureReduce.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);

            FileInputFormat.setInputPaths(job, new Path("/data/" + plant + "_Generation_Data.csv"),
                    new Path("/data/" + plant + "_Weather_Sensor_Data.csv"));

            FileOutputFormat.setOutputPath(job, new Path("/result/result2"));

            if (job.waitForCompletion(true)) {
                System.out.println("GetTemperature Successfully!!");
            } else {
                System.out.println("GetTemperature failure!!");
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
