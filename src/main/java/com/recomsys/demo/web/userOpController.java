package com.recomsys.demo.web;

import com.alibaba.fastjson.JSONObject;
import com.recomsys.demo.web.Entity.Skill;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class userOpController {

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
