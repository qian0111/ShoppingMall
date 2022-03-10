package com.qian.service.manager.impl;

import com.qian.dao.IGoodsDao;
import com.qian.dao.IManagerDao;
import com.qian.model.manager.Goods;
import com.qian.model.manager.Manager;
import com.qian.service.manager.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GoodsService implements IGoodsService {
    @Autowired
    private IManagerDao managerDao;
    @Autowired
    private IGoodsDao goodsDao;

    @Override
    public List<Manager> query(Manager manager) {
        return managerDao.query(manager);
    }

    @Override
    public Manager loginCheck(Manager manager) {//根据账号密码判断是否登录成功
        List<Manager> managers = managerDao.query(manager);
        if(managers == null && managers.size()==0){
            return null;
        }
        return managers.get(0);
    }

    @Override
    public List<Goods> goodsList(Goods goods) {
        goods.setPageNo((goods.getPageNo()-1) * goods.getPageCount());
        return goodsDao.query(goods);
    }

    @Override
    public List<Goods> goods(Goods goods) {
        return goodsDao.query(goods);
    }

    @Override
    public Integer countGoods(Goods goods) {
        return goodsDao.countGoods(goods).intValue();
    }

    @Override
    public List<Goods> parentCategory() {
        return goodsDao.parentCategory();
    }

    @Override
    public List<Goods> categoryTree() {
        return goodsDao.categoryTree();
    }

    @Override
    public List<Goods> categoryNow(Integer parentId) {
        //目录树
        List<Goods> categoryTree = categoryTree();
        //存储对应一级目录的二级目录
        List<Goods> categoryNow = new ArrayList<>();
        for(Goods category : categoryTree){
            //记录每个商品的目录结构
            Goods categorise = new Goods();
            if (category.getParentId() == parentId){
                categorise.setcId(category.getcId());
                categorise.setcName(category.getcName());
                categoryNow.add(categorise);
            }
        }
        return categoryNow;
    }

    @Override
    public int upGoods(Goods goods) {
        return goodsDao.upGoods(goods);
    }

    @Override
    public int onGoods(Goods goods) {
        return goodsDao.insertOne(goods);
    }

    @Override
    public int offGoods(String[] ids) {
        for(String id : ids){
            int idInt = Integer.parseInt(id);
            int row = goodsDao.offGoods(idInt);
            if(row == 0){
                return 0;
            }
        }
        return 1;
    }

    @Override
    public int reOnGoods(String[] ids) {
        for(String id : ids){
            int idInt = Integer.parseInt(id);
            int row = goodsDao.reOnGoods(idInt);
            if(row == 0){
                return 0;
            }
        }
        return 1;
    }
}
