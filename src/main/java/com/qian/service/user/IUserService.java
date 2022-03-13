package com.qian.service.user;

import com.alibaba.fastjson.JSONObject;
import com.qian.model.manager.Goods;
import com.qian.model.manager.Order;
import com.qian.model.user.User;

import java.util.List;

public interface IUserService {
    //查询用户
    public User loginCheck(User user);
    //添加新用户
    public int register(User user);
    //生成并缓存热门推荐
    public void recommend();
    //订单信息预览
    public Order preOrder(Integer buyCount, Integer gId, Integer uId);
    //创建订单
    public JSONObject generateOrder(Integer buyCount, Integer gId, Integer uId);
    //支付
    public JSONObject afterPay(String userPass, String orderNo, Integer uId);

}
