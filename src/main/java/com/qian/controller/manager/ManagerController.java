package com.qian.controller.manager;

import com.qian.config.RedisUtil;
import com.qian.service.manager.IGoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

//个人商店管理系统
@RestController
@RequestMapping("/m/manage")
public class ManagerController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IGoodsService managerService;

    //主页面
    @RequestMapping("/mainPage")
    public ModelAndView mainPage(Integer id){
        logger.info("主页面:id="+id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("mName", redisUtil.get("m"+id));
        mv.setViewName("business/b_main");
        return mv;
    }



}
