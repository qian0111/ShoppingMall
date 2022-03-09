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

    //跳转至登录页
    @RequestMapping("/loginPage")
    public ModelAndView loginPage(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("business/b_login");
        return mv;
    }

    //登录检查
    @RequestMapping("/loginCheck")
    public JSONObject loginCheck(Manager manager){
        //MD5加密
        manager.setmPass(MD5Util.encode(manager.getmPass()));
        logger.info("管理员登录验证：" + (manager.getmName() + "," + manager.getmPass()));
        Manager m = managerService.loginCheck(manager);
        if(m == null){
            return resJson(0, "fail", null);
        }
        //添加到redis
        Integer res = redisUtil.set("m"+m.getId(), m.getmName());
        logger.info("添加redis缓存，状态码：" + res);

        return resJson(1, "success", m);
    }

    //主页面
    @RequestMapping("/mainPage")
    public ModelAndView mainPage(Integer id){
        logger.info("主页面:id="+id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("mName", redisUtil.get("m"+id));
        mv.setViewName("business/b_main");
        return mv;
    }

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
        //获取是否更新成功
        int row = managerService.upGoods(goods);
        return resJson(row,"success",null);
    }

    //跳转至上架弹窗页
    @RequestMapping("onGoodsPage")
    public ModelAndView onGoodsPage(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("business/goods/b_onGoods");
        return mv;
    }

    //上架新商品
    @RequestMapping("onGoods")
    public JSONObject onGoods(Goods goods){
        //获取是否更新成功
        int row = managerService.onGoods(goods);
        return resJson(row,"success",null);
    }

    //下架商品
    @RequestMapping("offGoods")
    public JSONObject offGoods(String[] arr){
        //产品下架
        int row = managerService.offGoods(arr);
        return resJson(row,"success",null);
    }

    //跳转至商品列表页
    @RequestMapping("/offGoodsListPage")
    public ModelAndView offGoodsListPage(Integer pageNo, Integer pageCount, String gName,
                                        Integer parentCategory, Integer categoryNow){
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

    //下架商品
    @RequestMapping("reOnGoods")
    public JSONObject reOnGoods(String[] arr){
        //产品下架
        int row = managerService.reOnGoods(arr);
        return resJson(row,"success",null);
    }

}
