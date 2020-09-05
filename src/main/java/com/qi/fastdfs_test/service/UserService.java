package com.qi.fastdfs_test.service;

import com.qi.fastdfs_test.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Qi
 * @create 2020-08-11 13:28
 */
@Service
public interface UserService {
    /**
     * 根据ID获取一个用户的信息
     * @param id  该用户的ID
     * @return
     */
    User getUserInfoById(Integer id);
}
