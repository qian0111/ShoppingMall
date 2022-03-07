package com.qian.service.manager.impl;

import com.qian.dao.manager.IGoodsDao;
import com.qian.dao.manager.IManagerDao;
import com.qian.model.manager.Goods;
import com.qian.model.manager.Manager;
import com.qian.service.manager.IManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService implements IManagerService {
    @Autowired
    private IManagerDao managerDao;
    @Autowired
    private IGoodsDao goodsDao;

    @Override
    public List<Manager> query(Manager manager) {
        return managerDao.query(manager);
    }

    @Override
    public boolean loginCheck(Manager manager) {//根据账号密码判断是否登录成功
        List<Manager> managers = managerDao.query(manager);
        if(managers == null && managers.size()==0){
            return false;
        }
        return true;
    }

    @Override
    public List<Goods> goodsList(Goods goods) {
        goods.setPageNo((goods.getPageNo()-1) * goods.getPageCount());
        return goodsDao.query(goods);
    }

    @Override
    public Integer countOnGoods(Goods goods) {
        return goodsDao.countOnGoods(goods).intValue();
    }

    @Override
    public Integer countOffGoods(Goods goods) {
        return goodsDao.countOffGoods(goods).intValue();
    }

    @Override
    public List<Goods> firstCategory() {
        return goodsDao.firstCategory();
    }

    @Override
    public List<Goods> categoryTree() {
        return goodsDao.categoryTree();
    }
}
