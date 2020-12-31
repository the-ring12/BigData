package com.mapreduce.gettmperature;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetTemperatureMap extends Mapper<LongWritable, Text, Text, Text> {
    Date begin = null;
    Date end = null;
    String plant = null;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//    SimpleDateFormat format4 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    Date date = null;


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
            System.out.println(begin.toString());
            System.out.println(end.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        if (!key.toString().equals("0")) {      //去掉第一行
            String[] s = value.toString().split(",");
            if (s.length == 7) {
                try {
                    if (plant.equals("Plant2")) {
                        date = format.parse(s[0]);
                    } else {
                        date = format2.parse(s[0]);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date.after(begin) && date.before(end)) {
                    System.out.println("Generation Data: " + value.toString());
                    context.write(new Text(format3.format(date)), new Text("power:" + s[5]));
                }
            } else if (s.length == 6) {
                double difference = 0;

                try {
                    date = format.parse(s[0]);
                } catch (ParseException e) {
//                    SimpleDateFormat format5 = new SimpleDateFormat("yyyy/M/dd H:mm");
//                    try {
//                        date = format5.parse(s[0]);
//                    } catch (ParseException ex) {
//                        format5 = new SimpleDateFormat("yyyy/M/dd HH:mm");
//                        try {
//                            date = format5.parse(s[0]);
//                        } catch (ParseException exc) {
//                            format5 = new SimpleDateFormat("yyyy/M/d H:mm");
//                            try {
//                                date = format5.parse(s[0]);
//                            } catch (ParseException e1) {
//                                format5 = new SimpleDateFormat("yyyy/M/d H:mm");
//                                try {
//                                    date = format5.parse(s[0]);
//                                } catch (ParseException e2) {
//                                    e2.printStackTrace();
//                                }
//                            }
//                        }
//                    }
                    e.printStackTrace();
                }
                if (date.after(begin) && date.before(end)) {
                    System.out.println("Weather Data: " + value.toString());
                    difference = Double.parseDouble(s[3]) - Double.parseDouble(s[4]);
                    context.write(new Text(format3.format(date)), new Text("difference:"+String.valueOf(difference)+":"+s[5]));
                }
            }
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        System.out.println("Map successfully!!");
    }
}
