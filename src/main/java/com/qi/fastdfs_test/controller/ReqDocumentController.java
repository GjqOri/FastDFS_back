package com.qi.fastdfs_test.controller;

import com.qi.fastdfs_test.entity.ReqDocument;
import com.qi.fastdfs_test.entity.Requirement;
import com.qi.fastdfs_test.service.ReqDocumentService;
import com.qi.fastdfs_test.util.fastdfs.FastDfsClient;
import com.qi.fastdfs_test.util.fastdfs.exception.FastDfsError;
import com.qi.fastdfs_test.util.fastdfs.exception.FastDfsException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @author Qi
 * @create 2020-08-11 13:23
 */
@RestController
@RequestMapping(path="/reqdoc")
public class ReqDocumentController {

    @Autowired
    ReqDocumentService reqDocumentService;
    @Autowired
    FastDfsClient fastDFSClient;

    /**
     * 根据需求ID查询该需求的所有文档
     * @param reqId 需求ID
     * @return
     */
    @GetMapping("/query/{id}")
    public List<ReqDocument> getReqDocumentsByReqId(@PathVariable("id") Integer reqId) {
        return this.reqDocumentService.getReqDocumentsByReqId(reqId);
    }

    /**
     * 需求文档批量上传(也可以单文件上传)
     * @param reqId     需求ID
     * @param documents 需求文档文件(允许一次上传多个文档)
     * @return          上传成功提示
     * @throws FastDfsException 上传时如果出现错误，会抛出相应的异常
     */
    @PostMapping("/upload")
    public String uploadDocuments(@RequestParam("reqId") Integer reqId, MultipartFile[] documents) throws FastDfsException {
        int len = documents.length;
        for (int i = 0; i < len; i++) {
            MultipartFile document = documents[i];
            try {
                InputStream fileStream = document.getInputStream();
                String fileName = document.getOriginalFilename();
                String fileExtName = fileName.substring(fileName.lastIndexOf(".") + 1);
                // 没有扩展名时fileExtName的值会和fileName一致
                if (!fileExtName.equals(fileName)) {
                    String[] result = fastDFSClient.uploadBefore(fileStream, fileExtName);
                    ReqDocument reqDocument = new ReqDocument();
                    reqDocument.setReqId(reqId);
                    // user id可以从session获取
                    reqDocument.setUploadUserId(1);
                    reqDocument.setDocName(fileName);
                    reqDocument.setDocSize(document.getSize());
                    reqDocument.setDocType(document.getContentType());
                    reqDocument.setGroupName(result[0]);
                    reqDocument.setRemoteFilePath(result[1]);

                    // affectedRow 为 0 表示需求文档添加失败
                    int affectedRow = reqDocumentService.addReqDocument(reqDocument);
                    System.out.println(affectedRow);
                }
                else {
                    // 文件没有扩展名异常
                    throw new FastDfsException(FastDfsError.FILE_WITHOUT_SUFFIX.getCode(), FastDfsError.FILE_WITHOUT_SUFFIX.getMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
                // 文件上传失败异常
                throw new FastDfsException(FastDfsError.FILE_UPLOAD_FAILED.getCode(), FastDfsError.FILE_UPLOAD_FAILED.getMessage());
            }
        }

        return "上传成功！";
    }

    /**
     * 单文件删除
     * 根据需求文档的ID删除需求文档文件和数据库中的记录
     *
     * @param docId 需求文档的ID
     * @return      受删除操作影响的记录数
     */
    @GetMapping("/delete/{id}")
    public int deleteByDocId(@PathVariable("id") Integer docId) {
        return this.reqDocumentService.deleteByDocId(docId);
    }

    /**
     * 单文件或多文件删除
     * 根据需求文档的ID字符串删除需求文档文件和数据库中的记录
     * @param ids   需求文档的ID字符串，用逗号分隔，如："1,2"、"1"、""
     * @return      受删除操作影响的记录数
     */
    @PostMapping("/delete")
    public int deleteByDocIds(@RequestParam("ids") String ids) {
        String[] idArray = StringUtils.split(ids,",");
        int affectedRow = 0;
        int itemLen = idArray.length;
        for (int i = 0; i < itemLen; i++) {
            affectedRow += this.reqDocumentService.deleteByDocId(Integer.parseInt(idArray[i]));
        }
        return affectedRow;
    }

    /**
     * 根据需求文档的ID下载需求文档
     * @param docId 需求文档ID
     * @return      ResponseEntity
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadByDocId(@PathVariable("id") Integer docId) {
        // 从 service 层获取需求文档的信息及文件
        Map<String, Object> docInfoMap = this.reqDocumentService.downloadByDocId(docId);
        if (docInfoMap != null) {
            byte[] fileBuff = (byte[])docInfoMap.get("fileBuff");
            ReqDocument reqDocument = (ReqDocument)docInfoMap.get("reqDocument");

            HttpHeaders headers = new HttpHeaders();
            // 设置响应类型为文件类型
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            // 设置响应时的文件大小, 用于自动提供下载进度
            headers.setContentLength(reqDocument.getDocSize());
            // 设置下载时的默认文件名
            try {
                headers.setContentDispositionFormData("attachment", URLEncoder.encode(reqDocument.getDocName(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                return null;
            }
            /*
            Spring会将下面这个实体对象返回给浏览器, 作为响应数据)
            参数1: 响应时的具体数据
            参数2: 响应时的头文件信息
            参数3: 响应时的状态码
            */
            return new ResponseEntity<byte[]>(fileBuff, headers, HttpStatus.OK);
        }
        else {
            return null;
        }
    }

    /**
     * 根据需求文档的ID获取URL地址
     * @param docId 需求文档ID
     * @return      URL地址
     */
    @GetMapping("/getUrl/{id}")
    public String getUrlByDocId(@PathVariable("id") Integer docId) {
        return this.reqDocumentService.getUrlByDocId(docId);
    }
}
