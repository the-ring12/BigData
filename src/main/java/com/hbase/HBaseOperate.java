package com.hbase;

import com.bean.Condition;
import com.hdfs.HDFSOperate;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HBaseOperate {

    public Configuration conf = null;

    public HBaseOperate() {
        conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "niit1234");
    }

    /**
    * @author: The_ring
    * @date: 2020/12/11 16:00
    * @description: 将csv数据插入hbase表中
    */
    public void insertDataToHBase(String sourcePath, String tableName, String column) {
        FileSystem fs = null;
        FSDataInputStream in = null;
        BufferedReader reader = null;
        HTable table = null;
        try {
            /**
            * date: 2020/12/11 16:01
            * log: getBufferedReader方法中已经将SileSystem资源关闭，所以获得的BufferedReader是一个无效对象
            */
//            BufferedReader reader = new HDFSOperate().getBufferedReader(sourcePath);

            /**
             * date: 2020/12/12 18:59
             * log: 不能将两个方法分离
             */
            fs = new HDFSOperate().getFileSystem();
            in = fs.open(new Path(sourcePath));
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }

            /**
            * date: 2020/12/12 19:09
            * log: 向表内添加数据
            */
            table = new HTable(conf, tableName);
            //将数据以逗号分割, 默认开始第一个数据为rowkey
            String[] columns = column.split(",");
            while ((line = reader.readLine()) != null) {
//                String[] data = line.split(",");
                /**
                * date: 2020/12/24 21:58
                * log: 将输出结果强制转换为csv时，仍然以空格分隔
                */
                System.out.println(line.replaceAll("\t", " "));
                String[] data = line.replaceAll("\t", " ").split(" ");
                Put put = new Put(Bytes.toBytes(data[0] + " " + data[1]));
                for (int i = 1; i < columns.length; i++) {
                    put.addColumn(Bytes.toBytes(columns[i].split(":")[0]), Bytes.toBytes(columns[i].split(":")[1]), Bytes.toBytes(data[i + 1]));
                }
                table.put(put);
            }
            System.out.println("The data import into hbase Successfully!!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (table != null) {
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
    * @author: The_ring
    * @date: 2020/12/16 8:02
    * @description: 读取hbase数据表内的数据
    */
    public void getData(String tableName) {
        HTable htable = null;
        Get get = null;
        Result rs = null;
        try {
            htable = new HTable(conf, tableName);
            Scan scan = new Scan();
            scan.setMaxVersions(4);
//            scan.setTimeRange(1600000000000L, 1602635693396L);
            ResultScanner scanner = htable.getScanner(scan);
            for (rs = scanner.next(); rs != null; rs = scanner.next()) {
                List<Cell> list = rs.getColumnCells(Bytes.toBytes("f1"), Bytes.toBytes("name"));
                for (Cell cell : list) {
                    System.out.println(Bytes.toString(CellUtil.cloneValue(cell)));
//                    System.out.println(cell.getQualifier().toString() + cell.getRow().toString());
                }
            }

//            get =new Get(Bytes.toBytes(rowkey));
//            rs = htable.get(get);
//            for (KeyValue keyValue : rs.raw()) {
//                System.out.println("column: " + new String (keyValue.getFamily()) + ":"
//                        + new String(keyValue.getQualifier()) + "\tvalue: "
//                        + new String(keyValue.getValue()));
//            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (htable != null) {
                try {
                    htable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
    * @author: The_ring
    * @date: 2020/12/24 22:14
    * @description: 获取hbase表内的数据
    */
    public List<Condition> getDataFromHbase(String tableName, String start, String stop) throws IOException {
        List<Condition> list = new ArrayList<>();

        //处理时间，设置rowkey范围
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date startTemp = null;
        Date stopTemp = null;
        ResultScanner scanner = null;
        HTable hTable = null;
        try {
            startTemp = format.parse(start);
            stopTemp = format.parse(stop);

        Calendar canlendar = Calendar.getInstance();
        canlendar.setTime(startTemp);
        int year = canlendar.get(Calendar.YEAR);
        int month = canlendar.get(Calendar.MONTH);
        int day = canlendar.get(Calendar.DAY_OF_MONTH);
        int hour = canlendar.get(Calendar.HOUR_OF_DAY);
        int minute = canlendar.get(Calendar.MINUTE);
        if (minute % 15 != 0) {
            minute = (minute / 15 + 1) * 15;
        }
        canlendar.set(year, month, day, hour, minute);
        String starTime = format1.format(canlendar.getTime());


        canlendar.setTime(stopTemp);
        year = canlendar.get(Calendar.YEAR);
        month = canlendar.get(Calendar.MONTH);
        day = canlendar.get(Calendar.DAY_OF_MONTH);
        hour = canlendar.get(Calendar.HOUR_OF_DAY);
        minute = canlendar.get(Calendar.MINUTE);
        if (minute % 15 != 0) {
            minute = (minute / 15) * 15;
        }
        canlendar.set(year, month, day, hour, minute);
        String stopTime = format1.format(canlendar.getTime());

        System.out.println(starTime + " " + stopTime);

        System.out.println("start reder data from hbase");

        //获取HTable
        hTable = new HTable(conf, tableName);

        //设置Scan
        Scan scan = new Scan();
        scan.setStartRow(starTime.getBytes());
        scan.setStopRow(stopTime.getBytes());

        scanner = hTable.getScanner(scan);
        Iterator<Result> iterator = scanner.iterator();

        Condition condition = null;
        //循环遍历 hashNext next
        //所有迭代器都会有hashNext 和 Next这两个方法
        while (iterator.hasNext()) {
            //一行结果
            Result next = iterator.next();      //返回数据的一条结果集
            condition = new Condition();
            System.out.println(new String(next.getRow()));
            condition.setTime(new String(next.getRow()));

            List<Cell> listCell = next.listCells();
            for (Cell cell : listCell) {
                if (new String(cell.getQualifier()).equals("DifferenceTemperature")) {
                    System.out.println(new String(cell.getValue()));
                    condition.setTemperature(Double.parseDouble(new String(cell.getValue())));
                }
                if (new String(cell.getQualifier()).equals("TotalPower")) {
                    System.out.println(new String(cell.getValue()));
                    condition.setPower((int)Double.parseDouble(new String(cell.getValue())));
                }
                if (new String(cell.getQualifier()).equals("Irradiation")) {
                    System.out.println(new String(cell.getValue()));
                    condition.setIrradiation(Double.parseDouble(new String(cell.getValue())));
                }
            }
            list.add(condition);
        }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
            if (hTable != null) {
                hTable.close();
            }
        }

        return list;
    }
}
