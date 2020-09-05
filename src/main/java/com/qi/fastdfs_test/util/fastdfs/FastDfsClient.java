package com.qi.fastdfs_test.util.fastdfs;

import com.qi.fastdfs_test.util.fastdfs.conn.FdfsConnectionPool;
import com.qi.fastdfs_test.util.fastdfs.exception.FastDfsError;
import com.qi.fastdfs_test.util.fastdfs.exception.FastDfsException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.csource.fastdfs.ProtoCommon;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Qi
 * @create 2020-08-06 17:24
 */
@Slf4j
public class FastDfsClient {

    private FdfsConnectionPool pool;
    // 单个文件的最大大小
    private final int MAX_FILE_SIZE = 20 * 1000 * 1000;

    public FastDfsClient(FdfsConnectionPool pool) {
        this.pool = pool;
    }

    /**
     * 文件上传前执行参数检查，读取文件流的操作
     * @param fileStream    文件输入流
     * @param fileExtName   文件扩展名
     * @return              存储文件资源定位信息的数组
     * @throws FastDfsException
     */
    public String[] uploadBefore(InputStream fileStream, String fileExtName) throws FastDfsException {
        // 上传前先检查参数
        if (fileStream == null) {
            throw new FastDfsException(FastDfsError.FILE_ISNULL.getCode(), FastDfsError.FILE_ISNULL.getMessage());
        }
        try {
            if (fileStream.available() > MAX_FILE_SIZE) {
                throw new FastDfsException(FastDfsError.FILE_OUT_SIZE.getCode(), FastDfsError.FILE_OUT_SIZE.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new FastDfsException(FastDfsError.FILE_ISNULL.getCode(), FastDfsError.FILE_ISNULL.getMessage());
        }

        // 读取文件字节流流
        byte[] fileBuff = null;
        try {
            fileBuff = new byte[fileStream.available()];
            fileStream.read(fileBuff, 0, fileBuff.length);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FastDfsException(FastDfsError.FILE_ISNULL.getCode(), FastDfsError.FILE_ISNULL.getMessage());
        }
        return this.upload(fileBuff, fileExtName);
    }

    /**
     * 通用文件上传方法
     * @param fileBuff      文件的字节数组
     * @param fileExtName   文件的扩展名
     * @return              存储文件资源定位信息的数组
     * @throws FastDfsException
     */
    public String[] upload(byte[] fileBuff, String fileExtName) throws FastDfsException {
        TrackerServer trackerServer = null;
        String[] result = null;
        try {
            trackerServer = pool.borrowObject();
            // Storage 的客户端对象，需要使用这个对象来完成具体的文件上传、下载和删除操作
            StorageClient storageClient = new StorageClient(trackerServer, null);

            /*
            upload_file 方法参数说明:
            参数1: 需要上传的文件的字节数组
            参数2: 需要上传的文件扩展名
            参数3: 文件的属性文件(可以不上传)
            */
            // 返回值数组中的第一个元素为上传后的文件所在的组名，第二个元素为文件的远程路径
            result = storageClient.upload_file(fileBuff, fileExtName, null);
            // 返回值为null表示上传失败
            if (result == null) {
                throw new FastDfsException(FastDfsError.FILE_UPLOAD_FAILED.getCode(), FastDfsError.FILE_UPLOAD_FAILED.getMessage());
            }
            else if (log.isDebugEnabled()) {
                log.debug("upload file success, group name is {}, file path is {}", result[0], result[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FastDfsException(FastDfsError.FILE_UPLOAD_FAILED.getCode(), FastDfsError.FILE_UPLOAD_FAILED.getMessage());
        } finally {
            // 返还对象
            pool.returnObject(trackerServer);
        }
        return result;
    }

    /**
     * 文件下载前先执行参数检查操作
     * @param groupName         组名
     * @param remoteFilePath    远程文件路径
     * @return                  文件的字节数组
     * @throws FastDfsException
     */
    public byte[] downloadBefore(String groupName, String remoteFilePath) throws FastDfsException {
        // 下载前先检查参数
        if (StringUtils.isBlank(groupName) || StringUtils.isBlank(remoteFilePath)) {
            throw new FastDfsException(FastDfsError.FILE_PATH_ISNULL.getCode(), FastDfsError.FILE_PATH_ISNULL.getMessage());
        }
        return this.download(groupName, remoteFilePath);
    }

    /**
     * 通用文件下载方法
     *
     * @param groupName         组名, 如: group1
     * @param remoteFilePath    远程文件路径, 如: M00/00/00/wKgmgl8w7XmAdXbpAAAv3ykrMnQ98.docx
     * @return                  文件的字节数组
     * @throws FastDfsException
     */
    public byte[] download(String groupName, String remoteFilePath) throws FastDfsException {
        TrackerServer trackerServer = null;
        byte[] fileBuff = null;
        try {
            trackerServer = pool.borrowObject();
            StorageClient storageClient = new StorageClient(trackerServer, null);

            // 获取文件的字节数组
            fileBuff = storageClient.download_file(groupName, remoteFilePath);
            // 返回值为null表示下载失败
            if (fileBuff == null) {
                throw new FastDfsException(FastDfsError.FILE_NOT_EXIST.getCode(), FastDfsError.FILE_NOT_EXIST.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FastDfsException(FastDfsError.FILE_DOWNLOAD_FAILED.getCode(), FastDfsError.FILE_DOWNLOAD_FAILED.getMessage());
        } finally {
            pool.returnObject(trackerServer);
        }
        return fileBuff;
    }

    /**
     * 删除前先执行参数检查操作
     * @param groupName         组名
     * @param remoteFilePath    远程文件路径
     * @return                  返回值为0时表示文件删除成功, 否则表示删除失败
     * @throws FastDfsException
     */
    public Integer deleteBefore(String groupName, String remoteFilePath) throws FastDfsException {
        // 删除前先检查参数
        if (StringUtils.isBlank(groupName) || StringUtils.isBlank(remoteFilePath)) {
            throw new FastDfsException(FastDfsError.FILE_PATH_ISNULL.getCode(), FastDfsError.FILE_PATH_ISNULL.getMessage());
        }
        return this.delete(groupName, remoteFilePath);
    }

    /**
     * 通用文件删除方法
     * @param groupName         组名，如：group1
     * @param remoteFilePath    远程文件路径，如：M00/00/00/wKgmgl8w7XmAdXbpAAAv3ykrMnQ98.docx
     * @return                  返回值为0时表示文件删除成功, 否则表示删除失败
     */
    public Integer delete(String groupName, String remoteFilePath) throws FastDfsException {
        TrackerServer trackerServer = null;
        Integer result = null;
        try {
            trackerServer = pool.borrowObject();
            StorageClient storageClient = new StorageClient(trackerServer, null);

            result = storageClient.delete_file(groupName, remoteFilePath);
            // 返回值为0表示删除失败
            if (result != 0) {
                throw new FastDfsException(FastDfsError.FILE_DELETE_FAILED.getCode(), FastDfsError.FILE_DELETE_FAILED.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FastDfsException(FastDfsError.FILE_DELETE_FAILED.getCode(), FastDfsError.FILE_DELETE_FAILED.getMessage());
        } finally {
            pool.returnObject(trackerServer);
        }
        return result;
    }

    /**
     * 获取访问服务器的token，拼接到地址后面
     * @param remoteFileName 远程文件名称，如：M00/00/00/wKgmgl8w-amAAg9bAAAv3ykrMnQ83.docx
     * @param httpSecretKey 密钥，如：FastDFS1234567890
     * @return              返回token，如：token=078d370098b03e9020b82c829c205e1f&ts=1508141521
     */
    public static String getToken(String remoteFileName, String httpSecretKey){
        // unix seconds
        int ts = (int) (System.currentTimeMillis() / 1000);
        // token
        String token = "null";

        try {
            token = ProtoCommon.getToken(remoteFileName, ts, httpSecretKey);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "token=" + token +
                "&ts=" + ts;
    }
}
