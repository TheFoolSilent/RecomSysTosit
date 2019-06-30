package com.recomsys.demo.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    /*
     * RequestMapping作用 ： 提供路由信息，负责URL到Controller中的具体函数的映射
     */
    @RequestMapping("/")
    public String hello() {
        return "Hello World";
    }
}
