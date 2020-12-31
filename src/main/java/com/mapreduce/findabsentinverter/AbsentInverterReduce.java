package com.mapreduce.findabsentinverter;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class AbsentInverterReduce extends Reducer<Text, IntWritable, IntWritable, NullWritable> {

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int sum = 0;
        for (IntWritable value : values) {
            sum += value.get();
        }
        System.out.println(sum);
        context.write(new IntWritable(sum), null);
    }
}
