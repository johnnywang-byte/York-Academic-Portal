package com.yorku.service.impl;

import com.yorku.exception.BusinessException;
import com.yorku.mapper.DeptMapper;
import com.yorku.pojo.Dept;
import com.yorku.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Academic Unit Service Implementation
 * York University Academic Admin Portal
 *
 * Description:
 * Implements business logic for managing Faculties and Departments.
 * Handles validation (e.g., data integrity checks before deletion) and audit metadata.
 *
 * @Description: 部门/院系管理业务实现类
 */
@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    /**
     * List All Academic Units
     * Retrieves the full directory of faculties and departments.
     *
     * 部门管理 - 查询所有部门
     */
    @Override
    public List<Dept> findAll() {
        return deptMapper.findAll();
    }

    /**
     * Delete Academic Unit
     * Business Rule: Cannot delete a unit if it still has assigned faculty/staff.
     *
     * 部门管理 - 根据ID删除 (包含业务规则校验)
     */
    @Override
    public void deleteById(Integer id) {
        // 1. Check for active staff assignments
        // 统计该部门下的员工数量
        Integer employeeCount = deptMapper.countEmployeesByDeptId(id);

        if (employeeCount > 0) {
            // 2. Throw exception if staff exist
            // 抛出业务异常：该部门下仍有教职工，禁止删除
            throw new BusinessException("Action Denied: Cannot delete academic unit with active faculty/staff assignments.");
        }

        // 3. Perform deletion if safe
        // 校验通过，执行删除操作
        deptMapper.deleteById(id);
    }

    /**
     * Create New Academic Unit
     * Automatically sets audit timestamps (createTime/updateTime).
     *
     * 部门管理 - 添加部门
     */
    @Override
    public void add(Dept dept) {
        // Set Audit Metadata
        // 补全基本属性
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());

        // Insert into Database
        // 调用持久层保存
        deptMapper.add(dept);
    }

    /**
     * Get Unit Details by ID
     * Retrieves specific information for editing purposes.
     *
     * 部门管理 - 根据ID查询详情
     */
    @Override
    public Dept getInfo(Integer id) {
        return deptMapper.getInfo(id);
    }

    /**
     * Update Unit Details
     * Updates modification timestamp automatically.
     *
     * 部门管理 - 修改部门信息
     */
    @Override
    public void update(Dept dept) {
        // Update Audit Timestamp
        // 更新最后修改时间
        dept.setUpdateTime(LocalDateTime.now());

        // Execute Update
        // 调用持久层更新
        deptMapper.update(dept);
    }
}