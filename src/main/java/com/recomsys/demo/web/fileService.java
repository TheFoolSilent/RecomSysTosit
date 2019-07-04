package com.recomsys.demo.web;

import com.recomsys.demo.ml.JobRec;
import com.recomsys.demo.ml.SkillRec;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JAVA File Util
 **/

public class fileService {
    public static String path = "/home/hadoop/IdeaProjects/RecomSysdemo/module_input/";

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

    public static boolean saveFile(MultipartFile file) {
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

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteFile(String filename) {
        File file = new File(path + filename);
        if (file.exists() && file.isFile()) {
            try {

                JobRec jobr = new JobRec();

                file.delete();

                if ((path + filename).equals(jobr.getData_addr())) {
                    List<String> list_file = getFillList();
                    if(list_file.size() > 0){
                        if(chooseFile(list_file.get(0))){
                            return true;
                        }
                    }

                    jobr.setData_addr(null);
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
     *change training data
     * check url validation
     *
     * */
    public static boolean chooseFile(String filename) {
        File file = new File(path + filename);
        // if spark_train_path is not null then do

        if (file.exists() && file.isFile()) {
            try {

                JobRec jobrec = new JobRec();
                SkillRec skillrec = new SkillRec();

//                if (jobrec.getAddr() == null || skillrec.getAddr() == null) {
//                    msg.put("msg", "error");
//                    msg.put("description", "training file not found");
//                    return msg.toJSONString();
//                }
                jobrec.setData_addr(path + filename);
                skillrec.setData_addr(path + filename);

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        return false;
    }

}
