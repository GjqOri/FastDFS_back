package com.qi.fastdfs_test.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qi.fastdfs_test.entity.Requirement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Qi
 * @create 2020-08-11 14:54
 */
@Mapper
@Repository
public interface RequirementMapper extends BaseMapper<Requirement> {
    Requirement getRequirementById(@Param("reqId") Integer reqId);
}
