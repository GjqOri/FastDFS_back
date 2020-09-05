package com.qi.fastdfs_test.util.fastdfs.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Qi
 * @create 2020-08-12 10:53
 */
@Getter
@AllArgsConstructor
@ToString
public enum FastDfsError {
    FILE_PATH_ISNULL(1600, "文件路径为空"),
    FILE_ISNULL(1601, "文件为空"),
    FILE_UPLOAD_FAILED(1602, "文件上传失败"),
    FILE_NOT_EXIST(1603, "文件不存在"),
    FILE_DOWNLOAD_FAILED(1604, "文件下载失败"),
    FILE_DELETE_FAILED(1605, "删除文件失败"),
    FILE_SERVER_CONNECTION_FAILED(1606, "文件服务器连接失败"),
    FILE_OUT_SIZE(1607, "文件超过大小"),
    FILE_TYPE_ERROR_IMAGE(1608, "图片类型错误"),
    FILE_TYPE_ERROR_DOC(1609, "文档类型错误"),
    FILE_TYPE_ERROR_VIDEO(1610, "音频类型错误"),
    FILE_TYPE_ERROR_COMPRESS(1611, "压缩文件类型错误"),
    FILE_WITHOUT_SUFFIX(1612, "文件没有后缀"),
    ;

    private int code;
    private String message;
}
