package com.qi.fastdfs_test.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qi.fastdfs_test.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Qi
 * @create 2020-08-11 10:54
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {
}
