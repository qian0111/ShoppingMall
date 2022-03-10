package com.qian.controller.manager;

import com.alibaba.fastjson.JSONObject;
import com.qian.config.RedisUtil;
import com.qian.model.manager.Goods;
import com.qian.model.manager.Manager;
import com.qian.service.manager.IGoodsService;
import com.qian.util.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

//个人商店管理系统
@RestController
@RequestMapping("/goodsmanage")
public class GoodsController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IGoodsService managerService;

    //跳转至商品列表页
    @RequestMapping("/onGoodsListPage")
    public ModelAndView onGoodsListPage(Integer pageNo, Integer pageCount, String gName,
                                        Integer parentCategory, Integer categoryNow){
        ModelAndView mv = new ModelAndView();
        //当没有传页码时，默认第1页
        if(pageNo == null) {
            pageNo = 1;
            pageCount = 6;
        }
        logger.info("商品列表页     查询信息: (商品名：" + gName + "，商品一级分类："
                +parentCategory +"，商品二级分类："+ categoryNow +")");
        //处理参数
        Goods goods = new Goods();
        if (gName != null) {
            goods.setgName(gName);
        }
        if(parentCategory != null){
            goods.setParentId(parentCategory);
        }
        if(categoryNow != null){
            goods.setcId(categoryNow);
        }
        goods.setgStatus(1);
        goods.setPageNo(pageNo);
        goods.setPageCount(pageCount);
        //查询商品列表
        List<Goods> goodsList = managerService.goodsList(goods);
        // 查询一级分类
        List<Goods> parentCategories = managerService.parentCategory();
        //将分页与列表信息传回前端
        mv.addObject("goodsList", goodsList);
        mv.addObject("parentCategories", parentCategories);
        System.out.println("-----"+parentCategories.toString());
        mv.addObject("pageNo", pageNo);
        mv.addObject("pageCount",pageCount);
        int totalCount = managerService.countGoods(goods);
        mv.addObject("totalCount", totalCount);
        mv.addObject("gName", gName);
        //控制下拉框选中
        if(goods.getParentId() == null){
            mv.addObject("parentId", 0);
        }
        else{
            mv.addObject("parentId", goods.getParentId());
        }
        System.out.println("------------------" +goods.toString());
        mv.addObject("goods", goods);
        mv.setViewName("business/goods/b_onGoodsList");
        return mv;
    }

    @RequestMapping("categoryNow")
    public JSONObject categoryNow(Integer parentId){
        //提取二级分类
        List<Goods> categoryNow = managerService.categoryNow(parentId);
        JSONObject json = new JSONObject();
        json.put("categoryNow", categoryNow);
        return json;
    }

    @RequestMapping("selectItself")
    public JSONObject selectItself(Integer parentId, Integer cId){
        //提取二级分类
        List<Goods> categoryNow = managerService.categoryNow(parentId);
        JSONObject json = new JSONObject();
        json.put("categoryNow", categoryNow);
        json.put("cIdNew",cId);
        return json;
    }

    //跳转至更新弹窗页
    @RequestMapping("upGoodsInfo")
    public ModelAndView upGoodsInfo(Integer id){
        ModelAndView mv = new ModelAndView();
        logger.info("打开更新弹窗");
        Goods g = new Goods();
        g.setId(id);
        Goods goods = managerService.goods(g).get(0);
        mv.addObject("goods", goods);
        mv.setViewName("business/goods/b_upGoodsInfo");
        return mv;
    }

    //提交更新信息
    @RequestMapping("upGoods")
    public JSONObject upGoods(Goods goods){
        logger.info("更新信息: (商品名：" + goods.getgName()+
                "，商品数量："+ goods.getgCount() + "，商品单价：" + goods.getgPrice() +
                "，商品一级分类：" + goods.getParentId() +"，商品二级分类："+ goods.getcId() +")");
        //获取是否更新成功
        int row = managerService.upGoods(goods);
        return resJson(row,"success",null);
    }

    //跳转至上架弹窗页
    @RequestMapping("onGoodsPage")
    public ModelAndView onGoodsPage(){
        ModelAndView mv = new ModelAndView();
        logger.info("添加新商品");
        mv.setViewName("business/goods/b_onGoods");
        return mv;
    }

    //上架新商品
    @RequestMapping("onGoods")
    public JSONObject onGoods(Goods goods){
        //获取是否更新成功
        logger.info("新商品信息: (商品名：" + goods.getgName()+
                "，商品数量："+ goods.getgCount() + "，商品单价：" + goods.getgPrice() +
                "，商品一级分类：" + goods.getParentId() +"，商品二级分类："+ goods.getcId() +")");
        int row = managerService.onGoods(goods);
        return resJson(row,"success",null);
    }

    //下架商品
    @RequestMapping("offGoods")
    public JSONObject offGoods(String[] arr){
        //产品下架
        logger.info("下架商品：" + arr.toString());
        int row = managerService.offGoods(arr);
        return resJson(row,"success",null);
    }

    //跳转至下架商品列表页
    @RequestMapping("/offGoodsListPage")
    public ModelAndView offGoodsListPage(Integer pageNo, Integer pageCount, String gName,
                                        Integer parentCategory, Integer categoryNow){
        ModelAndView mv = new ModelAndView();
        logger.info("下架商品页     查询信息: (商品名：" + gName + "，商品一级分类："
                +parentCategory +"，商品二级分类："+ categoryNow +")");
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
        if(parentCategory != null){
            goods.setParentId(parentCategory);
        }
        if(categoryNow != null){
            goods.setcId(categoryNow);
        }
        goods.setgStatus(0);
        goods.setPageNo(pageNo);
        goods.setPageCount(pageCount);
        //查询商品列表
        List<Goods> goodsList = managerService.goodsList(goods);
        // 查询一级分类
        List<Goods> parentCategories = managerService.parentCategory();
        //将分页与列表信息传回前端
        mv.addObject("goodsList", goodsList);
        mv.addObject("parentCategories", parentCategories);
//        System.out.println("-----"+parentCategories.toString());
        mv.addObject("pageNo", pageNo);
        mv.addObject("pageCount",pageCount);
        int totalCount = managerService.countGoods(goods);
        mv.addObject("totalCount", totalCount);
        mv.addObject("gName", gName);
        //控制下拉框选中
        if(goods.getParentId() == null){
            mv.addObject("parentId", 0);
        }
        else{
            mv.addObject("parentId", goods.getParentId());
        }
//        System.out.println("------------------" +goods.toString());
        mv.addObject("goods", goods);
        mv.setViewName("business/goods/b_offGoodsList");
        return mv;
    }

    //上架商品
    @RequestMapping("reOnGoods")
    public JSONObject reOnGoods(String[] arr){
        //产品上架
        logger.info("上架商品：" + arr.toString());
        int row = managerService.reOnGoods(arr);
        return resJson(row,"success",null);
    }
}
