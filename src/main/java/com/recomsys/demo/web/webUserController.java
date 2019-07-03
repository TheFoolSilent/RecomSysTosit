package com.recomsys.demo.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class webUserController {


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String userLogin(User st, HttpServletRequest request) {

        System.out.println(st.getUsername());
        if(st.getUsername() == null || st.getPassword() ==null)return "index";

        try {
            User user = userService.login(st);  // login
            if (user != null) {

                request.getSession().setAttribute("username", user.getUsername());  //将用户信息放到session中
                request.getSession().setAttribute("password", user.getPassword());  //将用户信息放到session中
                request.getSession().setAttribute("photo", "xxx.jpg");
                request.getSession().setMaxInactiveInterval(3600);
                // 设置session存储时间，以秒为单位，3600=60*60即为60分钟

                if (user.getUsername().equals("root")) {
                    return "redirect:"+"administer";
                }
            }

            String referer = request.getHeader("referer");
            return "redirect:"+referer;

        } catch (Exception e) {

            JSONObject errresult = new JSONObject();
            errresult.put("msg", "3");
            errresult.put("description", e.toString());
            e.printStackTrace();
            return errresult.toJSONString();
        }

    }



    @RequestMapping("/register")
    public String UIRegister(HttpServletRequest request) {
        if(request.getSession().getAttribute("username") != null){
            String referer = request.getHeader("referer");
            return "redirect:" + referer;
        }
        return "userregister";
    }


    @PostMapping("/registerraise")
    @ResponseBody
    public String userRegister(@RequestBody User user, HttpServletRequest request) {

        String result = "1";
        JSONObject sendback = new JSONObject();

        List<User> u = userService.findAllUser();  //获取所有用户名

        if (user.getPassword().equals("")) {
            result = "3";    //密码不能为空
            sendback.put("description", "password can't be null");

        } else if (user.getUsername().equals("")) {
            result = "4";    //账号不能为空
            sendback.put("description", "username can't be null");

        } else {
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
            request.getSession().setAttribute("username", user.getUsername());  //将用户信息放到session中
            request.getSession().setAttribute("password", user.getPassword());  //将用户信息放到session中
            request.getSession().setAttribute("photo", "xxx.jpg");
            request.getSession().setMaxInactiveInterval(3600);
        }

        sendback.put("msg", result);
        return sendback.toJSONString();
    }


}
