package com.qian.controller.manager;

import com.alibaba.fastjson.JSONObject;
import com.qian.config.RedisUtil;
import com.qian.model.manager.Manager;
import com.qian.service.manager.IGoodsService;
import com.qian.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

//个人商店管理系统
@RestController
@RequestMapping("/qian")
public class PageCotroller extends BaseController{
    private Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IGoodsService managerService;
    //跳转至登录页
    @RequestMapping("/loginPage")
    public ModelAndView loginPage(){
        logger.info("进入登录页");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("business/b_login");
        return mv;
    }

    //登录检查
    @RequestMapping("/loginCheck")
    public JSONObject loginCheck(Manager manager){
        //MD5加密
        manager.setmPass(MD5Util.encode(manager.getmPass()));
        logger.info("管理员登录验证：" + (manager.getmName() + "," + manager.getmPass()));
        Manager m = managerService.loginCheck(manager);
        if(m == null){
            return resJson(0, "fail", null);
        }
        //添加到redis
        Integer res = redisUtil.set("m"+m.getId(), m.getmName());
        logger.info("添加redis缓存，状态码：" + res);

        return resJson(1, "success", m);
    }
}

