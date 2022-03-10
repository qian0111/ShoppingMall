package com.qian.service.manager.impl;

import com.qian.dao.IOrderDao;
import com.qian.dao.IUserDao;
import com.qian.model.manager.Order;
import com.qian.service.manager.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private IOrderDao orderDao;

    @Autowired
    private IUserDao userDao;

    @Override
    public List<Order> orderList(Order order) {
        order.setPageNo((order.getPageNo()-1) * order.getPageCount());
        List<Order> orderList = orderDao.query(order);
        for(Order o : orderList){
            if(o.getOrderStatus() == 3){
                o.setStatusName("已签收");
            }
            if(o.getOrderStatus() == 5){
                o.setStatusName("已退款");
            }
        }
        return orderList;
    }

    @Override
    public int countOrder(Order order) {
        return orderDao.countOrder(order).intValue();
    }

    //开启事务: 1修改订单状态为已修改  2执行退款操作
    @Override
    @Transactional
    public int refond(String orderNo) {
        int flag =  orderDao.refond(orderNo);
        if(flag == 1){
            //获取订单信息
            Order o = new Order();
            o.setOrderNo(orderNo);
            Order order = orderDao.query(o).get(0);
            //根据订单的用户id获取用户余额
            BigDecimal money = userDao.getMoney(order.getuId());
            //退款
            BigDecimal upMoney = money.add(order.getPayMoney());
            order.setPayMoney(upMoney);
            int row = userDao.refond(order);
            if (row == 1){
                return row;
            }
            return 0;
        }
        return 0;
    }

    @Override
    public int deliver(String orderNo) {
        return orderDao.deliver(orderNo);
    }

    @Override
    public int deliverTask() {
        Order order = new Order();
        List<Order> orderList = orderDao.query(order);
        for(Order o : orderList){
            if(o.getOrderStatus() == 1){
                int row = deliver(o.getOrderNo());
                if(row == 0){
                    return 0;
                }
            }
        }
        return 1;
    }
}
