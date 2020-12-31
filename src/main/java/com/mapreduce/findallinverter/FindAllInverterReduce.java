package com.mapreduce.findallinverter;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class FindAllInverterReduce extends Reducer<Text, IntWritable, NullWritable, Text> {

    String inverters = null;

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        System.out.println(key.toString());
        inverters = key.toString() + "," + inverters;
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        context.write(null, new Text(inverters));
    }
}
