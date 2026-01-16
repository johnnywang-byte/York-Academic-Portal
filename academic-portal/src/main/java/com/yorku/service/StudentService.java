package com.yorku.service;

import com.yorku.pojo.PageResult;
import com.yorku.pojo.Student;
import com.yorku.pojo.StudentQueryParam;

import java.util.List;
import java.util.Map;

/**
 * Student Service Interface
 * York University Academic Portal
 */
public interface StudentService {

    /**
     * Paged Query for Students
     * 分页查询
     */
    PageResult<Student> page(StudentQueryParam studentQueryParam);

    /**
     * Add New Student
     * 新增学员
     */
    void add(Student student);

    /**
     * Get Student by ID
     * 根据ID查询
     */
    Student findById(Integer id);

    /**
     * Update Student Info
     * 修改学员信息
     */
    void update(Student student);

    /**
     * Batch Delete Students
     * 批量删除
     */
    void delete(List<Integer> ids);

    /**
     * Update Student GPA
     * 更新绩点 (Update GPA)
     * @param id Student ID
     * @param gpa New GPA Value (Double)
     */
    void updateGPA(Integer id, Double gpa);

    /**
     * Get Course Enrollment Stats (Bar Chart)
     * 统计课程人数
     */
    Map<String, Object> getStudentCountData();

    /**
     * Get Student Year Level Stats (Pie Chart)
     * 统计年级分布
     */
    List<Map<String, Object>> getStudentYearLevelData();
}