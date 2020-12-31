package com.mapreduce.findabsentinverter;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AbsentInverterMap extends Mapper<LongWritable, Text, Text, IntWritable> {

    IntWritable one = null;
    Date begin = null;
    Date end = null;
    String plant = null;
    String inverter = null;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd");
    Date date = null;
//15-05-2020 00:00

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {

        String start = context.getConfiguration().get("start");
        String stop = context.getConfiguration().get("stop");
        plant = context.getConfiguration().get("plant");
        inverter = context.getConfiguration().get("inverter");
        System.out.println(start + stop);
        try {
//            begin.setTime(format.parse(start));
//            end.setTime(format.parse(stop));
            begin = format.parse(start);
            end = format.parse(stop);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        if (!key.toString().equals("0")) {
            System.out.println(value.toString());
            String[] data = value.toString().split(",");
            try {
                if (plant.equals("Plant2")) {
                    date = format.parse(data[0]);
                } else {
                    date = format2.parse(data[0]);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println(date.toString());
            if (date.after(begin) && date.before(end)) {
                System.out.println(data[2]);
                    if (data[2].equals(inverter)) {
                        System.out.println(1);
                        context.write(new Text(data[2]), new IntWritable(1));
                    }
            }
        }
    }
}
