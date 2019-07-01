package com.recomsys.demo.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;


@Controller
public class TestController {

    /*
     * RequestMapping作用 ： 提供路由信息，负责URL到Controller中的具体函数的映射
     */
    @RequestMapping("/")
    public String hello() {
        return "user_login.html";
    }


    @RequestMapping("/index")
    public String addSession(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        if (session.getAttribute("username") == null) {

            return "index.html";  // return error json
        } else if(session.getAttribute("username").toString().equals("root")){
            return "";  // return root html
        }else {
            return "";  // return user html
        }
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String userLogin(@RequestBody User st, HttpServletRequest request) {

        JSONObject result = new JSONObject();

        try {
            User user = userService.login(st);  //登录
            if (user != null) {

                request.getSession().setAttribute("username", user);  //将用户信息放到session中
                request.getSession().setMaxInactiveInterval(3600);
                // 设置session存储时间，以秒为单位，3600=60*60即为60分钟

                result.put("msg", "1");
                result.put("description", "success");

            } else {
                result.put("msg", "2");
                result.put("description", "username or password error");
            }
        } catch (Exception e) {

            JSONObject errresult = new JSONObject();
            errresult.put("msg", "3");
            errresult.put("description", e.toString());
            e.printStackTrace();
            return errresult.toJSONString();
        }
        return result.toJSONString();
    }


    @PostMapping("/register")
    @ResponseBody
    public String userRegister(@RequestBody User user) {

        String result = "1";
        JSONObject sendback = new JSONObject();

        List<User> u = userService.findAllUser();  //获取所有用户名

        if (user.getPassword().equals("")) {
            result = "3";    //密码不能为空
            sendback.put("description", "password can't be null");

        } else if (user.getUsername().equals("")) {
            result = "4";    //账号不能为空
            sendback.put("description", "username can't be null");

        }else{
            for (int i = 0; i < u.size(); i++) {  //遍历用户名跟获取到的用户名比较
                if (u.get(i).getUsername().equals(user.getUsername())) {
                    result = "2";   //用户名已经被注册
                    sendback.put("description", "username has been registered");
                    break;
                }
            }
        }

        if (result.equals("1")) {
            userService.addUser(user);
            sendback.put("description", "register successfully");
        }

        sendback.put("msg", result);
        return sendback.toJSONString();
    }

}
