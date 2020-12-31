package com.mapreduce.findallinverter;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FindAllInverterMap extends Mapper<LongWritable, Text, Text, IntWritable> {

    IntWritable one = new IntWritable(1);

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        System.out.println(value.toString());
        if (!key.toString().equals("0")) {
            String[] data = value.toString().split(",");
            context.write(new Text(data[2]), one);
        }
    }
}
