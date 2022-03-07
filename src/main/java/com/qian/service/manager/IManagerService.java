package com.qian.service.manager;

import com.qian.model.manager.Goods;
import com.qian.model.manager.Manager;

import java.util.List;

public interface IManagerService {
    //查询管理员信息
    public List<Manager> query(Manager manager);

    //判断登录资格
    public boolean loginCheck(Manager manager);

    //查询商品列表
    public List<Goods> goodsList(Goods goods);

    //查询上架商品总数
    public Integer countOnGoods(Goods goods);

    //查询下架商品总数
    public Integer countOffGoods(Goods goods);

    //查询一级分类
    public List<Goods> firstCategory();

    //查询二级分类
    public List<Goods> categoryTree();
}
