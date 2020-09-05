package com.qi.fastdfs_test.service;

import com.qi.fastdfs_test.entity.Requirement;
import org.springframework.stereotype.Service;

/**
 * @author Qi
 * @create 2020-08-11 14:55
 */
@Service
public interface RequirementService {
    int addRequirement(Requirement requirement);
}
