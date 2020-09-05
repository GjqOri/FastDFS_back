package com.qi.fastdfs_test.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qi.fastdfs_test.entity.ReqDocument;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Qi
 * @create 2020-08-11 10:54
 */
@Mapper
@Repository
public interface ReqDocumentMapper extends BaseMapper<ReqDocument> {
    List<ReqDocument> getReqDocumentsByReqId(@Param("reqId") Integer reqId);
}
