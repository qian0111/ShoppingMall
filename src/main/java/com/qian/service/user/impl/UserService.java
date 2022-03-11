package com.qian.service.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.qian.config.RedisUtil;
import com.qian.controller.manager.GoodsController;
import com.qian.dao.IGoodsDao;
import com.qian.dao.IOrderDao;
import com.qian.dao.IUserDao;
import com.qian.model.manager.Goods;
import com.qian.model.manager.Order;
import com.qian.model.user.User;
import com.qian.service.user.IUserService;
import com.qian.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
        order.setPrice(price);
        return order;
    }


    @Override
    @Transactional
    /*
    开启事务
    下订单：
    1验证库存是否充足
    2充足则扣减库存
    3生成订单号 日期+MD5(uId)前五位
    4创建订单，状态码为0（未支付）
     */
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
        if(row == 0){
            logger.info("数据库未执行");
            return resJson(0,"数据库未执行",null);
        }
        //生成订单号
        SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmsss");//设置日期格式
        String date = df.format(new Date());
        String suffix = MD5Util.encode(uId.toString()).substring(5);
        String orderNo = date + suffix;
        order.setOrderNo(orderNo);
        order.setOrderStatus(0);//0-创建订单 未支付
       //创建订单
        String no = orderDao.insert(order);
        return resJson(1,"创建成功",no);
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
