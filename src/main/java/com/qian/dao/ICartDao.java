package com.qian.dao;

import com.qian.model.user.Cart;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICartDao {
    //购物车清单
    public List<Cart> query(Cart cart);
    //查询该物品是否在购物车中
    public Long count(Cart cart);
    //插入物品到购物车
    public int insert(Cart cart);
    //修改物品数量
    public int update(Cart cart);
    //从购物车删除某物品
    public int delete(Cart cart);
}
