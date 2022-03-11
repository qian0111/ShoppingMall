package com.qian.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.qian.config.RedisUtil;
import com.qian.controller.manager.BaseController;
import com.qian.controller.manager.GoodsController;
import com.qian.model.user.User;
import com.qian.service.manager.IGoodsService;
import com.qian.service.user.IUserService;
import com.qian.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

//个人商店管理系统
@RestController
@RequestMapping("/u")
public class BeginCotroller extends BaseController {
    private Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IGoodsService managerService;
    @Autowired
    private IUserService userService;
    //跳转至登录页
    @RequestMapping("/loginPage")
    public ModelAndView loginPage(){
        logger.info("进入登录页");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("customer/c_login");
        return mv;
    }

    //登录检查
    @RequestMapping("/loginCheck")
    public JSONObject loginCheck(User user){
        //MD5加密
        user.setUserPass(MD5Util.encode(user.getUserPass()));
        logger.info("用户登录验证：手机号：" + (user.getUserPhone() + ",用户名：" +user.getUserName() + ",密码" + user.getUserPass()));
        User u = userService.loginCheck(user);
        if(u == null){
            return resJson(0, "fail", null);
        }
        //添加到redis
        Integer res = redisUtil.set("u"+u.getId(), u.getUserName());
        logger.info("添加redis缓存，状态码：" + res);
        userService.recommend();
        return resJson(1, "success", u);
    }

    //跳转至注册页
    @RequestMapping("/registerPage")
    public ModelAndView registerPage(){
        logger.info("进入注册页");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("customer/c_register");
        return mv;
    }

    //注册
    @RequestMapping("/register") //接收所有方式的HTTP请求，GET/POST
    public JSONObject register(User user){
        //获取请求中的参数
        user.setUserPass(MD5Util.encode(user.getUserPass()));
        //添加新用户
        int id = userService.register(user);
        logger.info("新用户注册" + id + ",  用户名：" + user.getUserName() +
                "手机号：" + user.getUserPhone() + "密码：" + user.getUserPass());
        //添加到redis
        Integer res = redisUtil.set("u"+id, user.getUserName());
        logger.info("添加redis缓存，状态码：" + res);
        userService.recommend();
        return resJson(1, "success", id);

    }
}

