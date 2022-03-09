package com.qian.service.manager.impl;

import com.qian.dao.manager.IOrderDao;
import com.qian.model.manager.Order;
import com.qian.service.manager.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private IOrderDao orderDao;

    @Override
    public List<Order> orderList(Order order) {
        order.setPageNo((order.getPageNo()-1) * order.getPageCount());
        return orderDao.query(order);
    }

    @Override
    public int countOrder(Order order) {
        return orderDao.countOrder(order).intValue();
    }

    @Override
    public int refond(String orderNo) {
        return orderDao.refond(orderNo);
    }
}
