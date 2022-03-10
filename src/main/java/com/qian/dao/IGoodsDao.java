package com.qian.dao;

import com.qian.model.manager.Goods;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IGoodsDao {
    //查询商品信息
    public List<Goods> query(Goods goods);

    //查询上架商品总数
    public Long countGoods(Goods goods);

    //查询一级分类
    public List<Goods> parentCategory();

    //查询分类树
    public List<Goods> categoryTree();

    //更新商品信息
    public int upGoods(Goods goods);

    //上架商品
    public int insertOne(Goods goods);

    //下架商品
    public int offGoods(int id);

    //重新上架
    public int reOnGoods(int id);
}
