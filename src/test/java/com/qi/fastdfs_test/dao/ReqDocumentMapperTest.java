package com.qi.fastdfs_test.dao;

import com.qi.fastdfs_test.entity.ReqDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author Qi
 * @create 2020-08-11 11:53
 */

@SpringBootTest
public class ReqDocumentMapperTest {

    @Autowired
    ReqDocumentMapper reqDocumentMapper;

    @Test
    public void insertTest() {
        ReqDocument reqDocument = new ReqDocument();
        reqDocument.setReqId(1);
        reqDocument.setUploadUserId(1);
        reqDocument.setDocName("test0807测试.docx");
        reqDocument.setDocSize(12255L);
        reqDocument.setDocType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        reqDocument.setGroupName("group1");
        reqDocument.setRemoteFilePath("M00/00/00/wKgmgl8yJ5eATG1xAAAv3ykrMnQ24.docx");

        int result = this.reqDocumentMapper.insert(reqDocument);
        System.out.println(result);
    }

    @Test
    public void selectByIdTest() {
        Integer id = 1;
        ReqDocument reqDocument = this.reqDocumentMapper.selectById(id);
        System.out.println(reqDocument);
    }

    @Test
    public void selectByReqIdTest() {
        Integer reqId = 1;
        List<ReqDocument> reqDocuments = this.reqDocumentMapper.getReqDocumentsByReqId(reqId);
        for (ReqDocument reqDocument : reqDocuments) {
            System.out.println(reqDocument);
        }
    }

}
