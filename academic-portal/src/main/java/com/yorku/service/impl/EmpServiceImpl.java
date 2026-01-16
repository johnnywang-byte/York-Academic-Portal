package com.yorku.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yorku.mapper.EmpExprMapper;
import com.yorku.mapper.EmpMapper;
import com.yorku.pojo.*;
import com.yorku.service.EmpLogService;
import com.yorku.service.EmpService;
import com.yorku.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Faculty & Staff Service Implementation
 * York University Academic Admin Portal
 *
 * Description:
 * Core business logic for personnel management.
 * Handles complex operations like cascading updates (Profile + Work History)
 * and secure authentication via Passport York.
 *
 * @Description: 教职工管理业务层实现类
 */
@Slf4j
@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private EmpExprMapper empExprMapper;
    @Autowired
    private EmpLogService empLogService;

    /**
     * Paginated Directory Search
     * Uses PageHelper to handle limit/offset logic automatically.
     *
     * 分页查询教职工列表
     */
    @Override
    public PageResult<Emp> page(EmpQueryParam empQueryParam){
        // 1. Setup Pagination
        // 设置分页参数
        PageHelper.startPage(empQueryParam.getPage(), empQueryParam.getPageSize());

        // 2. Execute Query
        // 调用Mapper查询
        List<Emp> empList = empMapper.list(empQueryParam);

        // 3. Encapsulate Results
        // 封装分页结果
        Page<Emp> pageResult = (Page<Emp>) empList;
        return new PageResult<>(pageResult.getTotal(), pageResult.getResult());
    }

    /**
     * Onboard New Faculty/Staff
     * Transactional: Ensures both profile and work history are saved atomically.
     *
     * 新增员工 (包含工作经历的保存)
     */
    @Transactional // Transaction Management / 事务管理
    @Override
    public void save(Emp emp) {
        try {
            // 1. Save Basic Profile
            // 保存员工基本信息
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());
            empMapper.insert(emp);

            // 2. Save Professional History (One-to-Many Relationship)
            // 保存员工工作经历
            List<EmpExpr> exprList = emp.getExprList();

            // Check if history list is not empty
            if (!CollectionUtils.isEmpty(exprList)) {
                // Assign the generated Emp ID to each history record
                exprList.forEach(empExpr -> {
                    empExpr.setEmpId(emp.getId());
                });

                log.info("📝 [York U Admin] Saving Work History: {}", exprList);
                empExprMapper.insertBatch(exprList);
            }
        } finally {
            // 3. System Audit Log (Legacy requirement)
            // 记录操作日志 (无论成功失败)
            EmpLog empLog = new EmpLog(null, LocalDateTime.now(), "New Appointment Created: " + emp.getName());
            empLogService.insertLog(empLog);
        }
    }

    /**
     * Batch Remove Personnel Records
     * Transactional: Rollback if any deletion fails.
     *
     * 批量删除员工 (级联删除工作经历)
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void delete(List<Integer> ids) {
        // 1. Delete Basic Profile
        // 批量删除员工基本信息
        empMapper.deleteByIds(ids);

        // 2. Delete Associated Work History
        // 批量删除关联的工作经历信息
        empExprMapper.deleteByEmpIds(ids);
    }

    /**
     * Retrieve Profile by ID
     *
     * 根据ID查询员工信息
     */
    @Override
    public Emp getInfo(Integer id) {
        return empMapper.getById(id);
    }

    /**
     * List All Active Staff
     *
     * 查询所有员工
     */
    @Override
    public List<Emp> findAll() {
        return empMapper.findAll();
    }

    /**
     * Update Personnel Profile
     * Strategy: "Delete-then-Insert" for work history updates.
     *
     * 修改员工信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Emp emp) {
        // 1. Update Basic Profile
        // 修改基本信息
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.updateById(emp);

        // 2. Update Work History (Reset Strategy)
        // 2.1 Delete existing history for this employee
        // 先删除旧的工作经历
        empExprMapper.deleteByEmpIds(Arrays.asList(emp.getId()));

        // 2.2 Insert new history list
        // 再添加新的工作经历
        List<EmpExpr> exprList = emp.getExprList();
        if (!CollectionUtils.isEmpty(exprList)) {
            exprList.forEach(empExpr -> {
                empExpr.setEmpId(emp.getId());
            });
            empExprMapper.insertBatch(exprList);
        }
    }

    /**
     * Passport York Authentication (Login)
     * Verifies credentials and issues a JWT token.
     *
     * 员工登录 (生成JWT令牌)
     */
    @Override
    public LoginInfo login(Emp emp) {
        // 1. Query by Username (Passport ID) and Password
        // 根据用户名密码查询
        Emp e = empMapper.selectByUsernameAndPwd(emp);

        // 2. Validate Credentials
        if (e != null) {
            log.info("✅ [Auth Success] User Logged In: {}", e.getUsername());

            // 3. Generate JWT Token
            // 生成令牌，包含 ID 和 Username
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", e.getId());
            claims.put("username", e.getUsername());
            String jwt = JwtUtils.generateToken(claims);

            // Return Login Info DTO
            return new LoginInfo(e.getId(), e.getUsername(), e.getName(), jwt);
        }

        // 3. Auth Failed
        // 登录失败
        return null;
    }
}