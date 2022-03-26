package com.qian.dao;

import com.qian.model.manager.Order;
import com.qian.model.user.User;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface IUserDao {
    //获得用户money
    public BigDecimal getMoney(Integer uId);
    //退款操作
    public int refond(Order order);
    //查询用户信息
    public List<User> query(User user);
    //添加新用户
    public int add(User user);
    //改动余额
    public int subMoney(User user);
}
