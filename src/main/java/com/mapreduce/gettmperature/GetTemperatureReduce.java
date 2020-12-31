package com.mapreduce.gettmperature;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class GetTemperatureReduce extends Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        System.out.println(key.toString());
        double sum = 0;
        String data = "";
        for (Text value : values) {
            String[] s = value.toString().split(":");
            if (s.length == 2) {
                sum += Double.parseDouble(s[1]);
            } else if (s.length == 3) {
                data = s[1] + " " + s[2];
            }
        }
        System.out.println("Date: " + sum + " other:" +  data);
        /**
        * date: 2020/12/25 9:59
        * log: Wether表中2020-06-03 14；00 数据不能存在，添加非空判断
        */
        if (!data.equals("")) {
            context.write(key, new Text(String.valueOf(sum) + " " + data));
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        System.out.println("Reduce Successfully");
    }
}
