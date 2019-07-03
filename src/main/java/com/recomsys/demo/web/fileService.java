package com.recomsys.demo.web;

import org.springframework.web.multipart.MultipartFile;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JAVA File Util
 *
 * **/

public class fileService {
//    public static String path = "/home/hadoop/IdeaProjects/RecomSysdemo/module_input/";

                public static String path = "C:/Users/sky/IdeaProjects/RecomSysTosit/module_input/";

                public static List<String> getFillList(){
                    ArrayList<String> filename_set = new ArrayList<>();
                    File file_path = new File(path);
                    File[] files = file_path.listFiles();
                    for (File file : files) {
                        if(file.isFile()){
                            filename_set.add(file.getName());
                        }
        }

        return filename_set;

    }

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

    public static boolean deleteFile(String filename){
        File file = new File(path + filename);
        if(file.exists() && file.isFile()){
            try{
                file.delete();
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
            return true;
        }

        return false;

    }


    public static boolean chooseFile(String filename){
        File file = new File(path + filename);
        if(file.exists() && file.isFile()){
            try{
                /**TODO
                 * modify the Sparks' training file
                 *
                 * */
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
            return true;
        }

        return false;
    }

}
