package com.qi.fastdfs_test.util.fastdfs.conn;

import org.csource.fastdfs.TrackerServer;

public interface FdfsConnection {
    TrackerServer borrowObject();

    void returnObject(TrackerServer trackerServer);
}
