package com.mapreduce.priodtotal;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PriodTotalMap extends Mapper<LongWritable, Text, Text, DoubleWritable> {
    Date begin = null;
    Date end = null;
    String plant = null;
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
            if (date.after(begin) && date.before(end)) {
                context.write(new Text(format3.format(date)), new DoubleWritable(Double.parseDouble(data[5])));
            }
        }
    }
}
