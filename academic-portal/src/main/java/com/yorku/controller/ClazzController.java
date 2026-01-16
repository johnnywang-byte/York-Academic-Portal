package com.yorku.controller;

import com.yorku.exception.BusinessException;
import com.yorku.pojo.Clazz;
import com.yorku.pojo.ClazzQueryParam;
import com.yorku.pojo.PageResult;
import com.yorku.pojo.Result;
import com.yorku.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Course Section Management Controller
 * York University Academic Admin Portal
 *
 * Description:
 * Handles API requests for Course Sections (formerly 'Class').
 * Provides endpoints for scheduling, querying, and managing academic course offerings.
 *
 * @Description: 课程/班级管理控制器
 * 处理课程单元的增删改查请求，用于管理教学班级安排。
 */
@RestController
@Slf4j
@RequestMapping("/clazzs")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    /**
     * Paginated Query for Course Sections
     * Retrieves a list of courses based on search criteria (Name, Date Range).
     *
     * 课程管理 - 分页查询
     * 根据条件（名称、日期范围）获取课程列表。
     */
    @GetMapping
    public Result page(ClazzQueryParam clazzQueryParam) {
        log.info("📢 [York U Admin] Querying Course Sections: {}", clazzQueryParam);
        PageResult<Clazz> pageResult = clazzService.page(clazzQueryParam);
        return Result.success(pageResult);
    }

    /**
     * Schedule New Course Section
     * Creates a new course offering and assigns it to the academic calendar.
     *
     * 课程管理 - 新增课程班级
     * 创建新的课程单元并安排到教学日历中。
     */
    @PostMapping
    public Result save(@RequestBody Clazz clazz) {
        log.info("📝 [York U Admin] Scheduling New Course: {}", clazz);
        clazzService.save(clazz);
        return Result.success();
    }

    /**
     * Remove Course Section
     * Deletes a specific course section by ID.
     * Throws BusinessException if students are currently enrolled.
     *
     * 课程管理 - 删除指定班级
     * 根据ID删除课程。如果已有学生选课，将抛出业务异常。
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Integer id) {
        try {
            clazzService.delete(id);
            log.info("🗑️ [York U Admin] Course Section Removed. ID: {}", id);
            return Result.success();
        } catch (BusinessException e) {
            // Capture business logic errors (e.g., Cannot delete course with active students)
            // 捕获业务异常（如：无法删除已有学生的课程），返回错误信息给前端
            log.error("❌ [Delete Failed] Business Rule Violation: {}", e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * Get Course Details by ID
     * Retrieves specific information for a single course section.
     *
     * 课程管理 - 根据ID查询详情
     * 获取单个课程班级的详细信息。
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable("id") Integer id) {
        log.info("🔍 [York U Admin] Fetching Course Details. ID: {}", id);
        Clazz clazz = clazzService.findById(id);
        return Result.success(clazz);
    }

    /**
     * Update Course Information
     * Modifies details of an existing course section (e.g., Room change, Instructor change).
     *
     * 课程管理 - 修改课程信息
     * 更新现有课程的详细信息（如更换教室、更换讲师）。
     */
    @PutMapping
    public Result update(@RequestBody Clazz clazz) {
        log.info("✏️ [York U Admin] Updating Course Info: {}", clazz);
        clazzService.update(clazz);
        return Result.success();
    }

    /**
     * List All Course Sections
     * Retrieves a full list of all active courses (Non-paginated).
     * Used for dropdown selections in other forms.
     *
     * 课程管理 - 查询所有班级
     * 获取所有课程列表（不分页），通常用于前端下拉框选择。
     */
    @GetMapping("/list")
    public Result findAll() {
        log.info("📂 [York U Admin] Loading Full Course List...");
        List<Clazz> clazzList = clazzService.findAll();
        return Result.success(clazzList);
    }
}