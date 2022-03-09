package com.qian.service.manager;

import com.qian.model.manager.Goods;
import com.qian.model.manager.Manager;

import java.util.List;

public interface IGoodsService {
    //查询管理员信息
    public List<Manager> query(Manager manager);

    //判断登录资格
    public Manager loginCheck(Manager manager);

    //查询商品列表
    public List<Goods> goodsList(Goods goods);

    //查询商品列表
    public List<Goods> goods(Goods goods);

    //查询商品总数
    public Integer countGoods(Goods goods);

    //查询一级分类
    public List<Goods> parentCategory();

    //查询分类树
    public List<Goods> categoryTree();

    //根据一级分类确定当前二级分类列表
    public List<Goods> categoryNow(Integer parentId);

    //更新商品信息
    public int upGoods(Goods goods);

    //上架商品
    public int onGoods(Goods goods);

    //下架商品
    public int offGoods(String[] ids);

    //重新上架商品
    public int reOnGoods(String[] ids);
}
