package com.recomsys.demo.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class rootOpController {

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
}
