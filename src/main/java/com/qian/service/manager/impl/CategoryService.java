package com.qian.service.manager.impl;

import com.qian.dao.ICategoryDao;
import com.qian.model.manager.Category;
import com.qian.service.manager.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    public ICategoryDao categoryDao;

    @Override
    public List<Category> queryAll(Category c) {
        return categoryDao.queryAll(c);
    }

    @Override
    public Integer add(Category c) {
        return categoryDao.add(c);
    }

    @Override
    public Integer update(Category c) {
        return update(c);
    }

    @Override
    public Integer delete(Integer id) {
        return categoryDao.delete(id);
    }
}
