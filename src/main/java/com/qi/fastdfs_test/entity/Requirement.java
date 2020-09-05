package com.qi.fastdfs_test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author Qi
 * @create 2020-08-11 14:52
 */
@TableName(value="sepp_req")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Requirement {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String note;
    private Integer pm;
    private Date createdTime;
    @TableField(exist = false)
    private List<ReqDocument> reqDocuments;
}
