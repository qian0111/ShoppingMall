package com.qian.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.qian.config.RedisUtil;
import com.qian.controller.manager.BaseController;
import com.qian.controller.manager.GoodsController;
import com.qian.model.manager.Goods;
import com.qian.model.manager.Order;
import com.qian.service.manager.IGoodsService;
import com.qian.service.user.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/m/buy")
public class UserController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private IUserService userService;
    @Autowired
    private RedisUtil redisUtil;

    //主页
    @RequestMapping("/mainPage") //接收所有方式的HTTP请求，GET/POST
    public ModelAndView mainPage(Integer id){
        logger.info("主页面:id="+id);
        ModelAndView mv = new ModelAndView();
        //用户名
        mv.addObject("userName", redisUtil.get("u"+id));
        mv.addObject("uId", id);
        //目录树
        List<Goods> cate = goodsService.categoryTree();
        List<Goods> parentCate = goodsService.parentCategory();
        mv.addObject("parentCate",parentCate);
        mv.addObject("cate", cate);
        //热门推荐列表
        List<Goods> goodsList = redisUtil.getList();
        mv.addObject("goodsList", goodsList);
        mv.setViewName("customer/c_main");
        return mv; //请求转发到页面，访问一个名为hello的前端页面
    }

    //商品详情页
    @RequestMapping("/describePage") //接收所有方式的HTTP请求，GET/POST
    public ModelAndView describePage(Integer id, Integer uId){
        logger.info("商品详情页:id="+id);
        ModelAndView mv = new ModelAndView();
        //用户名
        mv.addObject("userName", redisUtil.get("u"+uId));
        mv.addObject("uId",uId);
        //根据id获取商品详情
        Goods goods = new Goods();
        goods.setId(id);
        Goods g = goodsService.goods(goods).get(0);
        mv.addObject("goods",g);
        mv.setViewName("customer/c_describe");
        return mv;
    }

    //商品详情页交换数据给商品支付页
    @RequestMapping("/payData")
    public JSONObject payData(Integer buyCount, Integer gId, Integer uId){
        return resJson(buyCount, gId, uId);
    }

    //商品支付页
    @RequestMapping("/payPage")
    public ModelAndView payPage(Integer buyCount, Integer gId, Integer uId){
        logger.info("商品支付页  用户：" + uId + ", 商品：" + gId);
        ModelAndView mv = new ModelAndView();
        //用户名
        mv.addObject("userName", redisUtil.get("u"+uId));
        mv.addObject("uId",uId);
        //获取订单详情
        Order order = userService.preOrder(buyCount, gId, uId);
        mv.addObject("order",order);
        mv.setViewName("customer/c_pay");
        return mv;
    }

    //支付弹窗
    @RequestMapping("/passPage")
    public ModelAndView passPage(){
        ModelAndView mv = new ModelAndView();
        logger.info("打开支付弹窗");
        mv.setViewName("customer/c_paywin");
        return mv;
    }

    //创建订单
    @RequestMapping("/generateOrder")
    public String generateOrder(Integer buyCount, Integer gId, Integer uId){
        JSONObject json = userService.generateOrder(buyCount, gId, uId);
        int res = json.getInteger("code");
        if(res == 0){
            return "0";
        }
        return json.getString("obj");
    }

    //支付
    @RequestMapping("/afterPay")
    public JSONObject afterPay(String userPass, String orderNo, Integer uId){
        JSONObject json = userService.afterPay(userPass, orderNo, uId);
        return json;
    }

    //订单页
    @RequestMapping("/orderPage")
    public ModelAndView orderPage(Integer uId){
        ModelAndView mv = new ModelAndView();
        //用户名
        mv.addObject("userName", redisUtil.get("u"+uId));
        mv.addObject("uId",uId);
        logger.info("订单页：");
        mv.setViewName("customer/c_order");
        return mv;
    }

}
