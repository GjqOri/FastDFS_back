package com.qi.fastdfs_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class FastdfsTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastdfsTestApplication.class, args);
    }

}
