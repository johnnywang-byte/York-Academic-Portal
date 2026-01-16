package com.yorku.controller;

import com.yorku.pojo.JobOption;
import com.yorku.pojo.Result;
import com.yorku.service.ReportService;
import com.yorku.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Analytics Controller
 * 报表统计控制器
 * Refactored for York University Academic Portal
 */
@Slf4j
@RequestMapping("/report")
@RestController
public class ReportController {

    // Inject ReportService for Employee/Staff stats (Legacy)
    // 注入 ReportService 处理员工统计 (保留原有逻辑)
    @Autowired
    private ReportService reportService;

    // Inject StudentService for Student stats (New Logic)
    // 注入 StudentService 处理学生统计 (新逻辑)
    @Autowired
    private StudentService studentService;

    /**
     * Get Faculty & Staff Role Stats
     * 获取教职工职位统计人数
     */
    @GetMapping("/empJobData")
    public Result getEmpJobData() {
        log.info("Request: Faculty/Staff Job Distribution - 获取教职工职位统计");
        // Calls the original ReportService (Assuming this hasn't been refactored yet)
        JobOption jobOption = reportService.getEmpJobData();
        return Result.success(jobOption);
    }

    /**
     * Get Faculty & Staff Gender Stats
     * 获取教职工性别统计人数
     */
    @GetMapping("/empGenderData")
    public Result getEmpGenderData() {
        log.info("Request: Faculty/Staff Gender Stats - 获取教职工性别统计");
        List<Map<String,Object>> genderList = reportService.getEmpGenderData();
        return Result.success(genderList);
    }

    /**
     * Get Course Enrollment Stats (Bar Chart)
     * 获取课程注册人数统计 (柱状图)
     */
    @GetMapping("/studentCountData")
    public Result getClassNumData() {
        log.info("Request: Course Enrollment Stats - 获取课程人数统计");

        // 🚨 CRITICAL CHANGE: Use studentService instead of reportService
        // 关键修改：调用我们在 StudentService 中新写的 getStudentCountData 方法
        Map<String, Object> map = studentService.getStudentCountData();

        return Result.success(map);
    }

    /**
     * Get Student Year Level Stats (Pie Chart)
     * 获取本科生年级分布统计 (圆环图)
     * (Formerly: Student Degree Data)
     */
    @GetMapping("/studentDegreeData")
    public Result getStudentEduData() {
        log.info("Request: Student Demographics (Year Level) - 获取学员年级统计");

        // 🚨 CRITICAL CHANGE: Use studentService instead of reportService
        // 关键修改：调用我们在 StudentService 中新写的 getStudentYearLevelData 方法
        List<Map<String, Object>> list = studentService.getStudentYearLevelData();

        return Result.success(list);
    }
}