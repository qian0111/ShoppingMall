package com.qian.dao.manager;

import com.qian.model.manager.Goods;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IGoodsDao {
    //查询商品信息
    public List<Goods> query(Goods goods);

    //查询上架商品总数
    public Long countOnGoods(Goods goods);

    //查询下架商品总数
    public Long countOffGoods(Goods goods);

    //查询一级分类
    public List<Goods> firstCategory();

    //查询二级分类
    public List<Goods> categoryTree();
}
