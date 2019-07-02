package com.recomsys.demo.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        return "index";
    }

    @RequestMapping("/contact")
    public String hello1() {
        return "contact";
    }

    @RequestMapping("/resume")
    public String hello2() {
        return "resume";
    }

    @RequestMapping("/job-board")
    public String hello3() {
        return "job-board";
    }

    @RequestMapping("/administer")
    public String hello4() {
        return "administer";
    }

    @RequestMapping("/ability-evaluate")
    public String hello5() {
        return "ability_evaluate";
    }


    @RequestMapping("/uploadurl")
    public String upload() {
        return "upload";
    }


    @RequestMapping("/admin")
    public String admin(HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession();

        JSONObject msg = new JSONObject();

        if (session.getAttribute("username").toString().equals("root")) {  // root login
            msg.put("msg", "2");
            msg.put("description", "Welcome, Administrator!");
            // return root html

        } else {
            msg.put("msg", "error");
            msg.put("description", "Not Found");
            // return error json
        }

        return msg.toJSONString();
    }


    @RequestMapping("/uploadfile")
    @ResponseBody
    public String uploadFile(@RequestParam(value = "file") MultipartFile file,
                             HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession();

        JSONObject msg = new JSONObject();
        if (session.getAttribute("username") == null) {
            msg.put("msg", "error");
            msg.put("description", "unknown error");

        } else if (session.getAttribute("username").toString().equals("root")) {  // root login

            // File Save
            if (fileService.saveFile(file)) {
                msg.put("msg", "success");
                msg.put("description", "Upload Success");

            } else {
                msg.put("msg", "exception");
                msg.put("description", "Save file error");
            }

            List<String> list_name = fileService.getFillList();

            msg.put("name_list", list_name);

            return msg.toJSONString();


        } else {
            msg.put("msg", "error");
            msg.put("description", "Login Overdue");
            // return error json
        }

        return msg.toJSONString();
    }


    @RequestMapping("/operatefile")
    @ResponseBody
    public String adminFile(@RequestParam(value = "filename") String filename,
                            @RequestParam(value = "state") String state,
                            HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession();

        JSONObject msg = new JSONObject();
        if (session.getAttribute("username") == null) {
            msg.put("msg", "error");
            msg.put("description", "unknown error");

        } else if (session.getAttribute("username").toString().equals("root")) {  // root login

            if(state == "2"){
                // File delete
                if (fileService.deleteFile(filename)) {
                    msg.put("msg", "success");
                    msg.put("description", "delete Success");

                } else {
                    msg.put("msg", "exception");
                    msg.put("description", "file not found or delete error");
                }

                List<String> list_name = fileService.getFillList();

                msg.put("name_list", list_name);

            }else if(state == "1"){  // choose training file
                if (fileService.chooseFile(filename)) {
                    msg.put("msg", "success");
                    msg.put("description", "choose Success");

                } else {
                    msg.put("msg", "exception");
                    msg.put("description", "file not found or choose error");
                }
            }

            return msg.toJSONString();


        } else {
            msg.put("msg", "error");
            msg.put("description", "Login Overdue");
            // return error json
        }

        return msg.toJSONString();
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String userLogin(@RequestBody User st, HttpServletRequest request) {

        JSONObject result = new JSONObject();

        try {
            User user = userService.login(st);  // login
            if (user != null) {

                request.getSession().setAttribute("username", user.getUsername());  //将用户信息放到session中
                request.getSession().setAttribute("password", user.getPassword());  //将用户信息放到session中
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
        } catch (Exception e) {

            JSONObject errresult = new JSONObject();
            errresult.put("msg", "3");
            errresult.put("description", e.toString());
            e.printStackTrace();
            return errresult.toJSONString();
        }
        return result.toJSONString();
    }


    @RequestMapping("/register")
    public String UIRegister() {
        return "userregister";
    }


    @PostMapping("/registerraise")
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
        }

        sendback.put("msg", result);
        return sendback.toJSONString();
    }


    @PostMapping("/findjob")
    @ResponseBody
    public String userRegister(@RequestBody Skill skill, HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        JSONObject msg = new JSONObject();

        if (session.getAttribute("username") == null) {
            msg.put("msg", "error");
            msg.put("description", "you are not login, please login first");
            return msg.toJSONString();  // return error json

        } else {
            if (skill.getWantjob().equals("")) {
                // TODO
                // Spark API
                // no want job


            } else {
                // TODO
                // Spark API
                // want job

            }


        }

        return msg.toJSONString();

    }


}
