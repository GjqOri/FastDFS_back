package com.qi.fastdfs_test.util.fastdfs.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Qi
 * @create 2020-08-12 10:52
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FastDfsException extends RuntimeException {
    private int code;
    private String msg;
}
