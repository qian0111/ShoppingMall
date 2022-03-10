package com.qian.service.user.impl;

import com.qian.dao.IUserDao;
import com.qian.model.manager.Manager;
import com.qian.model.user.User;
import com.qian.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserDao userDao;
    @Override
    public User loginCheck(User user) {
        List<User> users = userDao.query(user);
        if(users == null && users.size()==0){
            return null;
        }
        return users.get(0);
    }

    @Override
    public int register(User user) {
        return userDao.add(user);
    }
}
