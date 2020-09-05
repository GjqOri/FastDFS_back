package com.qi.fastdfs_test.service.impl;

import com.qi.fastdfs_test.dao.ReqDocumentMapper;
import com.qi.fastdfs_test.entity.ReqDocument;
import com.qi.fastdfs_test.service.ReqDocumentService;
import com.qi.fastdfs_test.util.fastdfs.FastDfsClient;
import com.qi.fastdfs_test.util.fastdfs.config.FdfsProperties;
import com.qi.fastdfs_test.util.fastdfs.exception.FastDfsException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Qi
 * @create 2020-08-11 13:28
 */
@Service
public class ReqDocumentServiceImpl implements ReqDocumentService {
    @Autowired
    ReqDocumentMapper reqDocumentMapper;
    @Autowired
    FastDfsClient fastDfsClient;
    @Autowired
    FdfsProperties fastDfsProperties;

    @Override
    public int addReqDocument(ReqDocument reqDocument) {
        return this.reqDocumentMapper.insert(reqDocument);
    }

    @Override
    public List<ReqDocument> getReqDocumentsByReqId(Integer reqId) {
        return this.reqDocumentMapper.getReqDocumentsByReqId(reqId);
    }

    @Override
    public int deleteByDocId(Integer docId) {
        // 1. 先通过ID查询该需求文档的组名和路径
        ReqDocument reqDocument = this.reqDocumentMapper.selectById(docId);
        if (reqDocument != null) {
            String groupName = reqDocument.getGroupName();
            String remoteFilePath = reqDocument.getRemoteFilePath();
            // 2. 物理删除(删除服务器中的文件)
            fastDfsClient.delete(groupName, remoteFilePath);
            // 3. 逻辑删除(删除数据库中的记录)
            return this.reqDocumentMapper.deleteById(docId);
        }
        else {
            return 0;
        }
    }

    @Override
    public Map<String, Object> downloadByDocId(Integer docId) {
        // 1. 先通过ID查询该需求文档的组名和路径
        ReqDocument reqDocument = this.reqDocumentMapper.selectById(docId);
        if (reqDocument != null) {
            String groupName = reqDocument.getGroupName();
            String remoteFilePath = reqDocument.getRemoteFilePath();
            byte[] fileBuff = fastDfsClient.downloadBefore(groupName, remoteFilePath);

            Map<String, Object> docInfoMap = new HashMap<String, Object>();
            docInfoMap.put("fileBuff", fileBuff);
            docInfoMap.put("reqDocument", reqDocument);
            return docInfoMap;
        }
        else {
            return null;
        }
    }

    @Override
    public String getUrlByDocId(Integer docId) {
        // 1. 先通过ID查询该需求文档的组名和路径
        ReqDocument reqDocument = this.reqDocumentMapper.selectById(docId);
        String groupName = reqDocument.getGroupName();
        String remoteFilePath = reqDocument.getRemoteFilePath();

        return fastDfsProperties.getServer() + groupName + "/" + remoteFilePath;
    }


}
