package com.recomsys.demo.web.Util;

import com.alibaba.fastjson.JSONObject;
import com.recomsys.demo.JavaConf;
import com.recomsys.demo.web.Entity.Question;
import com.recomsys.demo.web.Entity.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User Account Util
 * 1. find all user
 * 2. add user
 * 3. check username and password
 * 4. save users' feedback
 **/

public class userService {

    private static String path = JavaConf.userPath;
    private static String feed_path = JavaConf.feedPath;

    /**
     * read file：FileReader
     */
    public static List<User> findAllUser() {
        List<User> arrayList = new ArrayList<>();
        try {
            FileReader fr = new FileReader(path);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {

                String username = str.split(" ")[0].trim();
                String password = str.split(" ")[1].trim();

                User user = new User(username, password);

                arrayList.add(user);
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 返回数组
        return arrayList;
    }

    /**
     * append file：FileWriter
     */
    public static void addUser(User user) {
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件

            FileWriter writer = new FileWriter(path, true);
            writer.write(user.getUsername() + " " + user.getPassword() + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * check user login validation
     * */

    public static User login(User user) {
        int flag = 0;
        try {
            FileReader fr = new FileReader(path);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {

                String username = str.split(" ")[0].trim();
                String password = str.split(" ")[1].trim();

                if (user.getUsername().equals(username)) {
                    if (user.getPassword().equals(password)) {
                        flag = 1;
                    }
                    break;
                }
            }

            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (flag == 1) {
            return user;
        }
        return null;
    }

    /**
     * Write file to save users' feedback
     * */

    public static boolean saveFeedback(Question question, String user) {
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(feed_path, true);
            JSONObject json = new JSONObject();

            json.put("username", user);
            json.put("subject", question.getSubject());
            json.put("message", question.getMessage());

            writer.write(json.toJSONString()+ "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

}
