package com.recomsys.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
public class PageController {

    /*
     * RequestMapping作用 ： 提供路由信息，负责URL到Controller中的具体函数的映射
     */

    @RequestMapping("/bb")
    public String bb() {
        return "test";
    }


    @RequestMapping("/")
    public String homepage(Model m, HttpServletRequest httpServletRequest) {

        String label;
        HttpSession session = httpServletRequest.getSession();
        if (session.getAttribute("username") == null) {
            label = "null";
        }else{
            label = session.getAttribute("username").toString();
        }

        m.addAttribute("chooseuser", label);
        return "index";
    }


    @RequestMapping("/contact")
    public String contactpage(Model m, HttpServletRequest httpServletRequest) {
        String label;
        HttpSession session = httpServletRequest.getSession();
        if (session.getAttribute("username") == null) {
            label = "null";
        }else{
            label = session.getAttribute("username").toString();
        }

        m.addAttribute("chooseuser", label);
        return "contact";
    }


    @RequestMapping("/resume")
    public String resumepage(Model m, HttpServletRequest httpServletRequest){

        HttpSession session = httpServletRequest.getSession();
        if (session.getAttribute("username") == null) {
            return "redirect:/";

        }else{
            String label = session.getAttribute("username").toString();
            m.addAttribute("chooseuser", label);
            return "resume";
        }
    }


    @RequestMapping("/job-board")
    public String job_boradpage(Model m, HttpServletRequest httpServletRequest) {
        String label;
        HttpSession session = httpServletRequest.getSession();
        if (session.getAttribute("username") == null) {
            label = "null";
        }else{
            label = session.getAttribute("username").toString();
        }

        m.addAttribute("chooseuser", label);
        return "job-board";
    }


    @RequestMapping("/ability-evaluate")
    public String ablitypage(Model m, HttpServletRequest httpServletRequest) {
        String label;
        HttpSession session = httpServletRequest.getSession();
        if (session.getAttribute("username") == null) {
            label = "null";
        }else{
            label = session.getAttribute("username").toString();
        }

        m.addAttribute("chooseuser", label);
        return "ability_evaluate";
    }


    @RequestMapping("/register")
    public String UIRegister(HttpServletRequest request) {
        if (request.getSession().getAttribute("username") != null) {
            String referer = request.getHeader("referer");
            return "redirect:" + referer;
        }
        return "userregister";
    }

//    @RequestMapping("/uploadurl")
//    public String upload() {
//        return "upload";
//    }


    @RequestMapping("/administer")
    public String adminpage(HttpServletRequest request, Model m) {

        HttpSession session = request.getSession();

        if (session.getAttribute("username").toString().equals("root")) {  // root login
            // return root html
            String label = session.getAttribute("username").toString();
            m.addAttribute("chooseuser", label);
            return "administer";

        }

        String referer = request.getHeader("referer");
        return "redirect:" + referer;
    }


    /**
     * test
     * **/
    @RequestMapping("/frame")
    public String gg() {
        return "gg";
    }

    @RequestMapping("/ww")
    public String w() {
        return "ww";
    }

}
