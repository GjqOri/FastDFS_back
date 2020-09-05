package com.qi.fastdfs_test.dao;

import com.qi.fastdfs_test.entity.Requirement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Qi
 * @create 2020-08-11 15:12
 */
@SpringBootTest
public class RequirementMapperTest {
    @Autowired
    RequirementMapper requirementMapper;

    @Test
    public void insertTest() {
        Requirement requirement = new Requirement();
        requirement.setTitle("新需求的标题");
        requirement.setNote("需求的备注信息");
        requirement.setPm(1);

        int result = this.requirementMapper.insert(requirement);
        System.out.println(result);
    }

    @Test
    public void selectByIdTest() {
        Integer id = 1;
        Requirement requirement = this.requirementMapper.getRequirementById(id);
        System.out.println(requirement);
        /*
        查询结果示例：
        Requirement(
          id=1,
          title=新需求的标题,
          note=,
          pm=1,
          createdTime=null,
          reqDocuments=[
            ReqDocument(docId=80, reqId=1, uploadUserId=1, uploadUser=User(userId=1, userAccount=null, password=null, userName=张三), docName=test0807测试.docx, docSize=12255, docType=application/vnd.openxmlformats-officedocument.wordprocessingml.document, groupName=group1, remoteFilePath=M00/00/01/wKgmgl82QW2APE6cAAAv3ykrMnQ82.docx, uploadDatetime=2020-08-14 15:46:53),
            ReqDocument(docId=81, reqId=1, uploadUserId=1, uploadUser=User(userId=1, userAccount=null, password=null, userName=张三), docName=test0807测试文档.txt, docSize=65, docType=text/plain, groupName=group1, remoteFilePath=M00/00/01/wKgmgl82QW2AesYLAAAAQcEqW1k589.txt, uploadDatetime=2020-08-14 15:46:53)
          ]
        )
        */
    }

}
