package com.recomsys.demo.web;

import com.alibaba.fastjson.JSONObject;

import com.recomsys.demo.ml.Rec;
import com.recomsys.demo.web.Entity.Question;
import com.recomsys.demo.web.Entity.Skill;
import com.recomsys.demo.web.Util.fileService;
import com.recomsys.demo.web.Util.searchUtil;
import com.recomsys.demo.web.Util.userService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class userOpController {

    /**
     * ABILITY AJAX URL
     * JOB BOARD AJAX URL
     * help users find some jobs
     * help users find some skills
     * input: json
     * return json
     * json format{
     *      msg: "",
     *      description: "",
     *      job_list: []
     * }
     * */

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

            if (skill.getState().equals("1")) {  // recommend job
                // Spark API Find Job

                try {

                    Rec rec = new Rec();
                    System.out.println(skill.getSkillset().toString());

                    List<String> res = rec.jobRec(skill.getSkillset());

                    System.out.println(res);

                    msg.put("msg", "success");
                    msg.put("job_list", res);

                } catch (Exception e) {
                    e.printStackTrace();
                    msg.put("msg", "error");
                    msg.put("descroption", e.toString());

                }
                return msg.toJSONString();


            } else if (skill.getState().equals("2")) {  // recommend skill

                // Spark API

                Rec rec = new Rec();

                try {

                    List<String> res = new ArrayList<>();

                    System.out.println(skill.getSkillset().toString());

                    if(skill.getWantjob().equals("")){
                        res = rec.skillRec(null, skill.getSkillset());

                    }else {

                        List<String> joblist = fileService.readJobList();
                        HashMap<Integer, String> hashMap = new HashMap<>();

                        joblist.forEach((x) -> {
                            hashMap.put(searchUtil.EditDistance(x, skill.getWantjob().trim()), x);
                        });

                        Set set = hashMap.keySet();
                        Object[] arr = set.toArray();
                        Arrays.sort(arr);

                        int cnt = 0;
                        for(Object key:arr){
                            List<String> temp = rec.skillRec(hashMap.get(key), skill.getSkillset());

                            res.addAll(temp);
                            cnt++;
                            if(cnt == 1)break;
                        }
                    }


                    System.out.println(res);

                    msg.put("msg", "success");
                    msg.put("job_list", res);

                } catch (Exception e) {
                    e.printStackTrace();
                    msg.put("msg", "error");
                    msg.put("job_list", e.toString());
                }

                return msg.toJSONString();

            }

            msg.put("msg", "null");
            return msg.toJSONString();
        }

    }

    /**
     * Contact AJAX URL
     * get users' problems
     * input: json
     * return json
     * json format{
     *     msg:"",
     *     description:""
     * }
     */

    @PostMapping("/feedback")
    @ResponseBody
    public String userProblem(@RequestBody Question question, HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        JSONObject msg = new JSONObject();

        if (session.getAttribute("username") != null) {
            userService.saveFeedback(question, session.getAttribute("username").toString());
            msg.put("msg", "success");
            msg.put("description", "We have received your feedback, Thanks");
        } else {
            msg.put("msg", "error");
            msg.put("description", "Login First Please");
        }

        return msg.toJSONString();
    }


}
