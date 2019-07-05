package com.recomsys.demo.web;

import com.alibaba.fastjson.JSONObject;
import com.recomsys.demo.web.Entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class WebAccountController {

    /**
     * Login AJAX URL
     * check username and password
     * build session
     * input: json
     * return json
     * json format {
     *      msg:"",
     *      description:""
     * }
     * */

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String userLogin(@RequestBody User st, HttpServletRequest request) {

        JSONObject result = new JSONObject();

        System.out.println(st.getUsername());
        if (st.getUsername() == null || st.getPassword() == null) return "index";

        try {
            User user = userService.login(st);  // login
            if (user != null) {

                request.getSession().setAttribute("username", user.getUsername());  //将用户信息放到session中
                request.getSession().setAttribute("password", user.getPassword());  //将用户信息放到session中
                request.getSession().setAttribute("photo", "xxx.jpg");
                request.getSession().setMaxInactiveInterval(3600);
                // 设置session存储时间，以秒为单位，3600=60*60即为60分钟

                if (user.getUsername().equals("root")) {
                    result.put("msg", "0");
                    result.put("description", "root success");

                } else {
                    result.put("msg", "1");
                    result.put("description", "user success");

                }
            } else {
                result.put("msg", "2");
                result.put("description", "username or password error");

            }

//            String referer = request.getHeader("referer");
//            return "redirect:"+referer;
            return result.toJSONString();

        } catch (Exception e) {

            JSONObject errresult = new JSONObject();
            errresult.put("msg", "3");
            errresult.put("description", e.toString());
            e.printStackTrace();
            return errresult.toJSONString();
        }

    }


    /**
     * Register AJAX URL
     * check register
     * input: json
     * return json
     * json format {
     *     msg:"",
     *     description:""
     * }
     */

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


    /**
     * Log Out AJAX URL
     * log out funtion
     * input: state
     * return json
     * json format {
     *     msg:""
     * }
     */

    @RequestMapping(value = "/logout")
    @ResponseBody
    public String userLogout(@RequestParam("state") String order, HttpServletRequest request) {

        JSONObject result = new JSONObject();

        if (order.equals("1")) {
            HttpSession session = request.getSession();
            if (session != null) {
                session.invalidate();
            }
            result.put("msg", "OK");
            return result.toJSONString();
        }
        return result.toJSONString();
    }

}
