package com.qian.dao;

import com.qian.model.manager.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderDao {
    //查询订单
    public List<Order> query(Order order);
    //查询总数
    public Long countOrder(Order order);
    //退款
    public int refond(String orderNo);
    //发货
    public int deliver(String orderNo);
    //创建订单
    public String insert(Order order);
}
