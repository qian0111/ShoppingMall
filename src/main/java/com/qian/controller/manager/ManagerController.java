package com.qian.controller.manager;

import com.qian.model.manager.Goods;
import com.qian.model.manager.Manager;
import com.qian.service.manager.IManagerService;
import com.qian.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/buyallmanage")//个人商店管理系统
public class ManagerController {

    @Autowired
    private IManagerService managerService;

    @RequestMapping("/loginPage")
    public ModelAndView loginPage(){//跳转至登录页
        ModelAndView mv = new ModelAndView();
        mv.setViewName("business/b_login");
        return mv;
    }

    @RequestMapping("/loginCheck")
    public String loginCheck(Manager manager){//登录检查
        //MD5加密
        manager.setmPass(MD5Util.encode(manager.getmPass()));
        //判断登录资格
        boolean y = managerService.loginCheck(manager);
        if (y){
            return "1";
        }
        return "0";
    }

    @RequestMapping("/mainPage")
    public ModelAndView mainPage(String mName){//跳转至登录页
        ModelAndView mv = new ModelAndView();
        mv.addObject("mName", mName);
        mv.setViewName("business/b_main");
        return mv; //请求转发到页面，访问一个名为hello的前端页面
    }

    @RequestMapping("/onGoodsListPage")
    public ModelAndView onGoodsListPage(Integer pageNo, Integer pageCount, String gName,
                                        Integer firstCategory, Integer secondCategory){//跳转至商品列表页
        ModelAndView mv = new ModelAndView();
        //当没有传页码时，默认第1页
        if(pageNo == null) {
            pageNo = 1;
            pageCount = 6;
        }
        //处理参数
        Goods goods = new Goods();
        if (gName != null) {
            goods.setgName(gName);
        }
        goods.setgStatus(1);
        goods.setPageNo(pageNo);
        goods.setPageCount(pageCount);
        System.out.println("************************************\n"+goods);
        //查询商品列表
        List<Goods> goodsList = managerService.goodsList(goods);
        // 查询一级分类
        List<Goods> firstCategories = managerService.firstCategory();
        //查询分类树
        List<Goods> categoryTree = managerService.categoryTree();
        //将分页与列表信息传回前端
        mv.addObject("goods", goods);
        mv.addObject("goodsList", goodsList);
        mv.addObject("firstCategories", firstCategories);
        mv.addObject("categoryTree", categoryTree);
        mv.addObject("pageNo", pageNo);
        mv.addObject("pageCount",pageCount);
        int totalCount = managerService.countOnGoods(goods);
        mv.addObject("totalCount", totalCount);
        mv.addObject("gName", gName);
        mv.setViewName("business/b_onGoodsList");

        return mv;
    }

}
