package com.qian.dao;

import com.qian.model.manager.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryDao {
    public List<Category> queryAll(Category c);
    public Integer add(Category c);
    public Integer update(Category c);
    public Integer delete(Integer id);
}
