package main.java.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghaoye on 2019/9/17.
 */
public class FileUtil {

    public List<String> findJavas(String dir){
        List<String> result = new ArrayList<>();
        File file = new File(dir);
        if(file.exists()){
            File[] files = file.listFiles();
            if (null != files) {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        result.addAll(findJavas(file2.getAbsolutePath()));
                    } else {
                        String javaPath = file2.getAbsolutePath();
                        if (javaPath.endsWith(".java"))
                            result.add(javaPath);
                    }
                }
            }
        } else {
            return result;
        }
        return result;
    }

    public List<String> createDir(String dir, List<String> fileList){
        List<String> deleted = new ArrayList<>();
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdir();
        }
        else {
            deleted = deleteDir(dir, fileList);
        }
        return deleted;
    }

    public static List<String> deleteDir(String path, List<String> fileList){
        List<String> deleted = new ArrayList<>();
        File file = new File(path);
        if(!file.exists()){//判断是否待删除目录是否存在
            System.err.println("The dir are not exists!");
            return deleted;
        }

        String[] content = file.list();//取得当前目录下所有文件和文件夹
        for(String name : content){
            String tmp = name.replaceFirst("-", "/");
            if (fileList.contains(tmp)){
                deleted.add(tmp);
                continue;
            }
            File temp = new File(path, name);
            if(temp.isDirectory()){//判断是否是目录
                deleted.addAll(deleteDir(temp.getAbsolutePath(), fileList));//递归调用，删除目录里的内容
                temp.delete();//删除空目录
            }else{
                if(!temp.delete()){//直接删除文件
                    System.err.println("Failed to delete " + name);
                }
            }
        }
        return deleted;
    }


    public List<String> readFile(String file) {
        List<String> content = new ArrayList<String>();
        try (FileReader reader = new FileReader(file);
             BufferedReader br = new BufferedReader(reader)
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim(); //去除结尾空格换行符
                content.add(line);
            }
            br.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

    public void saveFile(List<String> comments, String projectPath) {
        String path = projectPath + ".txt";
        File file = new File(path);
        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file, true);
            for (String comment : comments) {
                writer.write(comment);
                writer.write("\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
