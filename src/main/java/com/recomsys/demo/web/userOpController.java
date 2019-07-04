package com.recomsys.demo.web;

import com.alibaba.fastjson.JSONObject;
import com.recomsys.demo.ml.JobRec;
import com.recomsys.demo.ml.SkillRec;
import com.recomsys.demo.web.Entity.Skill;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

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

            if(skill.getState().equals("1")){  // recommend job
                // TODO Spark API Find Job

                String[] master_skill = skill.getSkillset().
                        toArray(new String[skill.getSkillset().size()]);

                try{
                    JobRec jobrec = new JobRec();
                    List<String> res = jobrec.jobRecs(master_skill);
//                    System.out.println(res);
                    msg.put("msg", "success");
                    msg.put("job_list", res);

                }catch (Exception e) {
                    e.printStackTrace();
                    msg.put("msg", "error");
                    msg.put("descroption", e.toString());

                }
                return msg.toJSONString();


            }else if (skill.getState().equals("2")){  // recommend skill

                String[] master_skill = skill.getSkillset().
                        toArray(new String[skill.getSkillset().size()]);

                    // TODO Spark API

                    SkillRec jobrec = new SkillRec();
                    try {
                        List<String> res = jobrec.skillRec(skill.getWantjob(), master_skill);
//                        System.out.println(res);
                        msg.put("msg", "success");
                        msg.put("job_list", res);

                    }catch (Exception e){
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

}
