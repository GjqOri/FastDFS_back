package com.qi.fastdfs_test.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Qi
 * @create 2020-08-11 10:39
 */
@TableName(value="document_files_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqDocument {
    @TableId(type = IdType.AUTO)
    private Integer docId;
    private Integer reqId;
    private Integer uploadUserId;
    @TableField(exist = false)
    private User uploadUser;
    private String docName;
    private Long docSize;
    private String docType;
    private String groupName;
    private String remoteFilePath;
    private String uploadDatetime;
}
