package com.recomsys.demo.web;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JAVA User Account Util
 * **/

public class userService {

    private static String path = "data/user.txt";

    /**
     * read file：FileReader
     */
    public static List<User> findAllUser(){
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

    public static User login(User user){
        int flag = 0;
        try {
            FileReader fr = new FileReader(path);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {

                String username = str.split(" ")[0].trim();
                String password = str.split(" ")[1].trim();

                if(user.getUsername().equals(username)){
                    if(user.getPassword().equals(password)) {
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

        if(flag == 1){
            return user;
        }
        return null;
    }

}
