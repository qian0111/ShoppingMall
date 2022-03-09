package com.qian.controller.manager;

import com.alibaba.fastjson.JSONObject;
import com.qian.model.manager.Order;
import com.qian.service.manager.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/ordermanage")
public class OrderController extends BaseController{
    @Autowired
    private IOrderService orderService;
    //跳转至订单页
    @RequestMapping("deliveredOrderPage")
    public ModelAndView deliveredOrderPage(Integer pageNo, Integer pageCount, String orderNo){
        ModelAndView mv = new ModelAndView();
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
        int row = orderService.refond(orderNo);
        return resJson(row,"success",null);
    }
}
