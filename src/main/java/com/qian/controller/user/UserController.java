package com.qian.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/buyall")
public class UserController {

    @RequestMapping("/main") //接收所有方式的HTTP请求，GET/POST
    public ModelAndView login(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("");
        return mv; //请求转发到页面，访问一个名为hello的前端页面
    }

}
