package com.mapreduce.inverterpower.priodtotal;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class InverterPowerReduce extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {



    @Override
    protected void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
        double sum = 0;
        for (DoubleWritable value : values) {
            sum += value.get();
            System.out.println(sum);
        }
        context.write(key, new DoubleWritable(sum));
    }
}
