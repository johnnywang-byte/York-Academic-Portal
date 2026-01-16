package com.yorku.controller;

import com.yorku.pojo.PageResult;
import com.yorku.pojo.Result;
import com.yorku.pojo.Student;
import com.yorku.pojo.StudentQueryParam;
import com.yorku.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Undergraduate Student Management Controller
 * York University Academic Admin Portal
 *
 * Description:
 * Manages student academic records, including enrollment, GPA updates,
 * profile modifications, and roster queries.
 *
 * @Description: 本科生学籍管理控制器 (学员管理)
 */
@RestController
@Slf4j
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * Student Records - Paginated Search
     * Retrieves student list based on filters (Name, Student No, GPA, Course Section).
     *
     * 学籍管理 - 分页查询
     * 根据条件（姓名、学号、GPA、班级）查询学生列表。
     */
    @GetMapping
    public Result page(StudentQueryParam studentQueryParam) {
        log.info("📢 [York U Admin] Querying Student Records: {}", studentQueryParam);
        PageResult<Student> pageResult = studentService.page(studentQueryParam);
        return Result.success(pageResult);
    }

    /**
     * Enroll New Student
     * Registers a new undergraduate student into the system.
     *
     * 学籍管理 - 录入新生
     * 录入新的学生信息（入学）。
     */
    @PostMapping
    public Result add(@RequestBody Student student) {
        log.info("📝 [York U Admin] Enrolling New Student: {}", student);
        studentService.add(student);
        return Result.success();
    }

    /**
     * Retrieve Student Profile
     * Fetches detailed academic record by Student ID (Primary Key).
     *
     * 学籍管理 - 查询详情 (回显)
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable("id") Integer id) {
        log.info("🔍 [York U Admin] Fetching Student Profile. ID: {}", id);
        Student student = studentService.findById(id);
        return Result.success(student);
    }

    /**
     * Update Student Record
     * Modifies personal or academic information (e.g., Phone, Address, Year Level).
     *
     * 学籍管理 - 修改学生信息
     */
    @PutMapping
    public Result update(@RequestBody Student student) {
        log.info("✏️ [York U Admin] Updating Student Record: {}", student);
        studentService.update(student);
        return Result.success();
    }

    /**
     * Remove Student Records (Batch Delete)
     * Permanently deletes student records from the database.
     *
     * 学籍管理 - 批量删除
     * 根据ID列表删除学生档案（退学/毕业归档）。
     */
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable("ids") List<Integer> ids) {
        log.info("🗑️ [York U Admin] Removing Student Records. IDs: {}", ids);
        studentService.delete(ids);
        return Result.success();
    }

    /**
     * Update Academic Standing (GPA)
     * Updates the Grade Point Average for a specific student.
     *
     * 学务管理 - 更新GPA
     * 更新学生的平均绩点。
     */
    @PutMapping("/gpa/{id}/{gpa}")
    public Result updateGPA(@PathVariable("id") Integer id, @PathVariable("gpa") Double gpa) {
        log.info("🎓 [York U Admin] Updating GPA. StudentID: {}, New GPA: {}", id, gpa);
        studentService.updateGPA(id, gpa);
        return Result.success();
    }
}