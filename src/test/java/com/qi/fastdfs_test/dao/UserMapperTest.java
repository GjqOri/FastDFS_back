package com.qi.fastdfs_test.dao;

import com.qi.fastdfs_test.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author Qi
 * @create 2020-08-11 10:55
 */
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void selectAllUserTest() {
        List<User> users = this.userMapper.selectList(null);
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void selectUserByIdTest() {
        Integer id = 1;
        User user = this.userMapper.selectById(id);
        System.out.println(user);
    }
}
