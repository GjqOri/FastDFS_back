package com.qi.fastdfs_test.service.impl;

import com.qi.fastdfs_test.dao.RequirementMapper;
import com.qi.fastdfs_test.entity.Requirement;
import com.qi.fastdfs_test.service.RequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Qi
 * @create 2020-08-11 14:55
 */
@Service
public class RequirementServiceImpl implements RequirementService {
    @Autowired
    RequirementMapper requirementMapper;

    @Override
    public int addRequirement(Requirement requirement) {
        return this.requirementMapper.insert(requirement);
    }
}
