package com.qi.fastdfs_test.service;

import com.qi.fastdfs_test.entity.ReqDocument;
import com.qi.fastdfs_test.util.fastdfs.exception.FastDfsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Qi
 * @create 2020-08-11 13:25
 */
@Service
public interface ReqDocumentService {
    int addReqDocument(ReqDocument reqDocument);
    List<ReqDocument> getReqDocumentsByReqId(Integer reqId);
    int deleteByDocId(Integer docId);
    Map<String, Object> downloadByDocId(Integer docId);
    String getUrlByDocId(Integer docId);
}
