package com.qian.controller.manager;

import com.qian.model.manager.Manager;
import com.qian.service.manager.IManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/buyallmanage")//个人商店管理系统
public class ManagerController {

    @Autowired
    private IManagerService managerService;

    @RequestMapping("/loginPage")
    public ModelAndView loginPage(){//跳转至登录页
        ModelAndView mv = new ModelAndView();
        mv.setViewName("business/b_login");
        return mv; //请求转发到页面，访问一个名为hello的前端页面
    }

    @RequestMapping("/loginCheck")
    public String loginCheck(Manager manager){//登录检查

        return "1";
    }

    @RequestMapping("/mainPage")
    public ModelAndView mainPage(){//跳转至登录页
        ModelAndView mv = new ModelAndView();
        mv.setViewName("business/b_login");
        return mv; //请求转发到页面，访问一个名为hello的前端页面
    }

}
