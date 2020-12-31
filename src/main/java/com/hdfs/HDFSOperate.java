package com.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HDFSOperate {

    /**
    * @author: The_ring
    * @date: 2020/12/11 10:54
    * @description: 获取客户端对象
    */
    public FileSystem getFileSystem() {
        Configuration conf = new Configuration();
        conf.set("dfs.blocksize", "128m");          //设置block大小
        FileSystem fs = null;
        try {
            fs = FileSystem.get(new URI("hdfs://niit1234:9000"), conf, "root");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return fs;
    }

    /**
    * @author: The_ring
    * @date: 2020/12/11 10:54
    * @description: 获取文件流，读取hdfs上的文件内容,默认下载到D盘
    */
    public void readFile() {
        FileSystem fs = null;
        FSDataInputStream in = null;
        BufferedReader reader = null;
        try {
            fs = getFileSystem();
            in = fs.open(new Path("/hbase/data/emp.csv"));
            reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
    * @date: 2020/12/15 16:30
    * @description: 处理mapreduce之后的所有inverter,然后mapreduce去判断某一时间段没有运行的inverter
    */
    public String[] getInverter() {
        String[] inverters = null;
        FileSystem fs = null;
        FSDataInputStream in = null;
        BufferedReader reader = null;
        try {
            fs = getFileSystem();
            in = fs.open(new Path("/hbase/data/emp.csv"));
            reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                inverters[i] = line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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

        return inverters;
    }

    /**
    * @author: The_ring
    * @date: 2020/12/11 15:47
    * @description: 读取文件内容，返回一个对象，供其他类读取文件内容调用
    */
    public BufferedReader getBufferedReader(String sourcePath) {
        FileSystem fs = null;
        FSDataInputStream in = null;
        BufferedReader reader = null;
        try {
            fs = getFileSystem();
            in = fs.open(new Path(sourcePath));
            reader = new BufferedReader(new InputStreamReader(in));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
        return reader;
    }

    /**
    * @author: The_ring
    * @date: 2020/12/11 11:05
    * @description: 从hdfs上下载文件到本地
    */
    public void loadFile(String sourceFile) {
        FileSystem fs = null;
        try {
            fs = getFileSystem();

//            fs.copyToLocalFile(false, new Path(sourceFile), new Path("D://"));
            /**
            * date: 2020/12/11 11:17
            * log: 默认为本地D盘存储下载文件失败，首先获取桌面的据对路径，将其下载到桌面
             * problem： 应用web Application 查找桌面路径，是否会出现问题
            */
            FileSystemView fsv = FileSystemView.getFileSystemView();
            File home = fsv.getHomeDirectory();
            String path = home.getPath();
            fs.copyToLocalFile(false, new Path(sourceFile), new Path(path));

            System.out.println("Load Successfully!!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
    * @date: 2020/12/15 0:58
    * @description: 对mapreduce的处理结果进行处理，删除其他文件，将重要数据移至指定目录，并删除原目录
    */
    public void handleFile(String resultName, String newName) {
        FileSystem fs = null;
        try {
            fs = getFileSystem();
            if (fs.exists(new Path("/result/" + newName))) {
                fs.delete(new Path("/result/" + newName), true);
            }
            fs.rename(new Path("/result/" + resultName + "/part-r-00000"), new Path("/result/" + newName));
            System.out.println("file rename to " + newName + " successfully");

            fs.delete(new Path("/result/" + resultName), true);
            System.out.println("directory /result/" + resultName + " delete successfully");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
    * @date: 2020/12/25 16:20
    * @description: 获取某一范围内记录条数
    */
    public long getLines(String start, String stop, String filepath) {
        long nums = 0l;
        FileSystem fs = null;
        FSDataInputStream in = null;
        BufferedReader reader = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date startTime = format.parse(start);
            Date stopTime = format.parse(stop);
            Date date = null;
            fs = getFileSystem();
            in = fs.open(new Path(filepath));
            reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                date = format.parse(line.split(",")[0]);
                if (date.after(startTime) && date.before(stopTime)) {
                    nums++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
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
        return nums;
    }
}
