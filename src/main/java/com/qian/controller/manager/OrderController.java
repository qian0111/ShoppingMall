package com.qian.controller.manager;

import com.alibaba.fastjson.JSONObject;
import com.qian.model.manager.Order;
import com.qian.service.manager.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/ordermanage")
public class OrderController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(GoodsController.class);
    @Autowired
    private IOrderService orderService;
    //跳转至发货订单页
    @RequestMapping("deliveredOrderPage")
    public ModelAndView deliveredOrderPage(Integer pageNo, Integer pageCount, String orderNo){
        ModelAndView mv = new ModelAndView();
        logger.info("发货订单页     查询信息: (订单号：" + orderNo + ")");
        //当没有传页码时，默认第1页
        if(pageNo == null) {
            pageNo = 1;
            pageCount = 6;
        }

        //处理参数
        Order order = new Order();
        if (orderNo != null){
            order.setOrderNo(orderNo);
        }
        order.setOrderStatus(1);//已支付，待发货
        order.setPageNo(pageNo);
        order.setPageCount(pageCount);

        //查询发货订单列表
        List<Order> orderList = orderService.orderList(order);

        //将分页与列表信息传回前端
        mv.addObject("orderList", orderList);
        mv.addObject("pageNo", pageNo);
        mv.addObject("pageCount",pageCount);
        int totalCount = orderService.countOrder(order);
        mv.addObject("totalCount", totalCount);
        mv.addObject("orderNo", orderNo);

        mv.setViewName("business/order/b_deliveredOrder");
        return mv;
    }

    //退款
    @RequestMapping("refond")
    public JSONObject refond(String orderNo){
        //退款
        logger.info("退款     订单号：" + orderNo + ")");
        int row = orderService.refond(orderNo);
        return resJson(row,"success",null);
    }

    //发货
    @RequestMapping("deliver")
    public JSONObject deliver(String orderNo){
        //发货
        logger.info("发货     订单号：" + orderNo + ")");
        int row = orderService.deliver(orderNo);
        return resJson(row,"success",null);
    }

    //跳转至退款订单页
    @RequestMapping("refundOrderPage")
    public ModelAndView refundOrderPage(Integer pageNo, Integer pageCount, String orderNo){
        ModelAndView mv = new ModelAndView();
        logger.info("退款订单页     查询信息: (订单号：" + orderNo + ")");
        //当没有传页码时，默认第1页
        if(pageNo == null) {
            pageNo = 1;
            pageCount = 6;
        }

        //处理参数
        Order order = new Order();
        if (orderNo != null){
            order.setOrderNo(orderNo);
        }
        order.setOrderStatus(4);//申请退货退款
        order.setPageNo(pageNo);
        order.setPageCount(pageCount);

        //查询退款订单列表
        List<Order> orderList = orderService.orderList(order);

        //将分页与列表信息传回前端
        mv.addObject("orderList", orderList);
        mv.addObject("pageNo", pageNo);
        mv.addObject("pageCount",pageCount);
        int totalCount = orderService.countOrder(order);
        mv.addObject("totalCount", totalCount);
        mv.addObject("orderNo", orderNo);

        mv.setViewName("business/order/b_refundOrder");
        return mv;
    }

    //跳转至完成订单页
    @RequestMapping("finishOrderPage")
    public ModelAndView finishOrderPage(Integer pageNo, Integer pageCount, String orderNo, Integer orderStatus){
        ModelAndView mv = new ModelAndView();
        logger.info("完成订单页     查询信息: (订单号：" + orderNo + "，状态：" + orderStatus + ")");
        //当没有传页码时，默认第1页
        if(pageNo == null) {
            pageNo = 1;
            pageCount = 6;
        }

        //处理参数
        Order order = new Order();
        if (orderNo != null){
            order.setOrderNo(orderNo);
        }
        if(orderStatus != null){
            order.setOrderStatus(orderStatus);
        }
        if(order.getOrderStatus() == null){
            order.setOrderStatus(3);//3-已签收
            order.setOrderStatus2(5);//5-已退款
        }
        order.setPageNo(pageNo);
        order.setPageCount(pageCount);

        //查询完成订单列表
        List<Order> orderList = orderService.orderList(order);

        //将分页与列表信息传回前端
        mv.addObject("orderList", orderList);
        mv.addObject("pageNo", pageNo);
        mv.addObject("pageCount",pageCount);
        int totalCount = orderService.countOrder(order);
        mv.addObject("totalCount", totalCount);
        mv.addObject("orderNo", orderNo);
        mv.addObject("orderStatus", orderStatus);

        mv.setViewName("business/order/b_finishOrder");
        return mv;
    }
}
