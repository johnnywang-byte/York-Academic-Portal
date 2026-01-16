package com.yorku.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yorku.exception.BusinessException;
import com.yorku.mapper.ClazzMapper;
import com.yorku.mapper.StudentMapper;
import com.yorku.pojo.Clazz;
import com.yorku.pojo.ClazzQueryParam;
import com.yorku.pojo.PageResult;
import com.yorku.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Course Section Service Implementation
 * York University Academic Admin Portal
 *
 * Description:
 * Implements business logic for managing academic course sections.
 * Handles validation rules (e.g., preventing deletion of active courses) and audit timestamps.
 *
 * @Description: 班级/课程单元管理业务实现类
 */
@Service
public class ClazzServiceImpl implements ClazzService {

    @Autowired
    private ClazzMapper clazzMapper;

    @Autowired
    private StudentMapper studentMapper;

    /**
     * Paginated Search for Course Sections
     *
     * 班级管理 - 分页查询
     */
    @Override
    public PageResult<Clazz> page(ClazzQueryParam clazzQueryParam) {
        // 1. Setup Pagination (PageHelper)
        // 设置分页参数
        PageHelper.startPage(clazzQueryParam.getPage(), clazzQueryParam.getPageSize());

        // 2. Execute Query
        // 调用Mapper查询
        List<Clazz> clazzList = clazzMapper.list(clazzQueryParam);

        // 3. Encapsulate Results
        // 封装结果并返回
        Page<Clazz> pageResult = (Page<Clazz>) clazzList;
        return new PageResult<>(pageResult.getTotal(), pageResult.getResult());
    }

    /**
     * Delete Course Section
     * Business Rule: Cannot delete a course if students are currently enrolled.
     *
     * 班级管理 - 删除班级 (包含业务规则校验)
     */
    @Override
    public void delete(Integer id) {
        // Check for active enrollments
        // 检查该班级下是否有学生
        Integer count = studentMapper.countByClazzId(id);

        if (count > 0) {
            // Throw Business Exception if students exist
            // 抛出业务异常：存在在读学生，禁止删除
            throw new BusinessException("Action Denied: Cannot delete course section with active student enrollments.");
        }

        // Proceed with deletion if safe
        // 校验通过，执行删除
        clazzMapper.delete(id);
    }

    /**
     * Schedule New Course Section
     * Automatically sets the audit timestamps (createTime/updateTime).
     *
     * 班级管理 - 新增班级
     */
    @Override
    public void save(Clazz clazz) {
        // Set Audit Metadata
        // 补全基础信息（创建时间和修改时间）
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());

        // Insert into Database
        // 调用持久层保存
        clazzMapper.save(clazz);
    }

    /**
     * Retrieve Course Details by ID
     *
     * 班级管理 - 根据ID查询详情
     */
    @Override
    public Clazz findById(Integer id) {
        return clazzMapper.findById(id);
    }

    /**
     * Update Course Information
     * Updates the modification timestamp automatically.
     *
     * 班级管理 - 修改班级信息
     */
    @Override
    public void update(Clazz clazz) {
        // Update Audit Timestamp
        // 更新修改时间
        clazz.setUpdateTime(LocalDateTime.now());

        // Execute Update
        clazzMapper.update(clazz);
    }

    /**
     * List All Course Sections
     *
     * 班级管理 - 查询所有班级
     */
    @Override
    public List<Clazz> findAll() {
        return clazzMapper.findAll();
    }
}