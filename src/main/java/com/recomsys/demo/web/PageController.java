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
    @RequestMapping("/")
    public String hello(Model m, HttpServletRequest httpServletRequest) {

        String label;
        HttpSession session = httpServletRequest.getSession();
        if (session.getAttribute("username") == null) {
            label = "<a class='modal-view button' href='#' data-toggle"+
                    "='modal' data-target='#productModal'>Login</a>";
        }else{
            label = "<label> " + session.getAttribute("username").toString() +" </label>";
        }

        m.addAttribute("chooseuser", label);
        return "index";
    }


    @RequestMapping("/contact")
    public String hello1(Model m, HttpServletRequest httpServletRequest) {
        String label;
        HttpSession session = httpServletRequest.getSession();
        if (session.getAttribute("username") == null) {
            label = "<a class='modal-view button' href='#' data-toggle"+
                    "='modal' data-target='#productModal'>Login</a>";
        }else{
            label = "<label> " + session.getAttribute("username").toString() +" </label>";
        }

        m.addAttribute("chooseuser", label);
        return "contact";
    }


    @RequestMapping("/resume")
    public String hello2(Model m, HttpServletRequest httpServletRequest){

        HttpSession session = httpServletRequest.getSession();
        if (session.getAttribute("username") == null) {
            return "redirect:/";

        }else{
            String label = "<label> " + session.getAttribute("username").toString() +" </label>";
            m.addAttribute("chooseuser", label);
            return "resume";
        }
    }


    @RequestMapping("/job-board")
    public String hello3(Model m, HttpServletRequest httpServletRequest) {
        String label;
        HttpSession session = httpServletRequest.getSession();
        if (session.getAttribute("username") == null) {
            label = "<a class='modal-view button' href='#' data-toggle"+
                    "='modal' data-target='#productModal'>Login</a>";
        }else{
            label = "<label> " + session.getAttribute("username").toString() +" </label>";
        }

        m.addAttribute("chooseuser", label);
        return "job-board";
    }


    @RequestMapping("/ability-evaluate")
    public String hello5(Model m, HttpServletRequest httpServletRequest) {
        String label;
        HttpSession session = httpServletRequest.getSession();
        if (session.getAttribute("username") == null) {
            label = "<a class='modal-view button' href='#' data-toggle"+
                    "='modal' data-target='#productModal'>Login</a>";
        }else{
            label = "<label> " + session.getAttribute("username").toString() +" </label>";
        }

        m.addAttribute("chooseuser", label);
        return "ability_evaluate";
    }


    @RequestMapping("/uploadurl")
    public String upload() {
        return "upload";
    }


    @RequestMapping("/administer")
    public String adminpage(HttpServletRequest request, Model m) {

        HttpSession session = request.getSession();

        if (session.getAttribute("username").toString().equals("root")) {  // root login
            // return root html
            String label = "<label> " + session.getAttribute("username").toString() +" </label>";
            m.addAttribute("chooseuser", label);
            return "administer";

        }

        String referer = request.getHeader("referer");
        return "redirect:" + referer;
    }


}
