package com.qian.service.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.qian.config.RedisUtil;
import com.qian.controller.manager.GoodsController;
import com.qian.dao.*;
import com.qian.model.manager.Goods;
import com.qian.model.manager.Order;
import com.qian.model.user.Cart;
import com.qian.model.user.Trade;
import com.qian.model.user.User;
import com.qian.service.user.IUserService;
import com.qian.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class UserService implements IUserService {
    private Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private IUserDao userDao;
    @Autowired
    private IGoodsDao goodsDao;
    @Autowired
    private IOrderDao orderDao;
    @Autowired
    private ITradeDao tradeDao;
    @Autowired
    private ICartDao cartDao;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public User loginCheck(User user) {
        List<User> users = userDao.query(user);
        if(users == null && users.size()==0){
            return null;
        }
        return users.get(0);
    }

    @Override
    public int register(User user) {
        return userDao.add(user);
    }

    @Override
    public void recommend() {
        Goods goods =new Goods();
        goods.setgStatus(1);
        List<Goods> goodsList = goodsDao.query(goods);
        int count = goodsDao.countGoods(goods).intValue();
        //随机生成6个热门推荐商品
        Random random = new Random();
        List<Goods> recGoods = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        for(int i=0; i<6; i++){
            int r = random.nextInt(count);
            //若随机数重复则重新生成
            while(list.contains(r)){
                r = random.nextInt(count);
            }
            list.add(r);
            recGoods.add(goodsList.get(r));
        }
        //添加到redis
        logger.info("添加热门商品redis缓存:"+list.toString());
        redisUtil.set(recGoods);
    }

    @Override
    public Order preOrder(Integer buyCount, Integer gId, Integer uId) {
        Goods g = new Goods();
        g.setId(gId);
        Goods goods = goodsDao.query(g).get(0);
        //获得订单的所有信息
        Order order = new Order();
        order.setuId(uId);
        order.setgId(gId);
        order.setgName(goods.getgName());
        order.setgImage(goods.getgImage());
        order.setgPrice(goods.getgPrice());
        order.setBuyCount(buyCount);
        //计算商品总价
        BigDecimal price = goods.getgPrice().multiply(BigDecimal.valueOf(buyCount));
        order.setPayMoney(price);
        return order;
    }

    /*
    开启事务
    下订单：
    1验证库存是否充足
    2充足则扣减库存
    3生成订单号 日期+MD5(uId)前五位
    4创建订单，状态码为0（未支付）
     */
    @Override
    @Transactional
    public JSONObject generateOrder(Integer buyCount, Integer gId, Integer uId) {
        //获得商品信息
        Goods g = new Goods();
        g.setId(gId);
        Goods goods = goodsDao.query(g).get(0);
        //获得订单信息
        Order order = preOrder(buyCount, gId, uId);
        //验证库存是否充足
        if(goods.getgCount() < order.getBuyCount()){
            logger.info("库存不足，创建订单失败");
            return resJson(0,"库存不足",null);
        }
        //扣减库存
        goods.setgCount(goods.getgCount()-order.getBuyCount());
        int row = goodsDao.subCount(goods);
        //生成订单号
        SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmsss");//设置日期格式
        String date = df.format(new Date());
        String suffixMD5 = MD5Util.encode(uId.toString()+gId.toString());
        String REGEX = "[^(0-9)]";
        String suffix = Pattern.compile(REGEX).matcher(suffixMD5).replaceAll("").trim().substring(0,5);
        String orderNo = date + suffix;
        order.setOrderNo(orderNo);
        order.setOrderStatus(0);//0-创建订单 未支付
       //创建订单
        int no = orderDao.insert(order);
        return resJson(no,"订单创建成功",orderNo);
    }

    /*
    开启事务
    支付
    1验证密码
    2验证余额
    3扣减余额
    4修改状态 1-已支付待发货
    5生成交易流水 1-支付
     */
    @Override
    @Transactional
    public JSONObject afterPay(String userPass, String orderNo, Integer uId) {
        //验证支付密码
        userPass = MD5Util.encode(userPass).toUpperCase(Locale.ROOT);
        User u = new User();
        u.setId(uId);
        User user = userDao.query(u).get(0);
        System.out.println();
        if(!user.getUserPass().equals(userPass)){
            logger.info("密码错误");
            return resJson(0,"支付密码错误",orderNo);
        }
        //验证余额
        Order o = new Order();
        o.setOrderNo(orderNo);
        Order order = orderDao.query(o).get(0);
        if(user.getMoney().compareTo(order.getPayMoney()) == -1){
            logger.info("余额不足");
            return resJson(2,"余额不足",orderNo);
        }
        //扣减余额
        user.setMoney(user.getMoney().subtract(order.getPayMoney()));
        int row = userDao.subMoney(user);
        if(row != 0){
            logger.info("余额已扣减");
        }
        //修改状态 1-已支付待发货
        Timestamp updateTime = new Timestamp(System.currentTimeMillis());
        order.setUpdateTime(updateTime);
        int row2 = orderDao.afterPay(order);
        if(row2 != 0){
            logger.info("状态已修改：1-已支付未发货");
        }
        //生成交易流水
        Trade trade = new Trade();
        trade.setUId(uId);
        trade.setTradeType(1);
        trade.setTradeMoney(order.getPayMoney());
        trade.setOrderNo(orderNo);
        int row3 = tradeDao.insert(trade);
        return resJson(1,"下单成功",null);
    }

    @Override
    public List<Order> orderList(Order order) {
        return null;
    }

    @Override
    public JSONObject upRefund(String orderNo) {
        int row = orderDao.upRefond(orderNo);
        if(row == 1){
            logger.info("申请退款成功：" + orderNo);
            return resJson(row,"申请成功",null);
        }
        logger.info("申请退款失败：" + orderNo);
        return resJson(row,"申请失败",null);
    }

    @Override
    public JSONObject recieved(String orderNo) {
        int row = orderDao.recieved(orderNo);
        if(row == 1){
            logger.info("签收成功：" + orderNo);
            return resJson(row,"签收成功",null);
        }
        logger.info("签收失败：" + orderNo);
        return resJson(row,"签收失败",null);
    }

    @Override
    public List<Cart> cart(Cart cart) {
        List<Cart> cartList = cartDao.query(cart);
        for(Cart c : cartList){
            Goods g = new Goods();
            g.setId(c.getGId());
            Goods goods = goodsDao.query(g).get(0);
            c.setGImage(goods.getgImage());
            c.setGName(goods.getgName());
            c.setGPrice(goods.getgPrice());
            c.setTotalPrice(goods.getgPrice().multiply(BigDecimal.valueOf(c.getBuyCount())));
        }
        return cartList;
    }

    @Override
    public int addCart(Cart cart) {
        int count = cartDao.count(cart).intValue();
        if(count == 0){
            return cartDao.insert(cart);
        }
        return cartDao.update(cart);
    }

    @Override
    public int delCart(Cart cart) {
        return cartDao.delete(cart);
    }

    /*
    开启事务
    1 更新账户余额
    2 生成交易流水
     */
    @Override
    @Transactional
    public int recharge(Integer uId, BigDecimal money) {
        User u = new User();
        u.setId(uId);
        User user = userDao.query(u).get(0);
        user.setMoney(user.getMoney().add(money));
        int row = userDao.subMoney(user);//更新账户余额
        if(row != 0){
            logger.info("充值成功");
            Trade trade = new Trade();
            trade.setUId(uId);
            trade.setTradeMoney(money);
            trade.setTradeType(2);
            int row2 = tradeDao.insert(trade);//生成交易流水
            if(row2 != 0){
                logger.info("生成交易流水");
                return 1;
            }
            return 0;
        }
        return 0;
    }

    public JSONObject resJson(Integer code, String msg, Object obj){
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        json.put("obj", obj);
        logger.info(json.toJSONString());
        return json;
    }

}
