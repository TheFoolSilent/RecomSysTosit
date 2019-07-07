package com.recomsys.demo.web;

import com.alibaba.fastjson.JSONObject;
import com.recomsys.demo.ml.Rec;
import com.recomsys.demo.web.Entity.FileOp;
import com.recomsys.demo.web.Util.fileService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * Root Operation Function
 * in Administer Page
 * */

@Controller
public class rootOpController {


    /**
     * Upload File Function
     * input file
     * return json
     * json format{
     *     msg:"",
     *     description:"",
     *     name_list:[],
     *     current:""
     * }
     * */
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

            List<String> list_name = fileService.getFileList();

            msg.put("name_list", list_name);
            msg.put("current", fileService.getChooseData());

            return msg.toJSONString();


        } else {
            msg.put("msg", "error");
            msg.put("description", "Login Overdue");
            // return error json
        }

        return msg.toJSONString();
    }


    /**
     * Operate File Function
     * 1. delete File
     * input json
     * return json
     * json format{
     *     msg:"",
     *     description:""
     *     name_list:[],
     *     current:""
     * }
     * */
    @RequestMapping("/operatefile")
    @ResponseBody
    public String adminFile(@RequestBody FileOp fileOp,
                            HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession();

//        System.out.println("11");

        JSONObject msg = new JSONObject();
        if (session.getAttribute("username") == null) {
//            System.out.println("12");
            msg.put("msg", "error");
            msg.put("description", "unknown error");

        } else if (session.getAttribute("username").toString().equals("root")) {  // root login

            if (fileOp.getState().equals("2")) {
                // File delete
                if (fileService.deleteFile(fileOp.getFilename())) {
                    msg.put("msg", "success");
                    msg.put("description", "delete Success");

                } else {
                    msg.put("msg", "exception");
                    msg.put("description", "file not found or delete error");
                }

                List<String> list_name = fileService.getFileList();

                msg.put("name_list", list_name);
                msg.put("current", fileService.getChooseData());
            } else {
//                System.out.println("13");
                msg.put("msg", "error");
                msg.put("description", "unknow error");

            }
            return msg.toJSONString();

        } else {
            msg.put("msg", "error");
            msg.put("description", "Login Overdue");
            // return error json
        }

        return msg.toJSONString();
    }


    /**
     * Training Model Function
     * input: json
     * return json
     * json format{
     *     msg:"",
     *     description:""
     * }
     * */
    @RequestMapping("/train")
    @ResponseBody
    public String trainmodle(@RequestBody FileOp fileOp, HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();

        JSONObject msg = new JSONObject();
        if (session.getAttribute("username") == null) {
            msg.put("msg", "error");
            msg.put("description", "unknown error");

        } else if (session.getAttribute("username").toString().equals("root")) {  // root login

            if (fileService.chooseFile(fileOp.getFilename())) {

                String choosedata = fileService.getChooseData();  // get chosen data name

                // Spark API

                Rec rec = new Rec();

                boolean flag = rec.chgTrainingData(fileService.path + choosedata);
                boolean flag2 = rec.writeJobList();

                if (flag && flag2) {
                    msg.put("msg", "success");
                    msg.put("description", "model generation success");

                } else {
                    msg.put("msg", "error");
                    msg.put("description", "Train Error");

                }

            } else {
                msg.put("msg", "error");
                msg.put("description", "Training File Error");
            }

        } else {
            msg.put("msg", "error");
            msg.put("description", "Login Overdue");
            // return error json
        }
        msg.put("current", fileService.getChooseData());

        return msg.toJSONString();
    }


    /**
     * Administer Page Initial
     * return file list
     * input: null
     * return: json
     * json format{
     *     msg:"",
     *     description:""
     *     name_list:[]
     * }
     * */
    @RequestMapping("/adinitial")
    @ResponseBody
    public String uploadFile(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();

        JSONObject msg = new JSONObject();
        if (session.getAttribute("username") == null) {
            msg.put("msg", "error");
            msg.put("description", "unknown error");

        } else if (session.getAttribute("username").toString().equals("root")) {  // root login
            msg.put("msg", "success");
            msg.put("description", "delete Success");
            List<String> list_name = fileService.getFileList();

            msg.put("name_list", list_name);
        }else{
            msg.put("msg", "error");
            msg.put("description", "Login Overdue");
        }
        msg.put("current", fileService.getChooseData());
        return msg.toJSONString();
    }
}
