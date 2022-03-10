package com.qian.service.manager;

import com.qian.model.manager.Category;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ICategoryService {
    public List<Category> queryAll(Category c);
    public Integer add(Category c);
    public Integer update(Category c);
    public Integer delete(Integer id);
}
