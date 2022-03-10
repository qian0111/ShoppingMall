package com.qian.service.manager;

import com.qian.model.manager.Order;

import java.util.List;

public interface IOrderService {
    //查询待发货列表
    public List<Order> orderList(Order order);
    //查询总数
    public int countOrder(Order order);
    //退款
    public int refond(String orderNo);
    //发货
    public int deliver(String orderNo);
    //定时任务发货
    public int deliverTask();

}
