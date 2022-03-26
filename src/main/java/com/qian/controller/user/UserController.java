package com.qian.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.qian.config.RedisUtil;
import com.qian.controller.manager.BaseController;
import com.qian.controller.manager.GoodsController;
import com.qian.model.manager.Goods;
import com.qian.model.manager.Order;
import com.qian.model.user.Cart;
import com.qian.model.user.User;
import com.qian.service.manager.IGoodsService;
import com.qian.service.manager.IOrderService;
import com.qian.service.user.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
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
    private IOrderService orderService;
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
    public ModelAndView orderPage(Integer uId, String gName, Integer pageNo, Integer pageCount){
        ModelAndView mv = new ModelAndView();
        //用户名
        mv.addObject("userName", redisUtil.get("u"+uId));
        mv.addObject("uId",uId);
        //获取订单列表
        Order order = new Order();
        if(gName != null){
            order.setgName(gName);
        }
        if(pageNo == null) {
            //当没有传页码时，默认第1页
            pageNo = 1;
            pageCount = 5;
        }
        order.setuId(uId);
        order.setPageNo(pageNo);
        order.setPageCount(pageCount);
        List<Order> orderList = orderService.orderList(order);
        mv.addObject("orderList",orderList);
        mv.addObject("gName", gName);
        int totalCount = orderService.countOrder(order);
        mv.addObject("totalCount", totalCount);
        mv.addObject("pageNo", pageNo);
        mv.addObject("pageCount",pageCount);
        mv.setViewName("customer/c_order");

        logger.info("订单页：" + uId);
        return mv;
    }

    //申请退款
    @RequestMapping("/refundApply")
    public JSONObject refundApply(String orderNo){
        logger.info("申请退款：" + orderNo);
        return userService.upRefund(orderNo);
    }

    //签收
    @RequestMapping("/recieved")
    public JSONObject recieved(String orderNo){
        logger.info("签收：" + orderNo);
        return userService.recieved(orderNo);
    }

    //搜索页
    @RequestMapping("/searchPage")
    public ModelAndView searchPage(Integer uId, String gName, Integer pageNo,
            Integer pageCount, Integer cId, Integer parentId){
        ModelAndView mv = new ModelAndView();
        //用户名
        mv.addObject("userName", redisUtil.get("u"+uId));
        mv.addObject("uId",uId);
        //获取商品列表
        Goods goods = new Goods();
        if(gName != null){
            goods.setgName(gName);
        }
        if(cId != null){
            goods.setcId(cId);
        }
        if (parentId != null){
            goods.setParentId(parentId);
        }
        if(pageNo == null) {
            //当没有传页码时，默认第1页
            pageNo = 1;
            pageCount = 8;
        }
        goods.setPageNo(pageNo);
        goods.setPageCount(pageCount);
        List<Goods> goodsList = goodsService.goodsList(goods);
        mv.addObject("goodsList",goodsList);
        mv.addObject("gName", gName);
        int totalCount = goodsService.countGoods(goods);
        mv.addObject("totalCount", totalCount);
        mv.addObject("pageNo", pageNo);
        mv.addObject("pageCount",pageCount);
        mv.setViewName("customer/c_search");

        logger.info("搜索页：" + uId + ", 搜索内容：" + gName);
        return mv;
    }

    //加入购物车
    @RequestMapping("/addCart")
    public JSONObject cartPage(Integer uId, Integer gId, Integer buyCount){
        ModelAndView mv = new ModelAndView();
        //添加加购信息信息
        Cart cart = new Cart();
        cart.setGId(gId);
        cart.setUId(uId);
        cart.setBuyCount(buyCount);
        //加购
        int row = userService.addCart(cart);
        if(row != 0){
            logger.info("加入购物车  用户：" + uId + "，商品：" + gId + "，数量：" + buyCount);
            return resJson(1,"商品已加入购物车", null);
        }
        logger.info("加入购物车失败");
        return resJson(0,"加入购物车失败", null);
    }

    //购物车
    @RequestMapping("/cartPage")
    public ModelAndView cartPage(Integer uId){
        ModelAndView mv = new ModelAndView();
        //用户名
        mv.addObject("userName", redisUtil.get("u"+uId));
        mv.addObject("uId",uId);
        //获取购物车清单
        Cart cart = new Cart();
        cart.setUId(uId);
        List<Cart> cartList = userService.cart(cart);
        mv.addObject("cartList", cartList);
        mv.setViewName("customer/c_cart");
        logger.info("购物车：" + uId);
        return mv;
    }

    //从购物车中删除
    @RequestMapping("/delCart")
    public JSONObject delCart(Integer uId, Integer gId){
        Cart cart = new Cart();
        cart.setUId(uId);
        cart.setGId(gId);
        int row = userService.delCart(cart);
        if(row != 0){
            logger.info("从购物车中删除  用户：" + uId + "，商品：" + gId);
            return resJson(1,"删除成功", null);
        }
        logger.info("删除购物车商品失败 用户：" + uId + "，商品：" + gId);
        return resJson(0,"删除失败", null);
    }

    //个人中心
    @RequestMapping("/infoPage")
    public ModelAndView infoPage(Integer uId){
        ModelAndView mv = new ModelAndView();
        //用户名
        mv.addObject("userName", redisUtil.get("u"+uId));
        mv.addObject("uId",uId);

        mv.setViewName("customer/c_info");
        logger.info("个人中心：" + uId);
        return mv;
    }

    //用户信息
    @RequestMapping("/detailPage")
    public ModelAndView detailPage(Integer uId){
        ModelAndView mv = new ModelAndView();
        //用户名
        mv.addObject("userName", redisUtil.get("u"+uId));
        mv.addObject("uId",uId);
        //用户信息
        User u = new User();
        u.setId(uId);
        User user = userService.loginCheck(u);
        mv.addObject("user",user);
        mv.setViewName("customer/c_details");
        logger.info("个人信息：" + uId);
        return mv;
    }

    //充值页
    @RequestMapping("/moneyPage")
    public ModelAndView moneyPage(Integer uId){
        ModelAndView mv = new ModelAndView();
        //用户名
        mv.addObject("userName", redisUtil.get("u"+uId));
        mv.addObject("uId",uId);
        //用户信息
        User u = new User();
        u.setId(uId);
        User user = userService.loginCheck(u);
        mv.addObject("user",user);
        mv.setViewName("customer/c_moeny");
        logger.info("充值页：" + uId);
        return mv;
    }

    //充值弹窗
    @RequestMapping("/rechargePage")
    public ModelAndView rechargePage(Integer uId){
        ModelAndView mv = new ModelAndView();
        //用户名
        mv.addObject("userName", redisUtil.get("u"+uId));
        mv.addObject("uId",uId);
        //用户信息
        User u = new User();
        u.setId(uId);
        User user = userService.loginCheck(u);
        mv.addObject("user",user);
        mv.setViewName("customer/c_recharge");
        logger.info("充值弹窗：" + uId);
        return mv;
    }

    //充值
    @RequestMapping("/recharge")
    public JSONObject recharge(Integer uId, BigDecimal money){
        int row = userService.recharge(uId, money);
        if(row != 0){
            logger.info("充值  用户：" + uId + "，金额：" + money);
            return resJson(1,"充值成功", null);
        }
        logger.info("充值  用户：" + uId + "，金额：" + money);
        return resJson(0,"充值失败", null);
    }

}
