package com.recomsys.demo.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public class Controller {
    @RestController
    public class HelloController {

        @RequestMapping("/hello")
        /**
         * RequestMapping作用 ： 提供路由信息，负责URL到Controller中的具体函数的映射
         */
        public String hello() {
            return "Hello World";
        }
    }
}
