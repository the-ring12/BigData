package com.mapreduce.hdfstohbase;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;


public class HDFSToHBaseDriver {

    public static void main(String[] args) {
        try {
            Job job = Job.getInstance();
            job.setJarByClass(HDFSToHBaseDriver.class);
            job.setJobName("HDFS to HBase");

            job.setMapperClass(HDFSMap.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(IntWritable.class);

            FileInputFormat.setInputPaths(job, new Path("/hbase/data/emp.csv"));

            String tablename = "";
            TableMapReduceUtil.initTableReducerJob(tablename, HBaseReduce.class, job);

            System.exit(job.waitForCompletion(true) ? 0 : 1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
