package com.yorku.controller;

import com.yorku.anno.Log;
import com.yorku.exception.BusinessException;
import com.yorku.pojo.Dept;
import com.yorku.pojo.Result;
import com.yorku.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Academic Unit Controller (Faculties & Departments)
 * York University Academic Admin Portal
 *
 * Description:
 * Manages the organizational structure of the university (e.g., Lassonde School of Engineering, Dept of Math).
 * Provides endpoints for creating, updating, and removing academic units.
 *
 * @Description: 部门/院系管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/depts")
public class DeptController {

    @Autowired
    private DeptService deptService;

    /**
     * List All Academic Units
     * Retrieves the full directory of faculties and departments.
     *
     * 部门管理 - 查询所有部门
     */
    @GetMapping
    public Result list(){
        log.info("📂 [York U Admin] Loading Academic Unit Directory...");
        List<Dept> deptList = deptService.findAll();
        return Result.success(deptList);
    }

    /**
     * Remove Academic Unit
     * Deletes a specific faculty or department record.
     * Note: This operation is audited via @Log.
     *
     * 部门管理 - 删除指定部门
     */
    @Log // Audit Log / 审计日志
    @DeleteMapping
    public Result delete(Integer id) {
        try {
            log.info("🗑️ [York U Admin] Deleting Academic Unit. ID: {}", id);
            deptService.deleteById(id);
            return Result.success();
        } catch (BusinessException e) {
            // Capture business logic errors (e.g., Unit contains active staff)
            // 捕获业务异常，返回错误信息给前端
            log.error("❌ [Delete Failed] Business Rule Violation: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * Create New Academic Unit
     * Registers a new faculty or department in the system.
     *
     * 部门管理 - 添加部门
     */
    @Log // Audit Log / 审计日志
    @PostMapping
    public Result add(@RequestBody Dept dept){
        log.info("📝 [York U Admin] Registering New Unit: {}", dept);
        deptService.add(dept);
        return Result.success();
    }

    /**
     * Get Unit Details
     * Retrieves information for a specific academic unit by ID (for editing purposes).
     *
     * 部门管理 - 根据ID查询（回显）
     */
    @Log // Audit Log / 审计日志
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id){
        log.info("🔍 [York U Admin] Fetching Unit Details. ID: {}", id);
        Dept dept = deptService.getInfo(id);
        return Result.success(dept);
    }

    /**
     * Update Academic Unit
     * Modifies the details of an existing faculty or department.
     *
     * 部门管理 - 修改部门
     */
    @Log // Audit Log / 审计日志
    @PutMapping
    public Result update(@RequestBody Dept dept){
        log.info("✏️ [York U Admin] Updating Unit Details: {}", dept);
        deptService.update(dept);
        return Result.success();
    }
}