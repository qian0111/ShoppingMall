package com.qian.service.user;

import com.qian.model.user.User;

public interface IUserService {
    //查询用户
    public User loginCheck(User user);
    //添加新用户
    public int register(User user);

}
