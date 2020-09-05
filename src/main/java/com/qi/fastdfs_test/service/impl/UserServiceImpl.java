package com.qi.fastdfs_test.service.impl;

import com.qi.fastdfs_test.dao.UserMapper;
import com.qi.fastdfs_test.entity.User;
import com.qi.fastdfs_test.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Qi
 * @create 2020-08-11 13:28
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public User getUserInfoById(Integer id) {
        return this.userMapper.selectById(id);
    }
}
