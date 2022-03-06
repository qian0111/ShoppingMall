package com.qian.dao.manager;

import com.qian.model.manager.Manager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IManagerDao {
    public List<Manager> query(Manager manager);
}
