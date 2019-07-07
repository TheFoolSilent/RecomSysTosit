package com.recomsys.demo.web;

import com.recomsys.demo.web.Entity.User;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JAVA File Util
 * **/

public class fileService {
    private static String choose_path = "data/choose.txt";
    public static String path = "/home/hadoop/IdeaProjects/RecomSysdemo/module_input/";
    private static String jobListPath = "data/joblist.txt";

    public static List<String> getFillList() {
        ArrayList<String> filename_set = new ArrayList<>();
        File file_path = new File(path);
        File[] files = file_path.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                filename_set.add(file.getName());
            }
        }

        return filename_set;

    }

    /**
     * save training data
     * check data validation
     * */

    public static boolean saveFile(MultipartFile file){
        if (file.isEmpty()) {
            System.out.println("文件为空");
        }

        String fileName = file.getOriginalFilename();  // origin filename
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名

        fileName = UUID.randomUUID() + suffixName;  // new filename

        File dest = new File(path + fileName);

        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            file.transferTo(dest);  // save file
            return true;

        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * delete training data
     * check url validation
     * */
    public static boolean deleteFile(String filename) {
        File file = new File(path + filename);
        if (file.exists() && file.isFile()) {
            try {

                String dataname = getChooseData();

                if(dataname == null){
                    return false;
                }

                file.delete();

                if (filename.equals(dataname)) {
                    List<String> list_file = getFillList();
                    if(list_file.size() > 0){
                        if(chooseFile(list_file.get(0))){
                            return true;
                        }
                    }

                    return false;

                }

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        return false;

    }


    /**
     * change training data
     * check url validation
     * */
    public static boolean chooseFile(String filename) {
        // if spark_train_path is not null then do
        try{
            FileWriter writer = new FileWriter(choose_path);

            writer.write(filename);
            writer.close();
            return true;

        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static String getChooseData(){
        String str;
        try {
            FileReader fr = new FileReader(choose_path);
            BufferedReader bf = new BufferedReader(fr);
            str = bf.readLine();

            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return str;
    }



    public static List<String> readJobList(){

        List<String> arrayList = new ArrayList<>();
        try{
            FileReader reader = new FileReader(jobListPath);
            BufferedReader br = new BufferedReader(reader);
            String str;
            while ((str = br.readLine())!=null){
                arrayList.add(str.trim());
            }

            return arrayList;

        }catch (IOException e){
            e.printStackTrace();
            return null;
        }

    }

    public static int EditDistance(String source, String target) {
        char[] sources = source.toCharArray();
        char[] targets = target.toCharArray();
        int sourceLen = sources.length;
        int targetLen = targets.length;
        int[][] d = new int[sourceLen + 1][targetLen + 1];
        for (int i = 0; i <= sourceLen; i++) {
            d[i][0] = i;
        }
        for (int i = 0; i <= targetLen; i++) {
            d[0][i] = i;
        }

        for (int i = 1; i <= sourceLen; i++) {
            for (int j = 1; j <= targetLen; j++) {
                if (sources[i - 1] == targets[j - 1]) {
                    d[i][j] = d[i - 1][j - 1];
                } else {
                    //插入
                    int insert = d[i][j - 1] + 1;
                    //删除
                    int delete = d[i - 1][j] + 1;
                    //替换
                    int replace = d[i - 1][j - 1] + 1;
                    d[i][j] = Math.min(insert, delete) > Math.min(delete, replace) ? Math.min(delete, replace) :
                            Math.min(insert, delete);
                }
            }
        }
        return d[sourceLen][targetLen];
    }


}
