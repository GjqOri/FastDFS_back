package com.qi.fastdfs_test.util.fastdfs.config;

import com.qi.fastdfs_test.util.fastdfs.FastDfsClient;
import com.qi.fastdfs_test.util.fastdfs.conn.FdfsConnectionPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.csource.fastdfs.TrackerClient;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FdfsProperties.class)
// 当给定的类名在类路径上存在，则实例化当前Bean
@ConditionalOnClass(TrackerClient.class)
@Slf4j
public class FdfsAutoConfiguration {

    private final FdfsProperties properties;

    public FdfsAutoConfiguration(FdfsProperties properties) {
        this.properties = properties;
    }

    @Bean
    public FdfsClientConfig fdfsClientConfig() {
        FdfsClientConfig clientConfig = new FdfsClientConfig();
        log.info("Fastdfs properties: {}", properties.toString());
        BeanUtils.copyProperties(properties, clientConfig);
        if (StringUtils.isNotEmpty(properties.getTrackerServers())) {
            clientConfig.setTrackerServers(properties.getTrackerServers());
        } else {
            clientConfig.setTrackerServers("127.0.0.1:22122");
        }
        log.info("Fastdfs client config: {}", clientConfig.toString());
        return clientConfig;
    }

    @Bean
    public FdfsConfig2Properties fdfsConfig2Properties() {
        return new FdfsConfig2Properties(fdfsClientConfig());
    }

    @Bean
    public FdfsConnectionPool fdfsConnectionPool() {
        return new FdfsConnectionPool(fdfsConfig2Properties());
    }

    @Bean
    public FastDfsClient fastDFSClient(FdfsConnectionPool pool) {
        return new FastDfsClient(pool);
    }
}
