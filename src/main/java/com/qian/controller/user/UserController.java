package com.qian.controller.user;

import com.qian.config.RedisUtil;
import com.qian.controller.manager.BaseController;
import com.qian.controller.manager.GoodsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/m/buy")
public class UserController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("/mainPage") //接收所有方式的HTTP请求，GET/POST
    public ModelAndView mainPage(Integer id){
        logger.info("主页面:id="+id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("userName", redisUtil.get("u"+id));
        mv.setViewName("customer/c_main");
        return mv; //请求转发到页面，访问一个名为hello的前端页面
    }

}
