package com.yorku.controller;

import com.yorku.pojo.Emp;
import com.yorku.pojo.EmpQueryParam;
import com.yorku.pojo.PageResult;
import com.yorku.pojo.Result;
import com.yorku.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Faculty & Staff Controller
 * York University Academic Admin Portal
 *
 * Description:
 * Manages personnel records for professors, lecturers, and administrative staff.
 * Includes onboarding, directory search, profile updates, and termination.
 *
 * @Description: 教职工管理控制器 (员工管理)
 */
@RestController
@Slf4j
@RequestMapping("/emps")
public class EmpController {

    @Autowired
    private EmpService empService;

    /**
     * Faculty Directory - Paginated Search
     * Retrieves a list of staff members based on filters (Name, Gender, Date Range).
     *
     * 教职工管理 - 分页查询
     * 根据条件查询员工列表。
     */
    @GetMapping
    public Result page(EmpQueryParam empQueryParam) {
        log.info("📢 [York U Admin] Querying Staff Directory: {}", empQueryParam);
        PageResult<Emp> pageResult = empService.page(empQueryParam);
        return Result.success(pageResult);
    }

    /**
     * Onboard New Staff Member
     * Registers a new employee in the university HR system.
     *
     * 教职工管理 - 新增员工
     * 录入新的教职工信息（入职）。
     */
    @PostMapping
    public Result save(@RequestBody Emp emp) {
        log.info("📝 [York U Admin] Onboarding New Staff: {}", emp);
        empService.save(emp);
        return Result.success();
    }

    /**
     * Remove Personnel Records (Batch Delete)
     * Deletes one or multiple staff records by ID list.
     *
     * 教职工管理 - 批量删除员工
     * 根据ID列表删除员工信息。
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids) {
        log.info("🗑️ [York U Admin] Removing Staff Records. IDs: {}", ids);
        empService.delete(ids);
        return Result.success();
    }

    /**
     * Retrieve Profile by ID
     * Fetches detailed information for a specific staff member (used for Edit form).
     *
     * 教职工管理 - 根据ID查询详情 (回显)
     */
    @GetMapping("/{id}")
    public Result getEmp(@PathVariable Integer id) {
        log.info("🔍 [York U Admin] Fetching Staff Profile. ID: {}", id);
        Emp emp = empService.getInfo(id);
        return Result.success(emp);
    }

    /**
     * Update Personnel Information
     * Modifies an existing staff member's profile.
     *
     * 教职工管理 - 修改员工信息
     */
    @PutMapping
    public Result update(@RequestBody Emp emp) {
        log.info("✏️ [York U Admin] Updating Staff Profile: {}", emp);
        empService.update(emp);
        return Result.success();
    }

    /**
     * List All Active Staff
     * Retrieves a complete list of all employees (Non-paginated).
     * Typically used for dropdown selection (e.g., assigning a Head Instructor).
     *
     * 教职工管理 - 查询所有员工
     * 获取所有员工列表（不分页），通常用于下拉框选择（如选择班主任）。
     */
    @GetMapping("/list")
    public Result findAll() {
        log.info("📂 [York U Admin] Loading Full Staff Directory...");
        List<Emp> empList = empService.findAll();
        return Result.success(empList);
    }
}