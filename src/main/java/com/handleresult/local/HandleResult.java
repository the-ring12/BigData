package com.handleresult.local;

import java.io.File;

public class HandleResult {

    private String parentPath = "";
    private String childPath = "";
    private String tartgetPath = "";

    public HandleResult(String source, String newName) {
        this.parentPath = "C:\\Users\\Tan\\Desktop\\archive\\" + source;
        childPath = "part-r-00000";
        tartgetPath = "C:\\Users\\Tan\\Desktop\\archive\\result\\" + newName;
    }

    public HandleResult(String parentPath, String childPath, String tartgetPath) {
        this.parentPath = parentPath;
        this.childPath = childPath;
        this.tartgetPath = tartgetPath;
    }

    public void handle() {
        File fsource = new File(parentPath, childPath);
        File ftartget = new File(tartgetPath);

        /**
        * date: 2020/12/15 0:49
        * log: 对指定的文件进行移动以及重命名
        */
        if (fsource.renameTo(ftartget)) {
            System.out.println("File move and rename successfully!");

            /**
            * date: 2020/12/15 0:48
            * log: 删除目录，先删除其子文件
            */
            File fparent = new File(parentPath);
            for (String file : fparent.list()) {
                if (new File(parentPath, file).delete()) {
                    System.out.println("chile file " + file + "delete successfully");
                }
            }
            if (fparent.delete()) {
                System.out.println("directory delete successfully");
            }

        }
    }
}
